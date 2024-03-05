<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>
<!-- 에러 처리 페이지가 되기 위해서는 page지시어에 isErrorPage 속성값으로 true를 주어야 한다. 내장객체 exception을 사용할 수 있다-->
<jsp:include page="/inc/top.jsp"/>

	<div class="container">
		<h2>서버 에러 발생</h2>
		<%
		//if(exception instanceof SQLIntegrityConstraintViolationException){
		if(true){
			%>
			<script>
				alert('회원만 글을 쓸 수 있습니다');
				history.back();
			</script>
			<% 
		}else{
		%>
		
		<h3>
			<%=exception.getMessage() %>
			<%
			
				exception.printStackTrace();
			%>
		
		</h3>
		<a href="../index.jsp">home으로 이동</a>
		<%
		}
		%>
	</div>
	
<jsp:include page="/inc/foot.jsp"/>

