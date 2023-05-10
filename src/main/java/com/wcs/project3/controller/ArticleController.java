package com.wcs.project3.controller;

import com.wcs.project3.entity.Article;
import com.wcs.project3.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/articles")
    public List<Article> getAll(){return articleRepository.findAll();}

    @GetMapping("/articles/{id}")
    public Article getArticle(@PathVariable Long id){return articleRepository.findById(id).get();}

    @PostMapping("/admin/articles")
    public Article createArticle(@RequestBody Article article) {return articleRepository.save(article);}

    @PutMapping("/admin/articles/{articleId}")
    public Article updateArticle(@PathVariable Long articleId, @RequestBody Article article){
        Article articleToUpdate = articleRepository.findById(articleId).get();
        articleToUpdate.setTitle(article.getTitle());
        articleToUpdate.setImage(article.getImage());
        articleToUpdate.setContent(article.getContent());
        return articleRepository.save(articleToUpdate);
    }

    @Transactional
    @DeleteMapping("/admin/articles/{articleId}")
    public Boolean deleteArticle(@PathVariable Long articleId){
        Article article = articleRepository.findById(articleId).get();
        // Supprimer la relation entre l'article et les utilisateurs qui l'ont ajoutée en favoris
        article.getFavoriteUsers().forEach(user -> user.getFavoriteArticles().remove(article));
        article.getFavoriteUsers().clear();
        // Effacer l'article
        articleRepository.deleteById(articleId);
        return true;
    }
}

