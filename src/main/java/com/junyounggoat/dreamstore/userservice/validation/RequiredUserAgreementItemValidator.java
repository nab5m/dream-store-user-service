package com.junyounggoat.dreamstore.userservice.validation;

import com.junyounggoat.dreamstore.userservice.constant.CodeGroupName;
import com.junyounggoat.dreamstore.userservice.repository.CodeRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RequiredUserAgreementItemValidator implements Validator {
    private static final String ERROR_CODE = "RequiredValueOmitted";

    private final CodeRepository codeRepository;

    @Builder
    @Getter
    public static class Target {
        private String field;
        private List<Integer> targetCodeList;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Integer[].class);
    }

    @Override
    public void validate(Object targetObject, Errors errors) {
        Target target = (Target) targetObject;

        List<Integer> baseCodeList = codeRepository.findCodeListByCodeGroupName(CodeGroupName.REQUIRED_USER_AGREEMENT_ITEM);
        if (!new HashSet<>(target.getTargetCodeList()).containsAll(baseCodeList)) {
            String ERROR_MESSAGE = "필수 동의항목을 체크하지 않았습니다.";

            try {
                errors.rejectValue(target.getField(), ERROR_CODE, ERROR_MESSAGE);
            } catch (NotReadablePropertyException exception) {
                errors.reject(ERROR_CODE, ERROR_MESSAGE);
            }
        }
    }

    public static void validateRequiredUserAgreementItem(RequiredUserAgreementItemValidator requiredUserAgreementItemValidator,
                                                         String field, List<Integer> codeList, Errors errors) {
        requiredUserAgreementItemValidator.validate(
                RequiredUserAgreementItemValidator.Target.builder()
                        .field(field)
                        .targetCodeList(codeList)
                        .build(),
                errors
        );
    }
}
