package com.mea.site.module.gen.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.mea.site.common.annotation.Log;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.support.Page;
import com.mea.site.common.utils.GenUtils;
import com.mea.site.common.utils.WebUtils;
import com.mea.site.module.gen.model.GeneratorTable;
import com.mea.site.module.gen.model.GeneratorTableColumn;
import com.mea.site.module.gen.service.GeneratorService;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jou on 2018/3/4. 15:58
 */
@Controller
@RequestMapping(value = "/gen")
public class GeneratorController {
    String prefix = "gen";
    @Autowired
    private GeneratorService generatorService;

    @Log("代码生成页面")
    @RequestMapping(value = {"/",""})
    public String view(){
        return WebUtils.requireView("gen/generator_table");
    }

    @Log("代码生成列表")
    @RequestMapping(value = {"list"})
    @ResponseBody
    public ResponseEntity list(GeneratorTable generatorTable, Page<GeneratorTable> page, Model model) {
        PageInfo<GeneratorTable> list = generatorService.findPage(page.put(generatorTable));
        return ResponseEntity.build().OK().calPage(list);
    }


    @RequestMapping(value = "/tableColumn")
    public String columns(String tableName,Model model){

        model.addAttribute("tableName",tableName);
         return WebUtils.requireView("gen/generator_table_column");
    }

    @Log("代码生成列表")
    @RequestMapping(value = {"listColumn"})
    @ResponseBody
    public ResponseEntity listColumn(GeneratorTableColumn generatorTable, Page<GeneratorTableColumn> page,String tableName, Model model) {
        List<GeneratorTableColumn> list = generatorService.findList(tableName);
        return ResponseEntity.build().OK(list);
    }

    @RequestMapping("/code/{tableName}")
    public void code(HttpServletRequest request, HttpServletResponse response,
                     @PathVariable("tableName") String tableName) throws IOException {
        String[] tableNames = new String[] { tableName };
        byte[] data = generatorService.generatorCode(tableNames);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"MeASite.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    @RequestMapping("/batchCode")
    public void batchCode(HttpServletRequest request, HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = new String[] {};
        tableNames = JSON.parseArray(tables).toArray(tableNames);
        byte[] data = generatorService.generatorCode(tableNames);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"MeASite.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        Configuration conf = GenUtils.getConfig();
        Map<String, Object> property = new HashMap<>(16);
        property.put("author", conf.getProperty("author"));
        property.put("email", conf.getProperty("email"));
        property.put("package", conf.getProperty("package"));
        property.put("autoRemovePre", conf.getProperty("autoRemovePre"));
        property.put("tablePrefix", conf.getProperty("tablePrefix"));
        model.addAttribute("property", property);
        return prefix + "/edit";
    }

    @ResponseBody
    @PostMapping("/update")
    ResponseEntity update(@RequestParam Map<String, Object> map) {
        try {
            PropertiesConfiguration conf = new PropertiesConfiguration("generator.properties");
            conf.setProperty("author", map.get("author"));
            conf.setProperty("email", map.get("email"));
            conf.setProperty("package", map.get("package"));
            conf.setProperty("autoRemovePre", map.get("autoRemovePre"));
            conf.setProperty("tablePrefix", map.get("tablePrefix"));
            conf.save();
        } catch (ConfigurationException e) {
            return ResponseEntity.build().error("保存配置文件出错");
        }
        return ResponseEntity.build().OK();
    }
}
