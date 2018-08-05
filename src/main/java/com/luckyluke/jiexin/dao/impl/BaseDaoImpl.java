package com.luckyluke.jiexin.dao.impl;

import com.luckyluke.jiexin.dao.BaseDao;
import com.luckyluke.jiexin.pagination.Page;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Luke Wang on 2018/7/25.
 */
public class BaseDaoImpl<T> extends SqlSessionDaoSupport implements BaseDao<T> {

    @Autowired
    //mybatis-spring 1.0无需此方法；mybatis-spring 1.2必须注入
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    private String ns;      //命名空间

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getNs() {
        return ns;
    }

    @Override
    public List<T> findPage(Page page) {
        List<T> oList = this.getSqlSession().selectList(ns + ".findPage", page);
        return oList;
    }

    @Override
    public List<T> find(Map paraMap) {
        List<T> oList = this.getSqlSession().selectList(ns + ".find", paraMap);
        return oList;
    }

    @Override
    public T get(Serializable id) {
        return this.getSqlSession().selectOne(ns + ".get", id);
    }

    @Override
    public void insert(T entity) {
        this.getSqlSession().insert(ns + ".insert", entity);
    }

    @Override
    public void update(T entity) {
        this.getSqlSession().delete(ns + ".update", entity);
    }

    @Override
    public void deleteById(Serializable id) {
        this.getSqlSession().delete(ns + ".deleteById", id);
    }

    @Override
    public void delete(Serializable[] ids) {
        this.getSqlSession().delete(ns + ".delete", ids);
    }

    @Override
    public List<T> find() {
        List<T> oList = this.getSqlSession().selectList(ns + ".find");
        return oList;
    }
}
