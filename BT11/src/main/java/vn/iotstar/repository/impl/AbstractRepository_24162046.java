package vn.iotstar.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig_24162046;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Lop cha cho cac repository: quan ly mo/dong EntityManager va transaction.
 */
public abstract class AbstractRepository_24162046 {

    protected <R> R query(Function<EntityManager, R> work) {
        EntityManager em = JpaConfig_24162046.getEntityManager();
        try {
            return work.apply(em);
        } finally {
            em.close();
        }
    }

    protected void transaction(Consumer<EntityManager> work) {
        EntityManager em = JpaConfig_24162046.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            work.accept(em);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
