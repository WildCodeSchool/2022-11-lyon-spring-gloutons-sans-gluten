package com.wcs.project3.controller;

import com.wcs.project3.entity.Category;
import com.wcs.project3.entity.Recipe;
import com.wcs.project3.entity.RecipeIngredient;
import com.wcs.project3.repository.CategoryRepository;
import com.wcs.project3.repository.RecipeIngredientRepository;
import com.wcs.project3.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class RecipeController {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RecipeIngredientRepository recipeIngredientRepository;

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes(){
        List<Recipe> recipes = recipeRepository.findAll();
        for (Recipe recipe : recipes){
        recipe.setNumberOfLikes(recipe.getLikeUsers().size());
        }
        return recipes;
    }

    @GetMapping("/recipes/{id}")
    public Recipe getRecipe(@PathVariable Long id){return recipeRepository.findById(id).get();}

    @PostMapping("/recipes")
    public Recipe createRecipe(@RequestParam(required = true) Long category, @RequestBody Recipe recipe) {
        Category categoryToUse = categoryRepository.findById(category).get();
        recipe.setCategory(categoryToUse);
        Recipe recipeToUse = recipeRepository.save(recipe);
        List<RecipeIngredient> ingredientsToUse = recipe.getIngredients();

        for (int i = 0; i < ingredientsToUse.size(); i++) {
            ingredientsToUse.get(i).setRecipe(recipeToUse);
            recipeIngredientRepository.save(ingredientsToUse.get(i));
        }
        return recipeToUse;
    }

    @PutMapping("/recipes/{id}")
    public Recipe updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe){
        Recipe recipeToUpdate = recipeRepository.findById(id).get();
        recipeToUpdate.setTitle(recipe.getTitle());
        recipeToUpdate.setImage(recipe.getImage());
        recipeToUpdate.setPersonNumber(recipe.getPersonNumber());
        recipeToUpdate.setPreparationTime(recipe.getPreparationTime());
        recipeToUpdate.setCookingTime(recipe.getCookingTime());
        recipeToUpdate.setTotalTime(recipe.getTotalTime());
        recipeToUpdate.setValidated(recipe.isValidated());
        return recipeRepository.save(recipeToUpdate);
    }

    @Transactional
//    s'assurer que toutes les opérations de suppression sont effectuées dans une transaction unique.
    @DeleteMapping("/recipes/{id}")
    public Boolean deleteRecipe(@PathVariable Long id) {
        Recipe recipe = recipeRepository.findById(id).get();
        // Supprimer la relation entre la recette et les utilisateurs qui l'ont aimée
        recipe.getLikeUsers().forEach(user -> user.getLikeRecipes().remove(recipe));
        recipe.getLikeUsers().clear();
        // Supprimer la relation entre la recette et les utilisateurs qui l'ont ajoutée en favoris
        recipe.getFavoriteUsers().forEach(user -> user.getFavoriteRecipes().remove(recipe));
        recipe.getFavoriteUsers().clear();
        // Effacer la recette
        recipeRepository.deleteById(id);
        return true;
    }
    @GetMapping("/recipes/{id}/likes")
    public int getNumberOfLikesForRecipe(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        int numberOfLikes = 0;
        if (recipe.isPresent()){
            numberOfLikes = recipe.get().getLikeUsers().size();
        }
        System.out.println(recipe.get().getLikeUsers().size());
        return numberOfLikes;
    }

}

