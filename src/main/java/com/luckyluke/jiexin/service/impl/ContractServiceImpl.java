package com.luckyluke.jiexin.service.impl;

import com.luckyluke.jiexin.dao.ContractDao;
import com.luckyluke.jiexin.dao.ContractProductDao;
import com.luckyluke.jiexin.dao.ExtCproductDao;
import com.luckyluke.jiexin.domain.Contract;
import com.luckyluke.jiexin.pagination.Page;
import com.luckyluke.jiexin.service.ContractService;

import com.luckyluke.jiexin.vo.ContractVO;
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
public class ContractServiceImpl implements ContractService {

    @Autowired
    ContractDao contractDao;

    @Autowired
    ContractProductDao contractProductDao;

    @Autowired
    ExtCproductDao extCproductDao;

    @Override
    public List<Contract> findPage(Page page) {
        return contractDao.findPage(page);
    }

    @Override
    public List<Contract> find(Map paraMap) {
        return contractDao.find(paraMap);
    }

    @Override
    public Contract get(Serializable id) {
        return contractDao.get(id);
    }

    @Override
    public void update(Contract contract) {
        contractDao.update(contract);
    }

    @Override
    public void insert(Contract contract) {
        contract.setId(UUID.randomUUID().toString());
        contract.setState(0);           //0草稿 1已上报
        contractDao.insert(contract);
    }


    @Override
    public void deleteById(Serializable id) {
        //级联删除：先删除附件
        Serializable[] ids = {id};
        extCproductDao.deleteByContractId(ids);

        //级联删除：再删除货物
        contractProductDao.deleteByContractId(ids);

        //删除合同
        contractDao.deleteById(id);
    }

    @Override
    public void delete(Serializable[] ids) {
        //级联删除：先删除附件
        extCproductDao.deleteByContractId(ids);

        //级联删除：再删除货物
        contractProductDao.deleteByContractId(ids);

        //删除合同
        contractDao.delete(ids);
    }

    @Override
    public List<Contract> find() {
        return contractDao.find();
    }

    @Override
    public void submit(Serializable[] ids) {
        Map<String,Object> map = new HashMap<>();
        map.put("state", 1);
        map.put("ids", ids);

        contractDao.updateState(map);
    }

    @Override
    public void cancel(Serializable[] ids) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", 0);
        map.put("ids", ids);

        contractDao.updateState(map);
    }

    @Override
    public ContractVO view(String id) {
        return contractDao.view(id);
    }
}
