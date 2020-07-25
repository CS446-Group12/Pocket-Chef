import recipes
import sqlite3

all_recipes = recipes.get_recipes()

def get_first(result):
    for r in result:
        return r[0]

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
        conn.execute(sql, (r.title, r.desc, r.fat, r.calories, r.protein, r.rating, r.sodium, r.ingredients_str, r.directions_str))
    
    for rid, recipe in enumerate(all_recipes):
        insert_recipe(recipe)
        insert_recipe_categories(conn, recipe, rid + 1) # database is 1-indexed (not 0-indexed)

def run():
    conn = sqlite3.connect('/Users/gill/Downloads/ece452proj/recipes.db')
    conn.executescript(open("queries.sql").read())
    insert_categories(conn)
    insert_recipes(conn)
    conn.commit()

run()