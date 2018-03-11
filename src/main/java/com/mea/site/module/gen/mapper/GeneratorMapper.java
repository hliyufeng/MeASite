package com.mea.site.module.gen.mapper;

import com.mea.site.common.base.mapper.BaseMapper;
import com.mea.site.module.gen.model.GeneratorTable;
import com.mea.site.module.gen.model.GeneratorTableColumn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jou on 2018/3/4. 15:33
 */
@Mapper
public interface GeneratorMapper {

    @Select("select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables"
            + " where table_schema = (select database())")
    List<GeneratorTable> list();

    @Select("select count(*) from information_schema.tables where table_schema = (select database())")
    int count(Map<String, Object> map);

    @Select("select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables \r\n"
            + "	where table_schema = (select database()) and table_name = #{tableName}")
    GeneratorTable getOne(String tableName);



    @Select("select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra ," +
            " column_default columnDefault ,is_nullable isNullable,column_type columnType from information_schema.columns\r\n"
            + " where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
    List<GeneratorTableColumn> listColumns(String tableName);


}
