package com.junyounggoat.dreamstore.userservice.swagger;

import com.junyounggoat.dreamstore.userservice.dto.CreateUserRequestDTO;
import com.junyounggoat.dreamstore.userservice.dto.TokenRefreshRequestDTO;
import com.junyounggoat.dreamstore.userservice.dto.TokenResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class TokenControllerDocs {
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "리프레시 토큰으로 엑세스 토큰 갱신")
    @RequestBody(
            content = @Content(schema = @Schema(implementation = TokenRefreshRequestDTO.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "리프레시 토큰 검증 성공 및 accessToken 재발급",
                    content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 리프레시 토큰",
                    content = @Content(schema = @Schema(example = "{\n" +
                            "    \"fieldErrors\": {\n" +
                            "        \"refreshToken\": \"리프레시 토큰이 유효하지 않습니다.\"\n" +
                            "    },\n" +
                            "    \"notFieldErrors\": []\n" +
                            "}")))
    })
    public @interface TokenRefreshDocs { }
}
