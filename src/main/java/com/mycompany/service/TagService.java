package com.mycompany.service;

import java.util.List;

import com.mycompany.entity.PageBean;
import com.mycompany.entity.Tag;

public interface TagService {
	
	public List<Tag> getAllTags();
	
	public List<Tag> getTagList(Tag s_tag, PageBean pageBean);
	
	public Long getTagCount(Tag s_tag);
	
	public void save(Tag tag);
	
	public void delete(Tag tag);
	
	public Tag getTagById(int id);
	
}
