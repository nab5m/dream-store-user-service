package com.junyounggoat.dreamstore.userservice.repository;

import com.junyounggoat.dreamstore.userservice.entity.*;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.*;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CodeRepository {
    private final JPAQueryFactory queryFactory;
    private final QCodeCategory qCodeCategory = QCodeCategory.codeCategory;
    private final BooleanExpression qCodeCategoryIsNotDeleted = qCodeCategory.timestamp.deletionDateTime.isNull();
    private final QCodeItem qCodeItem = QCodeItem.codeItem;
    private final BooleanExpression qCodeItemIsNotDeleted = qCodeItem.timestamp.deletionDateTime.isNull();
    private final QCodeGroup qCodeGroup = QCodeGroup.codeGroup;
    private final BooleanExpression qCodeGroupIsNotDeleted = qCodeGroup.timestamp.deletionDateTime.isNull();
    private final QCodeGroupItem qCodeGroupItem = QCodeGroupItem.codeGroupItem;
    private final BooleanExpression qCodeGroupItemIsNOtDeleted = qCodeGroupItem.timestamp.deletionDateTime.isNull();

    public List<Integer> findCodeListByCodeGroupName(String codeGroupName) {
        return queryFactory.selectFrom(qCodeGroupItem)
                .select(qCodeItem.code)
                .innerJoin(qCodeGroupItem.codeGroup, qCodeGroup)
                .innerJoin(qCodeGroupItem.codeItem, qCodeItem)
                .where(qCodeGroup.codeGroupName.eq(codeGroupName)
                        .and(qCodeGroupIsNotDeleted)
                        .and(qCodeGroupItemIsNOtDeleted)
                        .and(qCodeItemIsNotDeleted))
                .fetch().stream().toList();
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CodeCategoryNameAndCodeName {
        private String codeCategoryName;
        private Integer code;
    }
    public List<CodeCategoryNameAndCodeName> findCodeItemListByCodeCategoryNameAndCodeName(List<CodeCategoryNameAndCodeName> codeCategoryNameAndCodeNameList) {
        List<StringTemplate> tuples = new LinkedList<>();
        codeCategoryNameAndCodeNameList.forEach(codeCategoryNameAndCodeName -> {
            tuples.add(Expressions.stringTemplate( "(( {0}, {1} ))",
                    codeCategoryNameAndCodeName.getCodeCategoryName(), codeCategoryNameAndCodeName.getCode()));
        });

        return queryFactory.selectFrom(qCodeItem)
                .select(Projections.constructor(CodeCategoryNameAndCodeName.class, qCodeCategory.codeCategoryName, qCodeItem.code))
                .innerJoin(qCodeItem.codeCategory, qCodeCategory)
                .where(qCodeItemIsNotDeleted
                        .and(qCodeCategoryIsNotDeleted)
                        .and(Expressions.list(qCodeCategory.codeCategoryName, qCodeItem.code)
                                .in(tuples.toArray(new Expression[0]))))
                .fetch().stream().toList();
    }
}
