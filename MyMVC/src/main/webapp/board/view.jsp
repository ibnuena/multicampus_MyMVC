<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class=container>
	<h1>MVC Board</h1>
	<br>
	<br>
	<c:if test="${vo eq null }">
		<script>
			alert('해당 글은 존재하지 않습니다');
			history.back();
		</script>
	</c:if>
	<c:if test="${vo ne null }">
		<table border="1">
			<tr>
				<td width="20%" class="m1"><b>글번호</b></td>
				<td width="30%">${vo.num}</td>
				<td width="20%" class="m1"><b>작성일</b></td>
				<td width="30%">${vo.wdate}</td>
			</tr>
			<tr>
				<td width="20%" class="m1"><b>글쓴이</b></td>
				<td width="30%">${vo.name}</td>
				<td width="20%" class="m1"><b>조회수</b></td>
				<td width="30%">${vo.readnum }</td>
			</tr>
			<tr>
				<td width="20%" class="m1"><b>첨부파일</b></td>
				<td colspan="3" class="text-left">&nbsp; 
				<!-- 첨부파일이 있다면 --> 
				<c:if test="${vo.fileName ne null }">
							<!-- request.getContextPath()와 동일 -->
					<a href="${pageContext.request.contextPath}/upload/${vo.fileName}" download>
						${vo.fileName }
					</a> 
					[ ${vo.fileSize } ]bytes
				</c:if>
				</td>
			</tr>
			<tr>
				<td width="20%" class="m1"><b>제목</b></td>
				<td colspan="3">${vo.title }</td>
			</tr>
			<tr>
				<td width="20%" class="m1">
					<b>글내용</b>
				</td>
				<td colspan="3" style="height:150px">
					<pre>${vo.content }</pre>
				</td>
			</tr>
			<tr>
				<td colspan="4" class="text-center">
					<a href="list.do">글목록</a>|
					<a href="#" onclick="goEdit()">글수정</a>| 
					<a href="#" onclick="goDel()">글삭제</a> |
					
				</td>
			</tr>
		</table>
	</c:if>
	<br><br>
	<div id="divPasswd" style="display:none">
		<!-- 수정/삭제시 사용 -->
		<form id="frm" method="post">
			<input type="hidden" name="num" value="${vo.num }">
				<!-- 수정 또는 삭제할 글번호를 hidden으로 넘김 -->
			<label for="passwd">비밀번호</label>
			<input type="password" name="passwd" id="passwd" placeholder="글비밀번호" required style="width:25%">
			<button class="btn" id="btn"></button>
		</form>
	</div>
</div>
<script>
	let obj = document.querySelector('#divPasswd');
	let btn1 = document.querySelector('#btn');
	let frm = document.querySelector('#frm');
	const goDel=function(){
		//var yn = confirm("정말 삭제하시겠습니까?");
		//if(!yn) return;
		
		obj.style.display = 'block';
		btn1.innerText = '글삭제';
		frm.action = 'delete.do';
	}//-------------------------
	const goEdit=function(){
		obj.style.display = 'block';
		btn1.innerText='글수정';
		frm.action='update.do';
	}//-------------------------
</script>