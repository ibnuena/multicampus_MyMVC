<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	// 컨텍스트명 구하기
	String ctx = request.getContextPath();
	// "/MyWeb"
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyWeb</title>
	<link rel="stylesheet" type="text/css" href="<%=ctx %>/css/style.css">
</head>
<body>

	<div class="wrap">
		<header>
			<!-- top menu -->
			<ul class="topMenu">
				<li><a href="<%=ctx %>/index.do">Home</a></li>
				<li><a href="<%=ctx %>/login/login.do">Login</a></li>
				<li><a href="#">a 님 로그인중</a>
				<li><a href="<%=ctx %>/login/logout.do">Logout</a></li>
				<li><a href="<%=ctx %>/member/join.do">Join</a></li>
				<li><a href="<%=ctx %>/board/input.do">게시판 글쓰기</a></li>
				<li><a href="<%=ctx %>/board/list.do">게시판 글목록</a></li>
				<li><a href="<%=ctx %>/login/mypage.do">MyPage</a></li>
			</ul>
		</header>
		