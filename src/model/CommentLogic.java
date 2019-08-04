package model;

import java.util.List;

import dao.CommentDAO;

public class CommentLogic {

	public List<Comment> loadComment() {
		CommentDAO dao = new CommentDAO();
		List<Comment> CommentList = dao.load();
		return CommentList;
	}

	public boolean addComment(Comment Comment, User user) {
		CommentDAO dao = new CommentDAO();
		boolean bo = dao.add(Comment, user);
		return bo;
	}

	public boolean deleteComment(int id) {
		System.out.println("deleteCommentで使うid"+id);
		CommentDAO dao = new CommentDAO();
		boolean bo = dao.delete(id);
		return bo;
	}

	/* 以下未実装0712*/
	public boolean likeComment(int id, int like) {
		CommentDAO dao = new CommentDAO();
		boolean bo = dao.like(id, like);
		return bo;
	}

	public boolean dislikeComment(int id, int dislike) {
		CommentDAO dao = new CommentDAO();
		boolean bo = dao.dislike(id, dislike);
		return bo;
	}

	public boolean editComment(Comment Comment) {
		CommentDAO dao = new CommentDAO();
		boolean bo = dao.edit(Comment);
		return bo;
	}


}
