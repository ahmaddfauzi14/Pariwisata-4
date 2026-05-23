package pariwisata.dao;

import java.util.List;

public interface RepositoryMethod<T, ID> {
    boolean create(T entity);
    T getById(ID id);
    List<T> getAll();
    boolean update(T entity);
    boolean delete(ID id);
}
