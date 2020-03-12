package webshop.module.User.service;

import webshop.core.exception.EntityNotFoundException;
import webshop.core.iinterface.Translator;
import webshop.core.service.TranslateService;
import webshop.module.User.UserModule;
import webshop.module.User.dao.CompanyDao;
import webshop.module.User.dao.UserDao;
import webshop.module.User.dao.UserRoleDao;
import webshop.module.User.exception.UserNotFoundException;
import webshop.module.User.model.Company;
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

    public static Company createCompany(Company company) {
        getCompanyDao().create(company);

        return company;
    }

    public static List<User> getAllUsers() {
        return getDao().all(User.class);
    }

    public static List<Company> getAllCompanies() {
        return getCompanyDao().all(Company.class);
    }

    public static User findUserById(long id) {
        return getDao().find(id);
    }

    public static Company findCompanyById(long id) {
        return getCompanyDao().find(id);
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

    public static Company findOrFailCompanyById(long id) throws UserNotFoundException {
        final String COMPANY_NOT_FOUND_MESSAGE = Translator.translate(
                "User id: " + id + " not found"
        );

        try {
            Company company = findCompanyById(id);

            if (company == null) {
                throw new UserNotFoundException(COMPANY_NOT_FOUND_MESSAGE);
            }

            return company;
        } catch (Exception e) {
            throw new UserNotFoundException(COMPANY_NOT_FOUND_MESSAGE);
        }
    }

    public static User findUserByUsername(String username) {
        return getDao().find(username);
    }

    public static Company findCompanyByUsername(String username) {
        return getCompanyDao().find(username);
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

    public static Company findOrFailCompanyByUsername(String username) throws UserNotFoundException {
        try {
            return getCompanyDao().find(username);
        } catch (Exception exception) {
            throw new UserNotFoundException(Translator.translate(
                    "Company with username: " + username + " does not exist"
            ));
        }
    }

    public static User updateUser(User user) throws UserNotFoundException {
        User oldUser = findOrFailUserById(user.getId());

        user.setRoles(oldUser.getRoles());
        user.setPassword(oldUser.getPassword());

        return getDao().update(user);
    }

    public static Company updateCompany(Company company) throws UserNotFoundException {
        User oldCompany = findOrFailUserById(company.getId());

        company.setPassword(oldCompany.getPassword());

        return getCompanyDao().update(company);
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

    public static boolean deleteCompanyById(long id) throws UserNotFoundException {
        Company companyToDelete = findOrFailCompanyById(id);

        getCompanyDao().delete(companyToDelete);

        try {
            findCompanyById(id);
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    public static void deleteUser(User user) {
        getDao().delete(user);
    }

    public static void deleteCompany(Company company) {
        getCompanyDao().delete(company);
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

    private static CompanyDao getCompanyDao() {
        return UserModule.getInstance().getCompanyDao();
    }

    private static UserRoleDao getUserRoleDao() {
        return UserModule.getInstance().getUserRoleDao();
    }
}
