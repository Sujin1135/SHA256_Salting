package com.blog.mango.user.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.blog.mango.module.ValidationModule;
import com.blog.mango.user.vo.User;

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
				"([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", user.getEmail() );
		ValidationModule.nullOrValidateCheck(errors, emailMatche, "email", "이메일 형식을 올바르게 입력해주세요");
		
		boolean passwordMatche = Pattern.matches(
				"^(?=.*\\d)(?=.*[~!@#$%^&*()_+])[A-Za-z\\d#$@!%&*?]{8,16}$", user.getPassword() );
		ValidationModule.nullOrValidateCheck(errors, passwordMatche, "password", "비밀번호는 8~16자리 사이로 문자, 숫자, 특수문자 조합입니다.");
	}

}