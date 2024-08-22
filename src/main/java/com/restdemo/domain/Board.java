package com.restdemo.domain;

public class Board {
	String name;
	String title;
	String contents;
	int p_board;
	int depth;
	int grpord;
	String b_datetime;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getP_board() {
		return p_board;
	}
	public void setP_board(int p_board) {
		this.p_board = p_board;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getGrpord() {
		return grpord;
	}
	public void setGrpord(int grpord) {
		this.grpord = grpord;
	}
	public String getB_datetime() {
		return b_datetime;
	}
	public void setB_datetime(String b_datetime) {
		this.b_datetime = b_datetime;
	}
}
