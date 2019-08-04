<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー登録|どこつぶ</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
</head>
<body>
<div id="main">
<div id="header" style="width:100%;background:#308dff;">
<h1>ユーザー登録</h1>
</div>
<div id="content">
<form method="post" action="/docoTsubu/UserServlet" enctype="multipart/form-data">
<dl>
<dt>プロフィール画像</dt><dd><input type="file" name="profileImg">
<input type="hidden" value="/Applications/Eclipse_2018-12.app/Contents/workspaceSV/chap10/WebContent/img" name="destination" id="destination">
</dd>
<dt>ログインID *</dt><dd><input type="text" size="20" name="loginId" required></dd>
<dt>パスワード *</dt><dd><input type="password" size="20" name="pass" required></dd>
<dt>メールアドレス</dt><dd><input type="text" size="40" name="mail"></dd>
<dt>名前</dt><dd><input type="text" size="20" name="name"></dd>
<dt>年齢</dt><dd><input type="text" size="20" name="age"></dd>
<dt></dt><dd><button name="state" value="registerConfirm">登録</button>
<!-- <button name="state" value="index">戻る</button> -->
<button type="button" name="state" value="index" onClick="history.back()">戻る</button>
</dd>
</dl>
</form>
</div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>