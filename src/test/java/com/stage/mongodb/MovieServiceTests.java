package com.stage.mongodb;

import com.stage.mongodb.dto.MovieDto;
import com.stage.mongodb.dto.MovieDtoInput;
import com.stage.mongodb.dto.MoviePatchDto;
import com.stage.mongodb.exceptions.MovieNotFoundException;
import com.stage.mongodb.mapper.MovieMapper;
import com.stage.mongodb.model.Movie;
import com.stage.mongodb.repository.MovieRepository;
import com.stage.mongodb.repository.ReviewRepository;
import com.stage.mongodb.service.MovieService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService;

    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        easyRandom = new EasyRandom();
    }

    @Test
    void testGetMovies() {
        List<Movie> movies = easyRandom.objects(Movie.class, 5).toList();

        when(movieRepository.findAll()).thenReturn(movies);

        List<MovieDto> response = movieService.getMovies();

        assertNotNull(response);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testGetMovieById_Success() {
        String id = "1";
        Movie movie = easyRandom.nextObject(Movie.class);
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);

        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        when(movieMapper.toMovieDto(movie)).thenReturn(movieDto);

        MovieDto response = movieService.getMovieById(id);

        assertNotNull(response);
        verify(movieRepository, times(1)).findById(id);
    }

    @Test
    void testGetMovieById_NotFound() {
        String id = "1";
        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieById(id));
        verify(movieRepository, times(1)).findById(id);
    }

    @Test
    void testInsertMovie() {
        MovieDtoInput movieDtoInput = easyRandom.nextObject(MovieDtoInput.class);
        Movie movie = easyRandom.nextObject(Movie.class);
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);

        when(movieMapper.toMovieFromDtoInput(movieDtoInput)).thenReturn(movie);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        when(movieMapper.toMovieDto(movie)).thenReturn(movieDto);

        MovieDto response = movieService.insertMovie(movieDtoInput);

        assertNotNull(response);
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testUpdateMovie_Success() {
        String id = "1";
        MovieDtoInput movieDtoInput = easyRandom.nextObject(MovieDtoInput.class);
        Movie movie = easyRandom.nextObject(Movie.class);
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);

        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        doNothing().when(movieMapper).updateMovieFromDtoInput(movieDtoInput, movie);
        when(movieRepository.save(movie)).thenReturn(movie);
        when(movieMapper.toMovieDto(movie)).thenReturn(movieDto);

        MovieDto response = movieService.updateMovie(movieDtoInput, id);

        assertNotNull(response);
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testUpdateMovie_NotFound() {
        String id = "1";
        MovieDtoInput movieDtoInput = easyRandom.nextObject(MovieDtoInput.class);

        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.updateMovie(movieDtoInput, id));
        verify(movieRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateMoviePartial_Success() {
        String id = "1";
        MoviePatchDto moviePatchDto = easyRandom.nextObject(MoviePatchDto.class);
        Movie movie = easyRandom.nextObject(Movie.class);
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);

        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        doNothing().when(movieMapper).updateMovieFromPatchDto(moviePatchDto, movie);
        when(movieRepository.save(movie)).thenReturn(movie);
        when(movieMapper.toMovieDto(movie)).thenReturn(movieDto);

        MovieDto response = movieService.updateMoviePartial(id, moviePatchDto);

        assertNotNull(response);

        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testUpdateMoviePartial_NotFound() {
        String id = "1";
        MoviePatchDto moviePatchDto = easyRandom.nextObject(MoviePatchDto.class);

        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.updateMoviePartial(id, moviePatchDto));
        verify(movieRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteMovie_Success() {
        String id = "1";
        when(movieRepository.existsById(id)).thenReturn(true);
        doNothing().when(movieRepository).deleteById(id);
        doNothing().when(reviewRepository).deleteByMovieId(id);

        assertDoesNotThrow(() -> movieService.deleteMovie(id));

        verify(movieRepository, times(1)).deleteById(id);
        verify(reviewRepository, times(1)).deleteByMovieId(id);
    }

    @Test
    void testDeleteMovie_NotFound() {
        String id = "1";
        when(movieRepository.existsById(id)).thenReturn(false);

        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovie(id));
        verify(movieRepository, never()).deleteById(id);
    }
}
