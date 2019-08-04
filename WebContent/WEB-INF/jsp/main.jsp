<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User, model.Mutter, model.Comment,
 java.util.List, java.util.ArrayList, java.util.Date, java.text.SimpleDateFormat" %>
<%--
<jsp:useBean class="model.User" id="loginUser" scope="session" />
<jsp:useBean id="productList" class="java.util.ArrayList" type="java.util.ArrayList<ProductBean>" scope="request" /> --%>
<%
//ユーザー情報とつぶやき情報を取得
User loginUser = (User)session.getAttribute("loginUser");
Mutter editMutter = (Mutter)session.getAttribute("editMutter");
List<Mutter> mutterList = (List<Mutter>)session.getAttribute("mutterList");
List<Comment> commentList = (List<Comment>)session.getAttribute("commentList");

String errorMsg = (String)request.getAttribute("errorMsg");
String errorComment = (String)request.getAttribute("errorComment");

//日時取得
Date now = new Date();
SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
String timeStamp = sdFormat.format(now);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/common.css">
<title>どこつぶ</title>
</head>
<body>
<div id="main">
<div id="header" style="width:100%;background:#308dff;">
<h1>どこつぶメイン</h1>
</div>

<div id="content">
<p><%= loginUser.getLoginId() %>さん、ログイン中　<a href="/docoTsubu/LogoutServlet">ログアウト</a></p>
<form name="loginResult" action="/docoTsubu/UserServlet" method="post">
<p>
<input type="hidden" name="state" value="loginResult">
<a href="#" onClick="document.loginResult.submit();return false;">ログインへ</a>
</p>
</form>
<p>ページ訪問時刻：<%= timeStamp %></p>
<form name="main" action="/docoTsubu/MainServlet" method="post">
<p>
<input type="hidden" name="state" value="main">
<a href="#" onClick="document.main.submit();return false;">更新</a>
</p>
</form>


<!-- つぶやきフォーム -->
<form action="/docoTsubu/MainServlet" method="post">
<textarea rows="5" cols="40" name="text"></textarea>
<input type="hidden" name="userName" value="<%= loginUser.getLoginId() %>">
<input type="hidden" name="createdAt" value="<%= timeStamp %>">
<button name="state" value="add">つぶやく</button>
<% if(errorMsg != null){ %>
<p style="color:red;"><%= errorMsg %></p>
<% } %>
</form>
<!-- end つぶやきフォーム -->
</div>


<hr>

<!-- つぶやき内容開始 -->
<% if(mutterList!=null){
for(Mutter mutter: mutterList){ %>

<!-- 削除後の分岐処理 -->
<% if(mutter.getText()!=null){ %>

<% if(loginUser.getLoginId().equals(mutter.getUserName())){ %>
<div style="border-left:1px solid #000;border-right:1px solid #000; border-top:1px dotted #000;border-bottom:1px dotted #000; width:608px; padding:3px 20px; background-color:#FFEBCD;">
<% }else{ %>
<div style="border:1px solid #000; border-bottom:1px dotted #000; width:608px; padding:3px 20px;">
<% } %>

<p><%= mutter.getCreatedAt() %></p>
<div>
<dl>
<dt style="float:left;">
<% if(mutter.getImgPath()!=null){ %>
<img width="48" alt="<%= mutter.getUserName() %>" src="<%= mutter.getImgPath() %>" style="display:inline;">
<% }%>
</dt>
<dd style="margin-left:80px;">
<!-- 編集ボタン後の表示の分岐処理ここから -->
<%if(editMutter!=null && editMutter.getId()==mutter.getId()){ %>
<form name="formEditTextArea">
<%= mutter.getUserName() %>：<textarea rows="5" cols="40" id="editTextarea" onKeyUp="funcEdit()"><%= mutter.getText() %></textarea>
</form>
<% }else{ %>
<%= mutter.getUserName() %>：<%= mutter.getText() %>
<% } %>
<!-- 編集ボタン後の表示の分岐処理ここまで -->
</dd>
</dl>
<div style="clear:both"></div>
</div>

<!-- Like開始 -->
<div>
<form action="/docoTsubu/MainServlet" method="post" style="display:inline;">
<input type="hidden" name="id" value="<%= mutter.getId() %>">
<input type="hidden" name="like" value="<%= mutter.getLike() %>">
<button name="state" value="likeCount">いいね</button>
：<%= mutter.getLike() %>人
</form>　
<form action="/docoTsubu/MainServlet" method="post" style="display:inline;">
<input type="hidden" name="id" value="<%= mutter.getId() %>">
<input type="hidden" name="dislike" value="<%= mutter.getDislike() %>">
<button name="state" value="dislikeCount">よくないね</button>
：<%= mutter.getDislike() %>人
</form>
<br>
</div>
<!-- Like終了 -->

<!-- edit開始 -->
<% if(editMutter!=null && editMutter.getId()==mutter.getId()){ %>
<div style="display:inline-block;padding-top:5px;">
<form action="/docoTsubu/MainServlet" method="post" style="display:inline;" name="formEditConfirm">
<input type="hidden" name="text" value="" id="textEditInput">
<input type="hidden" name="updatedAt" value="<%= timeStamp %>">
<button type="submit" name="state" value="editConfirm,<%= mutter.getId() %>">編集確定</button>
</form>
</div>
<script type="text/javascript">
function funcEdit(){
var text = document.getElementById("editTextarea").value;//document.formEditTextArea.elements[0].value;
document.getElementById("textEditInput").value = text;
}
</script>
<% }else if(mutter.getUserName().equals(loginUser.getLoginId())) { %>
<div style="display:inline-block;padding-top:5px;">
<form action="/docoTsubu/MainServlet" method="post" style="display:inline;">
<input type="hidden" name="text" value="<%= mutter.getText() %>">
<button type="submit" name="state" value="edit,<%= mutter.getId() %>">編集</button>
</form>
</div>
<% } %>
<!-- edit終了 -->


<!-- delete開始 -->
<% if(mutter.getUserName().equals(loginUser.getLoginId())) { %>
<div style="display:inline-block;padding-top:5px;">
<button type="button" onclick="funcMutterDelete(<%= mutter.getId() %>)">削除</button>
<!-- <button name="state" value="delete, スクリプトレットでmutterid">削除</button> -->
</div>
<% } %>
<!-- delete修了 -->


<!-- comment開始 -->
<!-- ここにコメントを表示 -->
<div style="background-color:#f9fbff;">
<p style="border-bottom:1px dashed #333;">＜コメント欄＞</p>
<%
/*commentリストを取得して、for文で回すセクション*/
if(commentList != null || commentList.size()!=0){
	for(Comment comment : commentList){
		/* コメント削除後の分岐処理 */
		if(comment.getCommentText()!=null){
			/* mutterとcommentの紐付け分岐処理 */
			if(mutter.getId()==comment.getMutterId()){
%>
<p><%= comment.getUserName() %>：<%= comment.getCreatedAt() %></p>
<div style="border-bottom:1px dashed #333;padding-bottom:7px;">
<%= comment.getCommentText() %>
<!-- commentDelete開始 -->
<% if(comment.getUserName().equals(loginUser.getLoginId())) { %>
<button type="button" onclick="funcCommentDelete(<%= comment.getId() %>)" style="float:right;margin-right:10px;">削除</button>
<div style="display:none;clear:both;"></div>
<% } //削除ボタン表示の分岐 終了 %>
</div>
<!-- commentDelete終了 -->
<%} //idマッチングのif文
} else{}//comment削除後の分岐の終わり
}//commentの拡張for文
}//commentListのif文%>

<form action="/docoTsubu/MainServlet" method="post">
<p>
<textarea rows="3" cols="30" name="commentText"></textarea>
<input type="hidden" name="mutterId" value="<%= mutter.getId() %>">
<input type="hidden" name="userName" value="<%= loginUser.getLoginId() %>">
<input type="hidden" name="createdAt" value="<%= timeStamp %>">
<button name="state" value="addComment">コメント</button>
</p>
</form>
<% if(errorComment != null){ %>
<p style="color:red;"><%= errorComment %></p>
<% } %>

<!-- つぶやきのソート機能。時間、いいね順、よくないね順、ユーザー順昇順降順 -->
<!-- comment終了 -->
</div>

</div>
<!-- mutter削除後の分岐処理終了 -->
<% } else{} ///mutter削除後の分岐処理%>
<%
	} //mutterListの繰り返し閉じ
} //つぶやき内容開始の閉じ %>
<!-- つぶやき内容修了 -->
<footer style="height:50px;padding-top:10px;background:#308dff;color:white;text-align:center;">
Copyright © 2019 docotsubu All Rights Reserved.
</footer>
</div>


<script type="text/javascript">
<!--
function funcMutterDelete(i) {
    if (confirm("削除します。よろしいですか？")) {
        var form = document.createElement("form");
        form.setAttribute("action", "/docoTsubu/MainServlet");
        form.setAttribute("method", "post");
        form.style.display = "none";
        document.body.appendChild(form);
        var input = document.createElement("input");
        input.setAttribute("type","hidden");
        input.setAttribute("name", "state");
        input.setAttribute("value", 'delete,'+i);
        form.appendChild(input);
        form.submit();
    }
}
function funcCommentDelete(i) {
    if (confirm("削除します。よろしいですか？")) {
        var form = document.createElement("form");
        form.setAttribute("action", "/docoTsubu/MainServlet");
        form.setAttribute("method", "post");
        form.style.display = "none";
        document.body.appendChild(form);
        var input = document.createElement("input");
        input.setAttribute("type","hidden");
        input.setAttribute("name", "state");
        input.setAttribute("value", 'deleteComment,'+i);
        form.appendChild(input);
        form.submit();
    }
}
-->
</script>
</body>
</html>