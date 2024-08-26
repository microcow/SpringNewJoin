package com.restdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restdemo.domain.Board;
import com.restdemo.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService  {

	@Autowired
	BoardMapper boardMapper;
	
	@Override
	public void createBoard(Board board) {
		boardMapper.createBoard(board);
	}
	
	@Override
	public void updateBoard(Board board) {
		boardMapper.updateBoard(board);
	}
	
	@Override
	public Board readBoard(int b_id) {
		return boardMapper.readBoard(b_id);
	}
	
	@Override
	public List<Board> boardList(){
		return boardMapper.boardList();
	}
}
