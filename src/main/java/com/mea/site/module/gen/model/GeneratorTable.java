package com.mea.site.module.gen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Michael Jou on 2018/3/4. 15:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "information_schema.tables")
public class GeneratorTable implements Serializable {

    @Column(name = "table_name")
    private String tableName;
    @Column(name = "engine")
    private String engine;
    @Column(name = "table_comment")
    private String tableComment;
    @Column(name = "create_time")
    private Date createTime;

}
