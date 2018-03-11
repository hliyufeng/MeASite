package com.mea.site.module.sys.dto;

import com.mea.site.common.base.dto.BaseDto;
import com.mea.site.module.sys.model.Office;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by Michael Jou on 2018/3/6. 11:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto {

    private String id;
    private OfficeDto company = new OfficeDto();    // 归属公司
    private OfficeDto office = new OfficeDto();    // 归属部门
    private String loginName;// 登录名
    private String no;        // 工号
    private String name;    // 姓名
    private String email;    // 邮箱
    private String phone;    // 电话
    private String mobile;    // 手机
    private String userType;// 用户类型
    private String loginIp;    // 最后登陆IP
    private Date loginDate;    // 最后登陆日期
    private String loginFlag;    // 是否允许登陆
    private String photo;    // 头像

}
