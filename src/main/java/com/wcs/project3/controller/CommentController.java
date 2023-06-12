package com.wcs.project3.controller;

import com.wcs.project3.entity.*;
import com.wcs.project3.payload.response.MessageResponse;
import com.wcs.project3.repository.CommentRepository;
import com.wcs.project3.repository.RecipeRepository;
import com.wcs.project3.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("comments")
    public List<Comment> getAll(){return commentRepository.findAll();}

    @GetMapping("comments/{id}")
    public Comment getCommentsById(@PathVariable Long id){return commentRepository.findById(id).get();}

    @GetMapping("recipes/{id}/comments")
    public List<Comment> getCommentsByRecipeId(@PathVariable Long id) {
        Recipe recipe = recipeRepository.findById(id).get();
        List<Comment> comments = recipe.getComments();
        List<Comment> notReportedComments = new ArrayList<>();
        for (Comment comment : comments){
            if (!comment.isReported()){
                notReportedComments.add(comment);
            }
        }
        return notReportedComments;
    }

    @GetMapping("reportedComments")
//    @PreAuthorize("hasRole('Admin')")
    public List<Comment> getReportedComments(){
        List<Comment> reportedComments = new ArrayList<>();
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments){
            if (comment.isReported()){
                reportedComments.add(comment);
            }
        }
        return reportedComments;
    }

    @PutMapping("comments/{commentId}")
    public void updateComment(@PathVariable Long commentId, @RequestBody Comment comment){
        Comment commentToUpdate = commentRepository.findById(commentId).get();
        commentToUpdate.setComment(comment.getComment());
        commentToUpdate.setReported(comment.isReported());
        commentRepository.save(commentToUpdate);
    }

    @PostMapping("comments")
    public ResponseEntity<?> createComment(@RequestParam Long recipeId, @RequestParam String username, @RequestBody Comment comment) {
        User user = userRepository.findByUsername(username).get();
        Recipe recipe = recipeRepository.findById(recipeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found with id " + recipeId));
        comment.setRecipe(recipe);
        comment.setUser(user);
        commentRepository.save(comment);
    return ResponseEntity.ok(new MessageResponse("Comment registered successfully!"));
    }

    @DeleteMapping("reportedComments/{commentId}")
//    @PreAuthorize("hasRole('Admin')")
    public Boolean deleteReportedComment(@PathVariable Long commentId) {
        // Récupérer le commentaire
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            // Récupérer l'utilisateur associé au commentaire
            User user = comment.getUser();
            // Vérifier si le commentaire est associé à une recette
//            Recipe recipe = comment.getRecipe();
//            if (recipe != null) {
//                // Désassocier le commentaire de la recette
//                recipe.getComments().remove(comment);
//            }
            // Désassocier le commentaire de l'utilisateur
            user.getComments().remove(comment);
            // Supprimer le commentaire de la base de données
            commentRepository.delete(comment);
        }
        return true;
    }

}
