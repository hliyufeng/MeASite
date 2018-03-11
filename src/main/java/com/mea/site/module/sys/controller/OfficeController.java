package com.mea.site.module.sys.controller;

import com.alibaba.fastjson.JSON;
import com.mea.site.common.annotation.Log;
import com.mea.site.common.base.controller.BaseController;
import com.mea.site.common.config.constants.Constant;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.utils.DictUtils;
import com.mea.site.common.utils.ShiroUtils;
import com.mea.site.common.utils.WebUtils;
import com.mea.site.module.sys.model.Dict;
import com.mea.site.module.sys.model.Office;
import com.mea.site.module.sys.model.User;
import com.mea.site.module.sys.service.OfficeService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jou on 2018/3/5. 15:25
 * 机构管理
 */
@Controller
@RequestMapping(value = "/sys/office")
public class OfficeController extends BaseController {

    private String prefix = "sys/office";

    @Autowired
    private OfficeService officeService;


    @ModelAttribute("office")
    public Office get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return officeService.get(id);
        } else {
            return new Office();
        }
    }

    @Log("机构页面")
    @RequiresPermissions("sys:office:view")
    @RequestMapping(value = {"/", ""})
    public String office(Model model) {
        List<Dict> dictList = DictUtils.getDictList(Constant.SYS_OFFICE_TYPE);
        model.addAttribute("sysOfficeType", JSON.toJSONString(dictList));
        return WebUtils.requireView(prefix + "/office");
    }

    /**
     * @param office
     * @return
     */
    @Log("机构列表")
    @RequiresPermissions("sys:office:list")
    @RequestMapping(value = {"list"})
    @ResponseBody
    public List<Office> list(Office office) {
        List<Office> offices = officeService.findList(office);
        return offices;
    }

    @Log("新增或修改机构")
    @RequiresPermissions({"sys:office:add","sys:office:edit"})
    @RequestMapping(value = "form")
    public String form(Office office, Model model) {
        User user = ShiroUtils.getUser();
        if (office.getParent() == null || office.getParent().getId() == null) {
            office.setParent(user.getOffice());
        }
        office.setParent(officeService.get(office.getParent().getId()));
        if (office.getArea() == null) {
            office.setArea(user.getOffice().getArea());
        }
        // 自动获取排序号
        if (StringUtils.isBlank(office.getId()) && office.getParent() != null) {
            int size = 0;
            List<Office> list = officeService.findAll();
            for (int i = 0; i < list.size(); i++) {
                Office e = list.get(i);
                if (e.getParent() != null && e.getParent().getId() != null
                        && e.getParent().getId().equals(office.getParent().getId())) {
                    size++;
                }
            }
            office.setCode(office.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size + 1 : 1), 3, "0"));
        }
        model.addAttribute("office", office);
        model.addAttribute("sysUserType", DictUtils.getDictList(Constant.SYS_USER_TYPE));
        model.addAttribute("sysData", DictUtils.getDictList(Constant.SYS_DATA));
        model.addAttribute("sysOfficeType", DictUtils.getDictList(Constant.SYS_OFFICE_TYPE));
        model.addAttribute("sysOfficeGrade",DictUtils.getDictList(Constant.SYS_OFFICE_GRADE));
        model.addAttribute("sysOfficeCommon",DictUtils.getDictList(Constant.SYS_OFFICE_COMMON));
        return WebUtils.requireView(prefix + "/form");
    }

    @Log("保存机构")
    @RequiresPermissions({"sys:office:save","sys:office:update"})
    @RequestMapping(value = "save")
    @ResponseBody
    public ResponseEntity save(Office office) {
        if (!beanValidator(office)) {
            return ResponseEntity.build().fail(Constant.SAVE_FAIL);
        }
        this.officeService.saveOrUpdate(office);
        return ResponseEntity.build().OK("保存机构'" + office.getName() + "'成功");
    }

    @Log("删除机构")
    @RequiresPermissions("sys:office:delete")
    @RequestMapping(value = "delete")
    @ResponseBody
    public ResponseEntity delete(Office office) {
        if (officeService.delete(office)) {
            return ResponseEntity.build().OK("删除机构成功");
        }
        return ResponseEntity.build().OK("删除机构失败");
    }


    /**
     * 获取机构JSON数据。
     *
     * @param extId    排除的ID
     * @param type     类型（1：公司；2：部门/小组/其它：3：用户）
     * @param grade    显示级别
     * @param response
     * @return
     */
    @Log("获取机构树")
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, @RequestParam(required = false) String type,
                                              @RequestParam(required = false) Long grade, @RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Office> list = officeService.findList(isAll);
        for (int i = 0; i < list.size(); i++) {
            Office e = list.get(i);
            if ((StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
                    && (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
                    && (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
                    && Constant.YES.equals(e.getUseable())) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("pIds", e.getParentIds());
                map.put("name", e.getName());
                if (type != null && "3".equals(type)) {
                    map.put("isParent", true);
                }
                mapList.add(map);
            }
        }
        return mapList;
    }

}
