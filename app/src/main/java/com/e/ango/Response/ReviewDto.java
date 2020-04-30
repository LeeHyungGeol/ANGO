package com.e.ango.Response;

import java.sql.Timestamp;

public class ReviewDto {

	private Long review_idx;
	private String review_id; //유저아이디
	private String content_id; //
	private String review_text; //리뷰
	private float review_score; //리뷰 점수
	private String time; //리뷰 작성 시간

	private String category_id;
	private String review_type;
	private String title;
	private boolean isSelected;


	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}


	public Long getReview_idx() {
		return review_idx;
	}

	public void setReview_idx(Long review_idx) {
		this.review_idx = review_idx;
	}

	public String getReview_id() {
		return review_id;
	}

	public void setReview_id(String review_id) {
		this.review_id = review_id;
	}

	public String getContent_id() {
		return content_id;
	}

	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}

	public String getReview_text() {
		return review_text;
	}

	public void setReview_text(String review_text) {
		this.review_text = review_text;
	}

	public float getReview_score() {
		return review_score;
	}

	public void setReview_score(float review_score) {
		this.review_score = review_score;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getReview_type() {
		return review_type;
	}

	public void setReview_type(String review_type) {
		this.review_type = review_type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "ReviewDto{" +
				"review_idx=" + review_idx +
				", review_id='" + review_id + '\'' +
				", content_id='" + content_id + '\'' +
				", review_text='" + review_text + '\'' +
				", review_score=" + review_score +
				", time='" + time + '\'' +
				", category_id='" + category_id + '\'' +
				", review_type='" + review_type + '\'' +
				", title='" + title + '\'' +
				", isSelected=" + isSelected +
				'}';
	}
}
