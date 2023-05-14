package com.wcs.project3.controller;


import com.wcs.project3.entity.*;
import com.wcs.project3.payload.response.MessageResponse;
import com.wcs.project3.repository.CommentsRepository;
import com.wcs.project3.repository.RecipeRepository;
import com.wcs.project3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CommentsController {

    @Autowired
    CommentsRepository commentsRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/comments")
    public List<Comments> getAll(){return commentsRepository.findAll();}
    @GetMapping("/comments/{id}")
    public Comments getComments(@PathVariable Long id){return commentsRepository.findById(id).get();}
    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@RequestParam Long recipeId, @RequestParam String username, @RequestBody Comments comment) {
        User user = userRepository.findByUsername(username).get();
        Recipe recipe = recipeRepository.findById(recipeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found with id " + recipeId));
        comment.setRecipe(recipe);
        comment.setUser(user);
        commentsRepository.save(comment);
    return ResponseEntity.ok(new MessageResponse("Comment registered successfully!"));
    }
}
