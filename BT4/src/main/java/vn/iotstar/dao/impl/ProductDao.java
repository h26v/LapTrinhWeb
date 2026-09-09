package vn.iotstar.dao.impl;

import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JPAConfig;
import vn.iotstar.dao.IProductDao;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;

public class ProductDao implements IProductDao {

    @Override
    public void insert(Product product) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            if (product.getCategory() != null) {
                product.setCategory(enma.getReference(Category.class, product.getCategory().getCategoryid()));
            }
            if (product.getCreatedDate() == null) {
                product.setCreatedDate(new Date());
            }
            enma.persist(product);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void update(Product product) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            Product old = enma.find(Product.class, product.getProductId());
            if (old == null) {
                throw new IllegalArgumentException("Khong tim thay san pham");
            }
            old.setProductName(product.getProductName());
            old.setDescription(product.getDescription());
            old.setPrice(product.getPrice());
            old.setStatus(product.getStatus());
            if (product.getImages() != null) {
                old.setImages(product.getImages());
            }
            if (product.getCategory() != null) {
                old.setCategory(enma.getReference(Category.class, product.getCategory().getCategoryid()));
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            Product product = enma.find(Product.class, id);
            if (product == null) {
                throw new Exception("Khong tim thay san pham co id = " + id);
            }
            enma.remove(product);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public Product findById(int id) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = enma.createQuery(
                    "SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.productId = :id",
                    Product.class);
            query.setParameter("id", id);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Product> findAll() {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = enma.createQuery(
                    "SELECT p FROM Product p LEFT JOIN FETCH p.category ORDER BY p.createdDate DESC, p.productId DESC",
                    Product.class);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Product> findAll(int page, int pagesize) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = enma.createQuery(
                    "SELECT p FROM Product p LEFT JOIN FETCH p.category ORDER BY p.createdDate DESC, p.productId DESC",
                    Product.class);
            query.setFirstResult((page - 1) * pagesize);
            query.setMaxResults(pagesize);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public int count() {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = enma.createQuery("SELECT count(p) FROM Product p", Long.class);
            return query.getSingleResult().intValue();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Product> findActive(int page, int pagesize) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = enma.createQuery(
                    "SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.status = 1 ORDER BY p.createdDate DESC, p.productId DESC",
                    Product.class);
            query.setFirstResult((page - 1) * pagesize);
            query.setMaxResults(pagesize);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public int countActive() {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = enma.createQuery(
                    "SELECT count(p) FROM Product p WHERE p.status = 1", Long.class);
            return query.getSingleResult().intValue();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Product> findNewestActive(int limit) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = enma.createQuery(
                    "SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.status = 1 ORDER BY p.createdDate DESC, p.productId DESC",
                    Product.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }
}
