package com.luckyluke.jiexin.dao.impl;

import com.luckyluke.jiexin.dao.OutProductVODao;
import com.luckyluke.jiexin.vo.OutProductVO;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Luke Wang on 2018/8/16.
 */
@Repository
public class OutProductVODaoImpl extends SqlSessionDaoSupport implements OutProductVODao {

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    @Override
    public List<OutProductVO> find(String time) {
        return this.getSqlSession().selectList("com.luckyluke.jiexin.mapper.OutProductVOMapper.find",time);
    }
}
