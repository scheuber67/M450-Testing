package ch.tbz.recipe.planner.mapper;

import ch.tbz.recipe.planner.domain.*;
import ch.tbz.recipe.planner.entities.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.UUID;

class RecipeEntityMapperTest {
    private final RecipeEntityMapper mapper = Mappers.getMapper(RecipeEntityMapper.class);
    private final UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");

    @Test
    void domainToEntity() {
        Ingredient ingredient = new Ingredient(id, "Mehl", "Fein", Unit.GRAMM, 500);
        Recipe recipe = new Recipe(id, "Brot", "Backen", "brot.jpg", List.of(ingredient));

        RecipeEntity result = mapper.domainToEntity(recipe);

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result.getId()).isEqualTo(id);
            soft.assertThat(result.getName()).isEqualTo("Brot");
            soft.assertThat(result.getDescription()).isEqualTo("Backen");
            soft.assertThat(result.getImageUrl()).isEqualTo("brot.jpg");
            soft.assertThat(result.getIngredients()).usingRecursiveComparison()
                    .isEqualTo(List.of(new IngredientEntity(id, "Mehl", "Fein", Unit.GRAMM, 500)));
        });
    }

    @Test
    void entityToDomain() {
        IngredientEntity ingredient = new IngredientEntity(id, "Mehl", "Fein", Unit.GRAMM, 500);
        RecipeEntity entity = new RecipeEntity(id, "Brot", "Backen", "brot.jpg", List.of(ingredient));

        Recipe result = mapper.entityToDomain(entity);

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result.getId()).isEqualTo(id);
            soft.assertThat(result.getName()).isEqualTo("Brot");
            soft.assertThat(result.getDescription()).isEqualTo("Backen");
            soft.assertThat(result.getImageUrl()).isEqualTo("brot.jpg");
            soft.assertThat(result.getIngredients()).usingRecursiveComparison()
                    .isEqualTo(List.of(new Ingredient(id, "Mehl", "Fein", Unit.GRAMM, 500)));
        });
    }
}
