package com.junyounggoat.dreamstore.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest
public class UserControllerTests {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(UserControllerTests.class);

    private final Map<String, String> createUserRequestJsonString = Map.of(
            "user_login_credentials", "{ \"user\": { \"user_person_name\": \"테스터1\", \"user_email_address\": \"tester1@example.com\", \"user_phone_number\": \"01000000001\" }, \"user_login_category_code_id\": 1, \"user_agreement_item_code_id_list\": [1, 2, 3], \"user_login_credentials\": { \"login_user_name\": \"tester1\", \"login_user_password\": \"testerPassword&#122\" }, \"user_privacy_usage_period\": { \"usage_start_date_time\": \"2023-09-10 14:14:00\", \"usage_end_date_time\": \"2024-09-09 23:59:59\" } }",
            "kakao_user", "{ \"user\": { \"user_person_name\": \"테스터1\", \"user_email_address\": \"tester1@example.com\", \"user_phone_number\": \"01000000001\" }, \"user_login_category_code_id\": 1, \"user_agreement_item_code_id_list\": [1, 2, 3], \"kakao_user\": {}, \"user_privacy_usage_period\": { \"usage_start_date_time\": \"2023-09-10 14:14:00\", \"usage_end_date_time\": \"2024-09-09 23:59:59\" } }",
            "naver_user", "{ \"user\": { \"user_person_name\": \"테스터1\", \"user_email_address\": \"tester1@example.com\", \"user_phone_number\": \"01000000001\" }, \"user_login_category_code_id\": 1, \"user_agreement_item_code_id_list\": [1, 2, 3], \"naver_user\": {}, \"user_privacy_usage_period\": { \"usage_start_date_time\": \"2023-09-10 14:14:00\", \"usage_end_date_time\": \"2024-09-09 23:59:59\" } }"
    );

    @BeforeEach
    private void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    private ResultActions createUser(String requestData) throws Exception {
        logger.debug("assertRequiredFieldResponse > requestData : " + requestData);

        // ToDo: 에러 메시지 검증
        return this.mockMvc.perform(post("/api/v1/user")
                        .content(requestData)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions assertCreateUserBadRequest(String requestData) {
        try {
            return createUser(requestData)
                    .andExpect(status().isBadRequest())
                    .andDo(document("createUser"));
        } catch (Exception e) {
            // TIL: RuntimeException을 던져도 테스트 실패함
            throw new RuntimeException(e);
        }
    }

    private String removeJsonKey(String jsonString, String removeKey)
    {
        DocumentContext documentContext = JsonPath.parse(jsonString);
        documentContext.delete(removeKey);

        return documentContext.jsonString();
    }

    private String updateJsonValue(String jsonString, String key, Object value)
    {
        DocumentContext documentContext = JsonPath.parse(jsonString);
        documentContext.set(key, value);

        return documentContext.jsonString();
    }

    @Test
    @DisplayName("사용자 생성 시 필수 값 검증")
    void createUserValidateRequiredFields() throws Exception {
        // ToDo: enum으로 바꿀 수 있을지
        String jsonString = createUserRequestJsonString.get("user_login_credentials");
        Map<String, Object> requiredFieldsBlankValueMap = Map.of(
                "$.user.user_person_name", "",
                "$.user.user_email_address", "",
                "$.user.user_phone_number", ""
        );
        List<String> requiredFieldsRemoveKeyList = List.of(
                "$.user.user_person_name",
                "$.user.user_email_address",
                "$.user.user_phone_number",
                "$.user_login_category_code_id",
                "$.user_agreement_item_code_id_list",
                "$.user_privacy_usage_period",
                "$.user_privacy_usage_period.usage_end_date_time",
                "$.user_privacy_usage_period.usage_start_date_time"
        );

        List<String> requestDataByUpdatingKeys = requiredFieldsBlankValueMap
                .entrySet()
                .stream()
                .map(jsonKeyValueEntry -> updateJsonValue(jsonString, jsonKeyValueEntry.getKey(), jsonKeyValueEntry.getValue()))
                .toList();
        List<String> requestDataByRemovingKeys = requiredFieldsRemoveKeyList
                .stream().map(removeKey -> removeJsonKey(jsonString, removeKey)).toList();

        Stream.concat(requestDataByUpdatingKeys.stream(), requestDataByRemovingKeys.stream())
                .forEach(this::assertCreateUserBadRequest);
    }

    @Test
    @DisplayName("로그인사용자이름으로 사용자 생성 시 필수 값 검증")
    void createUserByLoginUserNameValidateRequiredFields() {
        String jsonString = createUserRequestJsonString.get("user_login_credentials");
        Map<String, Object> requiredFieldsBlankValueMap = Map.of(
                "$.user_login_credentials.login_user_name", "",
                "$.user_login_credentials.login_user_password", ""
        );
        List<String> requiredFieldsRemoveKeyList = List.of(
                "$.user_login_credentials",
                "$.user_login_credentials.login_user_name",
                "$.user_login_credentials.login_user_password"
        );


        List<String> requestDataByUpdatingKeys = requiredFieldsBlankValueMap
                .entrySet()
                .stream()
                .map(jsonKeyValueEntry -> updateJsonValue(jsonString, jsonKeyValueEntry.getKey(), jsonKeyValueEntry.getValue()))
                .toList();
        List<String> requestDataByRemovingKeys = requiredFieldsRemoveKeyList
                .stream().map(removeKey -> removeJsonKey(jsonString, removeKey)).toList();

        Stream.concat(requestDataByUpdatingKeys.stream(), requestDataByRemovingKeys.stream())
                .forEach(this::assertCreateUserBadRequest);
    }

    @Test
    @DisplayName("카카오사용자로 사용자 생성 시 필수 값 검증")
    void createUserByKakaoUserValidateRequiredFields() throws Exception {
        String jsonString = createUserRequestJsonString.get("kakao_user");
        List<String> requiredFieldsRemoveKeyList = List.of(
                "$.kakao_user"
        );

        List<String> requestDataByRemovingKeys = requiredFieldsRemoveKeyList
                .stream().map(removeKey -> removeJsonKey(jsonString, removeKey)).toList();

        requestDataByRemovingKeys
                .forEach(this::assertCreateUserBadRequest);
    }

    @Test
    @DisplayName("네이버사용자로 사용자 생성 시 필수 값 검증")
    void createUserByNaverUserValidateRequiredFields() throws Exception {
        String jsonString = createUserRequestJsonString.get("naver_user");
        List<String> requiredFieldsRemoveKeyList = List.of(
                "$.naver_user"
        );

        List<String> requestDataByRemovingKeys = requiredFieldsRemoveKeyList
                .stream().map(removeKey -> removeJsonKey(jsonString, removeKey)).toList();

        requestDataByRemovingKeys
                .forEach(this::assertCreateUserBadRequest);
    }

    @Test
    @DisplayName("사용자 생성 시 중복된 사용자 검증")
    void createUserValidateDuplicatedUser() {
        String jsonString = createUserRequestJsonString.get("user_login_credentials");
    }

    @Test
    @DisplayName("사용자 생성 시 사용자 입력형식 검증")
    void createUserValidateMalformedUser() {

    }

    @Test
    @DisplayName("사용자 생성 시 사용자 로그인 자격증명 입력형식 검증")
    void createUserValidateMalformedUserLoginCredentials() {

    }

    @Test
    @DisplayName("사용자 생성 시 성공 응답 JWT 검증")
    void createUserSuccessResponseIncludesJwt() {

    }
}
