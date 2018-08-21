package com.luckyluke.jiexin.dao.impl;

import com.luckyluke.jiexin.dao.ContractProductDao;
import com.luckyluke.jiexin.domain.ContractProduct;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created by Luke Wang on 2018/8/13.
 */
@Repository
public class ContractProductDaoImpl extends BaseDaoImpl<ContractProduct> implements ContractProductDao {

    public ContractProductDaoImpl() {
        super.setNs("com.luckyluke.jiexin.mapper.ContractProductMapper");
    }

    //级联删除：根据 合同id 删除货物
    @Override
    public void deleteByContractId(Serializable[] ids) {
        super.getSqlSession().delete(super.getNs() + ".deleteByContractId", ids);
    }
}
