
package com.mea.site.module.sys.model;

import com.mea.site.common.base.model.TreeEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 机构Entity
 * @author Michaeljou
 */
@Table(name = "sys_office")
public class Office extends TreeEntity<Office> {

	private static final long serialVersionUID = 1L;
	@Transient
	private Area area;		// 归属区域
	@Column(name = "parent_id")
	@Transient
	private Long parentIdl;
	@Transient
	private String parentIdt;
	@Column(name = "code")
	private String code; 	// 机构编码
	@Column(name = "type")
	private String type; 	// 机构类型（1：公司；2：部门；3：小组）
	@Column(name = "grade")
	private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	@Column(name = "address")
	private String address; // 联系地址
	@Column(name = "zip_code")
	private String zipCode; // 邮政编码
	@Column(name = "master")
	private String master; 	// 负责人
	@Column(name = "phone")
	private String phone; 	// 电话
	@Column(name = "fax")
	private String fax; 	// 传真
	@Column(name = "email")
	private String email; 	// 邮箱
	@Column(name = "useable")
	private String useable;//是否可用
	@Transient
	private List<String> childDeptList;//快速添加子部门
	
	public Office(){
		super();
		this.type = "2";
	}

	public Office(String id){
		super(id);
	}
	
	public List<String> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<String> childDeptList) {
		this.childDeptList = childDeptList;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}


//	@JsonBackReference
//	@NotNull
	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}


	@NotNull
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	
	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=1, max=1)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=100)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(min=0, max=100)
	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Length(min=0, max=200)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Length(min=0, max=200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getParentIdl() {
		return parentIdl;
	}

	public void setParentIdl(Long parentIdl) {
		this.parentIdl = parentIdl;
	}

	public String getParentIdt() {
		return parentIdt;
	}

	public void setParentIdt(String parentIdt) {
		this.parentIdt = parentIdt;
	}

	@Override
	public String toString() {
		return name;
	}
}