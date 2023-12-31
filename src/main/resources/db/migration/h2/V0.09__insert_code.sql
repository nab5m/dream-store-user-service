BEGIN TRANSACTION;

SET @CODE_CATEGORY_LOGIN_CATEGORY_NAME = '로그인유형';
SET @CODE_CATEGORY_USER_AGREEMENT_NAME = '사용자동의항목';
SET @CODE_CATEGORY_GENDER_NAME = '성별';
SET @CODE_CATEGORY_USER_PRIVACY_USAGE_PERIOD_NAME = '사용자개인정보사용기간';

SET @CODE_GROUP_REQUIRED_USER_AGREEMENT_NAME = '필수 사용자동의항목';

SET @CODE_ITEM_REQUIRED_PRIVACY_COLLECTION_AGREEMENT_NAME = '개인정보 수집 및 이용 동의 (필수)';
SET @CODE_ITEM_TERMS_OF_SERVICE_NAME = '이용약관 (필수)';

INSERT INTO `code_category`(code_category_name)
    VALUES (@CODE_CATEGORY_LOGIN_CATEGORY_NAME),
        (@CODE_CATEGORY_USER_AGREEMENT_NAME),
        (@CODE_CATEGORY_GENDER_NAME),
        (@CODE_CATEGORY_USER_PRIVACY_USAGE_PERIOD_NAME);

SET @CODE_CATEGORY_LOGIN_CATEGORY_ID = SELECT code_category_id FROM `code_category`
    WHERE code_category_name = @CODE_CATEGORY_LOGIN_CATEGORY_NAME;

SET @CODE_CATEGORY_USER_AGREEMENT_ID = SELECT code_category_id FROM `code_category`
    WHERE code_category_name = @CODE_CATEGORY_USER_AGREEMENT_NAME;

SET @CODE_CATEGORY_GENDER_ID = SELECT code_category_id FROM `code_category`
    WHERE code_category_name = @CODE_CATEGORY_GENDER_NAME;

SET @CODE_CATEGORY_USER_PRIVACY_USAGE_PERIOD_ID = SELECT code_category_id FROM `code_category`
    WHERE code_category_name = @CODE_CATEGORY_USER_PRIVACY_USAGE_PERIOD_NAME;

INSERT INTO `code_item`(code_category_id, code_name, code, sort_priority)
    VALUES (@CODE_CATEGORY_LOGIN_CATEGORY_ID, '로그인 사용자 이름, 비밀번호', 0, 0),
        (@CODE_CATEGORY_LOGIN_CATEGORY_ID, 'SMS 간편인증', 1, 0),
        (@CODE_CATEGORY_LOGIN_CATEGORY_ID, '이메일주소 간편인증', 2, 0),
        (@CODE_CATEGORY_LOGIN_CATEGORY_ID, '카카오사용자', 3, 0),
        (@CODE_CATEGORY_LOGIN_CATEGORY_ID, '네이버사용자', 4, 0),
        (@CODE_CATEGORY_USER_AGREEMENT_ID, @CODE_ITEM_REQUIRED_PRIVACY_COLLECTION_AGREEMENT_NAME, 0, 2),
        (@CODE_CATEGORY_USER_AGREEMENT_ID, '개인정보 수집 및 이용 동의 (선택)', 1, 1),
        (@CODE_CATEGORY_USER_AGREEMENT_ID, @CODE_ITEM_TERMS_OF_SERVICE_NAME, 2, 3),
        (@CODE_CATEGORY_USER_AGREEMENT_ID, 'SMS 수신 (선택)', 3, 0),
        (@CODE_CATEGORY_USER_AGREEMENT_ID, '푸시알림 수신 (선택)', 4, 0),
        (@CODE_CATEGORY_USER_AGREEMENT_ID, '이메일 수신 (선택)', 5, 0),
        (@CODE_CATEGORY_GENDER_ID, '남자', 0, 0),
        (@CODE_CATEGORY_GENDER_ID, '여자', 1, 0),
        (@CODE_CATEGORY_USER_PRIVACY_USAGE_PERIOD_ID, '1년', 31536000, 0),
        (@CODE_CATEGORY_USER_PRIVACY_USAGE_PERIOD_ID, '3년', 94608000, 0),
        (@CODE_CATEGORY_USER_PRIVACY_USAGE_PERIOD_ID, '5년', 157680000, 0),
        (@CODE_CATEGORY_USER_PRIVACY_USAGE_PERIOD_ID, '평생회원', 0, 0);

INSERT INTO `code_group`(code_group_name)
    VALUES (@CODE_GROUP_REQUIRED_USER_AGREEMENT_NAME);

SET @CODE_GROUP_REQUIRED_USER_AGREEMENT_ID = SELECT code_group_id FROM `code_group`
    WHERE code_group_name = @CODE_GROUP_REQUIRED_USER_AGREEMENT_NAME;

SET @CODE_ITEM_REQUIRED_PRIVACY_COLLECTION_AGREEMENT_ID = SELECT code_item_id FROM `code_item`
    WHERE code_name = @CODE_ITEM_REQUIRED_PRIVACY_COLLECTION_AGREEMENT_NAME;
SET @CODE_ITEM_TERMS_OF_SERVICE_ID = SELECT code_item_id FROM `code_item`
    WHERE code_name = @CODE_ITEM_TERMS_OF_SERVICE_NAME;

INSERT INTO `code_group_item`(code_group_id, code_item_id)
    VALUES (@CODE_GROUP_REQUIRED_USER_AGREEMENT_ID, @CODE_ITEM_REQUIRED_PRIVACY_COLLECTION_AGREEMENT_ID),
        (@CODE_GROUP_REQUIRED_USER_AGREEMENT_ID, @CODE_ITEM_TERMS_OF_SERVICE_ID);

COMMIT;