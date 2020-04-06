package webshop.module.Tag;

import io.dropwizard.setup.Environment;
import webshop.core.iinterface.CoreModule;
import webshop.core.service.CoreHelper;
import webshop.core.service.CoreService;
import webshop.module.Product.resource.ProductResource;
import webshop.module.Tag.dao.TagsDao;
import webshop.module.Tag.model.Tag;
import webshop.module.Tag.resource.TagResource;

// TODO: TAGS LINKING TAGS
public class TagModule extends CoreModule<TagsDao> {
    private static volatile TagModule TAG_MODULE_INSTANCE;

    private TagsDao mTagDao;

    public TagModule() {
        addEntityToModule(Tag.class);
    }

    @Override
    public void init(Environment environment) {
        initDao();
        initResources(environment);
    }

    @Override
    public void initResources(Environment environment) {
        environment.jersey().register(new TagResource());
    }

    @Override
    public void initDao() {
        mTagDao = new TagsDao(
                CoreService.getInstance()
                        .getHibernate()
                        .getSessionFactory()
        );
    }

    @Override
    public TagsDao getDao() {
        return mTagDao;
    }

    public static TagModule getInstance() {
        if (CoreHelper.isNull(TAG_MODULE_INSTANCE)) {
            synchronized (TagModule.class) {
                if (CoreHelper.isNull(TAG_MODULE_INSTANCE)) {
                    TAG_MODULE_INSTANCE = new TagModule();
                }
            }
        }

        return TAG_MODULE_INSTANCE;
    }
}
