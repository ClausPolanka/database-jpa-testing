package jpatesting.v1.business;

import jpatesting.v1.entities.User;
import jpatesting.v1.entities.Telephone;
import jpatesting.v1.persistence.UserDao;
import jpatesting.v1.entities.UserDto;

import java.util.List;

public class UserFacadeImpl implements UserFacade {

    private static final String TELEPHONE_STRING_FORMAT = "%s (%s)";
    private UserDao userDao;

    public UserDto getUserById(long id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        List<String> telephoneDtos = userDto.getTelephones();
        for (Telephone telephone : user.getTelephones()) {
            String telephoneDto = String.format(TELEPHONE_STRING_FORMAT, telephone.getNumber(), telephone.getType());
            telephoneDtos.add(telephoneDto);
        }
        return userDto;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
