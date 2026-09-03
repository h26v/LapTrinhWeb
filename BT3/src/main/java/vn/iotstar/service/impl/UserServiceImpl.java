package vn.iotstar.service.impl;

import vn.iotstar.dao.IUserDao;
import vn.iotstar.dao.impl.UserDao;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;

public class UserServiceImpl implements IUserService {
    private final IUserDao userDao = new UserDao();

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        return userDao.login(username, password);
    }

    @Override
    public User updateProfile(int id, String fullName, String phone, String avatar) {
        return userDao.updateProfile(id, fullName, phone, avatar);
    }
}
