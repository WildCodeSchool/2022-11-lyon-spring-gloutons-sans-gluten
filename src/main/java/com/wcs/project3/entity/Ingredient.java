package com.wcs.project3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    private String logo;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RecipeIngredient> recipes;

    public Ingredient() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<RecipeIngredient> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeIngredient> recipes) {
        this.recipes = recipes;
    }
}

