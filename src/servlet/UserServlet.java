package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang3.math.NumberUtils;

import model.User;
import model.UserLogic;

@WebServlet("/UserServlet")
@MultipartConfig(fileSizeThreshold = 10000, location="/Applications/Eclipse_2018-12.app/Contents/workspaceSV/docoTsubu/WebContent/img" ,maxFileSize = 5
		* 1024 * 1024)
//ファイルアップした時のメモリの保存先。temp　だいたいtmpとか。
//location = "/Applications/Eclipse_2018-12.app/Contents/workspaceSV/docoTsubu/WebContent/img",
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/newRegister.jsp");
		dispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		request.setCharacterEncoding("UTF-8");
		String param = request.getParameter("state");
		String[] state = param.split(",");

		if (state[0] == null) {
			//			procSessionError(request, response, session);
			return;
		}

		switch (state[0]) {
		// トップページ
		case "index":
			procIndex(request, response);
			break;
		//マイページ
		case "mypage":
			procMyPage(request, response, session);
			break;
		//ログイン結果ページ
		case "loginResult":
			procLoginResult(request, response);
			break;
		// 新規登録
		case "newRegister":
			procNewRegister(request, response);
			break;
		//新規登録確認
		case "registerConfirm":
			procRegisterConfirm(request, response, session);
			break;
		// 新規登録完了/新規登録未完了
		case "register":
			procRegister(request, response, session);
			break;
		// 編集
		case "edit":
			procEdit(request, response);
			break;
		//編集確認
		case "editConfirm":
			procEditConfirm(request, response, session);
			break;
		// 編集完了/編集未完了
		case "update":
			procUpdate(request, response, session);
			break;
		// 削除確認
		case "deleteConfirm":
			procDeleteConfirm(request, response, session);
			break;
		//削除完了/削除未完了
		case "delete":
			procDelete(request, response, session);
			break;
		//// セッション･エラー
		//default:
		///procSessionError(request, response, session);
		//break;
		}

	}

	/**************************
	 * トップページへ
	 * ***************************/
	private void procIndex(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/docoTsubu/");
		dispatcher.forward(request, response);

	}

	/**************************
	 * マイページへ
	 * ***************************/
	private void procMyPage(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		//ログインユーザーのインスタンスをセッションスコープから取得
		User user = (User) session.getAttribute("loginUser");

		//セッションがなくなった場合は、ログインページへ戻す。
		if (user == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/mypage.jsp");
			dispatcher.forward(request, response);
		}

	}

	/**************************
	 * ログイン結果ページへ
	 * ***************************/
	private void procLoginResult(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginResult.jsp");
		dispatcher.forward(request, response);

	}

	/**************************
	 * 新規ユーザー登録関連
	 * ***************************/
	private void procNewRegister(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/newRegister.jsp");
		dispatcher.forward(request, response);

	}

	//registerConfirmに送信するサーブレット
	private void procRegisterConfirm(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		//formのパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String loginId = request.getParameter("loginId");
		String pass = request.getParameter("pass");
		String mail = request.getParameter("mail");
		String name = request.getParameter("name");
		String age_s = request.getParameter("age");
		Integer age = -1;//null でもOK

		//ageのint処理。
		if (!age_s.isEmpty() && NumberUtils.isNumber(age_s)) {
			age = Integer.valueOf(age_s);
		}

		//パラメータから送られてきたfile情報、file名取得の記述
		String imgPath = null;
		Part part = request.getPart("profileImg");

		//ファイル名を取得するためにlogic立ち上げ
		UserLogic userLogic = new UserLogic();
		String fileName = userLogic.extractFileName(part);

		//受け取ったパラメータでインスタンス生成
		User user = new User(loginId, pass, mail, name, age);

		//fileのアップロードがあった場合の処理。
		if (!fileName.isEmpty()) {
			part.write(fileName);
			System.out.println("part.writeだよ");
			imgPath = "img/" + fileName;
			user.setImgPath(imgPath);
			System.out.println("filePathだよ");
		}

		session = request.getSession();
		session.setAttribute("registerUser", user);
		request.setAttribute("fileName", fileName);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/registerConfirm.jsp");
		dispatcher.forward(request, response);

	}



	//ユーザー登録に関連するサーブレット、登録完了したら、loginページに戻す。
	private void procRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		//既存のインスタンスをセッションスコープから取得
		User user = (User) session.getAttribute("registerUser");

		if (user == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}

		//データベース接続用ロジックと連携し、ユーザー追加
		UserLogic userLogic = new UserLogic();
		Boolean bo = userLogic.addUser(user);

		//追加成功
		if (bo) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>"
					+ "<p>登録完了しました。<br><a href='http://localhost:8081/docoTsubu/LoginServlet'>ログインページへ</p>"
					+ "</body></html>");
		} //追加失敗
		else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>"
					+ "<p>登録失敗しました。<br><a href='http://localhost:8081/docoTsubu/UserServlet'>新規登録ページへ</p>"
					+ "</body></html>");
		}

	}

	/**************************
	 * ユーザー編集関連
	 * ***************************/
	private void procEdit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp");
		dispatcher.forward(request, response);

	}

	private void procEditConfirm(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		//formのパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String loginId = request.getParameter("loginId");
		String pass = request.getParameter("pass");
		String mail = request.getParameter("mail");
		String name = request.getParameter("name");
		String age_s = request.getParameter("age");
		Integer age = -1;//null でもOK

		//ageのint処理。
		if (!age_s.isEmpty() && NumberUtils.isNumber(age_s)) {
			age = Integer.valueOf(age_s);
		}

		//パラメータから送られてきたfile情報、file名取得の記述
		String imgPath = null;
		Part part = request.getPart("profileImg");

		//ファイル名を取得するためにlogic立ち上げ
		UserLogic userLogic = new UserLogic();
		String fileName = userLogic.extractFileName(part);
		System.out.println("fileNameは"+fileName);

		//受け取ったパラメータでインスタンス生成
		User loginUser = (User)session.getAttribute("loginUser");
		User user = new User(loginUser.getId(), loginId, pass, mail, name, age);

		//fileのアップロードがあった場合の処理。
		if (!fileName.isEmpty()) {
			part.write(fileName);
			System.out.println("part.writeだよ");
			imgPath = "img/" + fileName;
			user.setImgPath(imgPath);
			System.out.println("filePathだよ");
		}

		session.setAttribute("editUser", user);
		request.setAttribute("fileName", fileName);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/editConfirm.jsp");
		dispatcher.forward(request, response);

	}

	private void procUpdate(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		//既存のインスタンスをセッションスコープから取得
		User editUser = (User) session.getAttribute("editUser");
		User loginUser = (User) session.getAttribute("loginUser");

		if (editUser == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/mypage.jsp");
			dispatcher.forward(request, response);
		}

		UserLogic userLogic = new UserLogic();
		Boolean bo = userLogic.updateUser(editUser, loginUser);

		//追加成功
		if (bo) {

			response.setContentType("text/html; charset=UTF-8");
			User updatedUser = userLogic.loadUser(loginUser.getLoginId());
			session.setAttribute("loginUser", updatedUser);
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>"
					+ "<p>更新成功しました。</p><form name=\"mypage\" action=\"/docoTsubu/UserServlet\" method=\"post\">\n" +
					"<p>\n" +
					"<input type=\"hidden\" name=\"state\" value=\"mypage\">\n" +
					"<a href=\"#\" onClick=\"document.mypage.submit();return false;\">マイページへ</a>\n" +
					"</p>\n" +
					"</form>"
					+ "</body></html>");

//			response.setContentType("text/html; charset=UTF-8");
//			PrintWriter out = response.getWriter();
//			out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>"
//					+ "<p>更新完了しました。<br><a href='http://localhost:8081/docoTsubu/LoginServlet'>ログインページへ</p>"
//					+ "</body></html>");
		} //追加失敗
		else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>"
					+ "<p>更新失敗しました。</p><form name=\"mypage\" action=\"/docoTsubu/UserServlet\" method=\"post\">\n" +
					"<p>\n" +
					"<input type=\"hidden\" name=\"state\" value=\"mypage\">\n" +
					"<a href=\"#\" onClick=\"document.mypage.submit();return false;\">マイページへ</a>\n" +
					"</p>\n" +
					"</form>"
					+ "</body></html>");
		}

	}

	/**************************
	 * ユーザー削除関連
	 * ***************************/
	private void procDeleteConfirm(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/deleteConfirm.jsp");
		dispatcher.forward(request, response);

	}

	private void procDelete(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		//既存のインスタンスをセッションスコープから取得
		User user = (User) session.getAttribute("loginUser");

		if (user == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/mypage.jsp");
			dispatcher.forward(request, response);
		}

		UserLogic userLogic = new UserLogic();
		Boolean bo = userLogic.deleteUser(user.getId());

		//追加成功
		if (bo) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>"
					+ "<p>削除完了しました。<br><a href='http://localhost:8081/docoTsubu/LoginServlet'>ログインページへ</p>"
					+ "</body></html>");
		} //追加失敗
		else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>"
					+ "<p>削除失敗しました。</p><form name=\"mypage\" action=\"/docoTsubu/UserServlet\" method=\"post\">\n" +
					"<p>\n" +
					"<input type=\"hidden\" name=\"state\" value=\"mypage\">\n" +
					"<a href=\"#\" onClick=\"document.mypage.submit();return false;\">マイページへ</a>\n" +
					"</p>\n" +
					"</form>"
					+ "</body></html>");
		}

	}

}
