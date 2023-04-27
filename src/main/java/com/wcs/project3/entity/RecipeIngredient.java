package com.wcs.project3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private int quantity;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="recipe_id")
    @JsonIgnore
    private Recipe recipe;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="ingredient_id")
    private Ingredient ingredient;

    public RecipeIngredient() { }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Recipe getRecipe() { return recipe; }

    public void setRecipe(Recipe recipe) { this.recipe = recipe; }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}

