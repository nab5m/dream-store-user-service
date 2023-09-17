ALTER TABLE `user`
ADD UNIQUE INDEX `user_phone_number_UNIQUE` (`user_phone_number` ASC) VISIBLE;

ALTER TABLE `user_login_credentials`
ADD UNIQUE INDEX `login_user_name_UNIQUE` (`login_user_name` ASC) VISIBLE;
