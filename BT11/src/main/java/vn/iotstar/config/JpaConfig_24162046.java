package vn.iotstar.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Layer - cau hinh JPA (EntityManagerFactory dung chung toan ung dung).
 * Co the ghi de thong tin ket noi bang System property: db.url, db.user, db.password, db.driver, db.dialect.
 */
public class JpaConfig_24162046 {

    private static final String PERSISTENCE_UNIT = "bookstore-24162046";

    private static final EntityManagerFactory FACTORY = buildFactory();

    private static EntityManagerFactory buildFactory() {
        Map<String, Object> overrides = new HashMap<>();
        override(overrides, "db.url", "jakarta.persistence.jdbc.url");
        override(overrides, "db.user", "jakarta.persistence.jdbc.user");
        override(overrides, "db.password", "jakarta.persistence.jdbc.password");
        override(overrides, "db.driver", "jakarta.persistence.jdbc.driver");
        override(overrides, "db.dialect", "hibernate.dialect");
        override(overrides, "db.ddl", "hibernate.hbm2ddl.auto");
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, overrides);
    }

    private static void override(Map<String, Object> overrides, String sysProp, String jpaProp) {
        String value = System.getProperty(sysProp);
        if (value != null && !value.isBlank()) {
            overrides.put(jpaProp, value);
        }
    }

    /**
     * Tra ve EntityManager moi. Nguoi goi phai tu dong (close).
     */
    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
}
