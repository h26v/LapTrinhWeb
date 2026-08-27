package vn.iotstar.service;

import vn.iotstar.entity.User;

public interface IUserService {
    User findByUsername(String username);

    User login(String username, String password);
}
