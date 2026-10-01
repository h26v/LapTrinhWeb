package vn.iotstar.repository;

import vn.iotstar.entity.Book_24162046;

import java.util.List;

public interface IBookRepository_24162046 {

    List<Book_24162046> findPage(int offset, int limit);

    long count();

    Book_24162046 findById(int bookid);

    void insert(Book_24162046 book, List<Integer> authorIds);

    void update(Book_24162046 book, List<Integer> authorIds);

    void delete(int bookid);
}
