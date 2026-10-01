package vn.iotstar.repository;

import vn.iotstar.entity.Author_24162046;

import java.util.List;

public interface IAuthorRepository_24162046 {

    List<Author_24162046> findPage(int offset, int limit);

    List<Author_24162046> findAll();

    long count();

    Author_24162046 findById(int authorId);

    void insert(Author_24162046 author);

    void update(Author_24162046 author);

    void delete(int authorId);
}
