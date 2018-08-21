package com.luckyluke.jiexin.controller.cargo.contract;

import com.luckyluke.jiexin.controller.BaseController;
import com.luckyluke.jiexin.domain.Contract;
import com.luckyluke.jiexin.service.ContractService;
import com.luckyluke.jiexin.vo.ContractVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Luke Wang on 2018/8/9.
 */
@Controller
public class ContractController extends BaseController {
    @Autowired
    ContractService contractService;

    @RequestMapping("/cargo/contract/list.action")
    public String find(Model model) {
        List<Contract> dataList = contractService.find(null);
        model.addAttribute("dataList", dataList);

        return "/cargo/contract/jContractList.jsp";
    }

    @RequestMapping("/cargo/contract/tocreate.action")
    public String toInsert() {
        return "/cargo/contract/jContractCreate.jsp";
    }

    @RequestMapping("/cargo/contract/insert.action")
    public String insert(Contract contract) {
        contractService.insert(contract);

        System.out.println("insert......");
        return "redirect:/cargo/contract/list.action";
    }

    @RequestMapping("/cargo/contract/toupdate.action")
    public String toupdate(String id, Model model) {
        Contract contract = contractService.get(id);
        model.addAttribute("obj", contract);

        return "/cargo/contract/jContractUpdate.jsp";
    }

    @RequestMapping("/cargo/contract/update.action")
    public String update(Contract contract) {
        contractService.update(contract);

        return "redirect:/cargo/contract/list.action";
    }

    @RequestMapping("/cargo/contract/delete.action")
    public String deleteById(String id) {
        contractService.deleteById(id);

        return "redirect:/cargo/contract/list.action";
    }

    @RequestMapping("/cargo/contract/toview.action")
    public String toview(String id, Model model) {
        ContractVO contractVO = contractService.view(id);
        model.addAttribute("obj", contractVO);

        return "/cargo/contract/jContractView.jsp";
    }

    @RequestMapping("/cargo/contract/submit.action")
    public String submit(String[] id) {
        contractService.submit(id);

        return "redirect:/cargo/contract/list.action";
    }

    @RequestMapping("/cargo/contract/cancel.action")
    public String cancel(String[] id) {
        contractService.cancel(id);

        return "redirect:/cargo/contract/list.action";
    }
}
