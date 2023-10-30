DROP TABLE `naver_user`;

CREATE TABLE `naver_user` (
    `naver_user_id` INT PRIMARY KEY AUTO_INCREMENT,
    `naver_user_login_category_id` INT NOT NULL,
    `naver_id` VARCHAR(255) NOT NULL UNIQUE,
    `naver_user_name` VARCHAR(30),
    `naver_user_nickname` VARCHAR(30),
    `naver_user_profile_image_url` VARCHAR(512),
    `naver_user_email_address` VARCHAR(320),
    `naver_user_birth_year` INT,
    `naver_user_birth_day` CHAR(10),
    `naver_user_phone_number` CHAR(15),
    `naver_user_phone_number_e164` CHAR(30),
    `naver_user_gender` CHAR(10),
    `creation_date_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_update_date_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deletion_date_time` DATETIME,
    CONSTRAINT `naver_user_login_category_id` FOREIGN KEY (`naver_user_login_category_id`) REFERENCES `user_login_category` (`user_login_category_id`)
);