package com.mycompany.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "t_bigtype")
public class BigType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(length = 50)
	private String name;
	
	private String remark;
	
	@OneToMany(mappedBy="bigType",fetch=FetchType.EAGER)
	private Set<SmallType> smallTypes = new LinkedHashSet<>();
	
	@OneToMany(mappedBy="bigType")
	private Set<Product> products = new LinkedHashSet<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Set<SmallType> getSmallTypes() {
		return smallTypes;
	}
	public void setSmallTypes(Set<SmallType> smallTypes) {
		this.smallTypes = smallTypes;
	}
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
}
