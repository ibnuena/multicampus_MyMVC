<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- message.jsp -->

<script type="text/javascript">

	// alert('${requestScope.msg}') // sessionScope.msg -> 세션에서 찾는다. requestScope -> request에서 찾는다
	alert('${msg}');
	location.href='${loc}'

</script>