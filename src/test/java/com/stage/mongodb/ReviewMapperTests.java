package com.stage.mongodb;

import com.stage.mongodb.dto.ReviewDto;
import com.stage.mongodb.dto.ReviewDtoInput;
import com.stage.mongodb.dto.ReviewPatchDto;
import com.stage.mongodb.mapper.MovieMapper;
import com.stage.mongodb.mapper.ReviewMapper;
import com.stage.mongodb.model.Review;
import com.stage.mongodb.repository.MovieRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReviewMapperTests {

    private ReviewMapper reviewMapper;
    private EasyRandom easyRandom;
    private MovieMapper movieMapper;
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {

        easyRandom = new EasyRandom();
        reviewMapper = new ReviewMapper(movieMapper, movieRepository);
    }

    @Test
    void testToReviewDto() {

        Review review = easyRandom.nextObject(Review.class);
        review.setId("1");
        review.setInsertDate(Instant.now());
        review.setUpdateDate(Instant.now());

        ReviewDto reviewDto = reviewMapper.toReviewDto(review);

        assertNotNull(reviewDto);
        assertNotNull(reviewDto.getInsertDate());
        assertNotNull(reviewDto.getUpdateDate());
        assertNotNull(reviewDto.getComment());

        assertEquals(review.getRating(), reviewDto.getRating());
        assertEquals(review.getComment(), reviewDto.getComment());
    }

    @Test
    void testToReviewFromDtoInput() {
        ReviewDtoInput reviewDtoInput = easyRandom.nextObject(ReviewDtoInput.class);

        Review review = reviewMapper.toReviewFromDtoInput(reviewDtoInput);

        assertNotNull(review);
        assertNotNull(review.getMovieId());
        assertNotNull(review.getComment());

        assertEquals(reviewDtoInput.getMovieId(), review.getMovieId());
        assertEquals(reviewDtoInput.getRating(), review.getRating());
        assertEquals(reviewDtoInput.getComment(), review.getComment());

    }

    @Test
    void testUpdateReviewFromDtoInput() {

        Review existingReview = easyRandom.nextObject(Review.class);
        ReviewDtoInput reviewDtoInput = easyRandom.nextObject(ReviewDtoInput.class);

        existingReview.setId("1");

        reviewMapper.updateReviewFromDtoInput(reviewDtoInput, existingReview);

        assertNotNull(existingReview.getId());
        assertNotNull(existingReview.getMovieId());
        assertNotNull(existingReview.getComment());
        assertNotNull(existingReview.getInsertDate());
        assertNotNull(existingReview.getUpdateDate());

        assertEquals(reviewDtoInput.getRating(), existingReview.getRating());
        assertEquals(reviewDtoInput.getComment(), existingReview.getComment());
    }

    @Test
    void testUpdateReviewFromPatchDto() {

        Review existingReview = easyRandom.nextObject(Review.class);

        ReviewPatchDto reviewPatchDto = easyRandom.nextObject(ReviewPatchDto.class);
        existingReview.setId("1");
        reviewPatchDto.setComment(null);

        reviewMapper.updateReviewFromPatchDto(reviewPatchDto, existingReview);

        assertNotNull(existingReview.getId());
        assertNotNull(existingReview.getMovieId());

        assertNotNull(existingReview.getComment());
        assertNotNull(existingReview.getInsertDate());
        assertNotNull(existingReview.getUpdateDate());

    }


}
