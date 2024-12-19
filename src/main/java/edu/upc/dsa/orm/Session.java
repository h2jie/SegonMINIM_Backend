package edu.upc.dsa.orm;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Session {

    <T> T save(T entity) throws SQLException;
    void close();
    <T> T get(Class<T> theClass, String ID) throws SQLException;
    <T> void update(Class<T> theClass, Map<String, Object> changes, Map<String, Object> selectors) throws SQLException;
    <T> void delete(Class<T> theClass, String id) throws SQLException;
    <T> void delete(Class<T> theClass, Map<String, Object> params) throws SQLException;
    <T> List<T> findAll(Class<T> theClass) throws SQLException;
    <T> List<T> findAll(Class<T> theClass, Map<String, Object> params) throws SQLException;
    <T> List<T> findAll(Class<T> theClass, String id) throws SQLException;

}
