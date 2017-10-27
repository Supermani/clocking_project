package com.primeton.manage.employee.service;

public class PageQuery {

	private int page = 1;
	private int size = 10;
	private String q;
	private String sort;
	private Boolean asc;

	/**
	 * 查询状态
	 * 
	 * pending: 待审核 ( passed is null )
	 * passed: 审核通过 ( passed = true )
	 * rejected: 审核不通过 ( passed = false )
	 * draft:  草稿未发布
	 * 其他: 自定义
	 */
	private String state;

	public PageQuery() { }

	public PageQuery(PageQuery query) {
		this.page = query.page;
		this.size = query.size;
		this.q = query.q;
		this.sort = query.sort;
		this.asc = query.asc;
		this.state = query.state;
	}

	public PageQuery(int page, int size) {
		this.page = page;
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getQ() {
		return q;
	}


	public String getSort() {
		return sort;
	}

	public boolean isAsc() {
		return asc != null && asc.booleanValue();
	}

	public Boolean getAsc() {
		return asc;
	}

	public void setAsc(Boolean asc) {
		this.asc = asc;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
