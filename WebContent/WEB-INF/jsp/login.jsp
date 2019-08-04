<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン|どこつぶ</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
</head>
<body>
<div id="main">
<div id="header" style="width:100%;background:#308dff;">
<h1>ログイン</h1>
</div>
<div id="content">
<form action="/docoTsubu/LoginServlet" method="post">
ログインID：<input type="text" name="loginId"><br>
パスワード：<input type="password" name="pass"><br>
<input type="submit" value="ログイン">
</form>
</div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>