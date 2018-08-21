package com.luckyluke.jiexin.dao.impl;

import com.luckyluke.jiexin.dao.SysCodeDao;
import com.luckyluke.jiexin.domain.SysCode;
import org.springframework.stereotype.Repository;

/**
 * Created by Luke Wang on 2018/8/16.
 */
@Repository
public class SysCodeDaoImpl extends BaseDaoImpl<SysCode> implements SysCodeDao {
    public SysCodeDaoImpl() {
        super.setNs("com.luckyluke.jiexin.mapper.SysCodeMapper");
    }
}
