package com.mea.site.common.base.controller;

import com.mea.site.common.beanvalidator.BeanValidators;
import com.mea.site.common.config.constants.Constant;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;

public abstract class  BaseController {
    @Autowired
    protected Validator validator;
    protected Map<String,Object> errorMsg = Maps.newConcurrentMap();

    /**
     * 服务端参数有效性验证
     * @param object 验证的实体对象
     * @param groups 验证组
     * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
     */
    protected boolean beanValidator(Object object, Class<?>... groups) {
        try{
            BeanValidators.validateWithException(validator, object, groups);
        }catch(ConstraintViolationException ex){
            List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
            list.add(0, "数据验证失败：");
            errorMsg.clear();
            errorMsg.put(Constant.ERROR_MESSAGE, list.toArray(new String[]{}));
            return false;
        }
        return true;
    }
}