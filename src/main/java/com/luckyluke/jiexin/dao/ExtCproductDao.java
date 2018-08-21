package com.luckyluke.jiexin.dao;

import com.luckyluke.jiexin.domain.ExtCproduct;

import java.io.Serializable;

/**
 * Created by Luke Wang on 2018/8/16.
 */
public interface ExtCproductDao extends BaseDao<ExtCproduct> {
    void deleteByContractProductId(Serializable[] ids);

    void deleteByContractId(Serializable[] ids);
}
