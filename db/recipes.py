import json

class recipe:
    def __init__(self, directions, fat, categories, calories, desc, protein, rating, title, ingredients, sodium):
        self.directions = directions
        self.directions_str = '\0'.join(directions)
        self.fat = int(fat) if fat else None
        self.categories = categories
        self.calories = int(calories) if calories else None
        self.desc = desc
        self.protein = int(protein) if protein else None
        self.rating = rating if rating else None
        self.title = title
        self.ingredients = ingredients
        self.ingredients_str = '\0'.join(ingredients)
        self.sodium = int(sodium) if sodium else None

def get_recipes():
    with open('db/full_format_recipes.json') as json_file:
        data = json.load(json_file)
        recipes = []
        for p in data:
            if not p: continue
            r = recipe(p['directions'], p['fat'], p['categories'], p['calories'], p['desc'], p['protein'], p['rating'], p['title'], p['ingredients'], p['sodium'])
            recipes.append(r)

    return recipes

def get_categories(recipes):
    categories = set()
    for r in recipes:
        for categ in r.categories:
            categories.add(categ)
    return list(categories)
