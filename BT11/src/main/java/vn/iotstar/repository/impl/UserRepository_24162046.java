package vn.iotstar.repository.impl;

import vn.iotstar.entity.User_24162046;
import vn.iotstar.repository.IUserRepository_24162046;

public class UserRepository_24162046 extends AbstractRepository_24162046 implements IUserRepository_24162046 {

    @Override
    public User_24162046 findByEmail(String email) {
        return query(em -> em.createQuery(
                        "SELECT u FROM User_24162046 u WHERE u.email = :email", User_24162046.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null));
    }

    @Override
    public User_24162046 findById(int id) {
        return query(em -> em.find(User_24162046.class, id));
    }

    @Override
    public long count() {
        return query(em -> em.createQuery("SELECT COUNT(u) FROM User_24162046 u", Long.class)
                .getSingleResult());
    }

    @Override
    public void insert(User_24162046 user) {
        transaction(em -> em.persist(user));
    }

    @Override
    public void update(User_24162046 user) {
        transaction(em -> em.merge(user));
    }
}
