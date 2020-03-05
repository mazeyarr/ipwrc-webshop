package webshop.core.iinterface;

import io.dropwizard.setup.Environment;

import java.util.ArrayList;
import java.util.List;

public interface WebshopModule<Dao> {
    final List<Class<?>> mEntities = new ArrayList<>();

    public void init(Environment environment);

    public void initResources(Environment environment);

    public default List<Class<?>> getEntities() {
        return mEntities;
    }

    public default void addEntityToModule(Class<?> entity) {
        mEntities.add(entity);
    }

    public Dao getDao();
}
