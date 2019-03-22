package com.mycompany.entity;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "t_user")
public class User {

	/**
	 * 管理员
	 */
	@Transient
	public static final int ADMINISTRATOR = -1;

	/**
	 * 平民
	 */
	@Transient
	public static final int NORMAL = 0;

	/**
	 * vip会员
	 */
	@Transient
	public static final int VIP = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 20)
	private String userName;

	@Column(length = 20)
	private String password;

	@Column(length = 20)
	private String realName;

	@Column(length = 20)
	private String nickName;

	@Column(length = 50)
	private String userImage;

	@Column(length = 5)
	private String sex;

	@Type(type = "calendar_date")
	private Calendar birthday;

	@Column(length = 20)
	private String identityCode;

	@Column(length = 20)
	private String email;

	@Column(length = 20)
	private String mobile;

	@Column(length = 200)
	private String address;

	private int status = NORMAL;

	@Transient
	private String stringStatus;

	@OneToOne(optional = false, cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "cartId", unique = true)
	private Cart cart;

	@OneToMany(mappedBy = "user")
	private Set<Comment> comments = new LinkedHashSet<>();

	@OneToMany(mappedBy = "user")
	private Set<InnerComment> innerComments = new LinkedHashSet<>();

	@OneToMany(mappedBy = "user")
	private Set<Order> orders = new LinkedHashSet<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Set<InnerComment> getInnerComments() {
		return innerComments;
	}

	public void setInnerComments(Set<InnerComment> innerComments) {
		this.innerComments = innerComments;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Calendar getBirthday() {
		return birthday;
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public String getIdentityCode() {
		return identityCode;
	}

	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	public void setStringStatus(String stringStatus) {
		this.stringStatus = stringStatus;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getStringStatus() {
		if (status == ADMINISTRATOR)
			return "管理员";
		if (status == NORMAL)
			return "普通用户";
		if (status == 1)
			return "VIP用户";
		return null;
	}
}
