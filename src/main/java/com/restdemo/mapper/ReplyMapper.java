package com.restdemo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.restdemo.domain.Reply;

@Mapper
public interface ReplyMapper {
	
	public List<Reply> ReplyList(int b_id);
	
	public void CreateReply(Reply reply);
}
