package jpatesting.v1.persistence;

import jpatesting.v1.entities.User;

public interface UserDao {
    void addUser(User user);

    User getUserById(long id);

    void deleteUser(long id);
}
