package com.luckyluke.jiexin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Luke Wang on 2018/7/26.
 */

@Controller
public class HomeController{

    @RequestMapping({"/home.action"})
    public String login()
    {
        return "/index.jsp";
    }

    @RequestMapping({"/fmain.action"})
    public String fmain()
    {
        return "/home/fmain.jsp";
    }

    @RequestMapping({"/title.action"})
    public String title()
    {
        return "/home/title.jsp";
    }

    @RequestMapping({"/left.action"})
    public String left()
    {
        return "/home/left.jsp";
    }

    @RequestMapping({"/main.action"})
    public String main()
    {
        return "/home/olmsgList.jsp";
    }

    @RequestMapping({"/sysadminMain.action"})
    public String sysadminMain()
    {
        return "/sysadmin/main.jsp";
    }

    @RequestMapping({"/sysadminLeft.action"})
    public String sysadminLeft()
    {
        return "/sysadmin/left.jsp";
    }

    @RequestMapping({"/baseinfoMain.action"})
    public String baseinfoMain()
    {
        return "/baseinfo/main.jsp";
    }

    @RequestMapping({"/baseinfoLeft.action"})
    public String baseinfoLeft()
    {
        return "/baseinfo/left.jsp";
    }

    @RequestMapping({"/cargoMain.action"})
    public String cargoMain()
    {
        return "/cargo/main.jsp";
    }

    @RequestMapping({"/cargoLeft.action"})
    public String cargoLeft()
    {
        return "/cargo/left.jsp";
    }
}
