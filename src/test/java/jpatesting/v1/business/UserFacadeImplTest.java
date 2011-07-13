package jpatesting.v1.business;

import jpatesting.v1.persistence.User;
import jpatesting.v1.persistence.UserDao;
import jpatesting.v1.persistence.UserDto;
import org.junit.Before;
import org.junit.Test;

import static jpatesting.v1.business.EntitiesHelper.assertUser;
import static jpatesting.v1.business.EntitiesHelper.newUserWithTelephone;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class UserFacadeImplTest {
    private UserFacadeImpl facade;
    private UserDao dao;

    @Before
    public void setFixtures() {
        facade = new UserFacadeImpl();
        dao = createMock(UserDao.class);
        facade.setUserDao(dao);
    }

    @Test
    public void testGetUserById() throws Exception {
        int id = 666;
        User user = newUserWithTelephone();
        expect(dao.getUserById(id)).andReturn(user);
        replay(dao);
        UserDto dto = facade.getUserById(id);
        assertUser(dto);
    }

    @Test
    public void testGetUserByIdUnknownId() throws Exception {
        int id = 666;
        expect(dao.getUserById(id)).andReturn(null);
        replay(dao);
        UserDto dto = facade.getUserById(id);
        assertNull(dto);
    }

}
