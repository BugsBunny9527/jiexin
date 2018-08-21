package com.luckyluke.jiexin.controller.cargo.contract;

import com.luckyluke.jiexin.dao.FactoryDao;
import com.luckyluke.jiexin.domain.ContractProduct;
import com.luckyluke.jiexin.domain.Factory;
import com.luckyluke.jiexin.service.ContractProductService;
import com.luckyluke.jiexin.service.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luke Wang on 2018/8/13.
 */
@Controller
public class ContractProductController {

    @Autowired
    ContractProductService contractProductService;

    @Autowired
    FactoryService factoryService;

    //跳转到新增页面
    @RequestMapping("/cargo/contractproduct/tocreate.action")
    public String tocreate(String contractId, Model model) {
        model.addAttribute("contractId",contractId);

        List<Factory> factoryList = factoryService.getFactoryList();
        model.addAttribute("factoryList", factoryList);

        Map paraMap = new HashMap();
        paraMap.put("contractId", contractId);
        List<ContractProduct> dataList = contractProductService.find(paraMap);
        model.addAttribute("dataList", dataList);

        return "/cargo/contract/jContractProductCreate.jsp";
    }

    //新增货物信息
    @RequestMapping("/cargo/contractproduct/insert.action")
    public String insert(ContractProduct contractProduct, Model model) {
        contractProductService.insert(contractProduct);

        model.addAttribute("contractId", contractProduct.getContractId());

        return "redirect:/cargo/contractproduct/tocreate.action";
    }

    //跳转到更新页面
    @RequestMapping("cargo/contractproduct/toupdate.action")
    public String toupdate(String id, Model model) {
        //将要修改的货物信息保存到 request 域中，转发到修改页面
        ContractProduct contractProduct = contractProductService.get(id);
        model.addAttribute("obj", contractProduct);

        //将所有启用的生产厂家信息保存到 request 域中，转发到修改页面，用作下拉列表
        List<Factory> factoryList = factoryService.getFactoryList();
        model.addAttribute("factoryList", factoryList);

        return "/cargo/contract/jContractProductUpdate.jsp";
    }

    //更新合同货物
    @RequestMapping("cargo/contractproduct/update.action")
    public String update(ContractProduct contractProduct) {
        contractProductService.update(contractProduct);

        return "redirect:/cargo/contract/list.action";
    }

    //删除单个合同货物
    @RequestMapping("cargo/contractproduct/deleteById.action")
    public String deleteById(String contractId, String id, Model model) {

        contractProductService.deleteById(id);

        //确保被添加到的合同不会丢失
        model.addAttribute("contractId", contractId);

        return "redirect:/cargo/contractproduct/tocreate.action";
    }

}
