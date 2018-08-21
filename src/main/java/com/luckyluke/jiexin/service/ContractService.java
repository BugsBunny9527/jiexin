package com.luckyluke.jiexin.service;

import com.luckyluke.jiexin.domain.Contract;
import com.luckyluke.jiexin.pagination.Page;
import com.luckyluke.jiexin.vo.ContractVO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Luke Wang on 2018/7/25.
 */
public interface ContractService {
    List<Contract> findPage(Page page);        //分页查询

    List<Contract> find(Map paraMap);          //带条件查询，条件可以为null，即没有条件；返回List对象

    Contract get(Serializable id);             //只查询一个，通常用于修改

    void insert(Contract contract);              //插入，用实体作为参数

    void update(Contract contract);              //修改，用实体作为参数

    void deleteById(Serializable id);         //按 id 删除，删除一条；支持整数型和字符串类型ID

    void delete(Serializable ids[]);           //批量删除；支持整数型和字符串类型ID

    List<Contract> find();

    void submit(Serializable ids[]);                               //上报

    void cancel(Serializable ids[]);                                //取消

    ContractVO view(String id);

}
