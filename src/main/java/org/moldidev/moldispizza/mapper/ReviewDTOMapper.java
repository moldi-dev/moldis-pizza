package org.moldidev.moldispizza.mapper;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.ReviewDTO;
import org.moldidev.moldispizza.entity.Review;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReviewDTOMapper implements Function<Review, ReviewDTO> {

    private final UserDTOMapper userDTOMapper;
    private final PizzaDTOMapper pizzaDTOMapper;

    @Override
    public ReviewDTO apply(Review review) {
        return new ReviewDTO(
                review.getReviewId(),
                userDTOMapper.apply(review.getUser()),
                review.getRating(),
                review.getComment(),
                pizzaDTOMapper.apply(review.getPizza())
        );
    }
}
