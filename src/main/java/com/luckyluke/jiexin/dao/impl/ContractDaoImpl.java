package com.luckyluke.jiexin.dao.impl;

import com.luckyluke.jiexin.dao.ContractDao;
import com.luckyluke.jiexin.domain.Contract;
import com.luckyluke.jiexin.vo.ContractVO;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Luke Wang on 2018/7/25.
 */
@Repository
public class ContractDaoImpl extends BaseDaoImpl<Contract> implements ContractDao {

    public ContractDaoImpl() {
        //设置命名空间
        super.setNs("com.luckyluke.jiexin.mapper.ContractMapper");
    }

    @Override
    public void updateState(Map map) {
        super.getSqlSession().update(super.getNs() + ".updateState", map);
    }

    @Override
    public ContractVO view(String id) {
        return super.getSqlSession().selectOne(super.getNs()+".view",id);
    }

}
