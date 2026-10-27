package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class RecipeUpdateTest {
    @Autowired RecipeService service;
    @Autowired JdbcTemplate jdbc;

    @Test
    void updatesExistingRecipeAndReplacesIngredientsWithoutAddingAnotherRecipe() {
        Recipe created = service.addRecipe(new Recipe(null, "Pasta", "Alt", "old.jpg", List.of(
                new Ingredient(null, "Tomate", "Frisch", Unit.PIECE, 2))));
        long recipeCount = jdbc.queryForObject("select count(*) from recipes", Long.class);
        UUID oldIngredientId = created.getIngredients().get(0).getId();
        Recipe changes = new Recipe(UUID.randomUUID(), "Brot", "Neu", "new.jpg", List.of(
                new Ingredient(oldIngredientId, "Mehl", "Fein", Unit.GRAMM, 500)));

        Recipe updated = service.updateRecipe(created.getId(), changes);
        Recipe loaded = service.getRecipeById(created.getId());

        assertThat(updated.getId()).isEqualTo(created.getId());
        assertThat(loaded.getName()).isEqualTo("Brot");
        assertThat(loaded.getDescription()).isEqualTo("Neu");
        assertThat(loaded.getImageUrl()).isEqualTo("new.jpg");
        assertThat(loaded.getIngredients()).hasSize(1);
        assertThat(loaded.getIngredients().get(0)).extracting(
                Ingredient::getName, Ingredient::getComment, Ingredient::getUnit, Ingredient::getAmount)
                .containsExactly("Mehl", "Fein", Unit.GRAMM, 500);
        assertThat(jdbc.queryForObject("select count(*) from recipes", Long.class)).isEqualTo(recipeCount);
        assertThat(jdbc.queryForObject("select count(*) from ingredients where id = ?", Long.class,
                oldIngredientId)).isZero();

        service.updateRecipe(created.getId(), new Recipe(null, "Brot", "", "", List.of()));
        assertThat(service.getRecipeById(created.getId()).getIngredients()).isEmpty();
    }

    @Test
    void rejectsUnknownRecipeAndInvalidChanges() {
        assertThatThrownBy(() -> service.updateRecipe(UUID.randomUUID(),
                new Recipe(null, "Brot", "", "", List.of())))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        error -> assertThat(error.getStatusCode().value()).isEqualTo(404));
        Recipe created = service.addRecipe(new Recipe(null, "Pasta", "", "", List.of()));
        assertThatThrownBy(() -> service.updateRecipe(created.getId(),
                new Recipe(null, " ", "", "", List.of())))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        error -> assertThat(error.getStatusCode().value()).isEqualTo(400));
        assertThat(service.getRecipeById(created.getId()).getName()).isEqualTo("Pasta");
    }
}
