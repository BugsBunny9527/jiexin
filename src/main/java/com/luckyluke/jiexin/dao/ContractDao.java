package com.luckyluke.jiexin.dao;

import com.luckyluke.jiexin.domain.Contract;
import com.luckyluke.jiexin.vo.ContractVO;

import java.util.Map;

/**
 * Created by Luke Wang on 2018/7/25.
 */
public interface ContractDao extends BaseDao<Contract> {
    void updateState(Map map);

    ContractVO view(String id);
}
