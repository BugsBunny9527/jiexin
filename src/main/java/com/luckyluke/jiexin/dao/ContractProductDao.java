package com.luckyluke.jiexin.dao;

import com.luckyluke.jiexin.domain.ContractProduct;

import java.io.Serializable;

/**
 * Created by Luke Wang on 2018/8/13.
 */
public interface ContractProductDao extends BaseDao<ContractProduct> {
    void deleteByContractId(Serializable[] ids);

}
