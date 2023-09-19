package com.junyounggoat.dreamstore.userservice.validation;

import com.junyounggoat.dreamstore.userservice.repository.CodeRepository;
import com.junyounggoat.dreamstore.userservice.repository.CodeRepository.CodeCategoryNameAndCodeName;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CodeExistValidator implements Validator {
    private final CodeRepository codeRepository;

    @Builder
    @Getter
    public static class Target {
        private List<TargetCodeItem> targetCodeList;
    }

    @Builder
    @Getter
    public static class TargetCodeItem {
        private String field;
        private CodeCategoryNameAndCodeName codeItem;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Target.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object targetObject, Errors errors) {
        Target target = (Target) targetObject;

        List<TargetCodeItem> targetCodeList = target.getTargetCodeList();
        List<CodeCategoryNameAndCodeName> foundCodeItemList =
                codeRepository.findCodeItemListByCodeCategoryNameAndCodeName(targetCodeList.stream().map(TargetCodeItem::getCodeItem).toList());

        Map<String, Map<Integer, List<CodeCategoryNameAndCodeName>>> codeCategoryNameCodeMap = foundCodeItemList.stream()
                .collect(Collectors.groupingBy(
                        CodeCategoryNameAndCodeName::getCodeCategoryName,
                        Collectors.groupingBy(CodeCategoryNameAndCodeName::getCode)));

        String ERROR_CODE = "NotExists";
        String ERROR_MESSAGE = "유효하지 않은 코드 값입니다.";

        targetCodeList.forEach((targetCodeItem) -> {
            String codeCategoryName = targetCodeItem.codeItem.getCodeCategoryName();
            if (!codeCategoryNameCodeMap.containsKey(codeCategoryName) ||
                    !codeCategoryNameCodeMap.get(codeCategoryName).containsKey(targetCodeItem.codeItem.getCode()))
            {
                try {
                    errors.rejectValue(targetCodeItem.getField(), ERROR_CODE, ERROR_MESSAGE);
                } catch (NotReadablePropertyException exception) {
                    errors.reject(ERROR_CODE, ERROR_MESSAGE);
                }
            }
        });
    }
}
