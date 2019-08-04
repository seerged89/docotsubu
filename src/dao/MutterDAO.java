package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Mutter;
import model.User;

public class MutterDAO extends BaseDao {

	/********************つぶやき取得メソッド load()**********************************************/
	/********************つぶやき取得メソッド load()**********************************************/

		public List<Mutter> load(){//loadはつぶやき登録済み前提の話
		List<Mutter> mutterList = new ArrayList<>();
		PreparedStatement pStmt = null;

		try{
			open();
			String sql = "select id, user_name, text, created_at, image_path, like_count, dislike_count from mutters order by created_at desc";
			pStmt = conn.prepareStatement(sql);

			ResultSet rs = pStmt.executeQuery();

			while(rs.next()){
				int id = rs.getInt("id");
				String userName = rs.getString("user_name");
				String text = rs.getString("text");
				Date createdAt = rs.getDate("created_at");//もしかしたらここでバグるかも。date型 timestampだからString無理かね？
				String imgPath = rs.getString("image_path");
				int likeCount = rs.getInt("like_count");
				int dislikeCount = rs.getInt("dislike_count");


				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String timeStamp = sdFormat.format(createdAt);

				Mutter mutter = new Mutter(id, userName, text, timeStamp, likeCount, dislikeCount);
				//Mutter(int mutterId, String userName, String text, String createdAt)
				if(imgPath !=null) {
					mutter.setImgPath(imgPath);
				}
				//それぞれのmutterがcommentListを持つ形で、mutterListに保存 session切れると消えてしまう。
//				mutter.setCommentList(new ArrayList<Comment>());
				mutterList.add(mutter);
			}
			System.out.println("mutterList#load完了");
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
		return mutterList;

	}


	/********************つぶやき追加メソッド add(Mutter mutter, User user)**********************************************/
	/********************つぶやき追加メソッド add(Mutter mutter, User user)**********************************************/

	public boolean add(Mutter mutter, User user){
		try{
			open();
			String sql = "insert into mutters(user_name, text, created_at, image_path, like_count, dislike_count) values(?,?,?,?,?,?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);


			//送られてきたインスタンスmutterのユーザーとつぶやきをvalueにセット。1,2
			pStmt.setString(1, mutter.getUserName());
			pStmt.setString(2, mutter.getText());
			pStmt.setString(3, mutter.getCreatedAt());
			if(user.getImgPath()!=null) {
				pStmt.setString(4, user.getImgPath());
			}else {
				pStmt.setString(4, null);
			}
			pStmt.setInt(5, mutter.getLike());
			pStmt.setInt(6, mutter.getDislike());


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

	/********************つぶやきイイねメソッド like(Mutter mutter)**********************************************/
	/********************つぶやきイイねメソッド like(Mutter mutter)**********************************************/

	public boolean like(int id, int like){
		try{
			open();
			String sql = "update mutters set like_count = ? where id = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);


			//送られてきたインスタンスmutterのユーザーとつぶやきをvalueにセット。1,2
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
			String sql = "update mutters set dislike_count = ? where id = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);


			//送られてきたインスタンスmutterのユーザーとつぶやきをvalueにセット。1,2
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


	/********************つぶやき削除メソッド delete(int id)**********************************************/
	/********************つぶやき削除メソッド delete(int id)**********************************************/

	public boolean delete(int id){
		try{
			open();
			String sql = "update mutters set text=null where id = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);


			//送られてきたidの値ををvalueにセット。1
			pStmt.setInt(1, id);

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

	/********************つぶやき編集メソッド edit(Mutter mutter)**********************************************/
	/********************つぶやき編集メソッド edit(Mutter mutter)**********************************************/

	public boolean edit(Mutter mutter){
		try{
			open();
			String sql = "update mutters set text=?, updated_at=? where id = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			//送られてきたidの値ををvalueにセット。1
			pStmt.setString(1, mutter.getText());
			pStmt.setString(2, mutter.getUpdatedAt());
			pStmt.setInt(3, mutter.getId());

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
