package com.restdemo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.restdemo.domain.Board;

@Mapper
public interface BoardMapper {
	
	public void createBoard(Board board);
	
}
