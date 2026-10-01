package vn.iotstar.service;

import vn.iotstar.entity.Rating_24162046;

import java.util.List;

public interface IRatingService_24162046 {

    List<Rating_24162046> findByBook(int bookid);

    /**
     * Them / cap nhat review cua user cho sach.
     * @throws IllegalArgumentException neu du lieu khong hop le
     */
    void saveReview(int userid, int bookid, Integer rating, String reviewText);

    long count();
}
