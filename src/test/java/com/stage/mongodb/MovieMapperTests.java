package com.stage.mongodb;

import com.stage.mongodb.dto.MovieDto;
import com.stage.mongodb.dto.MovieDtoInput;
import com.stage.mongodb.dto.MoviePatchDto;
import com.stage.mongodb.mapper.MovieMapper;
import com.stage.mongodb.model.Movie;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MovieMapperTests {

    private MovieMapper movieMapper;
    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        easyRandom = new EasyRandom();
        movieMapper = new MovieMapper();

    }

    @Test
    void testToMovieDto() {
        Movie movie = easyRandom.nextObject(Movie.class);
        movie.setId("1");
        movie.setInsertDate(Instant.now());
        movie.setUpdateDate(Instant.now());

        MovieDto movieDto = movieMapper.toMovieDto(movie);

        assertNotNull(movieDto);
        assertNotNull(movieDto.getInsertDate());
        assertNotNull(movieDto.getUpdateDate());
        assertNotNull(movieDto.getReleaseDate());
        assertNotNull(movieDto.getId());

        assertEquals(movie.getTitle(), movieDto.getTitle());
        assertEquals(movie.getReleaseDate(), movieDto.getReleaseDate());
    }

    @Test
    void testToMovieFromDtoInput() {
        MovieDtoInput movieDtoInput = easyRandom.nextObject(MovieDtoInput.class);

        Movie movie = movieMapper.toMovieFromDtoInput(movieDtoInput);

        assertNotNull(movie);
        assertNotNull(movie.getReleaseDate());
        assertNotNull(movie.getTitle());

        assertEquals(movieDtoInput.getTitle(), movie.getTitle());
        assertEquals(movieDtoInput.getReleaseDate(), movie.getReleaseDate());
    }

    @Test
    void testUpdateMovieFromDtoInput() {
        Movie existingMovie = easyRandom.nextObject(Movie.class);
        MovieDtoInput movieDtoInput = easyRandom.nextObject(MovieDtoInput.class);

        existingMovie.setId("1");

        movieMapper.updateMovieFromDtoInput(movieDtoInput, existingMovie);

        assertNotNull(existingMovie.getId());
        assertNotNull(existingMovie.getTitle());
        assertNotNull(existingMovie.getReleaseDate());
        assertNotNull(existingMovie.getInsertDate());
        assertNotNull(existingMovie.getUpdateDate());

        assertEquals(movieDtoInput.getTitle(), existingMovie.getTitle());
    }

    @Test
    void testUpdateMovieFromPatchDto() {
        Movie existingMovie = easyRandom.nextObject(Movie.class);

        MoviePatchDto patchDto = easyRandom.nextObject(MoviePatchDto.class);

        existingMovie.setId("1");
        patchDto.setTitle(null);

        movieMapper.updateMovieFromPatchDto(patchDto, existingMovie);

        assertNotNull(existingMovie.getId());
        assertNotNull(existingMovie.getTitle());
        assertNotNull(existingMovie.getReleaseDate());
        assertNotNull(existingMovie.getInsertDate());
        assertNotNull(existingMovie.getUpdateDate());

    }
}
