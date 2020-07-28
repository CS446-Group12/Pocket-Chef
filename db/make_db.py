import recipes
import sqlite3
import clean_ingredients
import sys

all_recipes = recipes.get_recipes()


def get_first(result):
    for r in result:
        return r[0]


def insert_ingredients(conn):
    def insert_ingredient(ingred):
        sql = "insert into INGREDIENT(name) values (?)"
        conn.execute(sql, (ingred,))

    for ingred in clean_ingredients.ingreds:
        insert_ingredient(ingred)


def insert_recipe_ingreds(conn, recipe, recipe_id):
    def insert_recipe_ingred(recipe_id, ingred):
        sql = "select ID from ingredient where name=?"
        ingred_id = get_first(conn.execute(sql, (ingred,)))
        sql = "insert or ignore into recipe_ingredient values(?,?)"
        conn.execute(sql, (recipe_id, ingred_id))

    ingreds = clean_ingredients.desc_to_ingreds(recipe)
    for ingred in ingreds:
        insert_recipe_ingred(recipe_id, ingred)


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


def insert_recipes(conn, limit):
    def insert_recipe(r):
        sql = "insert into RECIPE(title, desc, fat, calories, protein, rating, sodium, ingredients, directions) values (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        conn.execute(sql, (r.title, r.desc, r.fat, r.calories, r.protein,
                           r.rating if r.rating else 0, r.sodium, r.ingredients_str, r.directions_str))

    for rid, recipe in enumerate(all_recipes):
        insert_recipe(recipe)
        # database is 1-indexed (not 0-indexed)
        insert_recipe_categories(conn, recipe, rid + 1)
        insert_recipe_ingreds(conn, recipe, rid + 1)
        if limit is not None and rid > limit:
            break


def run():

    if len(sys.argv) >= 2:
        limit = int(sys.argv[1])
    else:
        limit = None

    # clear the old db
    with open('PocketChef/app/src/main/assets/recipes.db', "w") as f:
        pass
    conn = sqlite3.connect('PocketChef/app/src/main/assets/recipes.db')
    conn.executescript(open("db/tables.sql").read())
    insert_categories(conn)
    insert_ingredients(conn)
    insert_recipes(conn, limit)
    conn.commit()


run()
