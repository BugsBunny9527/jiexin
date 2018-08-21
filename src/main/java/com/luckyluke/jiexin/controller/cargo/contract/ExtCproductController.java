package com.luckyluke.jiexin.controller.cargo.contract;

import com.luckyluke.jiexin.controller.BaseController;
import com.luckyluke.jiexin.domain.ExtCproduct;
import com.luckyluke.jiexin.domain.Factory;
import com.luckyluke.jiexin.domain.SysCode;
import com.luckyluke.jiexin.service.ExtCproductService;
import com.luckyluke.jiexin.service.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luke Wang on 2018/8/16.
 */
@Controller
public class ExtCproductController extends BaseController{

    @Autowired
    FactoryService factoryService;

    @Autowired
    ExtCproductService extCproductService;

    //跳转到新增页面
    @RequestMapping("/cargo/extcproduct/tocreate.action")
    public String tocreate(String contractProductId, Model model){
        //维护对应货物的 id
        model.addAttribute("contractProductId",contractProductId);

        //可用生产厂家列表
        List<Factory> factoryList = factoryService.getFactoryList();
        model.addAttribute("factoryList", factoryList);

        //分类下拉列表
        List<SysCode> ctypeList = extCproductService.getSysCodeList();
        model.addAttribute("ctypeList", ctypeList);

        //对应货物下的附件信息
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("contractProductId", contractProductId);
        List<ExtCproduct> dataList = extCproductService.find(paraMap);
        model.addAttribute("dataList",dataList);

        return "/cargo/contract/jExtCproductCreate.jsp";
    }

    //新增附件
    @RequestMapping("/cargo/extcproduct/insert.action")
    public String insert(ExtCproduct extCproduct, Model model) {
        extCproductService.insert(extCproduct);

        //因为要重定向到新增页面，需要维护对应的货物 id
        model.addAttribute("contractProductId", extCproduct.getContractProductId());
        //重定向到新增页面
        return "redirect:/cargo/extcproduct/tocreate.action";
    }

    //跳转到更新页面
    @RequestMapping("/cargo/extcproduct/toupdate.action")
    public String toupdate(String id, Model model) {
        ExtCproduct obj = extCproductService.get(id);
        model.addAttribute("obj", obj);

        //可用生产厂家列表
        List<Factory> factoryList = factoryService.getFactoryList();
        model.addAttribute("factoryList", factoryList);

        //分类下拉列表
        List<SysCode> ctypeList = extCproductService.getSysCodeList();
        model.addAttribute("ctypeList", ctypeList);

        return "/cargo/contract/jExtCproductUpdate.jsp";
    }

    //修改附件
    @RequestMapping("/cargo/extcproduct/update.action")
    public String update(ExtCproduct extCproduct,Model model) {

        extCproductService.update(extCproduct);

        //传递主表id
        model.addAttribute("contractProductId", extCproduct.getContractProductId());

        //重定向到新增页面
        return "redirect:/cargo/extcproduct/tocreate.action";

    }

    //删除一个附件信息
    @RequestMapping("/cargo/extcproduct/deleteById.action")
    public String deleteById(String id,String contractProductId,Model model) {

        extCproductService.deleteById(id);

        //传递主表id
        model.addAttribute("contractProductId", contractProductId);

        return "redirect:/cargo/extcproduct/tocreate.action";
    }
}
