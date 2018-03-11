package com.mea.site.module.gen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Michael Jou on 2018/3/4. 15:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "information_schema.columns")
public class GeneratorTableColumn implements Serializable {

    @Column(name = "column_name")
    private String columnName;
    @Column(name = "data_type")
    private String dataType;
    @Column(name = "column_comment")
    private String columnComment;
    @Column(name = "column_key")
    private String columnKey;
    @Column(name = "extra")
    private String extra;
    /**
     * 默认值
     */
    @Column(name = "column_default")
    private String columnDefault;
    @Column(name = "is_nullable")
    private boolean isNullable;
    @Column(name = "column_type")
    private String columnType;

}
