package com.luckyluke.jiexin.service.impl;

import com.luckyluke.jiexin.dao.ExtCproductDao;
import com.luckyluke.jiexin.dao.SysCodeDao;
import com.luckyluke.jiexin.domain.ExtCproduct;
import com.luckyluke.jiexin.domain.SysCode;
import com.luckyluke.jiexin.pagination.Page;
import com.luckyluke.jiexin.service.ExtCproductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Luke Wang on 2018/8/16.
 */
@Service
public class ExtCproductServiceImpl implements ExtCproductService
{

    @Autowired
    ExtCproductDao extCproductDao;

    @Autowired
    SysCodeDao sysCodeDao;

    @Override
    public List<ExtCproduct> findPage(Page page) {
        return extCproductDao.findPage(page);
    }

    @Override
    public List<ExtCproduct> find(Map paraMap) {
        return extCproductDao.find(paraMap);
    }

    @Override
    public ExtCproduct get(Serializable id) {
        return extCproductDao.get(id);
    }

    @Override
    public void update(ExtCproduct extCproduct) {
        extCproductDao.update(extCproduct);
    }

    @Override
    public void insert(ExtCproduct extCproduct) {
        extCproduct.setId(UUID.randomUUID().toString());
        extCproductDao.insert(extCproduct);
    }

    @Override
    public List<SysCode> getSysCodeList() {
        Map paraMap = new HashMap();
        paraMap.put("parentId", "0104");        //0104附件分类

        return sysCodeDao.find(paraMap);
    }



    @Override
    public void deleteById(Serializable id) {
        extCproductDao.deleteById(id);
    }

    @Override
    public void delete(Serializable[] ids) {
        extCproductDao.delete(ids);
    }

    @Override
    public List<ExtCproduct> find() {
        return extCproductDao.find();
    }


}
