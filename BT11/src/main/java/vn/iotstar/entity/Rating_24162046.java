package vn.iotstar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "rating")
public class Rating_24162046 implements Serializable {

    @EmbeddedId
    private RatingId_24162046 id;

    @MapsId("userid")
    @ManyToOne
    @JoinColumn(name = "userid")
    private User_24162046 user;

    @MapsId("bookid")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookid")
    private Book_24162046 book;

    @Column(name = "rating", columnDefinition = "tinyint")
    private Integer rating;

    @Column(name = "review_text", columnDefinition = "text")
    private String reviewText;

    public Rating_24162046() {
    }

    public RatingId_24162046 getId() {
        return id;
    }

    public void setId(RatingId_24162046 id) {
        this.id = id;
    }

    public User_24162046 getUser() {
        return user;
    }

    public void setUser(User_24162046 user) {
        this.user = user;
    }

    public Book_24162046 getBook() {
        return book;
    }

    public void setBook(Book_24162046 book) {
        this.book = book;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
