## Spring 로그인 및 회원가입

## 로그인
<ul>
	<li>WebBinder를 이용한 데이터 바인딩 및 정규식을 이용한 벨리데이션 체크</li>
	<li>SHA256 + Salt를 이용한 비밀번호 암호화</li>
	<li>쿠키와 세션을 이용한 자동 로그인</li>
</ul>

### WebBinder를 이용한 데이터 바인딩 <hr />

loginView.jsp

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

UserControllerImpl

	@InitBinder("login") // 바인더의 name값 지정
	public void loginBinder(WebDataBinder binder) {
		binder.addValidators(userLoginValidate); // 로그인에 대한 벨리데이션 추가
	}
	
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	public void loginView(HttpSession session, HttpServletResponse response, HttpServletRequest request, 
			// ModelAttribute 어노테이션에 InitBinder에 지정한 name 값을 입력한다
			// @Valid 어노테이션을 이용하여 벨리데이션 적용
			ModelAndView view, @ModelAttribute("login") @Valid User user, BindingResult result) throws Exception {
				// 소스 생략
	}
	
UserLoginValidate
	
	@Component
	public class UserLoginValidate implements Validator {

	@Override
	public boolean supports(Class<?> clazz) { // Vo 객체가 Validator가 지원 가능한지 확인
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User)target; // 해당 유저 객체를 가져온다
		
		// 정규식을 이용하여 이메일과 비밀번호가 형식에 맞는지 체크한다
		boolean emailMatche = Pattern.matches(
					"([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", 	user.getEmail() );
			ValidationModule.nullOrValidateCheck(errors, emailMatche, "email", "이메일 형식을 올바르게 입력해주세요");
			
			boolean passwordMatche = Pattern.matches(
					"^(?=.*\\d)(?=.*[~!@#$%^&*()_+])[A-Za-z\\d#$@!%&*?]{8,16}$", user.getPassword() );
			ValidationModule.nullOrValidateCheck(errors, passwordMatche, "password", "비밀번호는 8~16자리 사이로 문자, 숫자, 특수문자 조합입니				다.");
		}
	
	}
	
nullOrValidateCheck
	
	// null체크와 정규식 유효성 검사를 편하게 하기 위하여 만든 유틸
	public class ValidationModule {
		// null 체크와 유효성 검사를 하는 메서드
		public static void nullOrValidateCheck (Errors errors, boolean matche, String field, String defaultMessage) {
			String nullFieldName = "";
		
		switch(field) {
			case "email" :
				nullFieldName = "이메일을";
				break;
			case "name" :
				nullFieldName = "이름을";
				break;
			case "password" :
				nullFieldName = "비밀번호를";
				break;
			case "pwdCheck" :
				nullFieldName = "비밀번호 확인을";
				break;
			case "comment" :
				nullFieldName = "소개를";
				break;
			case "gender" :
				nullFieldName = "성별을";
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, "nullvalue", nullFieldName + " 입력해주세요");
		
		if (!matche) errors.rejectValue(field, "negativevalue", defaultMessage);
		}
	}

### SHA256 + Salt를 이용한 비밀번호 암호화 <hr />

UserServiceImpl

	@Override
	public User login(User user) throws UserFailLoginException {
		User resultUser; // 반환할 회원정보를 담을 객체
		User salt = userDao.getSalt(user.getEmail()); // salt가 담긴 Vo 객체
		String password = ""; // 솔팅한 비밀번호를 담을 객체
		
		try { // 아이디 확인
			password = SHA256Util.getEncrypt(user.getPassword(), salt.getSalt()); // 입력받은 비밀번호에 솔팅한 값을 담는		다
			
			user.setPassword(password); // Vo 객체에 솔팅한 패스워드를 저장한다
			
		} catch (NullPointerException e) { // salt가 비어있는 객체일 경우 아이디가 존재하지 않는것이다
			throw new UserFailLoginException("존재하지 않는 아이디입니다.");
		}
		
		try {
			resultUser = userDao.loginUser(user); // 솔팅한 비밀번호를 이용하여 로그인 시도
			@SuppressWarnings("unused")
			String pwd = resultUser.getPassword(); // 비밀번호가 틀렸을 경우 NullPointerException이 발생
		} catch (NullPointerException e) {
			throw new UserFailLoginException("비밀번호가 틀립니다.");
		}
		
		return resultUser; // 정상적으로 로그인이 성공하였을 경우 회원정보를 담은 Vo 반환
	}
	
	
SHA256Util

	public static String getEncrypt(String source, String salt) {
	        return getEncrypt(source, salt.getBytes());
	    }
	    
	    public static String getEncrypt(String source, byte[] salt) {
	        
	        String result = ""; // 결과를 담을 객체
	        
	        byte[] a = source.getBytes(); // 입력받은 비밀번호를 byte 배열로 변환한다
	        byte[] bytes = new byte[a.length + salt.length]; // 바이트 배열의 길이는 비밀번호 길이 + salt 길이
	        
	        System.arraycopy(a, 0, bytes, 0, a.length); // bytes 에 입력받은 비밀번호를 추가한다
	        System.arraycopy(salt, 0, bytes, a.length, salt.length); // a를 입력한 다음 index부터 salt를 추가한다
	        
	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA-256"); // SHA-256 해시기법 인코딩을 하기 위한 인스턴스 			생성
	            md.update(bytes); // 인코딩
	            
	            byte[] byteData = md.digest(); // 인코딩한 값을 담는 byte 배열
	            
	            StringBuffer sb = new StringBuffer();
	            for (int i = 0; i < byteData.length; i++) { // SHA-256 인코딩 결과값을 담은 byte 배열을 int 값으로 변환하여 			저장
	                sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
	            }
	            
	            result = sb.toString(); // 최종 결과값을 result에 반환
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        
	        return result;
	    }
	    
	    public static String generateSalt() { 
	        Random random = new Random(); // 랜덤 클래스 생성
	        
	        byte[] salt = new byte[8]; // 길이가 8인 byte 배열 생성
	        random.nextBytes(salt); // byte 배열에 랜덤값 생성
	        
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < salt.length; i++) {
	            // byte 값을 Hex(16진수) 값으로 바꾸기.
	            sb.append(String.format("%02x",salt[i]));
	        }
	        
	        return sb.toString();
	    }
	    
UserMapper.xml
	
	<!-- 로그인 -->
	<select id="loginUser" parameterType="com.blog.mango.user.vo.User" resultMap="user">
		select EMAIL, NAME, GENDER, COMMENT, IMG_PATH, SALT
		from BLOG_USER
		where EMAIL = #{email} and PASSWORD = #{password}
		limit 1
	</select>
	


### 쿠키와 세션을 이용한 자동 로그인 <hr />

UserController

	try {
			User loginUser = userService.login(user); // 로그인 시도, 성공하면 회원 정보를 loginUser에 담는다
			session.setAttribute("login", loginUser);
			
			// 자동 로그인이 true면 sessionId와 로그인 유지기간을 지정해준다
			if (user.getKeepLogin()) {
				int amount = 60 * 60 * 24 * 7; // 7일
				String sessionId = session.getId(); // 세션 고유id
				User keepLoginUser =
						new User(user.getEmail(), sessionId, new Date(System.currentTimeMillis() + (1000 * 			amount)));
				
				Cookie cookie = new Cookie("loginCookie", sessionId); // 쿠키 생성
				cookie.setPath("/"); // 쿠키가 적용되는 path 설정
				cookie.setMaxAge(amount); // 쿠키 유효기간 설정
				response.addCookie(cookie); // 브라우저에 쿠키 저장
				
				userService.keepLogin(keepLoginUser);
			}
			
			out.println(PrintWriterUtil.goScript(resultMessage, request.getContextPath()+ "/index"));
			
		} catch (UserFailLoginException e) { // userService.login 중 아이디나 비밀번호가 틀릴 경우
			resultMessage = e.getMessage(); // 예외 메세지를 resultMessage 에 담는다
			out.println(PrintWriterUtil.goScript(resultMessage, request.getContextPath()+ "/user/login")); // 예외 		메세지와 이동할 경로를 스크립트로 view에 보낸다
		} finally {
			out.flush();
			out.close();
		}
		
AuthenticationInterceptor
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws 			Exception {
		HttpSession session = request.getSession();
		
		Object loginSession = session.getAttribute("login"); // 세션을 가져온다
		if (loginSession == null) { // 세션이 비었을 경우
			Cookie cookie = WebUtils.getCookie(request, "loginCookie"); // loginCookie를 가져온다
			
			if (cookie != null) { // loginCookie가 비어있지 않을 경우
				User user = userService.cookieLogin(cookie.getValue()); // cookie를 이용하여 회원 정보를 받아와 Vo 객체에 		담는다
				
				if (user != null) { // 정보를 성공적으로 받아왔을 경우
					session.setAttribute("login", user); // 세션에 정보를 저장한다
					return false;
				}
			}
		}
		
		return true;
	}
	
UserMapper

	<!-- 솔트 얻어오기 -->
	<select id="getSalt" parameterType="String" resultMap="user">
		select SALT
		from BLOG_USER
		where EMAIL = #{email}
		limit 1
	</select>

	<!-- 로그인시 로그인 유지를 체크했을 경우 -->
	<update id="keepLogin" parameterType="com.blog.mango.user.vo.User">
		update BLOG_USER
		<set>
			SESSION_ID = #{sessionId},
			SESSION_LIMIT = #{sessionLimit},
		</set>
		where EMAIL = #{email}
	</update>

	<!-- 자동 로그인 -->
	<select id="cookieLogin" parameterType="String" resultMap="user">
		select EMAIL, NAME, GENDER, COMMENT, IMG_PATH
		from BLOG_USER
		where SESSION_ID = #{sessionId} and SESSION_LIMIT > now()
		limit 1
	</select>
		
