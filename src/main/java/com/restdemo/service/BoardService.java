package com.restdemo.service;

import java.util.List;
import com.restdemo.domain.Board;

public interface BoardService {

	public void createBoard(Board board);
	
	public void updateBoard(Board board);
	
	public Board readBoard(int b_id);
	
	public List<Board> boardList();
	
	public void boardDelete(int b_id);
	
	public void changeBoard(Board board);
	
	public void updateGrpord(Board board);
}
