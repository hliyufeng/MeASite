package com.mea.site.common.base.model;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.feilong.core.Validator;
import com.mea.site.common.utils.ShiroUtils;
import com.mea.site.common.utils.StringUtils;
import com.mea.site.module.sys.model.User;
import com.google.common.collect.Maps;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Map;

/**
 * Created by MichaelJou on 2018/02/02.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseEntity<T> extends AbstractEntity<T> {

    private static final long serialVersionUID = 1L;

    /**
     * 实体编号（唯一标识）
     */
    @Transient
    protected String id;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long idI;

    /**
     * 当前用户
     */
    @Transient
    @JsonIgnore
    protected User currentUser;

    /**
     * 自定义SQL（SQL标识，SQL内容）
     */
    @Transient
    @JsonIgnore
    protected Map<String, String> sqlMap;

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     */
    @Transient
    @JsonIgnore
    protected boolean isNewRecord = false;

    /**
     * 插入之前执行方法，子类实现
     */
    public abstract void preInsert();

    /**
     * 更新之前执行方法，子类实现
     */
    public abstract void preUpdate();

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     *
     * @return
     */
    public boolean getIsNewRecord() {
        return isNewRecord || StringUtils.isBlank(getId());
    }

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     */
    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    @Transient
    @JsonIgnore
    public static final String DEL_FLAG_NORMAL = "0";
    @Transient
    @JsonIgnore
    public static final String DEL_FLAG_DELETE = "1";
    @Transient
    @JsonIgnore
    public static final String DEL_FLAG_AUDIT = "2";


    public BaseEntity() {

    }

    public BaseEntity(String id) {
        this();
        this.id = id;
        setId(id);
    }

    public String getId() {
        if (Validator.isNotNullOrEmpty(idI)) {
            id = String.valueOf(idI);
        }
        return id;
    }

    public void setId(String id) {
        if (Validator.isNotNullOrEmpty(id)) {
            if (NumberUtil.isLong(id)) {
                setIdI(Long.valueOf(id));
            }
        }
        this.id = id;
    }

    public Long getIdI() {
        return idI;
    }

    public void setIdI(Long idI) {
        if (Validator.isNotNullOrEmpty(idI)) {
            this.id = String.valueOf(idI);
        }
        this.idI = idI;
    }

    @JsonIgnore
    @XmlTransient
    public Map<String, String> getSqlMap() {
        if (sqlMap == null) {
            sqlMap = Maps.newHashMap();
        }
        return sqlMap;
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    @JsonIgnore
    @XmlTransient
    public User getCurrentUser() {
        if (currentUser == null) {
            currentUser = ShiroUtils.getUser();
        }
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }


}
