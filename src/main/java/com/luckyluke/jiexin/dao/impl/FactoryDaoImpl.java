package com.luckyluke.jiexin.dao.impl;

import com.luckyluke.jiexin.dao.FactoryDao;
import com.luckyluke.jiexin.domain.Factory;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Luke Wang on 2018/7/25.
 */
@Repository
public class FactoryDaoImpl extends BaseDaoImpl<Factory> implements FactoryDao {

    public FactoryDaoImpl() {
        //设置命名空间
        super.setNs("com.luckyluke.jiexin.mapper.FactoryMapper");
    }

    @Override
    public void updateState(Map map) {
        super.getSqlSession().update(super.getNs() + ".updateState", map);
    }
}
