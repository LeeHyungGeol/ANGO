package com.e.ango.Response;

public class CategoryDto {

	private String category_id;
	private String category_nm;
	private int total;

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCategory_nm() {
		return category_nm;
	}

	public void setCategory_nm(String category_nm) {
		this.category_nm = category_nm;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "CategoryVO [category_id=" + category_id + ", category_nm=" + category_nm + ", total=" + total + "]";
	}

}
