package vn.iotstar.service.impl;

import vn.iotstar.entity.Rating_24162046;
import vn.iotstar.repository.IRatingRepository_24162046;
import vn.iotstar.repository.impl.RatingRepository_24162046;
import vn.iotstar.service.IRatingService_24162046;

import java.util.List;

public class RatingService_24162046 implements IRatingService_24162046 {

    private final IRatingRepository_24162046 ratingRepository = new RatingRepository_24162046();

    @Override
    public List<Rating_24162046> findByBook(int bookid) {
        return ratingRepository.findByBook(bookid);
    }

    @Override
    public void saveReview(int userid, int bookid, Integer rating, String reviewText) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Số sao phải từ 1 đến 5");
        }
        if (reviewText == null || reviewText.isBlank()) {
            throw new IllegalArgumentException("Nội dung review không được để trống");
        }
        ratingRepository.save(userid, bookid, rating, reviewText.trim());
    }

    @Override
    public long count() {
        return ratingRepository.count();
    }
}
