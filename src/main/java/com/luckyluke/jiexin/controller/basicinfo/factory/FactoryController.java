package com.luckyluke.jiexin.controller.basicinfo.factory;

import com.luckyluke.jiexin.controller.BaseController;
import com.luckyluke.jiexin.domain.Factory;
import com.luckyluke.jiexin.service.FactoryService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luke Wang on 2018/7/25.
 */
@Controller
public class FactoryController extends BaseController {

    @Autowired
    FactoryService factoryService;

    //列表
    @RequestMapping("/basicinfo/factory/list.action")
    public String list(Model model) {
        List<Factory> dataList = factoryService.find();
        model.addAttribute("dataList", dataList);          //将数据传递到页面

        return "/basicinfo/factory/jFactoryList.jsp";       //转向页面
    }

    //跳转到新增页面
    @RequestMapping("/basicinfo/factory/tocreate.action")
    public String tocreate(){
        return  "/basicinfo/factory/jFactoryCreate.jsp";
    }

    //新增生产厂家
    @RequestMapping("/basicinfo/factory/insert.action")
    public String insert(Factory factory){
        factoryService.insert(factory);

        return "redirect:/basicinfo/factory/list.action";   //转向列表的action
    }

    //跳转到修改页面
    @RequestMapping("/basicinfo/factory/toupdate.action")
    public String toupdate(String id, Model model) {

        Factory obj = factoryService.get(id);
        model.addAttribute("obj", obj);

        return "/basicinfo/factory/jFactoryUpdate.jsp";
    }

    //修改生产厂家
    @RequestMapping("/basicinfo/factory/update.action")
    public String update(Factory factory) {

        factoryService.update(factory);

        return "redirect:/basicinfo/factory/list.action";
    }

    //删除生产厂家
    @RequestMapping("/basicinfo/factory/deleteById.action")
    public String deleteById(String id) {
        factoryService.deleteById(id);

        return "redirect:/basicinfo/factory/list.action";
    }

    //批量删除生产厂家
    @RequestMapping("/basicinfo/factory/delete.action")
    public String delete(@RequestParam("id") String[] ids) {
        factoryService.delete(ids);

        return "redirect:/basicinfo/factory/list.action";
    }

    //查看生产厂家
    @RequestMapping("/basicinfo/factory/toview.action")
    public String toview(String id, Model model) {
        Factory obj = factoryService.get(id);
        model.addAttribute("obj", obj);

        return "/basicinfo/factory/jFactoryView.jsp";
    }

    //批量启用
    @RequestMapping("/basicinfo/factory/start.action")
    public String statr(@RequestParam("id") String[] ids) {
        factoryService.start(ids);

        return "redirect:/basicinfo/factory/list.action";
    }

    //批量停用
    @RequestMapping("/basicinfo/factory/stop.action")
    public String stop(@RequestParam("id") String[] ids) {
        factoryService.stop(ids);

        return "redirect:/basicinfo/factory/list.action";
    }
}
