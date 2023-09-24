CREATE UNIQUE INDEX `user_phone_number_UNIQUE` ON `user` (`user_phone_number`);

CREATE UNIQUE INDEX `login_user_name_UNIQUE` ON `user_login_credentials` (`login_user_name`);
