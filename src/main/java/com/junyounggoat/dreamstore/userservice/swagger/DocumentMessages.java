package com.junyounggoat.dreamstore.userservice.swagger;

import static com.junyounggoat.dreamstore.userservice.validation.UserValidation.*;

abstract class DocumentMessages {
    public static final String USER_PRIVACY_USAGE_PERIOD_CODE_DESCRIPTION = "<h3>사용자개인정보사용기간 - 코드</h3>" +
            "코드가 초 단위로 표시됩니다. <br/>" +
            "<ul>" +
            "<li>1년 - 31536000</li>" +
            "<li>3년 - 94608000</li>" +
            "<li>5년 - 157680000</li>" +
            "<li>평생회원 - 0</li>" +
            "</ul>";

    public static final String USER_PERSON_NAME_DESCRIPTION = "<h3>사용자사람이름</h3>" +
            USER_PERSON_NAME_MESSAGE;

    public static final String USER_EMAIL_ADDRESS_DESCRIPTION = "<h3>사용자이메일주소 (UNIQUE)</h3>" +
            USER_EMAIL_ADDRESS_MESSAGE;

    public static final String USER_PHONE_NUMBER_DESCRIPTION = "<h3>사용자휴대폰번호 (UNIQUE)</h3>" +
            USER_PHONE_NUMBER_MESSAGE;
    
    public static final String USER_NICKNAME_DESCRIPTION = "<h3>사용자닉네임 (UNIQUE)</h3>" +
            USER_NICKNAME_ERROR_MESSAGE;

    public static final String USER_GENDER_CODE_DESCRIPTION = "<h3>사용자성별코드</h3>" +
            "<ul>" +
            "<li>남자 - 0</li>" +
            "<li>여자 - 1</li>" +
            "</ul>";

    public static final String USER_BIRTH_DATE_DESCRIPTION = "<h3>사용자생년월일</h3>" +
            USER_BIRTH_DATE_ERROR_MESSAGE;
}
