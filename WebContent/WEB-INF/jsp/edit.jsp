<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<% User loginUser = (User)session.getAttribute("loginUser"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー編集確認|どこつぶ</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
</head>
<body>
<div id="main">
<div id="header" style="width:100%;background:#308dff;">
<h1>ユーザー編集</h1>
</div>
<div id="content">
<form method="post" action="/docoTsubu/UserServlet" enctype="multipart/form-data" name="form1">
<dl>
<dt>プロフィール画像</dt>
<dd>
 <% if(loginUser.getImgPath()!=null){ %>
<img src="<%= loginUser.getImgPath() %>" alt="<%= loginUser.getLoginId() %>" width="50">
 <% }else{} %>
 <input type="file" name="profileImg">
<input type="hidden" value="/Applications/Eclipse_2018-12.app/Contents/workspaceSV/chap10/WebContent/img" name="destination" id="destination">
</dd>
<dt>ログインID</dt><dd><input type="text" size="20" name="loginId" value="<%= loginUser.getLoginId() %>"></dd>
<dt>パスワード</dt><dd><input type="password" size="20" name="pass" value="********" onfocus="return funcPassFocus()" onBlur="return funcPassBlur()"></dd>
<dt>パスワード(確認用)</dt><dd><input type="password" size="20" name="pass2" value=""></dd>
<dt>メールアドレス</dt><dd><input type="text" size="40" name="mail" value="
<% if(loginUser.getMail()!=null){ out.print(loginUser.getMail()); } else {} %>
"></dd>
<dt>名前</dt><dd><input type="text" size="20" name="name" value="
<% if(loginUser.getName()!=null){ out.print(loginUser.getName()); } else {} %>
"></dd>
<dt>年齢</dt><dd><input type="text" size="20" name="age" value="
<% if(loginUser.getAge()!=-1){ out.print(loginUser.getAge()); } else {} %>
"></dd>
<dt></dt><dd><button name="state" value="editConfirm"  onclick="return funcConfirm()">編集確認</button>
<button type="button" onClick="history.back()">戻る</button>
</dd>
</dl>
</form>
</div>
<script type="text/javascript">
	function funcConfirm() {
		if (document.form1.pass.value != document.form1.pass2.value) {
			alert("確認用のパスワードが一致しません");
			return false;
		}
	}
	var count = 1;
	function funcPassFocus() {
		if(count==1){
		document.form1.pass.value = "";
		}
		count++;
	}
	function funcPassBlur() {
		document.form1.pass.value = document.form1.pass.value;
}
</script>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>