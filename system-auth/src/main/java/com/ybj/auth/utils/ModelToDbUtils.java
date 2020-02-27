package com.ybj.auth.utils;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Field;

/**
 * 实体与数据库匹配通用类
 * @author caicai.gao
 */
public class ModelToDbUtils<T> {

    /**
     * 将map中特定key的值转换为实体T中同名属性对应的数据库字段
     * eg: T user model:updateTime db:UPDATE_TIME
     *     转换前 map {"field":"updateTime","type":"asc"}
     *     转换后 map01 {"field":"UPDATED_TIME","type":"asc"}
     *
     * @param entity 实体
     * @param serverParams 待转换map
     * @return Map<String,Object> 已转换map
     */
    @SuppressWarnings("unchecked")
    public Map<String,Object> convertToDb(T entity, Map<String,Object> serverParams) throws NoSuchFieldException {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> sortMap = (Map<String, Object>) serverParams.get("sort");
        String sortField = (String) sortMap.get("field");
        // 应用反射，获取实体的属性信息
        Field fields = entity.getClass().getDeclaredField(sortField);
        // 排序字段 （获取实体的注解信息：TableField）
        params.put("sortField", fields.getDeclaredAnnotation(TableField.class).value());
        // 排序类型
        params.put("sortType",sortMap.get("type"));
        // 过滤字段
        Map<String, Object> filtersMap = (Map<String, Object>) serverParams.get("columnFilters");
        Map<String, Object> columnFiltersMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : filtersMap.entrySet()) {
            Field filterField = entity.getClass().getDeclaredField(entry.getKey());
            columnFiltersMap.put(filterField.getDeclaredAnnotation(TableField.class).value(), entry.getValue());
        }
        if(!columnFiltersMap.isEmpty()){
            params.put("columnFilters",columnFiltersMap);
        }
        return params;
    }

    /**
     * 获取实体属性对应数据库字段名称
     * @param entity 实体
     * @return Map<String,Object> key-value 实体属性-数据库字段
     * @throws NoSuchFieldException NoSuchFieldException
     */
    public Map<String,Object> convertToDb(T entity) throws NoSuchFieldException {
        Map<String,Object> dbMap = new HashMap<>();
        Field[] modelField = entity.getClass().getDeclaredFields();
        for(Field field : modelField){
            String dbColumnName = field.getDeclaredAnnotation(TableField.class).value();
            dbMap.put(field.getName(), dbColumnName);
        }
        return dbMap;
    }

    /**
     * 获取实体属性对应数据库字段名称
     * @param entity 实体
     * @param field 属性名称
     * @return String 数据库字段
     * @throws NoSuchFieldException NoSuchFieldException
     */
    public String getDbColumn(T entity, String field) throws NoSuchFieldException {
        Field modelField = entity.getClass().getDeclaredField(field);
        return modelField.getDeclaredAnnotation(TableField.class).value();
    }


}
