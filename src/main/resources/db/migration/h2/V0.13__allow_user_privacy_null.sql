ALTER TABLE `user`
    ALTER COLUMN `user_email_address` varchar(320) NULL;

CREATE UNIQUE INDEX `user_email_address_UNIQUE` ON `user` (`user_email_address`);

ALTER TABLE `user`
    ALTER COLUMN `user_phone_number` char(15) NULL;

ALTER TABLE `user_login_credentials`
    ALTER COLUMN `login_user_password` VARCHAR(72) NULL;