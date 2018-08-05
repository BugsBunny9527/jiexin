package com.luckyluke.jiexin.service.impl;

import com.luckyluke.jiexin.dao.FactoryDao;
import com.luckyluke.jiexin.domain.Factory;
import com.luckyluke.jiexin.pagination.Page;
import com.luckyluke.jiexin.service.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Luke Wang on 2018/7/25.
 */
@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired
    FactoryDao factoryDao;

    @Override
    public List<Factory> findPage(Page page) {
        return factoryDao.findPage(page);
    }

    @Override
    public List<Factory> find(Map paraMap) {
        return factoryDao.find(paraMap);
    }

    @Override
    public Factory get(Serializable id) {
        return factoryDao.get(id);
    }

    @Override
    public void insert(Factory factory) {
        //设置 UUID
        factory.setId(UUID.randomUUID().toString());
        //默认为启用状态
        factory.setState("1");
        factoryDao.insert(factory);
    }

    @Override
    public void update(Factory factory) {
        factoryDao.update(factory);
    }

    @Override
    public void deleteById(Serializable id) {
        factoryDao.deleteById(id);
    }

    @Override
    public void delete(Serializable[] ids) {
        factoryDao.delete(ids);
    }

    @Override
    public List<Factory> find() {
        return factoryDao.find();
    }

    @Override
    public void start(Serializable[] ids) {
        Map<String,Object> map = new HashMap<>();
        map.put("state", 1);
        map.put("ids", ids);

        factoryDao.updateState(map);
    }

    @Override
    public void stop(Serializable[] ids) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", 0);
        map.put("ids", ids);

        factoryDao.updateState(map);
    }
}
