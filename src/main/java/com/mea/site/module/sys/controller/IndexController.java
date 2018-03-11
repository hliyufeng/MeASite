package com.mea.site.module.sys.controller;

import com.alibaba.fastjson.JSON;
import com.mea.site.common.annotation.Log;
import com.mea.site.common.base.controller.BaseController;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.utils.CacheUtils;
import com.mea.site.common.utils.ShiroUtils;
import com.mea.site.common.utils.WebUtils;
import com.mea.site.module.sys.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * @author Michael Jou
 * @PACKAGE com.mea.site.mybatis.mappers.module.sys.controller
 * @program: MeASite
 * @description: ${description}
 * @create: 2018-02-24 17:57
 */
@Controller
public class IndexController extends BaseController {

    @Log("请求登录页面")
    @RequestMapping(value = {"/", ""})
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        if( ShiroUtils.isLogin()){
            return WebUtils.redirect("/index");
        }
        return WebUtils.redirect("/login");
    }

    @RequestMapping(value = "/success")
    @ResponseBody
    public ResponseEntity success(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.build().OK();
    }

    @Log("请求主页页面")
    @GetMapping(value = "/index")
    public String index(Model model) {
        model.addAttribute("userinfo", ShiroUtils.getUser());
        return "index";
    }

    @Log("请求主页面")
    @RequestMapping(value = {"/main"})
    public String main() {
        return "main";
    }

    @RequestMapping("/403")
    String error403(HttpServletRequest request,HttpServletResponse response) {
          if(WebUtils.isAjax(request)){
              WebUtils.writeJson(response,JSON.toJSONString(ResponseEntity.build().OK(403)));
          }
        return "403";
    }
}
