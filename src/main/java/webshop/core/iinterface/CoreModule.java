package webshop.core.iinterface;

import io.dropwizard.setup.Environment;

import java.util.ArrayList;
import java.util.List;

public abstract class CoreModule<Dao> {
    protected List<Class<?>> mEntities = new ArrayList<>();

    public abstract void init(Environment environment);

    public abstract void initResources(Environment environment);

    public abstract void initDao();

    public void runSeeds() {}

    public List<Class<?>> getEntities() {
        return mEntities;
    }

    public void addEntityToModule(Class<?> entity) {
        mEntities.add(entity);
    }

    public abstract Dao getDao();
}
