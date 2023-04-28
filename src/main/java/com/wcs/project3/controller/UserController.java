package com.wcs.project3.controller;

import com.wcs.project3.entity.Article;
import com.wcs.project3.entity.User;
import com.wcs.project3.repository.ArticleRepository;
import com.wcs.project3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

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

    @GetMapping( "/{userId}/favorites")
    public List<Article> getFavoriteArticlesByUser(@PathVariable Long userId){
        userRepository.findById(userId).get();
        return articleRepository.findUser_FavoriteArticlesByUsersId(userId);}

    @PostMapping   ("/{userId}/favorites/{articleId}")
    public User addFavorite( @PathVariable Long userId,@PathVariable Long articleId){
        User userWhoAdds = userRepository.findById(userId).get();
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

    @DeleteMapping("/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public boolean deleteUser(@PathVariable String username) {
        User userToDelete = userRepository.findByUsername(username).get();
        userRepository.deleteById(userToDelete.getId());
        return true;
    }
}
