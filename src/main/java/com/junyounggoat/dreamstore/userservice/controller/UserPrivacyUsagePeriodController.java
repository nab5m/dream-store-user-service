package com.junyounggoat.dreamstore.userservice.controller;

import com.junyounggoat.dreamstore.userservice.constant.CodeCategoryName;
import com.junyounggoat.dreamstore.userservice.service.TokenService;
import com.junyounggoat.dreamstore.userservice.swagger.UserPrivacyUsagePeriodControllerDocs;
import com.junyounggoat.dreamstore.userservice.validation.CodeExistValidator;
import com.junyounggoat.dreamstore.userservice.validation.NotValidException;
import com.junyounggoat.dreamstore.userservice.dto.UpdateUserPrivacyUsagePeriodRequestDTO;
import com.junyounggoat.dreamstore.userservice.repository.CodeRepository;
import com.junyounggoat.dreamstore.userservice.service.UserPrivacyUsagePeriodService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-privacy-usage-period")
@Tag(name = "UserPrivacyUsagePeriodController", description = "사용자개인정보사용기간 컨트롤러")
public class UserPrivacyUsagePeriodController {
    private final UserPrivacyUsagePeriodService userPrivacyUsagePeriodService;
    private final CodeExistValidator codeExistValidator;

    @PutMapping("")
    @UserPrivacyUsagePeriodControllerDocs.UpdateUserPrivacyUsagePeriod
    public void updateUserPrivacyUsagePeriod(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestBody @Valid UpdateUserPrivacyUsagePeriodRequestDTO updateUserPrivacyUsagePeriodRequestDTO,
                                             Errors errors)
    {
        NotValidException.throwIfErrorExists(errors);

        List<CodeExistValidator.TargetCodeItem> codeItemList = List.of(
                CodeExistValidator.TargetCodeItem.builder()
                        .field("userPrivacyUsagePeriodCode")
                        .codeItem(CodeRepository.CodeCategoryNameAndCodeName.builder()
                                .codeCategoryName(CodeCategoryName.USER_PRIVACY_USAGE_PERIOD.getName())
                                .code(updateUserPrivacyUsagePeriodRequestDTO.getUserPrivacyUsagePeriodCode())
                                .build())
                        .build()
        );
        CodeExistValidator.validateCodeExists(codeExistValidator, codeItemList, errors);
        NotValidException.throwIfErrorExists(errors);

        Long userId = TokenService.getUserIdFromUserDetails(userDetails);
        userPrivacyUsagePeriodService.updateUserPrivacyUsagePeriod(userId, updateUserPrivacyUsagePeriodRequestDTO);
    }
}
