package jpatesting.v1.dataaccess;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Settings;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.impl.SessionFactoryImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;

public abstract class AbstractJpaTestCase {
    private static EntityManagerFactory entityManagerFactory;
    protected static Connection connection;
    protected EntityManager em;
    private EntityManager entityManager;

    @BeforeClass
    public static void setupDatabase() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        connection = getConnection(entityManagerFactory);
    }

    public static Connection getConnection(Object object) throws Exception {
        Connection connection = null;
        if (object instanceof EntityManagerFactoryImpl) {
            EntityManagerFactoryImpl impl = (EntityManagerFactoryImpl) object;
            SessionFactory sessionFactory = impl.getSessionFactory();
            if (sessionFactory instanceof SessionFactoryImpl) {
                SessionFactoryImpl sfi = (SessionFactoryImpl) sessionFactory;
                Settings settings = sfi.getSettings();
                ConnectionProvider provider = settings.getConnectionProvider();
                connection = provider.getConnection();
            }
        }
        return connection;
    }

    @AfterClass
    public static void closeDatabase() throws Exception {
        if (connection != null) {
            connection.close();
            connection = null;
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Before
    public void setEntityManager() {
        em = entityManagerFactory.createEntityManager();
    }

    @After
    public void closeEntityManager() {
        em.close();
    }

    protected void beginTransaction() {
        em.getTransaction().begin();
    }

    protected void commitTransaction() {
        em.getTransaction().commit();
    }

    protected void commitTransaction(boolean clearContext) {
        commitTransaction();
        if (clearContext) {
            em.clear();
        }
    }
}
