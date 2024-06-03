package org.moldidev.moldispizza.mapper;

import org.moldidev.moldispizza.dto.ReviewDTO;
import org.moldidev.moldispizza.entity.Review;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ReviewDTOMapper implements Function<Review, ReviewDTO> {
    private final UserDTOMapper userDTOMapper;
    private final PizzaDTOMapper pizzaDTOMapper;

    public ReviewDTOMapper(UserDTOMapper userDTOMapper, PizzaDTOMapper pizzaDTOMapper) {
        this.userDTOMapper = userDTOMapper;
        this.pizzaDTOMapper = pizzaDTOMapper;
    }

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
