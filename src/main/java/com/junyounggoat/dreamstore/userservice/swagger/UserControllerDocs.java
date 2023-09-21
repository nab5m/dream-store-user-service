package com.junyounggoat.dreamstore.userservice.swagger;

import com.junyounggoat.dreamstore.userservice.dto.CreateUserRequestDTO;
import com.junyounggoat.dreamstore.userservice.dto.AccessTokenResponseDTO;
import com.junyounggoat.dreamstore.userservice.dto.MyUserDTO;
import com.junyounggoat.dreamstore.userservice.dto.OtherUserDTO;
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

import static com.junyounggoat.dreamstore.userservice.config.OpenApiConfig.SECURITY_SCHEME_NAME;
import static com.junyounggoat.dreamstore.userservice.controller.UserController.USER_NOT_FOUND_MESSAGE;
import static com.junyounggoat.dreamstore.userservice.validation.UserLoginCredentialsValidation.LOGIN_USER_NAME_MESSAGE;
import static com.junyounggoat.dreamstore.userservice.validation.UserLoginCredentialsValidation.RAW_LOGIN_USER_PASSWORD_MESSAGE;
import static com.junyounggoat.dreamstore.userservice.validation.UserValidation.*;

public abstract class UserControllerDocs {
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "사용자로그인자격증명으로 가입")
    @RequestBody(
            description = "<h3>사용자사람이름</h3>" +
                    USER_PERSON_NAME_MESSAGE +
                    "<h3>사용자이메일주소 (UNIQUE)</h3>" +
                    USER_EMAIL_ADDRESS_MESSAGE +
                    "<h3>사용자휴대폰번호 (UNIQUE)</h3>" +
                    USER_PHONE_NUMBER_MESSAGE +
                    "<h3>로그인사용자이름 (UNIQUE)</h3>" +
                    LOGIN_USER_NAME_MESSAGE +
                    "<h3>로그인사용자비밀번호</h3>" +
                    RAW_LOGIN_USER_PASSWORD_MESSAGE +
                    "<h3>사용자동의항목 - 코드</h3>" +
                    "<ul>" +
                    "<li>개인정보 수집 및 이용 동의(필수) - 0 </li>" +
                    "<li>개인정보 수집 및 이용 동의 (선택) - 1 </li>" +
                    "<li>이용약관 (필수) -2 </li>" +
                    "<li>SMS 수신 (선택) - 3 </li>" +
                    "<li>푸시알림 수신 (선택) - 4 </li>" +
                    "<li>이메일 수신 (선택) - 5 </li>" +
                    "</ul>" +
                    "<h3>사용자개인정보사용기간 - 코드</h3>" +
                    "코드가 초 단위로 표시됩니다. <br/>" +
                    "<ul>" +
                    "<li>1년 - 31536000</li>" +
                    "<li>3년 - 94608000</li>" +
                    "<li>5년 - 157680000</li>" +
                    "<li>평생회원 - 0</li>" +
                    "</ul>",
            content = @Content(schema = @Schema(implementation = CreateUserRequestDTO.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공 및 accessToken 발급",
                    content = @Content(schema = @Schema(implementation = AccessTokenResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "필수값 누락, 잘못된 형식의 입력, 중복된 값 입력",
                    content = @Content(schema = @Schema(example = "{\n" +
                            "    \"errors\": {\n" +
                            "        \"user.userPersonName\": \"2~30자의 이름을 입력해주세요.\",\n" +
                            "        \"userLoginCredentials.loginUserName\": \"영문, 숫자, 한글, 특수문자로 4~20자를 입력해주세요.\"\n" +
                            "    }\n" +
                            "}")))
    })
    public @interface CreateUserDocs { }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "사용자로그인자격증명으로 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공 및 accessToken 발급",
                    content = @Content(schema = @Schema(implementation = AccessTokenResponseDTO.class))),
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
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공",
                    content = @Content(schema = @Schema(implementation = MyUserDTO.class))),
            @ApiResponse(responseCode = "401", description = "잘못된 토큰",
                    content = @Content(schema = @Schema(example = "로그인이 필요합니다."))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(schema = @Schema(example = USER_NOT_FOUND_MESSAGE)))
    })
    public @interface GetMyUserDocs { }


    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "사용자 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공",
                    content = @Content(schema = @Schema(implementation = OtherUserDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(schema = @Schema(example = USER_NOT_FOUND_MESSAGE)))
    })
    public @interface GetOtherUserDocs { }
}
