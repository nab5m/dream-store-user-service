package com.junyounggoat.dreamstore.user.api.swagger;

import com.junyounggoat.dreamstore.user.api.config.OpenApiConfig;
import com.junyounggoat.dreamstore.user.api.dto.UpdateUserPrivacyUsagePeriodRequestDTO;
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

import static com.junyounggoat.dreamstore.user.api.swagger.DocumentMessages.USER_PRIVACY_USAGE_PERIOD_CODE_DESCRIPTION;

public abstract class UserPrivacyUsagePeriodControllerDocs {
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "사용자개인정보사용기간 수정")
    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    @RequestBody(
            description = USER_PRIVACY_USAGE_PERIOD_CODE_DESCRIPTION,
            content = @Content(schema = @Schema(implementation = UpdateUserPrivacyUsagePeriodRequestDTO.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자개인정보사용기간 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 사용자개인정보사용기간 코드",
                    content = @Content(schema = @Schema(example = "사용자개인정보사용기간 코드가 유효하지 않습니다."))),
            @ApiResponse(responseCode = "401", description = "로그인하지 않은 사용자",
                    content = @Content(schema = @Schema(example = "로그인이 필요합니다."))),
            @ApiResponse(responseCode = "404", description = "기존 사용자개인정보사용기간 없음",
                    content = @Content(schema = @Schema(example = "기존 사용자개인정보사용기간을 찾을 수 없습니다.")))
    })
    public @interface UpdateUserPrivacyUsagePeriod { }
}
