package edu.upc.dsa.util;

import edu.upc.dsa.DB.SQLNotInsert;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class QueryHelper {

    public static String CreateQueryINSERT(Object entity){

        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName()).append(" (");

        String[] fields = ObjectHelper.getFields(entity);

        StringJoiner sj = new StringJoiner(", ");
        for(String field: fields){
            try {
                if(!entity.getClass().getDeclaredField(field).isAnnotationPresent(SQLNotInsert.class)) {
                    sj.add(field);
                }
            } catch (NoSuchFieldException ignored) { }
        }
        sb.append(sj);

        sb.append(") VALUES (");

        sj = new StringJoiner(", ");
        for (String field: fields) {

            try {
                if (!entity.getClass().getDeclaredField(field).isAnnotationPresent(SQLNotInsert.class)) {
                    sj.add("?");
                }
            } catch (NoSuchFieldException ignored) {}
        }
        sb.append(sj).append(") RETURNING id;");

        return sb.toString();

    }

    public static <T> String CreateQuerySELECTbyId(Class<T> entity){

        StringBuffer sb = new StringBuffer("SELECT * FROM ");
        sb.append(entity.getSimpleName()).append(" WHERE ID=?;");

        return sb.toString();

    }

    public static <T> String CreateQuerySELECTbyParams(Class<T> theClass, Map<String,Object> params){

        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(theClass.getSimpleName());
        if(params == null) return sb.append(";").toString();
        sb.append(" WHERE true");
        for (String key : params.keySet()) {
            sb.append(" AND ").append(key).append("=?");
        }
        sb.append(";");
        return sb.toString();
    }

    public static <T> String CreateUpdate(Class<T> theClass, Map<String, Object> changes, Map<String, Object> selectors){
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(theClass.getSimpleName()).append(" SET ");
        StringJoiner joiner = new StringJoiner(", ");
        changes.forEach((key, val) -> joiner.add(key + "=?"));
        sb.append(joiner).append(" WHERE true ");
        selectors.forEach((key, val) -> sb.append("AND ").append(key).append("=? "));
        sb.append(";");
        return sb.toString();
    }

    public static <T> String CreateQueryDELETEbyParams(Class<T> theClass, Map<String,Object> params){
        StringBuffer sb = new StringBuffer("DELETE FROM ");
        sb.append(theClass.getSimpleName());
        if(params == null) return sb.append(";").toString();
        sb.append(" WHERE true ");
        for (String key : params.keySet()) {
            sb.append(" AND ").append(key).append("=?");
        }
        sb.append(";");
        return sb.toString();
    }

}
