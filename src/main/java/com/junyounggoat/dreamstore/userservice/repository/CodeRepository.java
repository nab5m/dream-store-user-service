package com.junyounggoat.dreamstore.userservice.repository;

import com.junyounggoat.dreamstore.userservice.constant.CodeGroupName;
import com.junyounggoat.dreamstore.userservice.entity.*;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CodeRepository {
    private final JPAQueryFactory queryFactory;
    private final QCodeCategory qCodeCategory = QCodeCategory.codeCategory;
    private final QCodeItem qCodeItem = QCodeItem.codeItem;
    private final Predicate qCodeItemIsNotDeleted = qCodeItem.timestamp.deletionDateTime.isNull();
    private final QCodeGroup qCodeGroup = QCodeGroup.codeGroup;
    private final Predicate qCodeGroupIsNotDeleted = qCodeGroup.timestamp.deletionDateTime.isNull();
    private final QCodeGroupItem qCodeGroupItem = QCodeGroupItem.codeGroupItem;
    private final Predicate qCodeGroupItemIsNOtDeleted = qCodeGroupItem.timestamp.deletionDateTime.isNull();

    public List<Integer> findCodeListByCodeGroupName(CodeGroupName codeGroupName) {
        return queryFactory.selectFrom(qCodeGroupItem)
                .select(qCodeItem.code)
                .innerJoin(qCodeGroupItem.codeGroup, qCodeGroup)
                .innerJoin(qCodeGroupItem.codeItem, qCodeItem)
                .where(qCodeGroup.codeGroupName.eq(codeGroupName.getName())
                        .and(qCodeGroupIsNotDeleted)
                        .and(qCodeGroupItemIsNOtDeleted)
                        .and(qCodeItemIsNotDeleted))
                .fetch().stream().toList();
    }
}
