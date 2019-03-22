package com.mycompany.entity;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "t_product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 50)
	private String name;

	private float price;

	private int stock;//库存

	private String imageName;

	@Column(length = 2000)
	private String description;

	private boolean hot;// 是否热卖

	private Calendar hotStartTime;

	private Calendar hotEndTime;

	private boolean onSale;// 是否特价

	private float onSalePrice;

	private Calendar onSaleStartTime;

	private Calendar onSaleEndTime;

	@ManyToOne
	@JoinColumn(name="bigTypeId")
	private BigType bigType;
	
	@ManyToOne
	@JoinColumn(name="smallTypeId")
	private SmallType smallType;
	
	@OneToMany(mappedBy="product",orphanRemoval=true)
	private Set<Comment> comments = new LinkedHashSet<>();
	
	@OneToMany(mappedBy="product")
	private Set<OrderProduct> orders = new LinkedHashSet<>();
	
	@OneToMany(mappedBy="product")
	private Set<CartProduct> carts = new LinkedHashSet<>();
	
	public Product() {
		super();
	}

	public Product(boolean hot, boolean onSale) {
		super();
		this.hot = hot;
		this.onSale = onSale;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigType getBigType() {
		return bigType;
	}

	public void setBigType(BigType bigType) {
		this.bigType = bigType;
	}

	public SmallType getSmallType() {
		return smallType;
	}

	public void setSmallType(SmallType smallType) {
		this.smallType = smallType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isHot() {
		return hot;
	}

	public void setHot(boolean hot) {
		this.hot = hot;
	}


	public boolean isOnSale() {
		return onSale;
	}

	public void setOnSale(boolean onSale) {
		this.onSale = onSale;
	}


	public Calendar getHotStartTime() {
		return hotStartTime;
	}

	public void setHotStartTime(Calendar hotStartTime) {
		this.hotStartTime = hotStartTime;
	}

	public Calendar getHotEndTime() {
		return hotEndTime;
	}

	public void setHotEndTime(Calendar hotEndTime) {
		this.hotEndTime = hotEndTime;
	}

	public Calendar getOnSaleStartTime() {
		return onSaleStartTime;
	}

	public void setOnSaleStartTime(Calendar onSaleStartTime) {
		this.onSaleStartTime = onSaleStartTime;
	}

	public Calendar getOnSaleEndTime() {
		return onSaleEndTime;
	}

	public void setOnSaleEndTime(Calendar onSaleEndTime) {
		this.onSaleEndTime = onSaleEndTime;
	}

	public Set<CartProduct> getCarts() {
		return carts;
	}

	public void setCarts(Set<CartProduct> carts) {
		this.carts = carts;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}


	public Set<OrderProduct> getOrders() {
		return orders;
	}

	public void setOrders(Set<OrderProduct> orders) {
		this.orders = orders;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getOnSalePrice() {
		return onSalePrice;
	}

	public void setOnSalePrice(float onSalePrice) {
		this.onSalePrice = onSalePrice;
	}
	
}
