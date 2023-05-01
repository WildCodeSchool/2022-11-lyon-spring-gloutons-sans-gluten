package com.wcs.project3.controller;

import com.wcs.project3.entity.Article;
import com.wcs.project3.repository.ArticleRepository;
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

    @PutMapping("/admin/articles/{id}")
    public Article updateArticle(@PathVariable Long id, @RequestBody Article article){
        Article articleToUpdate = articleRepository.findById(id).get();
        articleToUpdate.setTitle(article.getTitle());
        articleToUpdate.setImage(article.getImage());
        articleToUpdate.setContent(article.getContent());
        return articleRepository.save(articleToUpdate);
    }

    @DeleteMapping("/admin/articles/{id}")
    public Boolean deleteArticle(@PathVariable Long id){
        articleRepository.deleteById(id);
        return true;
    }

}

