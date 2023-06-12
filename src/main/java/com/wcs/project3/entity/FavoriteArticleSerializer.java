package com.wcs.project3.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteArticleSerializer extends JsonSerializer<List<Article>> {

    @Override
    public void serialize(List<Article> favoriteArticles, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<Map<String, Object>> serializedRecipes = new ArrayList<>();

        for (Article article : favoriteArticles) {
            Map<String, Object> serializedRecipe = new HashMap<>();
//            serializedRecipe.put("id", article.getId());
            serializedRecipe.put("title", article.getTitle());
            serializedRecipes.add(serializedRecipe);
        }

        jsonGenerator.writeObject(serializedRecipes);
    }
}
