package com.blog.mango.module;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

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
