package com.blog.mango.user.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.blog.mango.module.ValidationModule;
import com.blog.mango.user.vo.User;

@Component("join")
public class UserJoinValidate implements Validator {

	@Override
	public boolean supports(Class<?> clazz) { // Vo 객체가 Validator를 지원하는지 확인
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		
		boolean emailMatche = Pattern.matches(
				"([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", user.getEmail() );
		ValidationModule.nullOrValidateCheck(errors, emailMatche, "email", "이메일 형식을 올바르게 입력해주세요");
	
		boolean nameMatche = Pattern.matches(
				"^[가-힣]{2,4}|[a-zA-Z]{2,10}$", user.getName() );
		ValidationModule.nullOrValidateCheck(errors, nameMatche, "name", "이름 형식은 한글명 2~4글자 영문명 2~10글자 내외로 입력해주세요.");
	
		boolean passwordMatche = Pattern.matches(
				"^(?=.*\\d)(?=.*[~!@#$%^&*()_+])[A-Za-z\\d#$@!%&*?]{8,16}$", user.getPassword() );
		ValidationModule.nullOrValidateCheck(errors, passwordMatche, "password", "비밀번호는 8~16자리 사이로 문자, 숫자, 특수문자 조합으로 설정해주세요.");
	
		boolean passwordCheck = user.getPassword().equals(user.getPwdCheck() );
		ValidationModule.nullOrValidateCheck(errors, passwordCheck, "pwdCheck", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
	
		boolean commentLength = user.getComment().length() <= 100;
		ValidationModule.nullOrValidateCheck(errors, commentLength, "comment", "소개는 100byte 내외로 입력해주세요.");
	
		boolean genderCheck = user.getGender() != 0 || user.getGender() == 1 || user.getGender() == 2;
		ValidationModule.nullOrValidateCheck(errors, genderCheck, "gender", "성별을 선택해주세요.");
		
	}
	
}