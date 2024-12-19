package edu.upc.dsa.util;

import java.lang.reflect.Field;

public class ObjectHelper {

    public static String[] getFields(Object entity){

        Class theClass = entity.getClass();

        Field[] fields = theClass.getDeclaredFields();

        String[] sFields= new String[fields.length];

        int i = 0;
        for(Field f : fields) sFields[i++]=f.getName();

        return sFields;
    }

    public static void setter(Object entity, String property, Object value) {
        try{
            Field field = entity.getClass().getDeclaredField(property);
            field.setAccessible(true);
            field.set(entity,value);
        }catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e){
            e.printStackTrace();
        }
    }

    public static Object getter(Object entity, String property){
        Class theClass = entity.getClass();
        try {
            Field field = theClass.getDeclaredField(property);
            field.setAccessible(true);
            Object res = field.get(entity);
            return res;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

}
