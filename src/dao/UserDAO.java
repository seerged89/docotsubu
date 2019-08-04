package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Login;
import model.User;


public class UserDAO extends BaseDao {

	/***************ログインチェック*********************/
	/***************ログインチェック*********************/
	public User findByLogin(Login login){
		PreparedStatement pStmt = null;
		User user = null;

		try{
			open();
			String sql = "select login_id, password, mail, name, age from users where login_id = ? and password = ?";
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, login.getLoginId());
			pStmt.setString(2, login.getPass());

			ResultSet rs = pStmt.executeQuery();

			while(rs.next()){
				String loginId = rs.getString("login_id");
				String pass = rs.getString("password");
				String mail = rs.getString("mail");
				String name = rs.getString("name");
				int age = rs.getInt("age");

				user = new User(loginId, pass, mail, name, age);
			}
			rs.close();
			pStmt.close();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch(ClassNotFoundException e){
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
		return user;

	}

	/***************loadUser**************/
	/***************loadUser**************/
	public User load(String loginId){
		PreparedStatement pStmt = null;
		User user = null;

		try{
			open();
			String sql = "select id, password, mail, name, age, image_path from users where login_id =?";
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, loginId);


			ResultSet rs = pStmt.executeQuery();

			while(rs.next()){
				int id = rs.getInt("id");
				String pass = rs.getString("password");
				String mail = rs.getString("mail");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String imgPath = rs.getString("image_path");

				user = new User(id, loginId, pass, mail, name, age);
				if (imgPath!=null) {
					user.setImgPath(imgPath);//nullなら　とか考える必要がある。
				}

			}
			rs.close();
			pStmt.close();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			return null;
		}finally{
			if(conn != null){
				try{
					pStmt.close();
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
					return null;
				}
			}
		}
		return user;
	}

	/**************addUser************************/
	/**************addUser************************/
	public boolean add(User user){
		PreparedStatement pStmt = null;

		//既存ユーザーチェック
		User existingUser = load(user.getLoginId());
		if(existingUser !=null) {
			System.out.println("既に登録済みのユーザーです。");
			return false;//booleanじゃなくて、Stringを返すことも可能。エラーメッセージを。
		}

		try{
			open();
			String sql = "insert into users(login_id, password, mail, name, age, image_path) values(?,?,?,?,?,?)";
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, user.getLoginId());
			pStmt.setString(2, user.getPass());
			pStmt.setInt(5, user.getAge());//空送信されたら、-1を入れる。
			pStmt.setString(6, user.getImgPath());

			if(user.getMail().isEmpty()) {
				pStmt.setString(3, null);
			}else {
				pStmt.setString(3, user.getMail());
			}
			if (user.getName().isEmpty()) {
				pStmt.setString(4, null);
			} else {
				pStmt.setString(4, user.getName());
			}

			//二重登録防止が必要。useridでloadして合致するものがあったら、return false;

			int result = pStmt.executeUpdate();

			if(result !=0) {
				return true;
			}

		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			return false;
		}finally{
			if(conn != null){
				try{
					pStmt.close();
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;

	}


	/**************updateUser************************/
	/**************updateUser************************/
	public boolean update(User editUser, User loginUser){
		ArrayList<PreparedStatement> pStmtList = new ArrayList<PreparedStatement>();
		ArrayList<Integer> resultList = new ArrayList<Integer>();

		try{
			open();
			System.out.println("DB開きました");
			String sqlLoginId = "update users set login_id=? where id=?";
			String sqlPass = "update users set password=? where id=?";
			String sqlMail = "update users set mail=? where id=?";
			String sqlName = "update users set name=? where id=?";
			String sqlAge = "update users set age=? where id=?";
			String sqlImgPath = "update users set image_path=? where id=?";

			if(!editUser.getLoginId().isEmpty()) {
				PreparedStatement pStmt = conn.prepareStatement(sqlLoginId);
				pStmt.setString(1, editUser.getLoginId());
				pStmt.setInt(2, editUser.getId());
				pStmtList.add(pStmt);
			}
			if(!editUser.getPass().isEmpty()) {
				PreparedStatement pStmt = conn.prepareStatement(sqlPass);
				pStmt.setString(1, editUser.getPass());
				pStmt.setInt(2, editUser.getId());
				pStmtList.add(pStmt);
			}
			if(!editUser.getMail().isEmpty()) {
				PreparedStatement pStmt = conn.prepareStatement(sqlMail);
				pStmt.setString(1, editUser.getMail());
				pStmt.setInt(2, editUser.getId());
				pStmtList.add(pStmt);
			}
			if(!editUser.getName().isEmpty()) {
				PreparedStatement pStmt = conn.prepareStatement(sqlName);
				pStmt.setString(1, editUser.getName());
				pStmt.setInt(2, editUser.getId());
				pStmtList.add(pStmt);
			}
			if(editUser.getAge()!=-1) {
				PreparedStatement pStmt = conn.prepareStatement(sqlAge);
				pStmt.setInt(1, editUser.getAge());
				pStmt.setInt(2, editUser.getId());
				pStmtList.add(pStmt);
			}
			if(editUser.getImgPath()!=null) {
				PreparedStatement pStmt = conn.prepareStatement(sqlImgPath);
				pStmt.setString(1, editUser.getImgPath());//edituser imagpathがヌル
				pStmt.setInt(2, editUser.getId());
				pStmtList.add(pStmt);
			}

			System.out.println("preparedStatementListにSQL文追加完了");

			for(PreparedStatement pStmt: pStmtList) {
				resultList.add(pStmt.executeUpdate());
				pStmt.close();//ここの記述どうだろ？？大丈夫かな？
			}
			System.out.println("SQL文の実行完了");

			int count=1;
			for (int result : resultList) {
//				System.out.println(count+"番目のresultの値は"+result);
				if(result ==0) {//1=Query OK, 1 row affected (0.02 sec)、更新行分返す。１行なら1。
					System.out.println(result);
					System.out.println(count+"番目のupdate失敗しました。");
					return false;
				}
				System.out.println(count+"番目のupdate成功しました。");
				count++;
			}
			//pStmt.close();
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			return false;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;

	}

	/**************deleteUser************************/
	/**************deleteUser************************/
	public boolean delete(int id){
		PreparedStatement pStmt = null;

		try{
			open();
			System.out.println("DB開きました");

			String sql = "update users set login_id = null, password = null where id=?";//論理削除ならupdate null -1 nullみたいな
			//一旦login_id passwordをnull外した！

			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, id);

			int result = pStmt.executeUpdate();

			if(result !=0) {
				return true;
			}

		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			return false;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;

	}
}
