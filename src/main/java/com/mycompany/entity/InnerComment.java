package com.mycompany.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_innercomment")
public class InnerComment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 1000)
	private String content;

	private Calendar createTime;

	@ManyToOne(optional=false)
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne
	@JoinColumn(name = "replyToUserId")
	private User replyToUser;//被回复的用户

	@ManyToOne(optional=false)
	@JoinColumn(name="parentCommentId")
	private Comment parentComment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getReplyToUser() {
		return replyToUser;
	}

	public void setReplyToUser(User replyToUser) {
		this.replyToUser = replyToUser;
	}

	public String getContent() {
		return content;
	}

	public Comment getParentComment() {
		return parentComment;
	}

	public void setParentComment(Comment parentComment) {
		this.parentComment = parentComment;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public Calendar getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Calendar createTime) {
		this.createTime = createTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
