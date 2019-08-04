package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Comment;
import model.CommentLogic;
import model.Mutter;
import model.MutterLogic;
import model.User;

@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//ログイン確認。ログインが確認できない場合は、loginページに戻す
		HttpSession session = request.getSession();
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
		//editMutterのsessionを消しとかないと
		session.removeAttribute("editMutter");//この記述結構どこでも重要になる。遷移する時は基本これをしないとダメ。

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
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
			//return;
		}

		switch (state[0]) {
		// メインページ
		case "main":
			procMain(request,response,session);
			break;
		// つぶやき追加
		case "add":
			procAdd(request,response,session);
			break;
		// いいね増やす
		case "likeCount":
			procLikeCount(request,response,session);
			break;
		// よくないね増やす
		case "dislikeCount":
			procDislikeCount(request,response,session);
			break;
		// つぶやき削除
		case "delete":
			procDelete(request,response,session,state[1]);
			break;
		// つぶやき編集
		case "edit":
			procEdit(request,response,session,state[1]);
			break;
		// つぶやき確定
		case "editConfirm":
			procEditConfirm(request,response,session,state[1]);
			break;
		// コメント追加
		case "addComment":
			procAddComment(request,response,session);
			break;
		// コメント追加
		case "deleteComment":
			procDeleteComment(request,response,session,state[1]);
			break;
		//// セッション･エラー
		//default:
		///procSessionError(request, response, session);
		//break;
		}
	}

	/**************************
	 * メインページへ
	 * ***************************/
	private void procMain(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		//ログイン確認。ログインが確認できない場合は、loginページに戻す
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
		//editMutterのsessionを消しとかないと
		session.removeAttribute("editMutter");//この記述結構どこでも重要になる。遷移する時は基本これをしないとダメ。

		List<Mutter> mutterList = (List<Mutter>)session.getAttribute("mutterList");
		List<Comment> commentList = (List<Comment>)session.getAttribute("commentList");

		if (mutterList==null) {
			MutterLogic mutterLogic = new MutterLogic();
			mutterList = mutterLogic.loadMutter();
		}
		if (commentList==null) {
			CommentLogic commentLogic = new CommentLogic();
			commentList = commentLogic.loadComment();
		}

		session.setAttribute("mutterList", mutterList);
		session.setAttribute("commentList", commentList);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
		dispatcher.forward(request, response);
	}

	/**************************
	 * つぶやき追加 Add
	 * ***************************/
	private void procAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		//ログインが確認できない場合は、loginページに戻す
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
		//editMutterのsessionを消しとかないと
		session.removeAttribute("editMutter");//この記述結構どこでも重要になる。遷移する時は基本これをしないとダメ。

		//main.jspから送られてくるつぶやきのパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String userName = request.getParameter("userName");
		String text = request.getParameter("text");
		String createdAt = request.getParameter("createdAt");


		if(text.length() > 140){
			String errorMsg = "つぶやきが長すぎます。140文字以下にしてください。";
			request.setAttribute("errorMsg", errorMsg);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
			dispatcher.forward(request, response);
		}
		if(text.isEmpty()){
			String errorMsg = "つぶやきが入力されていません";
			request.setAttribute("errorMsg", errorMsg);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
			dispatcher.forward(request, response);

		}


		//セッションからログイン情報を取得
		User user = (User)session.getAttribute("loginUser");

		//パラメータでmutterインスタンス生成 likeとdislikeはデフォルトで0が入っている。idは自動インクレメント, imgPathをloginUserから取得
		Mutter mutter = new Mutter(userName,text,createdAt);

		if(user.getImgPath() != null) {
			mutter.setImgPath(user.getImgPath());
		}
		System.out.println("mutterのinstance生成はここで終わってる。");

		//インスタンス生成完了したので、それをaddのロジックをかける
		MutterLogic mutterLogic = new MutterLogic();
		mutterLogic.addMutter(mutter, user);

		System.out.println("mutterのDB追加終わってる。");

		//新規つぶやきが登録完了したので、再度mutterListをDB取得し、セッション上のリストを上書き
		List<Mutter> mutterList = mutterLogic.loadMutter();
		session.setAttribute("mutterList", mutterList);

		//追加後はsendRedirect リフレッシュボタン対策
		response.sendRedirect("/docoTsubu/MainServlet");
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
//		dispatcher.forward(request, response);
	}

	/**************************
	 * いいね増やす LikeCount 20190709
	 * ***************************/
	private void procLikeCount(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		//ログインが確認できない場合は、loginページに戻す
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
		//editMutterのsessionを消しとかないと
		session.removeAttribute("editMutter");//この記述結構どこでも重要になる。遷移する時は基本これをしないとダメ。

		//main.jspから送られてくるつぶやきのパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String idStr = request.getParameter("id");
		String likeStr = request.getParameter("like");

		int id = Integer.parseInt(idStr);
		int like = Integer.parseInt(likeStr);


		MutterLogic mutterLogic = new MutterLogic();
		mutterLogic.likeMutter(id, like);

		//いいねを増やしたので、再度mutterListをDB取得し、セッション上のリストを上書き
		List<Mutter> mutterList = mutterLogic.loadMutter();
		session.setAttribute("mutterList", mutterList);

		//いいね後はsendRedirect リフレッシュボタン対策
		response.sendRedirect("/docoTsubu/MainServlet");
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
//		dispatcher.forward(request, response);
	}

	/**************************
	 * よくないね増やす DisikeCount
	 * ***************************/
	private void procDislikeCount(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		//ログインが確認できない場合は、loginページに戻す
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}

		//editMutterのsessionを消しとかないと
		session.removeAttribute("editMutter");//この記述結構どこでも重要になる。遷移する時は基本これをしないとダメ。

		//main.jspから送られてくるつぶやきのパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String idStr = request.getParameter("id");
		String dislikeStr = request.getParameter("dislike");

		int id = Integer.parseInt(idStr);
		int dislike = Integer.parseInt(dislikeStr);


		MutterLogic mutterLogic = new MutterLogic();
		mutterLogic.dislikeMutter(id, dislike);

		//いいねを増やしたので、再度mutterListをDB取得し、セッション上のリストを上書き
		List<Mutter> mutterList = mutterLogic.loadMutter();
		session.setAttribute("mutterList", mutterList);

		//よくないね後はsendRedirect リフレッシュボタン対策
		response.sendRedirect("/docoTsubu/MainServlet");
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
//		dispatcher.forward(request, response);
		}

	/**************************
	 * つぶやき削除 delete //つぶやきに付随しているコメントも削除する。
	 * ***************************/
	private void procDelete(HttpServletRequest request, HttpServletResponse response, HttpSession session, String idStr)
			throws ServletException, IOException {

		//ログインが確認できない場合は、loginページに戻す
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
		//editMutterのsessionを消しとかないと
		session.removeAttribute("editMutter");//この記述結構どこでも重要になる。遷移する時は基本これをしないとダメ。


		int id = Integer.parseInt(idStr);

		MutterLogic mutterLogic = new MutterLogic();
		mutterLogic.deleteMutter(id);//論理削除 jsp側でnull処理

		//削除したので、再度mutterListをDB取得し、セッション上のリストを上書き
		List<Mutter> mutterList = mutterLogic.loadMutter();
		session.setAttribute("mutterList", mutterList);

		//削除後はsendRedirect リフレッシュボタン対策
		response.sendRedirect("/docoTsubu/MainServlet");
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
//		dispatcher.forward(request, response);
	}

	/**************************
	 * つぶやき編集 edit //つぶやきに付随しているコメントも削除する。
	 * ***************************/
	private void procEdit(HttpServletRequest request, HttpServletResponse response, HttpSession session, String idStr)
			throws ServletException, IOException {

		//ログインが確認できない場合は、loginページに戻す
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
		int id = Integer.parseInt(idStr);

		//main.jspから送られてくるつぶやきのパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String text = request.getParameter("text");

		Mutter editMutter = new Mutter();
		editMutter.setId(id);
		editMutter.setText(text);

		session.setAttribute("editMutter", editMutter);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
		dispatcher.forward(request, response);
	}

	private void procEditConfirm(HttpServletRequest request, HttpServletResponse response, HttpSession session, String idStr)
			throws ServletException, IOException {

		//ログインが確認できない場合は、loginページに戻す
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}

		//main.jspから送られてくるつぶやきのパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String text = request.getParameter("text");
		String updatedAt = request.getParameter("updatedAt");

		Mutter editMutter = (Mutter)session.getAttribute("editMutter");
		editMutter.setText(text);
		editMutter.setUpdatedAt(updatedAt);

		//DBに編集したmutterを送ってアップデート。
		MutterLogic mutterLogic = new MutterLogic();
		mutterLogic.editMutter(editMutter);

		//編集したデータはDBに登録済みなので、ここでsessionスコープから消して、main.jspを通常の表示に切り替える。
		session.removeAttribute("editMutter");//この記述結構どこでも重要になる。遷移する時は基本これをしないとダメ。

		//編集したので、再度mutterListをDB取得し、セッション上のリストを上書き
		List<Mutter> mutterList = mutterLogic.loadMutter();
		session.setAttribute("mutterList", mutterList);

		//編集後はsendRedirect リフレッシュボタン対策
		response.sendRedirect("/docoTsubu/MainServlet");
		//RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
		//dispatcher.forward(request, response);
	}

	/**************************
	 * コメント追加 AddComment
	 * ***************************/
	private void procAddComment(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		//ログインが確認できない場合は、loginページに戻す
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
		//editMutterのsessionを消しとかないと
		session.removeAttribute("editMutter");//この記述結構どこでも重要になる。遷移する時は基本これをしないとダメ。

		//main.jspから送られてくるつぶやきのパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String mutterIdStr = request.getParameter("mutterId");
		String userName = request.getParameter("userName");
		String commentText = request.getParameter("commentText");
		String createdAt = request.getParameter("createdAt");


		if(commentText.length() > 140){
			String errorMsg = "コメントが長すぎます。140文字以下にしてください。";
			request.setAttribute("errorMsg", errorMsg);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
			dispatcher.forward(request, response);
		}else if(commentText.isEmpty()){
			String errorMsg = "コメントが入力されていません";
			request.setAttribute("errorMsg", errorMsg);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
			dispatcher.forward(request, response);
		}
		int mutterId = Integer.parseInt(mutterIdStr);

		//パラメータでcommentインスタンス生成 likeとdislikeはデフォルトで0が入っている。idは自動インクレメント, imgPathをloginUserから取得
		Comment comment = new Comment(mutterId, userName, commentText, createdAt);

		if(loginUser.getImgPath() != null) {
			comment.setImgPath(loginUser.getImgPath());
		}
		System.out.println("commentのinstance生成はここで終わってる。");

		//インスタンス生成完了したので、それをaddのロジックをかける
		CommentLogic commentLogic = new CommentLogic();
		commentLogic.addComment(comment, loginUser);

		System.out.println("commentのDB追加終わってる。");

		//新規コメントが登録完了したので、再度commentListをDB取得し、セッション上のリストを上書き
		List<Comment> commentList = commentLogic.loadComment();

//		//ここの段階でmutterListを取得して、該当するmutterインスタンスに、commentを追加することも可能。
		//このやり方だとsession切れた時に、コメントが消えてしまうよ汗
//		List<Mutter> mutterList = (List<Mutter>)session.getAttribute("mutterList");
//		for(Mutter mutter: mutterList) {
//			for(Comment comments: commentList) {
//				if(mutter.getId()==comments.getMutterId()) {
//					mutter.getCommentList().add(comments);
//				}
//			}
//		}
//		session.setAttribute("mutterList", mutterList);

		session.setAttribute("commentList", commentList);


		//追加後はsendRedirect リフレッシュボタン対策
		response.sendRedirect("/docoTsubu/MainServlet");
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
//		dispatcher.forward(request, response);
	}

	/**************************
	 * コメント削除 DeleteComment
	 * ***************************/
	private void procDeleteComment(HttpServletRequest request, HttpServletResponse response, HttpSession session, String idStr)
			throws ServletException, IOException {

		//ログインが確認できない場合は、loginページに戻す
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
		//editMutterのsessionを消しとかないと
		session.removeAttribute("editMutter");//この記述結構どこでも重要になる。遷移する時は基本これをしないとダメ。

		int id = Integer.parseInt(idStr);

		//commentDeleteLogicにidを渡して削除実行
		CommentLogic commentLogic = new CommentLogic();
		commentLogic.deleteComment(id);

		System.out.println("commentの論理削除の終わり");

		//新規コメントが登録完了したので、再度commentListをDB取得し、セッション上のリストを上書き
		List<Comment> commentList = commentLogic.loadComment();

		session.setAttribute("commentList", commentList);

		//追加後はsendRedirect リフレッシュボタン対策
		response.sendRedirect("/docoTsubu/MainServlet");
	}


}
