package com.restdemo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.restdemo.domain.Board;

@Mapper
public interface BoardMapper {
	
	public void createBoard(Board board);
	
	public void updateBoard(Board board);
	
	public Board readBoard(int b_id);
	
	public List<Board> boardList();
}
