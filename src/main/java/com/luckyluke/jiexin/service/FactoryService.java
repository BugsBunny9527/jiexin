package com.luckyluke.jiexin.service;

import com.luckyluke.jiexin.domain.Factory;
import com.luckyluke.jiexin.pagination.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Luke Wang on 2018/7/25.
 */
public interface FactoryService {
    List<Factory> findPage(Page page);        //分页查询

    List<Factory> find(Map paraMap);          //带条件查询，条件可以为null，即没有条件；返回List对象

    Factory get(Serializable id);             //只查询一个，通常用于修改

    void insert(Factory factory);              //插入，用实体作为参数

    void update(Factory factory);              //修改，用实体作为参数

    void deleteById(Serializable id);         //按 id 删除，删除一条；支持整数型和字符串类型ID

    void delete(Serializable ids[]);           //批量删除；支持整数型和字符串类型ID

    List<Factory> find();

    void start(Serializable ids[]);                               //启用

    void stop(Serializable ids[]);                                //停用

}
