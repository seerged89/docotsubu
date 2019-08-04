<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<% String fileName = (String)request.getAttribute("fileName"); %>
<% User editUser = (User)session.getAttribute("editUser"); %>
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
<form method="post" action="/docoTsubu/UserServlet">

<dl>
<dt>プロフィール画像</dt>
<dd>
 <% if(editUser.getImgPath()!=null){ %>
<%= fileName %>
 <% }else{} %>
 </dd>
<dt>ログインID</dt><dd><%= editUser.getLoginId() %></dd>
<dt>パスワード</dt><dd><% if(!editUser.getPass().isEmpty()){out.print("********");} %></dd>
<dt>メールアドレス</dt><dd><% if(editUser.getMail()!=null){ out.print(editUser.getMail()); } else {} %></dd>
<dt>名前</dt><dd><% if(editUser.getName()!=null){ out.print(editUser.getName()); } else {} %></dd>
<dt>年齢</dt><dd>
<% if(editUser.getAge()!=-1){ %>
<%= editUser.getAge() %>
<% }else{} %>
</dd><!-- もしageが-1なら表示しないっていう分岐  -->
<dt></dt><dd><button name="state" value="update">更新</button>
<button name="state" value="newRegister">戻る</button>
</dd>
</dl>
</form>
</div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>