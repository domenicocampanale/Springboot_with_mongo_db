package com.stage.mongodb.mapper;

import com.stage.mongodb.dto.*;
import com.stage.mongodb.model.Movie;
import com.stage.mongodb.model.Review;
import com.stage.mongodb.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class ReviewMapper {

    private final MovieMapper movieMapper;
    private final MovieRepository movieRepository;

    public ReviewDto toReviewDto(Review review, Movie movie) {

        MovieDto movieDto = movieMapper.toMovieDto(movie);

        return ReviewDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .insertDate(formatData(review.getInsertDate()))
                .movieDto(movieDto)
                .updateDate(formatData(review.getUpdateDate()))
                .build();
    }

    public ReviewDto toReviewDto(Review review) {

        return ReviewDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .insertDate(formatData(review.getInsertDate()))
                .updateDate(formatData(review.getUpdateDate()))
                .build();
    }

    public Review toReviewFromDtoInput(ReviewDtoInput reviewDtoInput) {
        if (reviewDtoInput == null) {
            return null;
        }

        return Review.builder()
                .movieId(reviewDtoInput.getMovieId())
                .rating(reviewDtoInput.getRating())
                .comment(reviewDtoInput.getComment())
                .build();
    }


    public void updateReviewFromDtoInput(ReviewDtoInput reviewDtoInput, Review review) {
        if (reviewDtoInput == null) {
            return;
        }

        review.setMovieId(reviewDtoInput.getMovieId());
        review.setRating(reviewDtoInput.getRating());
        review.setComment(reviewDtoInput.getComment());
    }


    public void updateReviewFromDtoUpdate(ReviewDtoUpdate reviewDtoUpdate, Review review) {
        if (reviewDtoUpdate == null) {
            return;
        }

        review.setRating(reviewDtoUpdate.getRating());
        review.setComment(reviewDtoUpdate.getComment());
    }


    public void updateReviewFromPatchDto(ReviewPatchDto patchDto, Review review) {
        if (patchDto == null) {
            return;
        }


        Optional.of(patchDto.getRating())
                .ifPresent(review::setRating);


        Optional.ofNullable(patchDto.getComment())
                .ifPresent(review::setComment);

    }


    public String formatData(Instant data) {

        if (data == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return data.atZone(java.time.ZoneId.systemDefault()).format(formatter);
    }

    public List<ReviewDto> listOfReviewsDto(List<Review> reviews, List<Movie> movies) {

        Map<String, MovieDto> movieDtoMap = movies.stream()
                .collect(Collectors.toMap(Movie::getId, movieMapper::toMovieDto));

        return reviews.stream().map(review -> {
            ReviewDto reviewDto = toReviewDto(review);

            reviewDto.setMovieDto(movieDtoMap.get(review.getMovieId()));

            return reviewDto;
        }).collect(Collectors.toList());
    }

}
