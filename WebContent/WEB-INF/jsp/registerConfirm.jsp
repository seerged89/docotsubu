<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<% String fileName = (String)request.getAttribute("fileName"); %>
<% User registerUser = (User)session.getAttribute("registerUser"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー登録確認|どこつぶ</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
</head>
<body>
<div id="main">
<div id="header" style="width:100%;background:#308dff;">
<h1>ユーザー登録確認</h1>
</div>
<div id="content">
<form method="post" action="/docoTsubu/UserServlet">

<dl>
<dt>プロフィール画像</dt>
<dd>
 <% if(registerUser.getImgPath()!=null){ %>
<%= fileName %>
 <% }else{} %>
 </dd>
<dt>ログインID</dt><dd><%= registerUser.getLoginId() %></dd>
<dt>パスワード</dt><dd><%= registerUser.getPass() %></dd>
<dt>メールアドレス</dt><dd><%= registerUser.getMail() %></dd>
<dt>名前</dt><dd><%= registerUser.getName() %></dd>
<dt>年齢</dt><dd>
<% if(registerUser.getAge()!=-1){ %>
<%= registerUser.getAge() %>
<% }else{} %>
</dd><!-- もしageが-1なら表示しないっていう分岐  -->
<dt></dt><dd><button name="state" value="register">確定</button>
<button name="state" value="newRegister">戻る</button>
</dd>
</dl>

<%-- アクションタグの使い方！（正）
<dl>
<dt>ユーザID</dt><dd><jsp:getProperty property="userId" name="registerUser"/></dd>
<dt>パスワード</dt><dd><jsp:getProperty property="pass" name="registerUser"/></dd>
<dt>メールアドレス</dt><dd><jsp:getProperty property="mail" name="registerUser"/></dd>
<dt>名前</dt><dd><jsp:getProperty property="name" name="registerUser"/></dd>
<dt>年齢</dt><dd><jsp:getProperty property="age" name="registerUser"/></dd>
<dt></dt><dd><button name="state" value="register">確定</button>
<button name="state" value="newRegister">戻る</button>
</dd>
</dl> --%>
</form>
</div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>