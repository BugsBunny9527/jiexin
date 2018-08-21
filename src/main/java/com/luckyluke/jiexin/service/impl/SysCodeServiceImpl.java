package com.luckyluke.jiexin.service.impl;

import com.luckyluke.jiexin.dao.SysCodeDao;
import com.luckyluke.jiexin.domain.SysCode;
import com.luckyluke.jiexin.service.SysCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Luke Wang on 2018/8/16.
 */
@Service
public class SysCodeServiceImpl implements SysCodeService {

    @Autowired
    SysCodeDao sysCodeDao;

    @Override
    public List<SysCode> find(Map paraMap) {
        return sysCodeDao.find(paraMap);
    }
}
