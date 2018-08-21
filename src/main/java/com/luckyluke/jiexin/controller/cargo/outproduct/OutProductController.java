package com.luckyluke.jiexin.controller.cargo.outproduct;

import com.luckyluke.jiexin.controller.BaseController;
import com.luckyluke.jiexin.service.OutProductVOService;
import com.luckyluke.jiexin.vo.OutProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Luke Wang on 2018/8/16.
 */
@Controller
public class OutProductController extends BaseController {
    @Autowired
    OutProductVOService outProductVOService;

    //跳转到打印页面
    @RequestMapping("/cargo/outproduct/toedit.action")
    public String toprint(){
        return "/cargo/outproduct/jOutProduct.jsp";
    }

    //打印出货表
    @RequestMapping("/cargo/outproduct/print.action")
    public void print(String inputDate) {
        //获取数据
        List<OutProductVO> outProductVOList = outProductVOService.find(inputDate);
    }

}
