package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JPAConfig;
import vn.iotstar.dao.IUserDao;
import vn.iotstar.entity.User;

public class UserDao implements IUserDao {

    @Override
    public User findById(int id) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            return enma.find(User.class, id);
        } finally {
            enma.close();
        }
    }

    @Override
    public User findByUsername(String username) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<User> query = enma.createQuery(
                    "SELECT u FROM User u WHERE u.userName = :username", User.class);
            query.setParameter("username", username);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            enma.close();
        }
    }

    @Override
    public User login(String username, String password) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<User> query = enma.createQuery(
                    "SELECT u FROM User u WHERE u.userName = :username AND u.passWord = :password",
                    User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            enma.close();
        }
    }

    @Override
    public User updateProfile(int id, String fullName, String phone, String avatar) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            User user = enma.find(User.class, id);
            if (user == null) {
                throw new IllegalArgumentException("Khong tim thay tai khoan");
            }

            // Chi cap nhat cac truong profile, khong merge du lieu tu form.
            user.setFullName(fullName);
            user.setPhone(phone);
            if (avatar != null) {
                user.setAvatar(avatar);
            }
            trans.commit();
            return user;
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            enma.close();
        }
    }
}
