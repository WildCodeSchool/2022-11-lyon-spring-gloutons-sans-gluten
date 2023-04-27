package com.wcs.project3.controller;

import com.wcs.project3.entity.RecipeIngredient;
import com.wcs.project3.repository.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class RecipeIngredientController {
    @Autowired
    RecipeIngredientRepository recipeIngredientRepository;

    @GetMapping("/quantity")
    public List<RecipeIngredient> getAll(){return recipeIngredientRepository.findAll();}
}

