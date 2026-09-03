package vn.iotstar.service;

import vn.iotstar.entity.User;

public interface IUserService {
    User findById(int id);

    User findByUsername(String username);

    User login(String username, String password);

    User updateProfile(int id, String fullName, String phone, String avatar);
}
