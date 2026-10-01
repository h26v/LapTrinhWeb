package vn.iotstar.service;

import vn.iotstar.entity.Author_24162046;
import vn.iotstar.util.PageResult_24162046;

import java.util.List;

public interface IAuthorService_24162046 {

    PageResult_24162046<Author_24162046> getPage(int page, int size);

    List<Author_24162046> findAll();

    Author_24162046 findById(int authorId);

    void create(Author_24162046 author);

    void update(Author_24162046 author);

    void delete(int authorId);

    long count();
}
