package webshop.module.User.service;

import webshop.core.exception.EntityNotFoundException;
import webshop.core.iinterface.Translator;
import webshop.core.service.TranslateService;
import webshop.module.User.UserModule;
import webshop.module.User.dao.UserDao;
import webshop.module.User.dao.UserRoleDao;
import webshop.module.User.exception.UserNotFoundException;
import webshop.module.User.model.User;
import webshop.module.User.model.UserRole;
import webshop.module.User.type.UserType;

import javax.persistence.NoResultException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserService {
    public static User createUser(User user, UserType userType) {
        getDao().create(user);

        Set<UserRole> roleSet = createRoleSetFrom(
                new UserRole(userType.toString(), user)
        );

        user.setRoles(roleSet);

        return user;
    }

    public static List<User> getAllUsers() {
        return getDao().all();
    }

    public static User findUserById(long id) {
        return getDao().find(id);
    }

    public static User findOrFailUserById(long id) throws UserNotFoundException {
        final String USER_NOT_FOUND_MESSAGE = Translator.translate(
                "User id: " + id + " not found"
        );

        try {
            User user = findUserById(id);

            if (user == null) {
                throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
            }

            return user;
        } catch (Exception e) {
            throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
    }

    public static User findUserByUsername(String username) {
        return getDao().find(username);
    }

    public static User findOrFailUserByUsername(String username) throws UserNotFoundException {
        try {
            return getDao().find(username);
        } catch (Exception exception) {
            throw new UserNotFoundException(Translator.translate(
                    "User with username: " + username + " does not exist"
            ));
        }
    }

    public static User updateUser(User user) throws UserNotFoundException {
        User oldUser = findOrFailUserById(user.getId());

        user.setRoles(oldUser.getRoles());
        user.setPassword(oldUser.getPassword());

        return getDao().update(user);
    }

    public static boolean deleteUserById(long id) throws UserNotFoundException {
        User userToDelete = findOrFailUserById(id);

        getDao().delete(userToDelete);

        try {
            findUserById(id);
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    public static void deleteUser(User user) {
        getDao().delete(user);
    }

    private static Set<UserRole> createRoleSetFrom(List<UserRole> roles) {
        roles.forEach(userRole -> getUserRoleDao().create(userRole));
        return new HashSet<>(roles);
    }

    private static Set<UserRole> createRoleSetFrom(UserRole role) {
        Set<UserRole> roleSet = new HashSet<>();

        roleSet.add(role);
        getUserRoleDao().create(role);

        return roleSet;
    }

    private static UserDao getDao() {
        return UserModule.getInstance().getDao();
    }

    private static UserRoleDao getUserRoleDao() {
        return UserModule.getInstance().getUserRoleDao();
    }
}
