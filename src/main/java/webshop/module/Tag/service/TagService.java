package webshop.module.Tag.service;

import webshop.core.iinterface.CoreValue;
import webshop.core.iinterface.Translator;
import webshop.module.Tag.TagModule;
import webshop.module.Tag.dao.TagsDao;
import webshop.module.Tag.exception.TagNotFoundException;
import webshop.module.Tag.model.Tag;

import java.util.List;

public class TagService {
    public static Tag createTag(Tag tag) {
        getDao().create(tag);

        return tag;
    }

    public static List<Tag> getAllTags() {
        return getDao().all(Tag.class);
    }

    public static Tag findTagById(long id) {
        return getDao().find(id);
    }

    public static Tag findOrFailTagById(long id) throws TagNotFoundException {
        final String TAG_NOT_FOUND_MESSAGE = Translator.translate(
                "Tag id: " + id + " not found"
        );

        try {
            Tag tag = findTagById(id);

            if (tag == CoreValue.UNSET_NULL) {
                throw new TagNotFoundException(TAG_NOT_FOUND_MESSAGE);
            }

            return tag;
        } catch (Exception e) {
            throw new TagNotFoundException(TAG_NOT_FOUND_MESSAGE);
        }
    }

    public static Tag updateTag(Tag tag) {
        getDao().update(tag);

        return findTagById(tag.getId());
    }

    public static boolean deleteTagById(long id) throws TagNotFoundException {
        Tag tag = findOrFailTagById(id);

        deleteTag(tag);

        try {
            findOrFailTagById(id);

            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static void deleteTag(Tag tag) {
        getDao().delete(tag);
    }

    private static TagsDao getDao() {
        return TagModule.getInstance().getDao();
    }
}
