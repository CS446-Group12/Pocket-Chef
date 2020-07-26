// choose recipes with atleast X matching ingredients to the ingredients given
public static String xMatchingQuery(List<Integer> ingredient_ids, int x){
    String query = "select recipe.id, count(recipe.id) as matches from recipe_ingredient " +
    "inner join recipe ON recipe_id=recipe.id "+
    "where ? "+
    "group by recipe.id "+
    "having matches >= # "+
    "order by matches desc";
    
    String where = "ingredient_id=" + String.valueOf(ingredient_ids.get(0));
        
    for (int i = 1; i < ingredient_ids.size(); i++) 
        where += " or ingredient_id=" + String.valueOf(ingredient_ids.get(i));
        
    return query.replace("?", where).replace("#", String.valueOf(x));
}
    
// choose the X top recipes that closely match the given ingredients
public static String topMatchingQuery(List<Integer> ingredient_ids, int x){
    String query = "select recipe.id, count(recipe.id) as matches from recipe_ingredient " +
    "inner join recipe ON recipe_id=recipe.id "+
    "where ? "+
    "group by recipe.id "+
    "order by matches desc limit #";
    
    String where = "ingredient_id=" + String.valueOf(ingredient_ids.get(0));
        
    for (int i = 1; i < ingredient_ids.size(); i++) 
        where += " or ingredient_id=" + String.valueOf(ingredient_ids.get(i));
        
    return query.replace("?", where).replace("#", String.valueOf(x));
}