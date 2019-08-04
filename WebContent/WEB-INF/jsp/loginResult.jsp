<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%-- <jsp:useBean class="model.User" id="loginUser" scope="session" />  これloginUser=nullが送られてこないわ。--%>
<% User loginUser = (User)session.getAttribute("loginUser"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>どこつぶ</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
</head>
<body>
<div id="main">
<div id="header" style="width:100%;background:#308dff;">
<h1>どこつぶログイン</h1>
</div>
<div id="content">
<% if(loginUser !=null){ %>
<p>ログインに成功しました</p>
<p>ようこそ<%= loginUser.getLoginId() %>さん</p>
<form name="main" action="/docoTsubu/MainServlet" method="post">
<p>
<input type="hidden" name="state" value="main">
<a href="#" onClick="document.main.submit();return false;">つぶやきの投稿・閲覧へ</a>
</p>
</form>

<form name="mypage" action="/docoTsubu/UserServlet" method="post">
<p>
<input type="hidden" name="state" value="mypage">
<a href="#" onClick="document.mypage.submit();return false;">マイページへ</a>
</p>
</form>
<p><a href="/docoTsubu/LogoutServlet">ログアウト</a></p>
<% }else{ %>
<p>ログインに失敗しました</p>
<a href="/docoTsubu/">トップへ</a>

<% } %>
</div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>