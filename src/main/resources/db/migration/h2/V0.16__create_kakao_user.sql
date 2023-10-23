DROP TABLE `kakao_user`;

CREATE TABLE `kakao_user` (
    `kakao_user_id` INT PRIMARY KEY AUTO_INCREMENT,
    `kakao_user_login_category_id` INT NOT NULL,
    `kakao_id` BIGINT NOT NULL UNIQUE,
    `kakao_user_connection_date_time` DATETIME NOT NULL,
    `kakao_user_nickname` VARCHAR(512),
    `kakao_user_thumbnail_image_url` VARCHAR(512),
    `kakao_user_profile_image_url` VARCHAR(512),
    `kakao_user_default_image_flag` BOOLEAN,
    `creation_date_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_update_date_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deletion_date_time` DATETIME,
    CONSTRAINT `kakao_user_login_category_id` FOREIGN KEY (`kakao_user_login_category_id`) REFERENCES `user_login_category`(`user_login_category_id`)
);