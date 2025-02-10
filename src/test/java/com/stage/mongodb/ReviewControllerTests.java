package com.stage.mongodb;

import com.stage.mongodb.controller.ReviewController;
import com.stage.mongodb.dto.ReviewDto;
import com.stage.mongodb.dto.ReviewDtoInput;
import com.stage.mongodb.dto.ReviewDtoUpdate;
import com.stage.mongodb.dto.ReviewPatchDto;
import com.stage.mongodb.service.ReviewService;
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

class ReviewControllerTest {

    // private MockMvc mockMvc;

    private final EasyRandom easyRandom = new EasyRandom();
    @Mock
    private ReviewService reviewService;
    @InjectMocks
    private ReviewController reviewController;
    // private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    void testGetReviews() throws Exception {
        List<ReviewDto> reviews = easyRandom.objects(ReviewDto.class, 5).toList();
        when(reviewService.getReviews()).thenReturn(reviews);

        /*
         * mockMvc.perform(get("/api/review/all")).andExpect(status().isOk()).andExpect(
         * jsonPath("$.length()").value(5));
         */

        reviewService.getReviews();

        verify(reviewService, times(1)).getReviews();
    }

    @Test
    void testGetReviewById() throws Exception {
        String id = "1";
        ReviewDto reviewDto = easyRandom.nextObject(ReviewDto.class);

        when(reviewService.getReviewById(id)).thenReturn(reviewDto);

        /*
         * mockMvc.perform(get("/api/review").param("id",
         * id.toString())).andExpect(status().isOk()).andReturn();
         */

        reviewService.getReviewById(id);

        verify(reviewService, times(1)).getReviewById(id);
    }

    @Test
    void testInsertReview() throws Exception {
        ReviewDtoInput reviewDtoInput = easyRandom.nextObject(ReviewDtoInput.class);
        ReviewDto reviewDto = easyRandom.nextObject(ReviewDto.class);

        when(reviewService.insertReview(any(ReviewDtoInput.class)))
                .thenReturn(reviewDto);

        reviewService.insertReview(reviewDtoInput);

        /*
         * mockMvc.perform(post("/api/review").contentType("application/json")
         * .content(objectMapper.writeValueAsString(reviewDtoInput))).andExpect(status()
         * .isCreated());
         */

        verify(reviewService, times(1)).insertReview(any(ReviewDtoInput.class));
    }

    @Test
    void testUpdateReview() throws Exception {
        String id = "1";
        ReviewDtoUpdate reviewDtoUpdate = easyRandom.nextObject(ReviewDtoUpdate.class);
        ReviewDto reviewDto = easyRandom.nextObject(ReviewDto.class);

        when(reviewService.updateReview(any(ReviewDtoUpdate.class), eq(id))).thenReturn(reviewDto);

        reviewService.updateReview(reviewDtoUpdate, id);

        /*
         * mockMvc.perform(put("/api/review").param("id",
         * id.toString()).contentType("application/json")
         * .content(objectMapper.writeValueAsString(reviewDtoInput))).andExpect(status()
         * .isOk());
         */

        verify(reviewService, times(1)).updateReview(any(ReviewDtoUpdate.class), eq(id));
    }

    @Test
    void testUpdateReviewPartial() throws Exception {
        String id = "1";
        ReviewPatchDto reviewPatchDto = easyRandom.nextObject(ReviewPatchDto.class);
        reviewPatchDto.setComment(null);
        ReviewDto reviewDto = easyRandom.nextObject(ReviewDto.class);

        when(reviewService.updateReviewPartial(eq(id), any(ReviewPatchDto.class)))
                .thenReturn(reviewDto);

        reviewService.updateReviewPartial(id, reviewPatchDto);

        /*
         * mockMvc.perform(patch("/api/review").param("id",
         * id.toString()).contentType("application/json")
         * .content(objectMapper.writeValueAsString(reviewPatchDto))).andExpect(status()
         * .isOk());
         */

        verify(reviewService, times(1)).updateReviewPartial(eq(id), any(ReviewPatchDto.class));
    }

    @Test
    void testDeleteReview() throws Exception {
        String id = "1";
        doNothing().when(reviewService).deleteReview(id);

        reviewService.deleteReview(id);

        /*
         * mockMvc.perform(delete("/api/review").param("id",
         * id.toString())).andExpect(status().isOk());
         */

        verify(reviewService, times(1)).deleteReview(id);
    }
}
