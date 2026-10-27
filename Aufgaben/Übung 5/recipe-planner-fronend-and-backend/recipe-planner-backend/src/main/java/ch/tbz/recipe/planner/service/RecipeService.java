package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.entities.RecipeEntity;
import ch.tbz.recipe.planner.mapper.RecipeEntityMapper;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import ch.tbz.recipe.planner.domain.Recipe;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class RecipeService {

    RecipeEntityMapper mapper;

    RecipeRepository repository;

    public RecipeService(RecipeEntityMapper mapper, RecipeRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public List<Recipe> getRecipes() {
        List<RecipeEntity> recipesEntities = repository.findAll();
        return recipesEntities.stream().map(mapper::entityToDomain).toList();
    }

    public Recipe getRecipeById(UUID recipeId) {
        return mapper.entityToDomain(repository.findById(recipeId).orElse(null));
    }

    public Recipe addRecipe(Recipe recipe) {
        var createdRecipe = repository.save(mapper.domainToEntity(recipe));
        return mapper.entityToDomain(createdRecipe);
    }
    @Transactional
    public Recipe updateRecipe(UUID recipeId, Recipe recipe) {
        RecipeEntity existing = repository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        if (recipe.getName() == null || recipe.getName().isBlank() || recipe.getIngredients() == null
                || recipe.getIngredients().stream().anyMatch(ingredient -> ingredient == null
                || ingredient.getName() == null || ingredient.getName().isBlank()
                || ingredient.getUnit() == null || ingredient.getAmount() < 1)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid recipe or ingredients");
        }

        existing.setName(recipe.getName().trim());
        existing.setDescription(recipe.getDescription());
        existing.setImageUrl(recipe.getImageUrl());
        // Replace the ingredients while retaining Hibernate's managed collection.
        var ingredients = mapper.domainToEntity(recipe).getIngredients();
        ingredients.forEach(ingredient -> ingredient.setId(null));
        existing.getIngredients().clear();
        existing.getIngredients().addAll(ingredients);
        return mapper.entityToDomain(repository.save(existing));
    }
}
