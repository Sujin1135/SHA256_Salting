<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
	<head>
		<jsp:include page="../head/head.jsp" />
		  
		<title>회원가입</title>
	</head>
	
	<body>
		<article class="article">
			<div class="joinForm">
				<div class="header text-center">
					<h1>
						회원가입
					</h1>
				</div>
			
				<form class="text-center">
					<div>
						<label for="email">이메일</label>
						<input type="email" id="email" name="email" />
					</div>
					
					<div>
						<label for="name">이름</label>
						<input type="text" id="name" name="name" />
					</div>
					
					<div>
						<label for="password">비밀번호</label>
						<input type="password" id="password" name="password" />
					</div>
					
					<div>
						<label for="pwdCheck">비밀번호 확인</label>
						<input type="password" id="pwdCheck" name="pwdCheck" />
					</div>
					
					<div>
						<label for="comment">소개</label>
						<div id="comment_div">
							<textarea id="comment" name="comment" rows="10" cols="25"></textarea>
						</div>
					</div>
					
					<div>
						<label for="gender">성별</label>
						<span id="gender">
							<label>
								<input type="radio" name="gender" value="male" checked="checked"/>남자
							</label>
							<label>
								<input type="radio" name="gender" value="female" />여자
							</label>
						</span>
					</div>
					
					<div>
						<button type="button" id="submit">등록</button>
						<button type="reset" id="cancel">취소</button>
					</div>
				</form>
			</div>
		</article>
		
		<script type="text/javascript">
			var submitButton = getIdObject("submit");
			submitButton.addEventListener("click", function(event) {
				doSubmit();
			});
		
			function doSubmit() {
				var param = {};
				param.email = getIdValue("email");
				param.name = getIdValue("name");
				param.password = getIdValue("password");
				param.pwdCheck = getIdValue("pwdCheck");
				param.comment = getIdValue("comment");
				
				var gender = document.getElementsByName("gender");
				for(i = 0; i < gender.length; i++) {
					if(gender[i].checked) param.gender = ++i;
				}
				
				$.ajax({
					url: "${pageContext.request.contextPath}/user/join",
					type: "POST",
					dataType: "json",
					data: param,
					success: function(data) {
						console.log(data.success);
						if (data.success != null && data.success != "") {
							goBack("${pageContext.request.contextPath}/index", data.success);
						} else {
							alert(data.fail);	
						}
					},
					error: function(error) {
						alert(error.fail);
						console.log(error.fail);
					}
				});
			}
		</script>
	</body>
</html>
