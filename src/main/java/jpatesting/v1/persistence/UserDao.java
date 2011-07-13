package jpatesting.v1.persistence;

public interface UserDao {
    void addUser(User user);

    User getUserById(long id);

    void deleteUser(long id);
}
