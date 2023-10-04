package com.junyounggoat.dreamstore.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.junyounggoat.dreamstore.userservice.config.EmbeddedRedisConfig;
import com.junyounggoat.dreamstore.userservice.constant.CodeGroupName;
import com.junyounggoat.dreamstore.userservice.constant.UserLoginCategoryCode;
import com.junyounggoat.dreamstore.userservice.dto.BadRequestDTO;
import com.junyounggoat.dreamstore.userservice.dto.OtherUserDTO;
import com.junyounggoat.dreamstore.userservice.dto.TokenResponseDTO;
import com.junyounggoat.dreamstore.userservice.entity.User;
import com.junyounggoat.dreamstore.userservice.repository.CodeRepository;
import com.junyounggoat.dreamstore.userservice.repository.UserRepository;
import com.junyounggoat.dreamstore.userservice.service.TokenService;
import com.junyounggoat.dreamstore.userservice.validation.CodeExistValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.junyounggoat.dreamstore.userservice.controller.UserController.USER_NOT_FOUND_MESSAGE;
import static com.junyounggoat.dreamstore.userservice.validation.UserLoginCredentialsValidation.LOGIN_USER_NAME_MAX_LENGTH;
import static com.junyounggoat.dreamstore.userservice.validation.UserLoginCredentialsValidation.LOGIN_USER_NAME_MIN_LENGTH;
import static com.junyounggoat.dreamstore.userservice.validation.UserValidation.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@Transactional
@Import({ EmbeddedRedisConfig.class })
public class UserControllerTests {
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CodeRepository codeRepository;
    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(UserControllerTests.class);

    private final Map<UserLoginCategoryCode, String> createUserRequestJsonString = Map.of(
            UserLoginCategoryCode.userLoginCredentials, "{\n" +
                    "    \"user\": {\n" +
                    "        \"userPersonName\": \"테스터\",\n" +
                    "        \"userEmailAddress\": \"tester1@example.com\",\n" +
                    "        \"userPhoneNumber\": \"01000000001\"\n" +
                    "    },\n" +
                    "    \"userLoginCredentials\": {\n" +
                    "        \"loginUserName\": \"테스터1\",\n" +
                    "        \"rawLoginUserPassword\": \"tester123%!!\"\n" +
                    "    },\n" +
                    "    \"userAgreementItemCodeList\": [0, 1, 2],\n" +
                    "    \"userPrivacyUsagePeriodCode\": 31536000\n" +
                    "}"
    );

    @BeforeEach
    private void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    private ResultActions requestPost(String endpoint, String requestData) throws Exception {
        logger.debug("requestPost > requestData : " + requestData);

        // ToDo: 에러 메시지 검증
        return this.mockMvc.perform(post(endpoint)
                .content(requestData)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private long createTestUser() throws Exception {
        String requestData = createUserRequestJsonString.get(UserLoginCategoryCode.userLoginCredentials);

        MvcResult mvcResult = this.mockMvc.perform(post("/api/v1/user")
                .content(requestData)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        return getUserIdFromTokenResponseDTO(mvcResult);
    }


    /* ToDo: UserLoginCategoryCode에 따라 endPoint와 documentName 가져오도록 수정 */
    private ResultActions assertPostRequestBadRequest(String endPoint, String requestData) {
        try {
            return requestPost(endPoint, requestData)
                    .andExpect(status().isBadRequest())
                    .andDo(document("createUser", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
        } catch (Exception e) {
            // TIL: RuntimeException을 던져도 테스트 실패함
            throw new RuntimeException(e);
        }
    }

    private ResultActions assertPostRequestCreated(String endPoint, String requestData) {
        try {
            return requestPost(endPoint, requestData)
                    .andExpect(status().isCreated())
                    .andDo(document("createUser", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
        } catch (Exception e) {
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

    private long getUserIdFromTokenResponseDTO(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        TokenResponseDTO tokenResponseDTO = objectMapper.readValue(responseBody, TokenResponseDTO.class);

        return ((Integer) TokenService.getClaims(tokenResponseDTO.getAccessToken()).get("userId")).longValue();
    }

    @Test
    @DisplayName("사용자 생성 시 필수 값 검증")
    void createUserValidateRequiredFields() {
        String jsonString = createUserRequestJsonString.get(UserLoginCategoryCode.userLoginCredentials);
        Map<String, Object> requiredFieldsBlankValueMap = Map.of(
                "$.user.userPersonName", "",
                "$.user.userEmailAddress", "",
                "$.user.userPhoneNumber", "",
                "$.userAgreementItemCodeList", "[]",
                "$.userPrivacyUsagePeriodCode", ""
        );
        List<String> requiredFieldsRemoveKeyList = List.of(
                "$.user.userPersonName",
                "$.user.userEmailAddress",
                "$.user.userPhoneNumber",
                "$.userAgreementItemCodeList",
                "$.userPrivacyUsagePeriodCode"
        );

        List<String> requestDataByUpdatingKeys = requiredFieldsBlankValueMap
                .entrySet()
                .stream()
                .map(jsonKeyValueEntry -> updateJsonValue(jsonString, jsonKeyValueEntry.getKey(), jsonKeyValueEntry.getValue()))
                .toList();
        List<String> requestDataByRemovingKeys = requiredFieldsRemoveKeyList
                .stream().map(removeKey -> removeJsonKey(jsonString, removeKey)).toList();

        Stream.concat(requestDataByUpdatingKeys.stream(), requestDataByRemovingKeys.stream())
                .forEach((requestData) -> assertPostRequestBadRequest("/api/v1/user", requestData));
    }

    @Test
    @DisplayName("사용자로그인자격증명으로 사용자 생성 시 필수 값 검증")
    void createUserByLoginUserNameValidateRequiredFields() {
        String jsonString = createUserRequestJsonString.get(UserLoginCategoryCode.userLoginCredentials);
        Map<String, Object> requiredFieldsBlankValueMap = Map.of(
                "$.userLoginCredentials.loginUserName", "",
                "$.userLoginCredentials.rawLoginUserPassword", ""
        );
        List<String> requiredFieldsRemoveKeyList = List.of(
                "$.userLoginCredentials",
                "$.userLoginCredentials.loginUserName",
                "$.userLoginCredentials.rawLoginUserPassword"
        );


        List<String> requestDataByUpdatingKeys = requiredFieldsBlankValueMap
                .entrySet()
                .stream()
                .map(jsonKeyValueEntry -> updateJsonValue(jsonString, jsonKeyValueEntry.getKey(), jsonKeyValueEntry.getValue()))
                .toList();
        List<String> requestDataByRemovingKeys = requiredFieldsRemoveKeyList
                .stream().map(removeKey -> removeJsonKey(jsonString, removeKey)).toList();

        Stream.concat(requestDataByUpdatingKeys.stream(), requestDataByRemovingKeys.stream())
                .forEach((requestData) -> assertPostRequestBadRequest("/api/v1/user", requestData));
    }

    @Test
    @DisplayName("사용자 생성 시 중복된 사용자 검증")
    void createUserValidateDuplicatedUser() throws Exception {
        String jsonString = createUserRequestJsonString.get(UserLoginCategoryCode.userLoginCredentials);
        assertPostRequestCreated("/api/v1/user", jsonString);

        Map<String, String> notDuplicatedUserValues = Map.of(
                "$.user.userEmailAddress", "new_tester@example.com",
                "$.user.userPhoneNumber", "01000000002",
                "$.userLoginCredentials.loginUserName", "new_tester"
        );
        List<Map.Entry<String, String>> notDuplicatedUserValueEntries = notDuplicatedUserValues.entrySet().stream().toList();

        // 하나의 필드만 중복된 상태로 요청
        for (int excludingIdx = 0; excludingIdx < notDuplicatedUserValueEntries.size(); excludingIdx++) {
            String requestData = jsonString;
            for (int currentIdx = 0; currentIdx  < notDuplicatedUserValueEntries.size(); currentIdx++) {
                if (excludingIdx == currentIdx) {
                    continue;
                }

                Map.Entry<String, String> jsonKeyValue = notDuplicatedUserValueEntries.get(currentIdx);
                requestData = updateJsonValue(requestData, jsonKeyValue.getKey(), jsonKeyValue.getValue());
            }

            assertPostRequestBadRequest("/api/v1/user", jsonString);
        }

        // 모든 필드를 중복 제거 후 요청
        String notDuplicatedUserRequestData = jsonString;
        for (int currentIdx = 0; currentIdx < notDuplicatedUserValueEntries.size(); currentIdx++) {
            Map.Entry<String, String> jsonKeyValue = notDuplicatedUserValueEntries.get(currentIdx);
            notDuplicatedUserRequestData = updateJsonValue(notDuplicatedUserRequestData, jsonKeyValue.getKey(), jsonKeyValue.getValue());
        }
        assertPostRequestCreated("/api/v1/user", notDuplicatedUserRequestData);
    }

    @Test
    @DisplayName("사용자 생성 시 사용자 입력형식 검증")
    void createUserValidateMalformedUser() {
        String jsonString = createUserRequestJsonString.get(UserLoginCategoryCode.userLoginCredentials);
        /*
        사용자사람이름: 글자수 제한 (최소, 최대)
        사용자이메일주소: 이메일주소 형식 ^[\w._%+-]+@[\w._-]+\.[\w]{2,}$ 에 맞는지, 최대 글자 320자
        사용자휴대폰번호: 휴대폰번호 형식 ^\d{8,15}$ 에 맞는지
        ToDo: 사용자동의항목: 필수값이 모두 포함되어 있는지 여부
         */
        Map<String, List<String>> malformedUserRequestDataMap = Map.of(
                "$.user.userPersonName", List.of(
                        "김".repeat(USER_PERSON_NAME_MIN_LENGTH - 1),
                        "김".repeat(USER_PERSON_NAME_MAX_LENGTH + 1)),
                "$.user.userEmailAddress", List.of(
                        "tester@co." + "a".repeat(USER_EMAIL_ADDRESS_MAX_LENGTH),
                        "tester@com",
                        "@com.com",
                        "tester@t.t"),
                "$.user.userPhoneNumber", List.of(
                        "1".repeat(USER_PHONE_NUMBER_MIN_LENGTH - 1),
                        "0".repeat(USER_PHONE_NUMBER_MAX_LENGTH + 1))
        );

        malformedUserRequestDataMap.entrySet().forEach(jsonKeyValue -> {
            List<String> values = jsonKeyValue.getValue();
            values.forEach(value -> {
                String malformedUserRequestData = updateJsonValue(jsonString, jsonKeyValue.getKey(), value);
                assertPostRequestBadRequest("/api/v1/user", malformedUserRequestData);
            });
        });
    }

    @Test
    @DisplayName("사용자 생성 시 사용자 로그인 자격증명 입력형식 검증")
    void createUserValidateMalformedUserLoginCredentials() {
        String jsonString = createUserRequestJsonString.get(UserLoginCategoryCode.userLoginCredentials);
        /*
        로그인사용자아이디: 영문, 숫자, 한글, 특수문자로 4~30자
            ^[\w가-힣\{\}\[\]\/?.,;:|\)*~`!^\-+<>@\#$%&\\\=\(\'\"]{4,30}$ 에 맞는지
        로그인사용자비밀번호: 영문, 숫자, 특수문자 조합으로 8~50자
            ^(?=.*[a-zA-Z])(?=.*[\{\}\[\]\/?.,;:|\)*~`!^\-+<>@\#$%&\\\=\(\'\"])(?=.*[0-9]).{8,50}$ 에 맞는지
         */
        Map<String, List<String>> malformedUserRequestDataMap = Map.of(
                "$.userLoginCredentials.loginUserName", List.of(
                        "김".repeat(LOGIN_USER_NAME_MIN_LENGTH - 1),
                        "김".repeat(LOGIN_USER_NAME_MAX_LENGTH + 1)),
                "$.userLoginCredentials.rawLoginUserPassword", List.of(
                        "a11kc##",
                        "a11kc##123".repeat(5) + "1",
                        "asdfasdf123123",
                        "asdfasdfasdf!@#!@#!@#",
                        "12312355!@#%^^@(*&(^&")
        );

        malformedUserRequestDataMap.entrySet().forEach(jsonKeyValue -> {
            List<String> values = jsonKeyValue.getValue();
            values.forEach(value -> {
                String malformedUserRequestData = updateJsonValue(jsonString, jsonKeyValue.getKey(), value);
                assertPostRequestBadRequest("/api/v1/user", malformedUserRequestData);
            });
        });
    }

    @Test
    @DisplayName("사용자 생성 시 성공 응답 JWT 검증")
    void createUserSuccessResponseIncludesJwt() throws UnsupportedEncodingException, JsonProcessingException {
        String jsonString = createUserRequestJsonString.get(UserLoginCategoryCode.userLoginCredentials);
        MvcResult mvcResult = assertPostRequestCreated("/api/v1/user", jsonString).andReturn();
        long userId = getUserIdFromTokenResponseDTO(mvcResult);
        assertTrue(userId > 0, "jwt의 userId가 비정상적입니다.");
    }

    private void createUserValidateCodeExists(final String fieldName, final String requestData) throws JsonProcessingException, UnsupportedEncodingException {
        BadRequestDTO expectedResponse = BadRequestDTO.builder()
                .fieldErrors(Map.of(fieldName, CodeExistValidator.ERROR_MESSAGE))
                .notFieldErrors(List.of())
                .build();

        String responseBody = assertPostRequestBadRequest("/api/v1/user", requestData)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        logger.debug("responseBody : " + responseBody);

        BadRequestDTO response = objectMapper.readValue(responseBody, BadRequestDTO.class);
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("사용자 생성 시 사용자개인정보사용기간코드 검증")
    void createUserValidateUserPrivacyUsagePeriodCode() throws UnsupportedEncodingException, JsonProcessingException {
        String fieldName = "userPrivacyUsagePeriodCode";

        String jsonString = createUserRequestJsonString.get(UserLoginCategoryCode.userLoginCredentials);
        int nonValidCode = -100;
        String requestData = updateJsonValue(jsonString,
                "$." + fieldName,
                nonValidCode);

        createUserValidateCodeExists(fieldName, requestData);
    }

    @Test
    @DisplayName("사용자 생성 시 사용자동의항목코드 검증")
    void createUserValidateRequiredUserAgreementItemCodeList() throws UnsupportedEncodingException, JsonProcessingException {
        String fieldName = "userAgreementItemCodeList";

        List<Integer> baseCodeList = codeRepository.findCodeListByCodeGroupName(CodeGroupName.REQUIRED_USER_AGREEMENT_ITEM.getName());

        String jsonString = createUserRequestJsonString.get(UserLoginCategoryCode.userLoginCredentials);
        int nonValidCode = -100;
        String requestData = updateJsonValue(jsonString,
                "$." + fieldName,
                Stream.concat(baseCodeList.stream(), Stream.of(nonValidCode)).toList());

        createUserValidateCodeExists(fieldName, requestData);
    }

    @Test
    @DisplayName("사용자 조회 성공")
    void getOtherUserSuccess() throws Exception {
        long userId = createTestUser();
        User user = userRepository.findUserByUserId(userId);

        OtherUserDTO expectedResponse = OtherUserDTO.builder()
                .user(user)
                .build();

        String responseBody = this.mockMvc.perform(get("/api/v1/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        OtherUserDTO response = objectMapper.readValue(responseBody, OtherUserDTO.class);
        assertEquals(response, expectedResponse);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 조회")
    void getOtherUserNotFound() throws Exception {
        this.mockMvc.perform(get("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}