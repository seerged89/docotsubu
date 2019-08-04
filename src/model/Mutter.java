package model;
import java.io.Serializable;

public class Mutter implements Serializable {
	private int id;
	private String userName;
	private String text;
	private String createdAt;
	private String updatedAt;
	private String imgPath;
	private int like;
	private int dislike;
//	private List<Comment> commentList;
	public Mutter (){
		like=0;
		dislike=0;
	}
	public Mutter(String userName, String text, String createdAt) {
		this.userName = userName;
		this.text = text;
		this.createdAt = createdAt;
	}

	public Mutter(int id, String userName, String text, String createdAt, int like, int dislike) {
		this.id = id;
		this.userName = userName;
		this.text = text;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
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
//	public List<Comment> getCommentList() {
//		return commentList;
//	}
//	public void setCommentList(List<Comment> commentList) {
//		this.commentList = commentList;
//	}


}
