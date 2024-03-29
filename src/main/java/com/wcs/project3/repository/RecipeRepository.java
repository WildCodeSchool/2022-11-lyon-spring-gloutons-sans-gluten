package com.wcs.project3.repository;

import com.wcs.project3.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe>findUser_FavoriteRecipesByFavoriteUsersUsername(String username);

    List<Recipe> findUser_LikeRecipesByLikeUsersUsername(String username);

    List<Recipe> findRecipesByCategoryName(String categoryName);
}
