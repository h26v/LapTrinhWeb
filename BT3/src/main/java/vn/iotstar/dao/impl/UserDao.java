package vn.iotstar.dao.impl;

import java.util.Date;

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
    public User findByEmail(String email) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<User> query = enma.createQuery(
                    "SELECT u FROM User u WHERE lower(u.email) = :email", User.class);
            query.setParameter("email", email == null ? null : email.toLowerCase());
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            enma.close();
        }
    }

    @Override
    public User login(String username, String password) {
        // Password duoc kiem tra o service de ho tro BCrypt va legacy plain text.
        return findByUsername(username);
    }

    @Override
    public void insert(User user) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(user);
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
    public void update(User user) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.merge(user);
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
    public void updatePassword(int id, String passwordHash) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            User user = enma.find(User.class, id);
            if (user == null) {
                throw new IllegalArgumentException("Khong tim thay tai khoan");
            }
            user.setPassWord(passwordHash);
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
    public void activateUser(int id) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            User user = enma.find(User.class, id);
            if (user == null) {
                throw new IllegalArgumentException("Khong tim thay tai khoan");
            }
            user.setActive(1);
            user.setActivationOtpHash(null);
            user.setActivationOtpExpiresAt(null);
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
    public void saveActivationOtp(int id, String otpHash, Date expiresAt) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            User user = enma.find(User.class, id);
            if (user == null) {
                throw new IllegalArgumentException("Khong tim thay tai khoan");
            }
            user.setActivationOtpHash(otpHash);
            user.setActivationOtpExpiresAt(expiresAt);
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
    public void saveResetOtp(int id, String otpHash, Date expiresAt) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            User user = enma.find(User.class, id);
            if (user == null) {
                throw new IllegalArgumentException("Khong tim thay tai khoan");
            }
            user.setResetOtpHash(otpHash);
            user.setResetOtpExpiresAt(expiresAt);
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
    public void clearActivationOtp(int id) {
        saveActivationOtp(id, null, null);
    }

    @Override
    public void clearResetOtp(int id) {
        saveResetOtp(id, null, null);
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
