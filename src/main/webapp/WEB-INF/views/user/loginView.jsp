<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
	<head>
		<jsp:include page="../head/head.jsp" />
		
		<title>로그인</title>
	</head>
	
	<body>
		<article class="article">
			<div class="joinForm">
				<div class="header text-center">
					<h1>
						로그인
					</h1>
				</div>
			
				<form class="text-center" action="./login" method="POST">
					<div>
						<label for="email">이메일</label>
						<input type="email" id="email" name="email" />
					</div>
					
					<div>
						<label for="password">비밀번호</label>
						<input type="password" id="password" name="password" />
					</div>
					
					<div>
						<label for="keepLogin">자동 로그인</label>
						<input type="checkbox" id="keepLogin" name="keepLogin" />
					</div>
					
					<div>
						<button type="submit" id="submit" class="float-center">로그인</button>
						<button type="reset" class="float-center">취소</button>
					</div>
				</form>
			</div>
		</article>
		
		<script type="text/javascript">
			/* var submitButton = getIdObject("submit");
			submitButton.addEventListener("click", function(event) {
				doSubmit();
			}); */
		
			/* function doSubmit() {
				var param = {};
				param.email = getIdValue("email");
				param.password = getIdValue("password");
				if (getIdObject("keepLogin").checked) param.keepLogin = true;
				
				$.ajax({
					url: "${pageContext.request.contextPath}/user/login",
					type: "POST",
					dataType: "json",
					data: param,
					success: function(data) {
						if (data.success != undefined && data.success != "") {
							goBack("${pageContext.request.contextPath}/index", data.success);
						} else {
							alert(data.fail);
						}
					},
					error: function(error) {
						alert(error.fail)
					}
				});
			} */
		</script>
	</body>
</html>