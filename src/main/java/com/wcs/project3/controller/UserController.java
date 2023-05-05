package com.wcs.project3.controller;

import com.wcs.project3.entity.Article;
import com.wcs.project3.entity.User;
import com.wcs.project3.repository.ArticleRepository;
import com.wcs.project3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public User getProfile(@PathVariable String username) {
        return userRepository.findByUsername(username).get();
    }

    @GetMapping( "/{username}/favorites")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public List<Article> getFavoriteArticlesByUser(@PathVariable String username){
       userRepository.findByUsername(username).get();
        return articleRepository.findUser_FavoriteArticlesByUsersUsername(username);}


    @PostMapping   ("/{username}/favorites/{articleId}")
    @PreAuthorize("#username == authentication.principal.username")
    public User addFavorite( @PathVariable String username,@PathVariable Long articleId){
        User userWhoAdds = userRepository.findByUsername(username).get();
        Article articleToAdd = articleRepository.findById(articleId).get();
        userWhoAdds.getFavoriteArticles().add(articleToAdd);
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
    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestBody Map<String, String> body) {

        // Mettre à jour le mot de passe dans la base de données
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setPassword(encoder.encode(body.get("newPassword")));
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

        @DeleteMapping("/{username}")
        @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
        public boolean deleteUser(@PathVariable String username) {

        User userToDelete = userRepository.findByUsername(username).get();
        userRepository.deleteById(userToDelete.getId());
        return true;
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
}
