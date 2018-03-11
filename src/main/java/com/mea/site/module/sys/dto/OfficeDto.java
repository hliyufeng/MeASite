package com.mea.site.module.sys.dto;

import com.mea.site.common.base.dto.BaseDto;
import com.mea.site.module.sys.model.Area;
import com.mea.site.module.sys.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Michael Jou on 2018/3/6. 11:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeDto extends BaseDto {
    private Area area;        // 归属区域
    private String code;    // 机构编码
    private String type;    // 机构类型（1：公司；2：部门；3：小组）
    private String grade;    // 机构等级（1：一级；2：二级；3：三级；4：四级）
    private String address; // 联系地址
    private String zipCode; // 邮政编码
    private String master;    // 负责人
    private String phone;    // 电话
    private String fax;    // 传真
    private String email;    // 邮箱
    private String useable;//是否可用
    private UserDto primaryPerson;//主负责人
    private UserDto deputyPerson ;;//副负责人
    protected OfficeDto parent ;    // 父级编号
    protected String parentIds; // 所有父级编号
    protected String name;    // 机构名称
    protected Integer sort;        // 排序
}
