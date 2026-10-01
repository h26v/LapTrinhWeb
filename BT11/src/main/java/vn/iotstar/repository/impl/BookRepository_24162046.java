package vn.iotstar.repository.impl;

import jakarta.persistence.EntityManager;
import vn.iotstar.entity.Author_24162046;
import vn.iotstar.entity.Book_24162046;
import vn.iotstar.repository.IBookRepository_24162046;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BookRepository_24162046 extends AbstractRepository_24162046 implements IBookRepository_24162046 {

    @Override
    public List<Book_24162046> findPage(int offset, int limit) {
        return query(em -> em.createQuery(
                        "SELECT b FROM Book_24162046 b ORDER BY b.bookid DESC", Book_24162046.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList());
    }

    @Override
    public long count() {
        return query(em -> em.createQuery("SELECT COUNT(b) FROM Book_24162046 b", Long.class)
                .getSingleResult());
    }

    @Override
    public Book_24162046 findById(int bookid) {
        return query(em -> em.find(Book_24162046.class, bookid));
    }

    @Override
    public void insert(Book_24162046 book, List<Integer> authorIds) {
        transaction(em -> {
            book.setAuthors(authorReferences(em, authorIds));
            em.persist(book);
        });
    }

    @Override
    public void update(Book_24162046 book, List<Integer> authorIds) {
        transaction(em -> {
            book.setAuthors(authorReferences(em, authorIds));
            em.merge(book);
        });
    }

    @Override
    public void delete(int bookid) {
        transaction(em -> {
            Book_24162046 book = em.find(Book_24162046.class, bookid);
            if (book == null) {
                return;
            }
            em.createQuery("DELETE FROM Rating_24162046 r WHERE r.id.bookid = :id")
                    .setParameter("id", bookid)
                    .executeUpdate();
            // Bo sach khoi gio hang cua moi user
            em.createNativeQuery("DELETE FROM cart_items WHERE bookid = ?1")
                    .setParameter(1, bookid)
                    .executeUpdate();
            // Don cu van giu ten + gia sach, chi bo lien ket
            em.createNativeQuery("UPDATE order_items SET bookid = NULL WHERE bookid = ?1")
                    .setParameter(1, bookid)
                    .executeUpdate();
            book.getAuthors().clear(); // xoa dong trong book_author
            em.remove(book);
        });
    }

    private Set<Author_24162046> authorReferences(EntityManager em, List<Integer> authorIds) {
        Set<Author_24162046> authors = new LinkedHashSet<>();
        if (authorIds != null) {
            for (Integer id : authorIds) {
                Author_24162046 author = em.find(Author_24162046.class, id);
                if (author != null) {
                    authors.add(author);
                }
            }
        }
        return authors;
    }
}
