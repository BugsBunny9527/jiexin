package com.luckyluke.jiexin.service;

import com.luckyluke.jiexin.vo.OutProductVO;

import java.util.List;

/**
 * Created by Luke Wang on 2018/8/16.
 */
public interface OutProductVOService {
    List<OutProductVO> find(String time);
}
