package dao;

import entity.SuperEntity;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T extends SuperEntity,ID extends Serializable> extends SuperDAO{
    List<T> findAll() throws SQLException;

    T find(ID pk) throws SQLException;

    boolean save(T entity) throws SQLException;

    boolean update(T entity) throws SQLException;

    boolean delete(ID pk) throws SQLException;
}
