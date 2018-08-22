package com.luckyluke.jiexin.service.impl;

import com.luckyluke.jiexin.dao.ContractProductDao;
import com.luckyluke.jiexin.dao.ExtCproductDao;
import com.luckyluke.jiexin.domain.ContractProduct;
import com.luckyluke.jiexin.pagination.Page;
import com.luckyluke.jiexin.service.ContractProductService;
import com.luckyluke.util.UtilFuns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Luke Wang on 2018/7/25.
 */
@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    ContractProductDao contractProductDao;

    @Autowired
    ExtCproductDao extCproductDao;

    @Override
    public List<ContractProduct> findPage(Page page) {
        return contractProductDao.findPage(page);
    }

    @Override
    public List<ContractProduct> find(Map paraMap) {
        return contractProductDao.find(paraMap);
    }

    @Override
    public ContractProduct get(Serializable id) {
        return contractProductDao.get(id);
    }

    @Override
    public void update(ContractProduct contractProduct) {
        //自动计算总金额=数量*单价       修改，删除，同步合同总金额
        if(UtilFuns.isNotEmpty(contractProduct.getCnumber()) && UtilFuns.isNotEmpty(contractProduct.getPrice())){
            contractProduct.setAmount(contractProduct.getCnumber()*contractProduct.getPrice());
        }

        contractProductDao.update(contractProduct);
    }

    @Override
    public void insert(ContractProduct contractProduct) {
        contractProduct.setId(UUID.randomUUID().toString());

        //自动计算总金额=数量*单价       修改，删除，同步合同总金额
        if(UtilFuns.isNotEmpty(contractProduct.getCnumber()) && UtilFuns.isNotEmpty(contractProduct.getPrice())){
            contractProduct.setAmount(contractProduct.getCnumber()*contractProduct.getPrice());
        }

        contractProductDao.insert(contractProduct);
    }

    @Override
    public void deleteById(Serializable id) {


        //添加级联删除功能,删除货物前先删除附件
        Serializable[] ids = {id};
        extCproductDao.deleteByContractProductId(ids);

        contractProductDao.deleteById(id);
    }

    @Override
    public void delete(Serializable[] ids) {
        contractProductDao.delete(ids);

        //添加级联删除功能
        extCproductDao.deleteByContractProductId(ids);
    }

    @Override
    public List<ContractProduct> find() {
        return contractProductDao.find();
    }



}
