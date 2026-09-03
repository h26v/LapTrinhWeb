package vn.iotstar.dao;

import vn.iotstar.entity.User;

public interface IUserDao {
    User findById(int id);

    User findByUsername(String username);

    User login(String username, String password);

    /**
     * Updates only the profile fields supplied by the authenticated user.
     * The returned entity is detached after the DAO closes its EntityManager.
     */
    User updateProfile(int id, String fullName, String phone, String avatar);
}
