package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Comment;
import model.User;

public class CommentDAO extends BaseDao {

	/********************つぶやき取得メソッド load() 0712**********************************************/
	/********************つぶやき取得メソッド load()**********************************************/

		public List<Comment> load(){//loadはつぶやき登録済み前提の話
		List<Comment> CommentList = new ArrayList<>();
		PreparedStatement pStmt = null;

		try{
			open();
			String sql = "select id, mutter_id, user_name, comment_text, created_at, image_path, like_count, dislike_count from comments order by created_at asc";
			pStmt = conn.prepareStatement(sql);

			ResultSet rs = pStmt.executeQuery();

			while(rs.next()){
				int id = rs.getInt("id");
				int mutterId = rs.getInt("mutter_id");
				String userName = rs.getString("user_name");
				String commentText = rs.getString("comment_text");
				Date createdAt = rs.getDate("created_at");//もしかしたらここでバグるかも。date型 timestampだからString無理かね？
				String imgPath = rs.getString("image_path");
				int likeCount = rs.getInt("like_count");
				int dislikeCount = rs.getInt("dislike_count");


				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String timeStamp = sdFormat.format(createdAt);

				Comment Comment = new Comment(id, mutterId, userName, commentText, timeStamp, likeCount, dislikeCount);
				//Comment(int CommentId, String userName, String text, String createdAt)
				if(imgPath !=null) {
					Comment.setImgPath(imgPath);
				}
				CommentList.add(Comment);
			}
			rs.close();
			pStmt.close();
		}catch(ClassNotFoundException|SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
					return null;
				}
			}
		}
		return CommentList;

	}


	/********************つぶやき追加メソッド add(Comment Comment, User user) 0712**********************************************/
	/********************つぶやき追加メソッド add(Comment Comment, User user)**********************************************/

	public boolean add(Comment Comment, User user){
		try{
			open();
			String sql = "insert into comments(mutter_id, user_name, comment_text, created_at, image_path, like_count, dislike_count) "
					+ "values(?,?,?,?,?,?,?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);


			//送られてきたインスタンスCommentのユーザーとつぶやきをvalueにセット。
			pStmt.setInt(1, Comment.getMutterId());
			pStmt.setString(2, Comment.getUserName());
			pStmt.setString(3, Comment.getCommentText());
			pStmt.setString(4, Comment.getCreatedAt());
			if(user.getImgPath()!=null) {
				pStmt.setString(5, user.getImgPath());
			}else {
				pStmt.setString(5, null);
			}
			pStmt.setInt(6, Comment.getLike());
			pStmt.setInt(7, Comment.getDislike());


			int result = pStmt.executeUpdate();// int?? 成功したら1, 失敗したら0を返すのかな？

			if(result !=1){
				return false;
			}
			pStmt.close();
		}catch(ClassNotFoundException|SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return true;
	}


	/********************つぶやき削除メソッド delete(int id)**********************************************/
	/********************つぶやき削除メソッド delete(int id)**********************************************/

	public boolean delete(int id){
		try{
			open();
			String sql = "update comments set comment_text=null where id = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			//送られてきたidの値ををvalueにセット。1
			pStmt.setInt(1, id);

			int result = pStmt.executeUpdate();// 成功したら1, 失敗したら0を返す

			if(result !=1){
				return false;
			}
			pStmt.close();
		}catch(ClassNotFoundException|SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return true;
	}


	/** 以下未実装0712**/
	/********************つぶやきイイねメソッド like(Comment Comment)**********************************************/
	/********************つぶやきイイねメソッド like(Comment Comment)**********************************************/

	public boolean like(int id, int like){
		try{
			open();
			String sql = "update comments set like_count = ? where id = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);


			//送られてきたインスタンスCommentのユーザーとつぶやきをvalueにセット。1,2
			pStmt.setInt(1, like+1);
			pStmt.setInt(2, id);

			int result = pStmt.executeUpdate();// int?? 成功したら1, 失敗したら0を返すのかな？

			if(result !=1){
				return false;
			}
			pStmt.close();
		}catch(ClassNotFoundException|SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/********************つぶやきよくないねメソッド dislike(int id, int like)**********************************************/
	/********************つぶやきよくないねメソッド dislike(int id, int like)**********************************************/

	public boolean dislike(int id, int dislike){
		try{
			open();
			String sql = "update comments set dislike_count = ? where id = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);


			//送られてきたインスタンスCommentのユーザーとつぶやきをvalueにセット。1,2
			pStmt.setInt(1, dislike+1);
			pStmt.setInt(2, id);

			int result = pStmt.executeUpdate();// int?? 成功したら1, 失敗したら0を返すのかな？

			if(result !=1){
				return false;
			}
			pStmt.close();
		}catch(ClassNotFoundException|SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return true;
	}


	/********************つぶやき編集メソッド edit(Comment Comment)**********************************************/
	/********************つぶやき編集メソッド edit(Comment Comment)**********************************************/

	public boolean edit(Comment Comment){
		try{
			open();
			String sql = "update comments set text=?, updated_at=? where id = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			//送られてきたidの値ををvalueにセット。1
			pStmt.setString(1, Comment.getCommentText());
			pStmt.setString(2, Comment.getUpdatedAt());
			pStmt.setInt(3, Comment.getId());

			int result = pStmt.executeUpdate();// int?? 成功したら1, 失敗したら0を返すのかな？

			if(result !=1){
				return false;
			}
			pStmt.close();
		}catch(ClassNotFoundException|SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return true;
	}

}
