ALTER TABLE `user`
    MODIFY COLUMN `user_email_address` varchar(320) NULL UNIQUE;

ALTER TABLE `user`
    MODIFY COLUMN `user_phone_number` char(15) NULL UNIQUE;

ALTER TABLE `user_login_credentials`
    MODIFY COLUMN `login_user_password` VARCHAR(72) NULL;