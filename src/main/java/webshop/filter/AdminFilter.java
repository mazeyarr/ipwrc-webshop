package webshop.filter;

import webshop.core.service.ExceptionService;
import webshop.filter.bindings.AdminBinding;
import webshop.module.User.service.AuthUserService;
import webshop.module.User.type.UserType;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Authentication Filter that prevents users to use resources that they are not permitted for
 *
 * @author Mazeyar Rezaei
 * @since 17-10-2019
 */
@Provider
@AdminBinding
@Priority(Priorities.AUTHENTICATION + 1)
public class AdminFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) {
        boolean isAdmin = AuthUserService.getInstance()
                .getAuthUser()
                .getRoles()
                .stream()
                .anyMatch(
                        (userRole -> userRole.getRole() == UserType.ADMIN)
                );

        if (!isAdmin) {
            ExceptionService.throwIlIllegalArgumentException(
                this.getClass(),
                "Authorization Failed: You are not an ADMINISTRATOR!",
                "Authorization Failed: User is not of ADMIN type...",
                Response.Status.FORBIDDEN
            );
        }
    }
}
