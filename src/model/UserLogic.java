package model;

import javax.servlet.http.Part;

import dao.UserDAO;

public class UserLogic {

	public boolean userLogin(Login login){
		UserDAO dao = new UserDAO();
		User user = dao.findByLogin(login);
		return user != null;
	}

	public User loadUser(String loginId) {
		UserDAO dao = new UserDAO();
		System.out.println("loaduserはきてる"+loginId);
		User user = dao.load(loginId);
		System.out.println("ここでとれてる？"+user.getLoginId());
		return user;
	}

	public boolean addUser(User user) {
		UserDAO dao = new UserDAO();
		boolean bo = dao.add(user);
		return bo;
	}

	public boolean updateUser(User editUser, User loginUser) {
		UserDAO dao = new UserDAO();
		boolean bo = dao.update(editUser, loginUser);
		return bo;
	}

	public boolean deleteUser(int id) {
		UserDAO dao = new UserDAO();
		boolean bo = dao.delete(id);
		return bo;
	}

	/**
	* ファイル名を取り出す
	* @param part パート
	* @return ファイル名
	*/
	public String extractFileName(Part part) {
		// System.out.println(part.getHeader("Content-Disposition"));
		// 例）これが出力される-> form-data; name="file"; filename="pic_278.png"
		String[] splitedHeader = part.getHeader("Content-Disposition").split(";");

		String fileName = null;
		for (String item : splitedHeader) {
			if (item.trim().startsWith("filename")) {//filename="pic_278.png"
				item = item.substring(item.indexOf('"'));//pic_278.png"
				//			  item = item.trim();
				item = item.replaceAll("\"", "");////pic_278.png
				fileName = item;////pic_278.png"
			}
		}
		return fileName;
	}


}
