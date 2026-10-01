package vn.iotstar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Khoa chinh ghep cua bang rating (userid, bookid).
 */
@Embeddable
public class RatingId_24162046 implements Serializable {

    @Column(name = "userid")
    private Integer userid;

    @Column(name = "bookid")
    private Integer bookid;

    public RatingId_24162046() {
    }

    public RatingId_24162046(Integer userid, Integer bookid) {
        this.userid = userid;
        this.bookid = bookid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RatingId_24162046 other)) return false;
        return Objects.equals(userid, other.userid) && Objects.equals(bookid, other.bookid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, bookid);
    }
}
