package com.wcs.project3.controller;

import com.wcs.project3.entity.Article;
import com.wcs.project3.entity.Comment;
import com.wcs.project3.entity.Recipe;
import com.wcs.project3.entity.User;
import com.wcs.project3.repository.ArticleRepository;
import com.wcs.project3.repository.CommentRepository;
import com.wcs.project3.repository.RecipeRepository;
import com.wcs.project3.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

//import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private EntityManager entityManager;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{username}")
    //@PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public User getProfile(@PathVariable String username) {
        return userRepository.findByUsername(username).get();
    }

    @GetMapping( "/{username}/favorites")
    //@PreAuthorize("#username == authentication.principal.username")
    public List<Article> getFavoriteArticlesByUser(@PathVariable String username){
       userRepository.findByUsername(username).get();
       return articleRepository.findUser_FavoriteArticlesByFavoriteUsersUsername(username);}

    @GetMapping( "/{username}/favoritesRecipes")
    //@PreAuthorize("#username == authentication.principal.username")
    public List<Recipe> getFavoriteRecipesByUser(@PathVariable String username){
        userRepository.findByUsername(username).get();
        return recipeRepository.findUser_FavoriteRecipesByFavoriteUsersUsername(username);}

    @GetMapping( "/{username}/likesRecipes")
    //@PreAuthorize("#username == authentication.principal.username")
    public List<Recipe> getlikeRecipesByUser(@PathVariable String username){
        userRepository.findByUsername(username).get();
        return recipeRepository.findUser_LikeRecipesByLikeUsersUsername(username);}

    @PostMapping   ("/{username}/favorites/{articleId}")
    @PreAuthorize("#username == authentication.principal.username")
    public User addFavorite( @PathVariable String username,@PathVariable Long articleId){
        User userWhoAdds = userRepository.findByUsername(username).get();
        Article articleToAdd = articleRepository.findById(articleId).get();
        userWhoAdds.getFavoriteArticles().add(articleToAdd);
        return userRepository.save(userWhoAdds);
    }
    @PostMapping   ("/{username}/favoritesRecipes/{recipeId}")
//    @PreAuthorize("#username == authentication.principal.username")
    public User addFavoriteRecipe( @PathVariable String username,@PathVariable Long recipeId){
        User userWhoAdds = userRepository.findByUsername(username).get();
        Recipe recipeToAdd = recipeRepository.findById(recipeId).get();
        userWhoAdds.getFavoriteRecipes().add(recipeToAdd);
        return userRepository.save(userWhoAdds);
    }


    @PostMapping   ("/{username}/likesRecipes/{recipeId}")
//    @PreAuthorize("#username == authentication.principal.username")
    public User addLikeRecipe( @PathVariable String username,@PathVariable Long recipeId){
        User userWhoAdds = userRepository.findByUsername(username).get();
        Recipe recipeToAdd = recipeRepository.findById(recipeId).get();
        userWhoAdds.getLikeRecipes().add(recipeToAdd);
        return userRepository.save(userWhoAdds);
    }

    @PutMapping("/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public boolean updateUser(@PathVariable String username, @RequestBody User user) {
        User userToUpdate = userRepository.findByUsername(username).get();
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());
        userRepository.save(userToUpdate);
        return true;
    }

    @PutMapping("/{userId}/password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestBody Map<String, String> body) {
        // Mettre à jour le mot de passe dans la base de données
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        boolean areSamePasswords = encoder.matches(body.get("actualPassword"), user.getPassword());
        if (areSamePasswords) {
            user.setPassword(encoder.encode(body.get("newPassword")));
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }
        return null;
    }

    @Transactional
    @DeleteMapping("/{username}")
//    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public boolean deleteUser(@PathVariable String username) {
        User userToDelete = userRepository.findByUsername(username).get();
        if (userToDelete != null) {
            // Récupérer les commentaires de l'utilisateur
//            List<Comment> comments = commentRepository.findByUser(userToDelete);

            // Supprimer les commentaires
//            commentRepository.deleteByUser(userToDelete);

            // Supprimer l'utilisateur
            userRepository.delete(userToDelete);
            return true;
        }
        return false;
    }

    @DeleteMapping   ("/{username}/favorites/{articleId}")
    @PreAuthorize("#username == authentication.principal.username")
    public Boolean deleteFavorite( @PathVariable String username,@PathVariable Long articleId){
        User userWhoDeletes = userRepository.findByUsername(username).get();
        Article articleToDelete = articleRepository.findById(articleId).get();
        userWhoDeletes.getFavoriteArticles().remove(articleToDelete);
        articleRepository.save(articleToDelete);
        return true;
    }

    @DeleteMapping   ("/{username}/favoritesRecipes/{recipeId}")
    @PreAuthorize("#username == authentication.principal.username")
    public Boolean deleteFavoriteRecipe( @PathVariable String username,@PathVariable Long recipeId){
        User userWhoDeletes = userRepository.findByUsername(username).get();
        Recipe recipeToDelete = recipeRepository.findById(recipeId).get();
        userWhoDeletes.getFavoriteRecipes().remove(recipeToDelete);
        recipeRepository.save(recipeToDelete);
        return true;
    }

    @DeleteMapping   ("/{username}/likesRecipes/{recipeId}")
    @PreAuthorize("#username == authentication.principal.username")
    public Boolean deleteLikeRecipe( @PathVariable String username,@PathVariable Long recipeId){
        User userWhoDeletes = userRepository.findByUsername(username).get();
        Recipe recipeToDelete = recipeRepository.findById(recipeId).get();
        userWhoDeletes.getLikeRecipes().remove(recipeToDelete);
        recipeRepository.save(recipeToDelete);
        return true;
    }
}
