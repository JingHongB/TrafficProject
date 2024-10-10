package com.example.utils;

import com.example.model.entity.BaseLocationEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * POI 工具类。
 */
public class PoiUtils {
    /**
     * 将源实体列表映射为目标实体列表。
     *
     * @param sourceList  要映射的源实体列表
     * @param targetClass 目标实体的类
     * @param <T>         目标实体的类型
     * @param <S>         源实体的类型
     * @return 映射后的目标实体列表
     */
    public static <T extends BaseLocationEntity, S extends BaseLocationEntity> List<T> PoiConvert(List<S> sourceList, Class<T> targetClass) {
        return sourceList.stream().map(source -> {
            try {
                T target = targetClass.getDeclaredConstructor().newInstance();
                target.setName(source.getName());
                target.setAddress(source.getAddress());
                target.setLongitude(source.getLongitude());
                target.setLatitude(source.getLatitude());
                return target;
            } catch (Exception e) {
                throw new RuntimeException("Failed to map entities", e);
            }
        }).collect(Collectors.toList());
    }
}
