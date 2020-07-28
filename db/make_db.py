import recipes
import sqlite3
import ingredients
import sys

all_recipes = recipes.get_recipes()
all_ingredients = ingredients.read_ingredients()

def get_first(result):
    for r in result:
        return r[0]

def insert_ingredients(conn):
    def insert_ingredient(i_id, ingred):
        sql = "insert into INGREDIENT(id, name) values (?, ?)"
        conn.execute(sql, (i_id, ingred))

    for i_id in all_ingredients:
        name = all_ingredients[i_id]
        insert_ingredient(i_id, name)

def insert_recipe_ingreds(conn, recipe, recipe_id):
    def insert_recipe_ingred(recipe_id, i_id):
        # sql = "select ID from ingredient where name=?"
        # ingred_id = get_first(conn.execute(sql, (ingred,)))
        sql = "insert or ignore into recipe_ingredient values(?,?)"
        conn.execute(sql, (recipe_id, i_id))

    for i_id in recipe.ingredient_ids:
        insert_recipe_ingred(recipe_id, i_id)

def insert_recipe_categories(conn, recipe, recipe_id):
    def insert_recipe_category(recipe_id, category):
        sql = "select ID from category where name=?"
        category_id = get_first(conn.execute(sql, (category,)))
        sql = "insert or ignore into recipe_category values(?,?)"
        conn.execute(sql, (recipe_id, category_id))

    for category in recipe.categories:
        insert_recipe_category(recipe_id, category)


def insert_categories(conn):
    def insert_category(category):
        sql = "insert into CATEGORY(name) values (?)"
        conn.execute(sql, (category,))

    categories = recipes.get_categories(all_recipes)
    for category in categories:
        insert_category(category)


def insert_recipes(conn):
    def insert_recipe(r):
        sql = "insert into RECIPE(title, desc, fat, calories, protein, rating, sodium, ingredients, directions) values (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        conn.execute(sql, (r.title, r.desc, r.fat, r.calories, r.protein,
                           r.rating if r.rating else 0, r.sodium, r.ingredients_str, r.directions_str))

    for rid, recipe in enumerate(all_recipes):
        insert_recipe(recipe)
        # database is 1-indexed (not 0-indexed)
        insert_recipe_categories(conn, recipe, rid + 1)
        insert_recipe_ingreds(conn, recipe, rid + 1)

def run():
    # No need for sampling or to clear DB because 
    # database population is much faster now (10 seconds) due to ID insertion.

    dbpath = '/Users/gill/Downloads/cs446-app/db/recipes.db'
    conn = sqlite3.connect(dbpath)
    
    conn.executescript(open("db/queries/tables.sql").read())
    insert_categories(conn)
    insert_ingredients(conn)
    insert_recipes(conn)
    conn.commit()

run()
