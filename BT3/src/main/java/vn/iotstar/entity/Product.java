package vn.iotstar.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "products")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p ORDER BY p.createdDate DESC, p.productId DESC")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private int productId;

    @Column(name = "productname", columnDefinition = "nvarchar(255) not null")
    private String productName;

    @Column(name = "description", columnDefinition = "nvarchar(1000) null")
    private String description;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "images", columnDefinition = "nvarchar(500) null")
    private String images;

    @Column(name = "status")
    private int status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createddate")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    public Product() {
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
