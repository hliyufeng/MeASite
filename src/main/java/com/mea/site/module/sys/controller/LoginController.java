package com.mea.site.module.sys.controller;

import com.feilong.core.Validator;
import com.mea.site.common.annotation.Log;
import com.mea.site.common.base.controller.BaseController;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.utils.CacheUtils;
import com.mea.site.common.utils.ShiroUtils;
import com.mea.site.common.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lenovo on 2018/2/25.
 */
@Controller
@Slf4j
public class LoginController extends BaseController {

    /**
     * 登录页面
     *
     * @return
     */
    @Log("访问登录页面")
    @RequestMapping(value = "/login")
    public String login() {
        if (ShiroUtils.isLogin()) {
            return WebUtils.redirect("/index");
        }
        return "login";
    }


    /**
     * 真正的登录
     *
     * @param username
     * @param password
     * @return
     */
    @Log("登录")
    @PostMapping("/login")
    @ResponseBody
    ResponseEntity ajaxLogin(String username, String password, String rememberMe, String code) {
        try {
            if (Validator.isNotNullOrEmpty(username) && Validator.isNotNullOrEmpty(password) && Validator.isNotNullOrEmpty(code)) {
                if (!CommonController.validateCode(code)) {
                    return ResponseEntity.build().fail("验证码错误");
                }

                UsernamePasswordToken token = new UsernamePasswordToken(username, password);
                token.setRememberMe(rememberMe == null ? false : true);
                Subject subject = SecurityUtils.getSubject();
                if (!subject.isAuthenticated()) {
                    if (log.isDebugEnabled()) {
                        log.debug("用户登录:{}", username);
                    }
                    subject.login(token);
                }
                return ResponseEntity.build().OK();
            } else {
                return ResponseEntity.build().fail("用户或密码错误");
            }

        } catch (AuthenticationException e) {
            log.error("登录失败", e);
            return ResponseEntity.build().fail("用户或密码错误");
        } catch (Exception e) {
            log.error("登录失败", e);
            return ResponseEntity.build().fail("用户或密码错误");
        }
    }

    @Log("退出登录")
    @RequestMapping("/logout")
    public String logout() {
        ShiroUtils.logout();
        return WebUtils.redirect("/login");
    }
}
