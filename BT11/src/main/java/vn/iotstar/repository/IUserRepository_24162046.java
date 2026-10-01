package vn.iotstar.repository;

import vn.iotstar.entity.User_24162046;

public interface IUserRepository_24162046 {

    User_24162046 findByEmail(String email);

    User_24162046 findById(int id);

    long count();

    void insert(User_24162046 user);

    void update(User_24162046 user);
}
