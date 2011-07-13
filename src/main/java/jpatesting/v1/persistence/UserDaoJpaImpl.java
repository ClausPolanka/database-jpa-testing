package jpatesting.v1.persistence;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UserDaoJpaImpl implements UserDao {
    private EntityManager entityManager;

    public void addUser(User user) {
        entityManager.persist(user);
    }

    public User getUserById(long id) {
        return entityManager.find(User.class, id);
    }

    public void deleteUser(long id) {
        String jql = "delete User where id = ?";
        Query query = entityManager.createQuery(jql);
        query.setParameter(1, id);
        query.executeUpdate();
    }
}
