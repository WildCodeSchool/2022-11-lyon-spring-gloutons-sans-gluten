package com.wcs.project3.controller;

import com.wcs.project3.entity.Category;
import com.wcs.project3.entity.Recipe;
import com.wcs.project3.entity.RecipeIngredient;
import com.wcs.project3.payload.request.CreateRecipeRequest;
import com.wcs.project3.repository.CategoryRepository;
import com.wcs.project3.repository.CommentsRepository;
import com.wcs.project3.repository.RecipeIngredientRepository;
import com.wcs.project3.repository.RecipeRepository;
import com.wcs.project3.repository.StepRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    StepRepository stepRepository;
    @Autowired
    RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    CommentsRepository commentsRepository;

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes(){
        List<Recipe> validatedRecipes = new ArrayList<>();
        List<Recipe> recipes = recipeRepository.findAll();
        for (Recipe recipe : recipes){
            if (recipe.isValidated()){
                validatedRecipes.add(recipe);
                recipe.setNumberOfLikes(recipe.getLikeUsers().size());
            }
        }
        return validatedRecipes;
    }
    @GetMapping("/notValidatedRecipes")
    public List<Recipe> getAllNotValidatedRecipes(){
        List<Recipe> notValidatedRecipes = new ArrayList<>();
        List<Recipe> recipes = recipeRepository.findAll();
        for (Recipe recipe : recipes){
            if (!recipe.isValidated()){
                notValidatedRecipes.add(recipe);
            }
        }
        return notValidatedRecipes;
    }

    @GetMapping("/recipes/{id}")
    public Recipe getRecipe(@PathVariable Long id){return recipeRepository.findById(id).get();}
    @GetMapping("/recipes/categories")
    public List<Recipe> getAllFromCategory (@RequestParam(required = true) String categoryName){
        return recipeRepository.findRecipesByCategoryName(categoryName);
    }

    @PostMapping("/recipes")
    public Recipe createRecipe(@RequestParam(required = true) Long category, @RequestBody CreateRecipeRequest body) {

        Recipe newRecipe = new Recipe();
        Category categoryToUse = categoryRepository.findById(category).get();

        newRecipe.setTitle(body.getTitle());
        newRecipe.setImage(body.getImage());
        newRecipe.setPersonNumber(body.getPersonNumber());
        newRecipe.setPreparationTime(body.getPreparationTime());
        newRecipe.setCookingTime(body.getCookingTime());
        newRecipe.setTotalTime(body.getTotalTime());
        newRecipe.setValidated(body.getValidated());
        newRecipe.setCategory(categoryToUse);
        newRecipe.setSteps(body.getSteps());
//        List<Step> stepsList = new ArrayList<>();
//
//        for (int i = 0; i < body.getSteps().size(); i++) {
//            Step newStep = stepRepository.save(body.getSteps().get(i));
//            stepsList.add(newStep);
//        }
        Recipe recipeToUse = recipeRepository.save(newRecipe);

        List<RecipeIngredient> ingredientsToUse = body.getIngredients();
        for (int i = 0; i < ingredientsToUse.size(); i++) {
            ingredientsToUse.get(i).setRecipe(recipeToUse);
            recipeIngredientRepository.save(ingredientsToUse.get(i));
        }
        return recipeToUse;
    }

    @Transactional
    @PutMapping("admin/recipes/{recipeId}/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public Recipe updateRecipe(@PathVariable Long recipeId,@RequestParam Long category, @RequestBody Recipe recipe){
        Category categoryToUse = categoryRepository.findById(category).get();
        Recipe recipeToUpdate = recipeRepository.findById(recipeId).get();
        recipeToUpdate.setTitle(recipe.getTitle());
        recipeToUpdate.setImage(recipe.getImage());
        recipeToUpdate.setPersonNumber(recipe.getPersonNumber());
        recipeToUpdate.setPreparationTime(recipe.getPreparationTime());
        recipeToUpdate.setCookingTime(recipe.getCookingTime());
        recipeToUpdate.setTotalTime(recipe.getTotalTime());
        recipeToUpdate.setValidated(recipe.isValidated());
        recipeToUpdate.setCategory(categoryToUse);
        recipeToUpdate.setSteps(recipe.getSteps());

        Recipe createdRecipe = recipeRepository.save(recipeToUpdate);

        recipeIngredientRepository.deleteRecipeIngredientsByRecipe(recipeToUpdate);

        List<RecipeIngredient> ingredientsToUse = recipe.getIngredients();
        for (int i = 0; i < ingredientsToUse.size(); i++) {
            ingredientsToUse.get(i).setRecipe(createdRecipe);
            recipeIngredientRepository.save(ingredientsToUse.get(i));
        }
        return createdRecipe;
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
        return numberOfLikes;
    }

}

