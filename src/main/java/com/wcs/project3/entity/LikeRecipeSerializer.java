package com.wcs.project3.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikeRecipeSerializer extends JsonSerializer<List<Recipe>> {

    @Override
    public void serialize(List<Recipe> likeRecipes, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<Map<String, Object>> serializedRecipes = new ArrayList<>();

        for (Recipe recipe : likeRecipes) {
            Map<String, Object> serializedRecipe = new HashMap<>();
//            serializedRecipe.put("id", recipe.getId());
            serializedRecipe.put("title", recipe.getTitle());
            serializedRecipes.add(serializedRecipe);
        }

        jsonGenerator.writeObject(serializedRecipes);
    }

}
