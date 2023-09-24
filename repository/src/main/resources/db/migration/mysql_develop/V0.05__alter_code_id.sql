-- 코드를 사용하는 테이블에서 코드 테이블과 fk로 연결하는 대신 코드 정수값을 그대로 사용하는 것으로 변경. 그에 따른 네이밍 변경 포함
ALTER TABLE `user`
    DROP CONSTRAINT `user_nonmember_flag_code_id`;

ALTER TABLE `user`
    DROP CONSTRAINT `login_block_flag_code_id`;

ALTER TABLE `user`
    DROP CONSTRAINT `user_gender_code_id`;

ALTER TABLE `user`
    RENAME COLUMN user_nonmember_flag_code_id TO user_nonmember_flag;

ALTER TABLE `user`
    MODIFY COLUMN user_nonmember_flag tinyint NOT NULL;

ALTER TABLE `user`
    RENAME COLUMN login_block_flag_code_id TO active_user_login_block_period_id;

ALTER TABLE `user`
    MODIFY COLUMN active_user_login_block_period_id int unsigned;

ALTER TABLE `user`
    RENAME COLUMN user_gender_code_id TO user_gender_code;

ALTER TABLE `user`
    MODIFY COLUMN user_gender_code int;

ALTER TABLE `user`
    ADD CONSTRAINT `active_user_login_block_period_id`
        FOREIGN KEY (`active_user_login_block_period_id`)
        REFERENCES `user_login_block_period` (`user_login_block_period_id`);

ALTER TABLE `user_login_category`
    DROP CONSTRAINT user_login_category_code_id;

ALTER TABLE `user_login_category`
    RENAME COLUMN user_login_category_code_id TO user_login_category_code;

ALTER TABLE `user_login_category`
    MODIFY COLUMN user_login_category_code int NOT NULL;

ALTER TABLE `user_agreement_item`
    DROP CONSTRAINT user_agreement_item_code_id;

ALTER TABLE `user_agreement_item`
    RENAME COLUMN user_agreement_item_code_id TO user_agreement_item_code;

ALTER TABLE `user_agreement_item`
    MODIFY COLUMN user_agreement_item_code int NOT NULL;

ALTER TABLE `user_login_block_period`
    DROP CONSTRAINT user_login_block_reason_code_id;

ALTER TABLE `user_login_block_period`
    RENAME COLUMN user_login_block_reason_code_id TO user_login_block_reason_code;

ALTER TABLE `user_login_block_period`
    MODIFY COLUMN user_login_block_reason_code int NOT NULL;

ALTER TABLE `address`
    DROP CONSTRAINT eupmyeondong_code_id;

ALTER TABLE `address`
    DROP CONSTRAINT sido_code_id;

ALTER TABLE `address`
    DROP CONSTRAINT sigungu_code_id;

ALTER TABLE `address`
    RENAME COLUMN eupmyeondong_code_id TO eupmyeondong_code;

ALTER TABLE `address`
    RENAME COLUMN sido_code_id TO sido_code;

ALTER TABLE `address`
    RENAME COLUMN sigungu_code_id TO sigungu_code;

ALTER TABLE `address`
    MODIFY COLUMN eupmyeondong_code int NOT NULL;

ALTER TABLE `address`
    MODIFY COLUMN sido_code int NOT NULL;

ALTER TABLE `address`
    MODIFY COLUMN sigungu_code int NOT NULL;

ALTER TABLE `code`
    RENAME TO `code_item`;

ALTER TABLE code_item
    ADD COLUMN code int NOT NULL;

ALTER TABLE `code_item`
    RENAME COLUMN code_id TO code_item_id;

ALTER TABLE `code_item`
    RENAME COLUMN parent_code_id TO parent_code_item_id;

ALTER TABLE `code_group_item`
    RENAME COLUMN code_id TO code_item_id;

ALTER TABLE `code_group_item`
    DROP CONSTRAINT code_id;

ALTER TABLE code_group_item
    ADD CONSTRAINT code_item_id FOREIGN KEY (code_item_id) REFERENCES code_item (code_item_id);
