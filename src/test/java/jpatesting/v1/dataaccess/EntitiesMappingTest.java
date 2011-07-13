package jpatesting.v1.dataaccess;

import jpatesting.v1.entities.User;
import org.junit.Test;

import static jpatesting.v1.business.EntitiesHelper.assertUserWithTelephone;
import static jpatesting.v1.business.EntitiesHelper.newUserWithTelephone;

public class EntitiesMappingTest extends AbstractJpaDbUnitELTemplateTestCaseJUnit44 {

    @Test
    @DataSets(setUpDataSet = "/user-with-telephone.xml")
    public void testLoadUserWithTelephone() {
        beginTransaction();
        long id = ELFunctionMapperImpl.getId(User.class);
        User user = em.find(User.class, id);
        commitTransaction();
        assertUserWithTelephone(user);
    }

    @Test
    @DataSets(assertDataSet = "/user-with-telephone.xml")
    public void testSaveUserWithTelephoneAgain() throws Exception {
        testSaveUserWithTelephone();
    }

    @Test
    @DataSets(setUpDataSet = "/user-with-telephone.xml")
    public void testLoadUserWithTelephoneOneMoreTime() {
        testLoadUserWithTelephone();
    }

    @Test
    @DataSets(assertDataSet = "/user-with-telephone.xml")
    public void testSaveUserWithTelephone() throws Exception {
        User user = newUserWithTelephone();
        beginTransaction();
        em.persist(user);
        commitTransaction();
    }

    @Test
    @DataSets(assertDataSet = "/user-with-telephone.xml")
    public void testSaveUserWithTelephoneOneMoreTime() throws Exception {
        testSaveUserWithTelephone();
    }

}
