package com.luckyluke.jiexin.dao.impl;

import com.luckyluke.jiexin.dao.ExtCproductDao;
import com.luckyluke.jiexin.domain.ExtCproduct;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created by Luke Wang on 2018/8/16.
 */
@Repository
public class ExtCproductDaoImpl extends BaseDaoImpl<ExtCproduct> implements ExtCproductDao {
    public ExtCproductDaoImpl() {
        super.setNs("com.luckyluke.jiexin.mapper.ExtCproductMapper");
    }

    //级联删除：根据货物 id 删除对应附件
    @Override
    public void deleteByContractProductId(Serializable[] ids) {
        super.getSqlSession().delete(super.getNs() + ".deleteByContractProductId" ,ids);
    }

    //级联删除：根据 合同id 删除对应附件
    @Override
    public void deleteByContractId(Serializable[] ids) {
        super.getSqlSession().delete(super.getNs() + ".deleteByContractId" ,ids);
    }
}
