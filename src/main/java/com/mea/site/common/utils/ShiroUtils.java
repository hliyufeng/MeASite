package com.mea.site.common.utils;

import com.mea.site.module.sys.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

@Slf4j
public class ShiroUtils {



    public static Subject getSubjct() {
        try {
            return SecurityUtils.getSubject();
        } catch (Exception e) {

        }
        return null;
    }

    public static void logout() {
        Subject subject = getSubjct();
        if (subject.isAuthenticated()) {
            getSubjct().logout();
        }
    }


    public static Session getSession() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
        } catch (InvalidSessionException e) {
            log.error("InvalidSessionException", e);
        }
        return null;
    }

    /**
     * 获取当前用户
     *
     * @return 取不到返回 new User()
     */
    public static User getUser() {
        Subject subject = getSubjct();
        if(subject!= null){
            User user = (User) subject.getPrincipal();
            if (user != null) {
                return user;
            }
        }

        return new User();
    }

    /**
     * 获取当前登录者对象
     */
    public static boolean  isLogin(){
        try{
            Subject subject = SecurityUtils.getSubject();
            Object obj = subject.getPrincipal();
            if (obj != null){
                return true;
            }
        }catch (UnavailableSecurityManagerException e) {

        }catch (InvalidSessionException e){

        }
        return false;
    }

}

