ALTER TABLE naver_user
    ALTER COLUMN naver_user_id int NOT NULL AUTO_INCREMENT;

ALTER TABLE kakao_user
    ALTER COLUMN kakao_user_id int NOT NULL AUTO_INCREMENT;

ALTER TABLE user_agreement_item
    ALTER COLUMN user_agreement_item_id int NOT NULL AUTO_INCREMENT;

ALTER TABLE user_login_block_period
    ALTER COLUMN user_login_block_period_id int NOT NULL AUTO_INCREMENT;