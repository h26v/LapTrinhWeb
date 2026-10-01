package vn.iotstar.service.impl;

import vn.iotstar.entity.Author_24162046;
import vn.iotstar.repository.IAuthorRepository_24162046;
import vn.iotstar.repository.impl.AuthorRepository_24162046;
import vn.iotstar.service.IAuthorService_24162046;
import vn.iotstar.util.PageResult_24162046;

import java.util.List;

public class AuthorService_24162046 implements IAuthorService_24162046 {

    private final IAuthorRepository_24162046 authorRepository = new AuthorRepository_24162046();

    @Override
    public PageResult_24162046<Author_24162046> getPage(int page, int size) {
        long total = authorRepository.count();
        int current = PageResult_24162046.normalizePage(page, size, total);
        List<Author_24162046> authors = authorRepository.findPage((current - 1) * size, size);
        return new PageResult_24162046<>(authors, current, size, total);
    }

    @Override
    public List<Author_24162046> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author_24162046 findById(int authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    public void create(Author_24162046 author) {
        author.setAuthorId(null);
        authorRepository.insert(author);
    }

    @Override
    public void update(Author_24162046 author) {
        authorRepository.update(author);
    }

    @Override
    public void delete(int authorId) {
        authorRepository.delete(authorId);
    }

    @Override
    public long count() {
        return authorRepository.count();
    }
}
