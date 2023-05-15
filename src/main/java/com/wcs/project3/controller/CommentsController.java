package com.wcs.project3.controller;


import com.wcs.project3.entity.*;
import com.wcs.project3.payload.response.MessageResponse;
import com.wcs.project3.repository.CommentsRepository;
import com.wcs.project3.repository.RecipeRepository;
import com.wcs.project3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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
    public Comments getCommentsById(@PathVariable Long id){return commentsRepository.findById(id).get();}

//    @GetMapping("/recipes/{id}/comments")
//    public List<Comments> getCommentsByRecipeId(@PathVariable Long id) {
//
//        Recipe recipe = recipeRepository.findById(id).get();
//        List<Comments> comments = recipe.getComments();
//        return comments;
//    }
//    @GetMapping("/recipes/{id}/comments")
//    public Map<String, List<Comments>> getCommentsByRecipeId(@PathVariable Long id) {
//        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
//        List<Comments> comments = commentsRepository.findByRecipeOrderByCreatedAtDesc(recipe);
//        Map<String, List<Comments>> userComments = new HashMap<>();
//        for (Comments comment : comments) {
//            User user = comment.getUser();
//            String username = user.getUsername();
//            if (!userComments.containsKey(username)) {
//                userComments.put(username, new ArrayList<>());
//            }
//            List<Comments> userCommentList = userComments.get(username);
//            boolean found = false;
//            for (Comments userComment : userCommentList) {
//                if (userComment.getId().equals(comment.getId())) {
//                    found = true;
//                    break;
//                }
//            }
//            if (!found) {
//                userCommentList.add(comment);
//            }
//        }
//        return userComments;
//    }


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
