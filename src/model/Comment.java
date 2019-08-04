package model;

public class Comment {
	private int id;
	private int mutterId;
	private String userName;
	private String commentText;
	private String createdAt;
	private String updatedAt;
	private String imgPath;
	private int like;
	private int dislike;
	public Comment() {
		like=0;
		dislike=0;
	}
	public Comment(int mutterId, String userName, String commentText, String createdAt) {
		this.mutterId = mutterId;
		this.userName = userName;
		this.commentText = commentText;
		this.createdAt = createdAt;
	}
	public Comment(int id, int mutterId, String userName, String commentText, String createdAt) {
		this.id = id;
		this.mutterId = mutterId;
		this.userName = userName;
		this.commentText = commentText;
		this.createdAt = createdAt;
	}
	public Comment(int id, int mutterId, String userName, String commentText, String createdAt, int like,
			int dislike) {
		this.id = id;
		this.mutterId = mutterId;
		this.userName = userName;
		this.commentText = commentText;
		this.createdAt = createdAt;
		this.like = like;
		this.dislike = dislike;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getMutterId() {
		return mutterId;
	}
	public void setMutterId(int mutterId) {
		this.mutterId = mutterId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public int getDislike() {
		return dislike;
	}
	public void setDislike(int dislike) {
		this.dislike = dislike;
	}




}
