package com.junyounggoat.dreamstore.userservice.swagger;

import com.junyounggoat.dreamstore.userservice.config.OpenApiConfig;
import com.junyounggoat.dreamstore.userservice.controller.UserController;
import com.junyounggoat.dreamstore.userservice.dto.*;
import com.junyounggoat.dreamstore.userservice.validation.UserLoginCredentialsValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.junyounggoat.dreamstore.userservice.service.KakaoLoginService.REQUEST_KAKAO_PROFILE_FAILED_MESSAGE;
import static com.junyounggoat.dreamstore.userservice.service.KakaoLoginService.REQUEST_KAKAO_TOKEN_FAILED_MESSAGE;

public abstract class UserControllerDocs {
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "사용자로그인자격증명으로 가입")
    @RequestBody(
            description = DocumentMessages.USER_PERSON_NAME_DESCRIPTION +
                    DocumentMessages.USER_EMAIL_ADDRESS_DESCRIPTION +
                    DocumentMessages.USER_PHONE_NUMBER_DESCRIPTION +
                    "<h3>로그인사용자이름 (UNIQUE)</h3>" +
                    UserLoginCredentialsValidation.LOGIN_USER_NAME_MESSAGE +
                    "<h3>로그인사용자비밀번호</h3>" +
                    UserLoginCredentialsValidation.RAW_LOGIN_USER_PASSWORD_MESSAGE +
                    "<h3>사용자동의항목 - 코드</h3>" +
                    "<ul>" +
                    "<li>개인정보 수집 및 이용 동의(필수) - 0 </li>" +
                    "<li>개인정보 수집 및 이용 동의 (선택) - 1 </li>" +
                    "<li>이용약관 (필수) -2 </li>" +
                    "<li>SMS 수신 (선택) - 3 </li>" +
                    "<li>푸시알림 수신 (선택) - 4 </li>" +
                    "<li>이메일 수신 (선택) - 5 </li>" +
                    "</ul>" +
                    DocumentMessages.USER_PRIVACY_USAGE_PERIOD_CODE_DESCRIPTION,
            content = @Content(schema = @Schema(implementation = CreateUserRequestDTO.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공 및 accessToken 발급",
                    content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "필수값 누락, 잘못된 형식의 입력, 중복된 값 입력",
                    content = @Content(schema = @Schema(example = "{\n" +
                            "    \"fieldErrors\": {\n" +
                            "        \"user.userPersonName\": \"2~30자의 이름을 입력해주세요.\",\n" +
                            "        \"userLoginCredentials.loginUserName\": \"영문, 숫자, 한글, 특수문자로 4~20자를 입력해주세요.\"\n" +
                            "    }," +
                            "    \"notFieldErrors\": []\n" +
                            "}")))
    })
    public @interface CreateUserDocs { }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "사용자로그인자격증명으로 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공 및 accessToken 발급",
                    content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 아이디 또는 비밀번호 입력",
                    content = @Content(schema = @Schema(example = "{\n" +
                            "    \"fieldErrors\": {},\n" +
                            "    \"notFieldErrors\": [\n" +
                            "        \"아이디나 비밀번호가 일치하지 않습니다.\"\n" +
                            "    ]\n" +
                            "}")))
    })
    public @interface LoginDocs { }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "로그인한 사용자 조회",
            description = "엑세스 토큰을 제공해야만 조회 가능합니다. <br />" +
                    "<a href=\"https://lemontia.tistory.com/1027\" target=\"_blank\">사용 방법</a>")
    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공",
                    content = @Content(schema = @Schema(implementation = MyUserDTO.class))),
            @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
                    content = @Content(schema = @Schema(example = "로그인이 필요합니다."))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(schema = @Schema(example = UserController.USER_NOT_FOUND_MESSAGE)))
    })
    public @interface GetMyUserDocs { }


    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "사용자 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공",
                    content = @Content(schema = @Schema(implementation = OtherUserDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(schema = @Schema(example = UserController.USER_NOT_FOUND_MESSAGE)))
    })
    public @interface GetOtherUserDocs { }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "사용자 수정")
    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    @RequestBody(
            description = DocumentMessages.USER_PERSON_NAME_DESCRIPTION +
                    DocumentMessages.USER_EMAIL_ADDRESS_DESCRIPTION +
                    DocumentMessages.USER_PHONE_NUMBER_DESCRIPTION +
                    DocumentMessages.USER_NICKNAME_DESCRIPTION +
                    DocumentMessages.USER_GENDER_CODE_DESCRIPTION +
                    DocumentMessages.USER_BIRTH_DATE_DESCRIPTION,
            content = @Content(schema = @Schema(implementation = UpdateMyUserRequestDTO.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 수정 성공",
                    content = @Content(schema = @Schema(implementation = OtherUserDTO.class))),
            // ToDo: message 상수화
            @ApiResponse(responseCode = "400", description = "필수값 누락, 잘못된 형식의 입력, 중복된 값 입력",
                    content = @Content(schema = @Schema(example =
                            "{\n" +
                            "    \"fieldErrors\": {\n" +
                            "        \"userPhoneNumber\": \"이미 사용 중인 휴대폰 번호입니다.\",\n" +
                            "        \"userEmailAddress\": \"이미 사용 중인 이메일 주소입니다.\"\n" +
                            "    },\n" +
                            "    \"notFieldErrors\": []\n" +
                            "}"
                    ))),
            @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
                    content = @Content(schema = @Schema(example = "로그인이 필요합니다."))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(schema = @Schema(example = UserController.USER_NOT_FOUND_MESSAGE)))
    })
    public @interface UpdateMyUserDocs { }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "카카오 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공 및 accessToken 발급",
                    content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "승인코드 미입력",
                    content = @Content(schema = @Schema(example = "{\n" +
                            "  \"fieldErrors\": {\n" +
                            "    \"authorizationCode\": \"공백일 수 없습니다\"\n" +
                            "  },\n" +
                            "  \"notFieldErrors\": []\n" +
                            "}"))),
            @ApiResponse(responseCode = "400 ", description = "카카오 엑세스 토큰 조회 실패",
                    content = @Content(schema = @Schema(example = REQUEST_KAKAO_TOKEN_FAILED_MESSAGE))),
            @ApiResponse(responseCode = "400  ", description = "카카오 프로필 조회 실패",
                    content = @Content(schema = @Schema(example = REQUEST_KAKAO_PROFILE_FAILED_MESSAGE))),
            @ApiResponse(responseCode = "404", description = "미가입 사용자",
                    content = @Content(schema = @Schema(description = "kakaoId: Long, 회원가입 시 사용", implementation = Long.class)))
    })
    public @interface KakaoLoginDocs { }
}
