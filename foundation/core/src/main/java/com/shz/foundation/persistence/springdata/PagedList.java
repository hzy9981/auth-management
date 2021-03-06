package com.shz.foundation.persistence.springdata;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * 用于承载 @Page 的分页数据，使之易于转成json格式
 * @author pc
 *
 * @param <T>
 */
public class PagedList<T> {
	private long totalCount;
	private int totalPages;
	private int page;
	private int size;
	private List<T> result;
	
	/**
	 * 初始化时判断总页数totalPages，实际当前页pageNo
	 * @param totalCount
	 * @param pageNo
	 * @param pageSize
	 */
	public PagedList(Page<T> page) {
		this.totalCount=page.getTotalElements();
		this.totalPages=page.getTotalPages();
		this.page=page.getNumber()+1;
		this.size=page.getSize();
		this.result=page.getContent();
	}
	
	/**
	 * @return the totalCount
	 */
	public long getTotalCount() {
		return totalCount;
	}
	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	/**
	 * @return the totalPages
	 */
	public long getTotalPages() {
		return totalPages;
	}
	/**
	 * @param totalPages the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
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

	/**
	 * @return the result
	 */
	public List<T> getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(List<T> result) {
		this.result = result;
	}	
}
