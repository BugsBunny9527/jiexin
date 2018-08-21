package com.luckyluke.jiexin.service.impl;

import com.luckyluke.jiexin.dao.OutProductVODao;
import com.luckyluke.jiexin.service.OutProductVOService;
import com.luckyluke.jiexin.vo.OutProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Luke Wang on 2018/8/16.
 */
@Service
public class OutProductVOServiceImpl implements OutProductVOService {

    @Autowired
    OutProductVODao outProductVODao;

    @Override
    public List<OutProductVO> find(String time) {
        return outProductVODao.find(time);
    }

}
