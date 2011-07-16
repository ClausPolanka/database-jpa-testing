package jpatesting.v1.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Settings;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.event.EventListeners;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.impl.SessionImpl;
import org.hibernate.tool.ant.JPAConfigurationTask;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;

public abstract class AbstractJpaTestCase {

    private static EntityManagerFactory entityManagerFactory;
    protected static Connection connection;
    protected EntityManager em;

    @BeforeClass
    public static void setupDatabase() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        connection = getConnection(entityManagerFactory);
    }

    @AfterClass
    public static void closeDatabase() throws Exception {
        if (connection != null) {
            // TODO: hsqldb was supposed to shutdown on close, but it isn't
            connection.prepareStatement("SHUTDOWN").execute();
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
        // change if statement below to true to figure out the Hibernate listeners
        if (false) {
            Object delegate = em.getDelegate();
            SessionImpl session = (SessionImpl) delegate;
            EventListeners listeners = session.getListeners();
            PostInsertEventListener[] originalPostInsertEventListener = listeners.getPostInsertEventListeners();
            for (PostInsertEventListener listener : originalPostInsertEventListener) {
                System.err.println(">>> PostInsertEventListener: " + listener.getClass().getName());
            }
        }
    }

    @After
    public void closeEntityManager() {
        assert em != null;
        em.close();
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

    protected void beginTransaction() {
        assert em != null;
        em.getTransaction().begin();
    }

    protected void commitTransaction() {
        assert em != null;
        em.getTransaction().commit();
    }

    protected void commitTransaction(boolean clearContext) {
        commitTransaction();
        if (clearContext) {
            em.clear();
        }
    }

    protected void analyzeSchema(SqlHandler handler) {
        SchemaExport export = new SchemaExport(new ConfigurationCreator().createConfiguration());

        // life would be much easier if Hibernate accepted a Writer on export...
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        PrintStream newOut = new PrintStream(outputStream);
        System.setOut(newOut);
        try {
            export.create(true, true);
            String sql = outputStream.toString();
            handler.handle(sql);
        } finally {
            System.setOut(oldOut);
            newOut.close();
        }

    }

    protected interface SqlHandler {
        void handle(String sql);
    }

    // easiest way to get a configuration is extending the Ant task
    private class ConfigurationCreator extends JPAConfigurationTask {
        @Override
        protected Configuration createConfiguration() {
            return super.createConfiguration();
        }
    }

}
