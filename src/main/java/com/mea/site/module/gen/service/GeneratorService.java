package com.mea.site.module.gen.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mea.site.common.base.service.BaseService;
import com.mea.site.common.base.service.CrudService;
import com.mea.site.common.support.Page;
import com.mea.site.common.utils.GenUtils;
import com.mea.site.module.gen.mapper.GeneratorMapper;
import com.mea.site.module.gen.model.GeneratorTable;
import com.mea.site.module.gen.model.GeneratorTableColumn;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * Created by Michael Jou on 2018/3/4. 15:35
 */
@Service
@Transactional(readOnly = false)
public class GeneratorService{

    @Autowired
    private GeneratorMapper generatorMapper;


    public PageInfo<GeneratorTable> findPage(Page<GeneratorTable> page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<GeneratorTable> eList = generatorMapper.list();
        PageInfo<GeneratorTable> pageInfos = new PageInfo<GeneratorTable>(eList);
        return pageInfos;
    }

    public List<GeneratorTableColumn> findList(String tableName) {

        List<GeneratorTableColumn> eList = generatorMapper.listColumns(tableName);
        return eList;
    }

    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            //查询表信息
            GeneratorTable table = generatorMapper.getOne(tableName);
            //查询列信息
            List<GeneratorTableColumn> columns = generatorMapper.listColumns(tableName);
            //生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
