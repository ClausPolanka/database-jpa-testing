package jpatesting.v1.business;

import jpatesting.v1.entities.UserDto;

public interface UserFacade {
    UserDto getUserById(long id);
}
