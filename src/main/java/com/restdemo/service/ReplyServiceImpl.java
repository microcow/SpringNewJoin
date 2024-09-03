package com.restdemo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.restdemo.domain.Reply;
import com.restdemo.mapper.BoardMapper;
import com.restdemo.mapper.ReplyMapper;

@Service
public class ReplyServiceImpl implements ReplyService{
	
	@Autowired
	ReplyMapper replyMapper;
	
	public List<Reply> ReplyList(int b_id){
		return replyMapper.ReplyList(b_id);
	}
	
	public void CreateReply(Reply reply) {
		replyMapper.CreateReply(reply);
	}
}
