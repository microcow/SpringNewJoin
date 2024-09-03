package com.restdemo.domain;

public class Reply {
	int r_id;
	int b_id;
	String r_content;
	String r_writer;
	int r_depth;
	int r_grpord;
	int p_rp;
	
	
	public int getR_id() {
		return r_id;
	}
	public void setR_id(int r_id) {
		this.r_id = r_id;
	}
	public int getB_id() {
		return b_id;
	}
	public void setB_id(int b_id) {
		this.b_id = b_id;
	}
	public String getR_content() {
		return r_content;
	}
	public void setR_content(String r_content) {
		this.r_content = r_content;
	}
	public String getR_writer() {
		return r_writer;
	}
	public void setR_writer(String r_writer) {
		this.r_writer = r_writer;
	}
	public int getR_depth() {
		return r_depth;
	}
	public void setR_depth(int r_depth) {
		this.r_depth = r_depth;
	}
	public int getR_grpord() {
		return r_grpord;
	}
	public void setR_grpord(int r_grpord) {
		this.r_grpord = r_grpord;
	}
	public int getP_rp() {
		return p_rp;
	}
	public void setP_rp(int p_rp) {
		this.p_rp = p_rp;
	}
}
