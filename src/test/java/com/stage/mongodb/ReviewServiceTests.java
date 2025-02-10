package com.stage.mongodb;

import com.stage.mongodb.dto.*;
import com.stage.mongodb.exceptions.MovieNotFoundException;
import com.stage.mongodb.exceptions.ReviewNotFoundException;
import com.stage.mongodb.mapper.MovieMapper;
import com.stage.mongodb.mapper.ReviewMapper;
import com.stage.mongodb.model.Movie;
import com.stage.mongodb.model.Review;
import com.stage.mongodb.repository.MovieRepository;
import com.stage.mongodb.repository.ReviewRepository;
import com.stage.mongodb.service.ReviewService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private ReviewService reviewService;

    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        easyRandom = new EasyRandom();

    }


    @Test
    void testGetReviewById_Success() {
        String id = "1";
        String movieId = "1234";
        Review review = easyRandom.nextObject(Review.class);
        review.setId(id);
        review.setMovieId(movieId);
        ReviewDto reviewDto = easyRandom.nextObject(ReviewDto.class);
        Movie movie = easyRandom.nextObject(Movie.class);
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(reviewMapper.toReviewDto(review)).thenReturn(reviewDto);
        when(movieMapper.toMovieDto(any(Movie.class))).thenReturn(easyRandom.nextObject(MovieDto.class));
        when(movieRepository.findById(review.getMovieId())).thenReturn(Optional.of(movie));

        ReviewDto response = reviewService.getReviewById(id);

        assertNotNull(response);
        verify(reviewRepository, times(1)).findById(id);
    }

    @Test
    void testGetReviewById_NotFound() {
        String id = "1";
        when(reviewRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReviewById(id));
        verify(reviewRepository, times(1)).findById(id);
    }

    @Test
    void testInsertReview() {
        ReviewDtoInput reviewDtoInput = easyRandom.nextObject(ReviewDtoInput.class);
        Review review = easyRandom.nextObject(Review.class);
        ReviewDto reviewDto = easyRandom.nextObject(ReviewDto.class);
        Movie movie = easyRandom.nextObject(Movie.class);

        when(movieRepository.existsById(reviewDtoInput.getMovieId())).thenReturn(true);
        when(movieRepository.findById(reviewDtoInput.getMovieId())).thenReturn(Optional.of(movie));
        when(reviewMapper.toReviewFromDtoInput(reviewDtoInput)).thenReturn(review);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.toReviewDto(review)).thenReturn(reviewDto);

        ReviewDto response = reviewService.insertReview(reviewDtoInput);

        assertNotNull(response);

        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testInsertReview_MovieNotFound() {
        ReviewDtoInput reviewDtoInput = easyRandom.nextObject(ReviewDtoInput.class);

        when(movieRepository.existsById(reviewDtoInput.getMovieId())).thenReturn(false);

        assertThrows(MovieNotFoundException.class, () -> reviewService.insertReview(reviewDtoInput));
        verify(movieRepository, times(1)).existsById(reviewDtoInput.getMovieId());
    }

    @Test
    void testUpdateReview_Success() {
        String id = "1";
        ReviewDtoUpdate reviewDtoUpdate = easyRandom.nextObject(ReviewDtoUpdate.class);
        Review review = easyRandom.nextObject(Review.class);
        ReviewDto reviewDto = easyRandom.nextObject(ReviewDto.class);
        Movie movie = easyRandom.nextObject(Movie.class);

        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(movieRepository.findById(review.getMovieId())).thenReturn(Optional.of(movie));
        doNothing().when(reviewMapper).updateReviewFromDtoUpdate(reviewDtoUpdate, review);
        when(reviewRepository.save(review)).thenReturn(review);
        when(reviewMapper.toReviewDto(review)).thenReturn(reviewDto);

        ReviewDto response = reviewService.updateReview(reviewDtoUpdate, id);

        assertNotNull(response);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testUpdateReview_NotFound() {
        String id = "1";
        ReviewDtoUpdate reviewDtoUpdate = easyRandom.nextObject(ReviewDtoUpdate.class);

        when(reviewRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.updateReview(reviewDtoUpdate, id));
        verify(reviewRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateReviewPartial_Success() {
        String id = "1";
        ReviewPatchDto reviewPatchDto = easyRandom.nextObject(ReviewPatchDto.class);
        Review review = easyRandom.nextObject(Review.class);
        ReviewDto reviewDto = easyRandom.nextObject(ReviewDto.class);
        Movie movie = easyRandom.nextObject(Movie.class);

        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(movieRepository.findById(review.getMovieId())).thenReturn(Optional.of(movie));
        doNothing().when(reviewMapper).updateReviewFromPatchDto(reviewPatchDto, review);
        when(reviewRepository.save(review)).thenReturn(review);
        when(reviewMapper.toReviewDto(review)).thenReturn(reviewDto);

        ReviewDto response = reviewService.updateReviewPartial(id, reviewPatchDto);

        assertNotNull(response);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testUpdateReviewPartial_NotFound() {
        String id = "1";
        ReviewPatchDto reviewPatchDto = easyRandom.nextObject(ReviewPatchDto.class);

        when(reviewRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.updateReviewPartial(id, reviewPatchDto));
        verify(reviewRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteReview_Success() {
        String id = "1";
        when(reviewRepository.existsById(id)).thenReturn(true);
        doNothing().when(reviewRepository).deleteById(id);

        assertDoesNotThrow(() -> reviewService.deleteReview(id));

        verify(reviewRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteReview_NotFound() {
        String id = "1";
        when(reviewRepository.existsById(id)).thenReturn(false);

        assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview(id));
        verify(reviewRepository, never()).deleteById(id);
    }
}
