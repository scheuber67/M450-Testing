package ch.tbz.recipe.planner.mapper;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.entities.IngredientEntity;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.util.UUID;

class IngredientEntityMapperTest {
    private final IngredientEntityMapper mapper = Mappers.getMapper(IngredientEntityMapper.class);
    private final UUID id = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void domainToEntity() {
        Ingredient ingredient = new Ingredient(id, "Mehl", "Fein", Unit.GRAMM, 250);

        IngredientEntity result = mapper.domainToEntity(ingredient);

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result.getId()).isEqualTo(id);
            soft.assertThat(result.getName()).isEqualTo("Mehl");
            soft.assertThat(result.getComment()).isEqualTo("Fein");
            soft.assertThat(result.getUnit()).isEqualTo(Unit.GRAMM);
            soft.assertThat(result.getAmount()).isEqualTo(250);
        });
    }

    @Test
    void entityToDomain() {
        IngredientEntity entity = new IngredientEntity(id, "Mehl", "Fein", Unit.GRAMM, 250);

        Ingredient result = mapper.entityToDomain(entity);

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result.getId()).isEqualTo(id);
            soft.assertThat(result.getName()).isEqualTo("Mehl");
            soft.assertThat(result.getComment()).isEqualTo("Fein");
            soft.assertThat(result.getUnit()).isEqualTo(Unit.GRAMM);
            soft.assertThat(result.getAmount()).isEqualTo(250);
        });
    }
}
