package com.masich.datab;

import com.masich.datab.utils.DatabaseUtils;

import java.util.List;

abstract public class Entity {
    /**
     * Saves (recursively by default) an iterable data structure of entities to the appropriate database table(s).
     * <p>
     * Saves the instances with corresponding primary key to the appropriate
     * database table(s). Simply calls {@link #{
     * DatabaseUtils.saveAll(boolean, Iterable)} with a <code>true</code>
     * value for a boolean saveRecursively.
     *
     * @param entities entities to save.
     */
    public static void saveAll(final Iterable<? extends Entity> entities) {
        DatabaseUtils.saveAll(entities);
    }

    /**
     * Deletes all of the instances from the corresponding database table.
     *
     * @param tableClass class of the database table entity.
     */
    public static boolean deleteAll(final Class<? extends Entity> tableClass) {
        return DatabaseUtils.deleteAll(tableClass);
    }

    /**
     * Returns the <code>entityClass</code> entity from the
     * appropriate database table with the corresponding id.
     * Entity also will be recursively initialized.
     *
     * @param tableClass class of the database table entity.
     * @param <T>        type of the result value. The type of <code>entityClass</code> in this case.
     * @return the entity of the appropriate database table,
     * <code>null</code> if something went wrong.
     */
    public static <T> T getById(Object id, Class<T> tableClass) {
        return DatabaseUtils.getById(id, tableClass);
    }

    /**
     * Returns all of the <code>entityClass</code> com from the
     * appropriate database table. Entities also will be recursively initialized.
     *
     * @param tableClass class of the database table entity.
     * @param <T>        type of the result value. The type of <code>entityClass</code> in this case.
     * @return all of the com of the appropriate database table,
     * emptyList if the database table is empty,
     * <code>null</code> if something went wrong.
     */
    public static <T> List<T> getAll(final Class<T> tableClass) {
        return DatabaseUtils.getAll(tableClass);
    }

    /**
     * Uses recursively saving by default.
     */
    public final void save() {
        save(true);
    }

    /**
     * @param saveRecursively if <code>true</code> the recursive saving will be used.
     */
    public final void save(boolean saveRecursively) {
        DatabaseUtils.save(saveRecursively, this);
    }

    /**
     * Does not use recursively deletion by default.
     */
    public final void delete() {
        delete(false);
    }

    /**
     * @param deleteRecursively if <code>true</code> the recursive deletion will be used.
     */
    //Todo: make foreign key NULLABLE in DB as requirement (?)
    //Fixme: Is recursion delete necessary? You can achieve it using pure DB settings (simply make Constraint on Foreign Key 'CASCADE' on delete)
    private void delete(boolean deleteRecursively) {
        DatabaseUtils.delete(deleteRecursively, this);
    }
}
