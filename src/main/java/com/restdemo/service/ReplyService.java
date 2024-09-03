package com.restdemo.service;

import java.util.List;
import com.restdemo.domain.Reply;

public interface ReplyService {

	public List<Reply> ReplyList(int b_id);
	
	public void CreateReply(Reply reply);

}
