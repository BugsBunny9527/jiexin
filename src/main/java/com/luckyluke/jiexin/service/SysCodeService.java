package com.luckyluke.jiexin.service;

import com.luckyluke.jiexin.domain.SysCode;

import java.util.List;
import java.util.Map;

/**
 * Created by Luke Wang on 2018/8/16.
 */
public interface SysCodeService {
    List<SysCode> find(Map paraMap);          //带条件查询，条件可以为null，即没有条件；返回List对象
}
