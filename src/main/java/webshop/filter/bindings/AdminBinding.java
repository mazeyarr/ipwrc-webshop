package webshop.filter.bindings;

import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define a binding for admin authentication, so that the filter can be linked
 * to the resources
 *
 * @author Mazeyar Rezaei
 * @since 18-12-2019
 */
@NameBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(value  = RetentionPolicy.RUNTIME)
public @interface AdminBinding {
}
