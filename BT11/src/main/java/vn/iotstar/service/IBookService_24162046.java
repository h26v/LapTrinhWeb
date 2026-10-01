package vn.iotstar.service;

import vn.iotstar.entity.Book_24162046;
import vn.iotstar.util.PageResult_24162046;

import java.util.List;

public interface IBookService_24162046 {

    /** Lay 1 trang sach, co kem so luong review cua tung sach. */
    PageResult_24162046<Book_24162046> getPage(int page, int size);

    /** Lay chi tiet sach, co kem so luong review. */
    Book_24162046 findById(int bookid);

    void create(Book_24162046 book, List<Integer> authorIds);

    void update(Book_24162046 book, List<Integer> authorIds);

    void delete(int bookid);

    long count();
}
