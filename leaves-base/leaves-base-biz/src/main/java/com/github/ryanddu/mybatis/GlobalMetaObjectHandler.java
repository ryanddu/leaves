package com.github.ryanddu.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * Mybatis Plus 自定义元对象字段填充控制器，实现公共字段自动写入
 * @author: ryan
 * @date: 2023/3/28 10:37
 **/
public class GlobalMetaObjectHandler implements MetaObjectHandler {

    /**
     * 创建时间
     */
    private final String gmtCreate = "gmtCreate";
    /**
     * 修改时间
     */
    private final String gmtUpdate = "gmtUpdate";

    /**
     * 新增时填充的字段
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        setInsertFieldValByName(gmtCreate, LocalDateTime.now(), metaObject);
        setInsertFieldValByName(gmtUpdate, LocalDateTime.now(), metaObject);
    }

    /**
     * 更新时需要填充字段
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setUpdateFieldValByName(gmtUpdate, LocalDateTime.now(), metaObject);
    }
}
