package com.restdemo.service;

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
}
