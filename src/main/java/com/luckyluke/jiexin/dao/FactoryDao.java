package com.luckyluke.jiexin.dao;

import com.luckyluke.jiexin.domain.Factory;

import java.util.Map;

/**
 * Created by Luke Wang on 2018/7/25.
 */
public interface FactoryDao extends BaseDao<Factory> {
    void updateState(Map map);
}
