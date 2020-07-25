import recipes
all_recipes = recipes.get_recipes()

def get_dict(recipes):
    ingred_count = {}
    for recipe in recipes:
        for ingreds in recipe.ingredients:
            for word_raw in ingreds.split():
                word = ''.join([x for x in word_raw if x.isalpha()])
                if not word: continue
                if word not in ingred_count:
                    ingred_count[word] = 1
                else:
                    ingred_count[word] += 1
    return ingred_count
    # return sorted(ingred_count.items(), key=lambda x: x[1], reverse=True)

def filter(ingred_count, freq=10**4):
    return [x for x in ingred_count if ingred_count[x] < freq]

def desc_to_ingreds(recipe):
   return [x for x in recipe.ingredients_str.split() if x in ingreds] 
   
ingreds = filter(get_dict(all_recipes))




