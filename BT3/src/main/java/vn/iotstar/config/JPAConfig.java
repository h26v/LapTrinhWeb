package vn.iotstar.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAConfig {
    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory("jpa-hibernate-mysql");

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void shutdown() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
