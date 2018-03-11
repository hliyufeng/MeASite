
package com.mea.site.module.sys.model;

import com.mea.site.common.base.model.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * 字典Entity
 * @author Michael
 */
@Table(name = "sys_dict")
public class Dict extends DataEntity<Dict> {

	private static final long serialVersionUID = 1L;
	@Column(name = "value")
	private String value;	// 数据值
	@Column(name = "label")
	private String label;	// 标签名
	@Column(name = "type")
	private String type;	// 类型
	@Column(name = "description")
	private String description;// 描述
	@Column(name = "sort")
	private Integer sort;	// 排序


	public Dict() {
		super();
	}
	
	public Dict(String id){
		super(id);
	}
	
	public Dict(String value, String label){
		this.value = value;
		this.label = label;
	}
	
	@XmlAttribute
	@Length(min=1, max=100)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlAttribute
	@Length(min=1, max=100)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Length(min=1, max=100)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	@Length(min=0, max=100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return label;
	}
}