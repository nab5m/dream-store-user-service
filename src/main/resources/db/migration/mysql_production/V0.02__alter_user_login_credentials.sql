-- NOT NULL 설정
ALTER TABLE user_login_credentials
    MODIFY COLUMN login_user_password VARCHAR(72) NOT NULL;