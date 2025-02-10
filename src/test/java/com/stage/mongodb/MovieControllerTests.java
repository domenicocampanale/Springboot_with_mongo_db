package com.stage.mongodb;

import com.stage.mongodb.controller.MovieController;
import com.stage.mongodb.dto.MovieDto;
import com.stage.mongodb.dto.MovieDtoInput;
import com.stage.mongodb.dto.MoviePatchDto;
import com.stage.mongodb.service.MovieService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MovieControllerTest {

    // private MockMvc mockMvc;

    private final EasyRandom easyRandom = new EasyRandom();
    @Mock
    private MovieService movieService;
    @InjectMocks
    private MovieController movieController;
    // private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    void testGetMovies() throws Exception {
        List<MovieDto> movies = easyRandom.objects(MovieDto.class, 5).toList();
        when(movieService.getMovies()).thenReturn(movies);

        movieService.getMovies();

        // mockMvc.perform(get("/api/movie/all")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(5));

        verify(movieService, times(1)).getMovies();
    }

    @Test
    void testGetMovieById() throws Exception {
        String id = "1";
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);

        when(movieService.getMovieById(id)).thenReturn(movieDto);

        movieService.getMovieById(id);

        verify(movieService, times(1)).getMovieById(id);
    }

    @Test
    void testInsertMovie() throws Exception {
        MovieDtoInput movieDtoInput = easyRandom.nextObject(MovieDtoInput.class);
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);

        when(movieService.insertMovie(any(MovieDtoInput.class))).thenReturn(movieDto);

        /*
         * mockMvc.perform(post("/api/movie").contentType("application/json")
         * .content(objectMapper.writeValueAsString(movieDtoInput))).andExpect(status().
         * isCreated());
         */

        movieService.insertMovie(movieDtoInput);

        verify(movieService, times(1)).insertMovie(any(MovieDtoInput.class));
    }

    @Test
    void testUpdateMovie() throws Exception {
        String id = "1";
        MovieDtoInput movieDtoInput = easyRandom.nextObject(MovieDtoInput.class);
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);

        when(movieService.updateMovie(any(MovieDtoInput.class), eq(id))).thenReturn(movieDto);

        /*
         * mockMvc.perform(put("/api/movie").param("id",
         * id.toString()).contentType("application/json")
         * .content(objectMapper.writeValueAsString(movieDtoInput))).andExpect(status().
         * isOk());
         */

        movieService.updateMovie(movieDtoInput, id);

        verify(movieService, times(1)).updateMovie(any(MovieDtoInput.class), eq(id));
    }

    @Test
    void testUpdateMoviePartial() throws Exception {
        String id = "1";
        MoviePatchDto moviePatchDto = easyRandom.nextObject(MoviePatchDto.class);
        moviePatchDto.setReleaseDate(null);
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);

        when(movieService.updateMoviePartial(eq(id), any(MoviePatchDto.class))).thenReturn(movieDto);

        /*
         * mockMvc.perform(patch("/api/movie").param("id",
         * id.toString()).contentType("application/json")
         * .content(objectMapper.writeValueAsString(moviePatchDto))).andExpect(status().
         * isOk());
         */

        movieService.updateMoviePartial(id, moviePatchDto);

        verify(movieService, times(1)).updateMoviePartial(eq(id), any(MoviePatchDto.class));
    }

    @Test
    void testDeleteMovie() throws Exception {
        String id = "1";
        doNothing().when(movieService).deleteMovie(id);

        // mockMvc.perform(delete("/api/movie").param("id",
        // id.toString())).andExpect(status().isOk());

        movieService.deleteMovie(id);

        verify(movieService, times(1)).deleteMovie(id);
    }
}
