
package com.mea.site.module.sys.model;


import com.mea.site.common.base.model.BaseEntity;
import com.mea.site.common.base.model.DataEntity;
import com.mea.site.common.base.model.TreeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 区域Entity
 *
 * @author Michael
 */
@Table(name = "sys_area")
@Data
@AllArgsConstructor
public class Area extends DataEntity<Area> {


    @Transient
    protected Area parent;    // 父级编号

    @Column(name = "parent_id")
    private String parentId;

    /**
     * 父级名称
     */
    @Column(name = "parent_name")
    private String parentName;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    @Column(name = "sort")
    private int sort;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "area_code")
    private String areaCode;

    @Column(name = "depth")
    private String depth;

    public Area() {
        super();
    }
}