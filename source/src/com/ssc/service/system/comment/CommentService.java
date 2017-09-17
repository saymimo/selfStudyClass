package com.ssc.service.system.comment;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ssc.dao.DaoSupport;
import com.ssc.util.PageData;

@Service("commentService")
public class CommentService {
	@Resource(name="daoSupport")
	  private DaoSupport dao;
	
	public List<PageData> findCommentList(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("CommentMapper.findCommentList", pd);
	}
	public PageData findComment(PageData pd) throws Exception{
		return (PageData)dao.findForObject("CommentMapper.findComment", pd);
	}
	
	public void updateByid(PageData pd) throws Exception{
		dao.update("CommentMapper.updateByid", pd);
	}
	public void saveComment(PageData pd) throws Exception{
		dao.save("CommentMapper.saveComment", pd);
	}
}
