package com.mycompany.service;

import java.util.List;

import com.mycompany.entity.Notice;
import com.mycompany.entity.PageBean;

public interface NoticeService {
	
	public List<Notice> getNoticeList(Notice s_notice, PageBean pageBean);
	
	public Notice getNoticeById(int id);
	
	public Long getNoticeCount(Notice s_notice);
	
	public void save(Notice notice);
	
	public void delete(Notice notice);
}
