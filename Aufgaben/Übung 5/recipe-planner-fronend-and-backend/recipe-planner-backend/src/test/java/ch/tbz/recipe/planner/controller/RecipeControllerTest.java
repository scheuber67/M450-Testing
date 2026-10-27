package ch.tbz.recipe.planner.controller;

import ch.tbz.recipe.planner.domain.*;
import ch.tbz.recipe.planner.mapper.RecipeEntityMapper;
import ch.tbz.recipe.planner.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerTest {
    private final RecipeService service = mock(RecipeService.class);
    private final ObjectMapper json = new ObjectMapper();
    private final UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final Recipe recipe = new Recipe(id, "Pasta", "Mit Tomaten", "pasta.jpg",
            List.of(new Ingredient(id, "Tomate", "Frisch", Unit.PIECE, 2)));
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(
                new RecipeController(service, mock(RecipeEntityMapper.class))).build();
    }

    @Test
    void getRecipesReturnsRecipesIncludingIngredients() throws Exception {
        when(service.getRecipes()).thenReturn(List.of(recipe));
        mvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json.writeValueAsString(List.of(recipe)), true));
        verify(service).getRecipes();
    }

    @Test
    void getRecipePassesIdAndReturnsRecipe() throws Exception {
        when(service.getRecipeById(id)).thenReturn(recipe);
        mvc.perform(get("/api/recipes/recipe/{id}", id)).andExpect(status().isOk())
                .andExpect(content().json(json.writeValueAsString(recipe), true));
        verify(service).getRecipeById(id);
    }

    @Test
    void addRecipeDeserializesRequestAndReturnsSavedRecipe() throws Exception {
        Recipe request = new Recipe(null, recipe.getName(), recipe.getDescription(),
                recipe.getImageUrl(), recipe.getIngredients());
        when(service.addRecipe(any(Recipe.class))).thenReturn(recipe);
        mvc.perform(post("/api/recipes").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(json.writeValueAsString(recipe), true));
        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(service).addRecipe(captor.capture());
        assertThat(captor.getValue()).isEqualTo(request);
    }
}

