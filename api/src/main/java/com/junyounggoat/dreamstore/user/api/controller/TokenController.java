package com.junyounggoat.dreamstore.user.api.controller;

import com.junyounggoat.dreamstore.user.api.dto.RefreshTokenDTO;
import com.junyounggoat.dreamstore.user.api.dto.TokenResponseDTO;
import com.junyounggoat.dreamstore.user.repository.redishash.RefreshToken;
import com.junyounggoat.dreamstore.user.api.service.TokenService;
import com.junyounggoat.dreamstore.user.api.swagger.TokenControllerDocs;
import com.junyounggoat.dreamstore.user.api.validation.NotValidException;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
@Tag(name = "RefreshTokenController", description = "리프레시 토큰 컨트롤러")
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/refresh")
    @ResponseStatus(code = HttpStatus.CREATED)
    @TokenControllerDocs.TokenRefreshDocs
    public TokenResponseDTO tokenRefresh(@RequestBody @Valid RefreshTokenDTO tokenRefreshRequestDTO, Errors errors) {
        NotValidException.throwIfErrorExists(errors);

        String refreshToken = tokenRefreshRequestDTO.getRefreshToken();
        Claims claims = TokenService.getClaims(refreshToken);

        if (claims == null) {
            throwNotValidRefreshTokenException(errors);
        }

        RefreshToken token = tokenService.getRefreshToken(refreshToken);
        if (token == null) {
            throwNotValidRefreshTokenException(errors);
        }

        String newAccessToken = tokenService.createAccessToken(token.getUserId());
        return TokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void throwNotValidRefreshTokenException(Errors errors) {
        errors.rejectValue("refreshToken", "InvalidRefreshToken", "리프레시 토큰이 유효하지 않습니다.");
        throw NotValidException.builder().errors(errors).build();
    }

    @DeleteMapping("")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @TokenControllerDocs.DeleteRefreshTokenDocs
    public void deleteRefreshToken(@RequestBody @Valid final RefreshTokenDTO refreshTokenDTO) {
        tokenService.deleteRefreshToken(refreshTokenDTO.getRefreshToken());
    }
}
