package com.wcs.project3.controller;


import com.wcs.project3.entity.Comments;
import com.wcs.project3.entity.Recipe;
import com.wcs.project3.repository.CommentsRepository;
import com.wcs.project3.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CommentsController {

    @Autowired
    CommentsRepository commentsRepository;

    @Autowired
    RecipeRepository recipeRepository;


    @PostMapping("/recipes/{recipeId}/comments")
    public ResponseEntity<Comments> createComment(@PathVariable Long recipeId, @RequestBody Comments comments) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found with id " + recipeId));

        comments.setRecipe(recipe);

        Comments savedComments = commentsRepository.save(comments);
        return ResponseEntity.created(URI.create("/recipes/" + recipeId + "/comments/" + savedComments.getId()))
                .body(savedComments);
    }


}
