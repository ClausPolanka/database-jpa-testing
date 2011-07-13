package jpatesting.v1.business;

import jpatesting.v1.persistence.UserDto;

public interface UserFacade {
    UserDto getUserById(long id);
}
