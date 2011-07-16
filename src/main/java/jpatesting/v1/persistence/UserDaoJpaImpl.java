package jpatesting.v1.persistence;

import jpatesting.v1.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class UserDaoJpaImpl implements UserDao {
    private EntityManager entityManager;

    public void addUser(User user) {
        entityManager.persist(user);
    }

    public User getUserById(long id) {
        String jql = "select user from User user left join fetch user.telephones where id = ?";
        Query query = entityManager.createQuery(jql);
        query.setParameter(1, id);
        @SuppressWarnings("unchecked")
        List<User> users = query.getResultList();
        return users.isEmpty() ? null : (User) users.get(0);
    }

    public void deleteUser(long id) {
        User user = getUserById(id);
        entityManager.remove(user);
    }

    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }
}
