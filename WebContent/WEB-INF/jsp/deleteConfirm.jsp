<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<% User loginUser = (User)session.getAttribute("loginUser"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>削除確認|どこつぶ</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
</head>
<body>
<div id="main">
<div id="header" style="width:100%;background:#308dff;">
<h1>ユーザー削除確認</h1>
</div>
<div id="content">
<p>本当に削除しますか？</p>
<form method="post" action="/docoTsubu/UserServlet">

<dl>
<dt>プロフィール画像</dt>
<dd>
 <% if(loginUser.getImgPath()!=null){ %>
<img src="<%= loginUser.getImgPath() %>" alt="<%= loginUser.getLoginId() %>" width="50">
 <% }else{} %>
 </dd>
<dt>ユーザID</dt><dd><%= loginUser.getLoginId() %></dd>
<dt>パスワード</dt><dd>********<% //loginUser.getPass() %></dd>
<dt>メールアドレス</dt><dd><% if(loginUser.getMail()!=null){ out.print(loginUser.getMail()); } else {} %></dd>
<dt>名前</dt><dd><% if(loginUser.getName()!=null){ out.print(loginUser.getName()); } else {} %></dd>
<dt>年齢</dt><dd>
<% if(loginUser.getAge()!=-1){ %>
<%= loginUser.getAge() %>
<% }else{} %>
</dd><!-- もしageが-1なら表示しないっていう分岐  -->
<dt></dt><dd><button name="state" value="delete">削除確定（退会）</button>
<button name="state" value="mypage">戻る</button>
</dd>
</dl>
</form>
</div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>