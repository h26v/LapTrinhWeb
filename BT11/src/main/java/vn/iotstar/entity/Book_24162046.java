package vn.iotstar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import vn.iotstar.util.Constant_24162046;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "books")
public class Book_24162046 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookid")
    private Integer bookid;

    @Column(name = "isbn")
    private Integer isbn;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "publisher", length = 100)
    private String publisher;

    @Column(name = "price", precision = 6, scale = 2)
    private BigDecimal price;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "cover_image", length = 100)
    private String coverImage;

    @Column(name = "quantity")
    private Integer quantity;

    // Bang trung gian book_author (bookid, author_id)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "bookid"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author_24162046> authors = new LinkedHashSet<>();

    // So luong review, duoc service tinh va gan vao khi hien thi
    @Transient
    private long reviewCount;

    public Book_24162046() {
    }

    /** Danh sach ten tac gia, cach nhau bang dau phay. */
    public String getAuthorNames() {
        return authors.stream()
                .map(Author_24162046::getAuthorName)
                .collect(Collectors.joining(", "));
    }

    /** Con hang hay khong. Dung trong JSP: ${book.inStock} */
    public boolean isInStock() {
        return quantity != null && quantity > 0;
    }

    /** So luong toi da duoc mua: khong vuot ton kho va khong qua CART_MAX_PER_ITEM. */
    public int getMaxOrderQuantity() {
        int stock = quantity == null ? 0 : Math.max(quantity, 0);
        return Math.min(stock, Constant_24162046.CART_MAX_PER_ITEM);
    }

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<Author_24162046> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author_24162046> authors) {
        this.authors = authors;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(long reviewCount) {
        this.reviewCount = reviewCount;
    }
}
