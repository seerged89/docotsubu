package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Login;
import model.User;
import model.UserLogic;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("editMutter");

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
		dispatcher.forward(request, response);
	}


	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		//formのパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String loginId = request.getParameter("loginId");
		String pass = request.getParameter("pass");

		//受け取ったパラメータでインスタンス生成
		Login login = new Login(loginId, pass);
		System.out.println(login.getLoginId());//ここも問題なく生成されてる

		//生成したインスタンスのログインチェック、LoginLogicを立ち上げる。booleanがreturnされるので、受取
		UserLogic userLogic = new UserLogic();
		boolean isLogin = userLogic.userLogin(login);//１ここは大丈夫
		System.out.println(isLogin);

		//loginresultに送るloginUserインスタンスの生成。isLogin=falseならuser=nullをsessionに
		HttpSession session = request.getSession();
		User user = null;
		//ログインOKならば、セッションの生成、スコープ保存
		if(isLogin){//１ここがあやしいか
			//データベース接続用ロジックと連携し、ユーザーのロード（ここで情報をフルにする）
			user = userLogic.loadUser(login.getLoginId());//１ここがあやしい
			System.out.println(user.getLoginId());
		}
		System.out.println("userは空?空ならtrue)"+(user==null));

		session.setAttribute("loginUser", user);
		//※注意事項データベースから受ける値がnullの時の空文字変換の処理を入れないといけない。intなら-1処理でjsp側で処理。

		//保存後、loginResultに遷移
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginResult.jsp");
		dispatcher.forward(request, response);

	}

}
