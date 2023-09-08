CREATE TABLE `code_category` (
  `code_category_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `code_category_name` varchar(30) NOT NULL UNIQUE,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletion_date_time` datetime DEFAULT NULL
);

CREATE TABLE `code` (
  `code_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `code_category_id` int NOT NULL,
  `code_name` varchar(30) NOT NULL,
  `code_value` int NOT NULL,
  `parent_code_id` int DEFAULT NULL,
  `sort_priority` int DEFAULT NULL,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletion_date_time` datetime DEFAULT NULL,
  CONSTRAINT `code_category_id` FOREIGN KEY (`code_category_id`) REFERENCES `code_category` (`code_category_id`),
  CONSTRAINT `parent_code_id` FOREIGN KEY (`parent_code_id`) REFERENCES `code` (`code_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE `code_group` (
  `code_group_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `code_group_name` varchar(30) NOT NULL,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletion_date_Time` datetime DEFAULT NULL
);

CREATE TABLE `code_group_item` (
  `code_group_item_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `code_group_id` int NOT NULL,
  `code_id` int NOT NULL,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletion_date_time` varchar(45) DEFAULT NULL,
  CONSTRAINT `code_group_id` FOREIGN KEY (`code_group_id`) REFERENCES `code_group` (`code_group_id`),
  CONSTRAINT `code_id` FOREIGN KEY (`code_id`) REFERENCES `code` (`code_id`)
);

CREATE TABLE `address` (
  `building_management_number` char(25) NOT NULL PRIMARY KEY,
  `road_name_address` varchar(50) NOT NULL,
  `land_number_address` varchar(50) NOT NULL,
  `zipcode` varchar(6) NOT NULL,
  `sido_code_id` int NOT NULL,
  `sigungu_code_id` int NOT NULL,
  `eupmyeondong_code_id` int NOT NULL,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletion_date_time` datetime DEFAULT NULL,
  CONSTRAINT `eupmyeondong_code_id` FOREIGN KEY (`eupmyeondong_code_id`) REFERENCES `code` (`code_id`),
  CONSTRAINT `sido_code_id` FOREIGN KEY (`sido_code_id`) REFERENCES `code` (`code_id`),
  CONSTRAINT `sigungu_code_id` FOREIGN KEY (`sigungu_code_id`) REFERENCES `code` (`code_id`)
);

CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_nonmember_flag_code_id` int NOT NULL,
  `login_block_flag_code_id` int NOT NULL,
  `user_email_address` varchar(320) NOT NULL,
  `user_phone_number` char(15) NOT NULL,
  `user_person_name` varchar(30) NOT NULL,
  `user_nickname` varchar(30) DEFAULT NULL,
  `default_user_shipping_address_id` int DEFAULT NULL,
  `user_gender_code_id` int DEFAULT NULL,
  `user_birth_date` date DEFAULT NULL,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletion_date_time` datetime DEFAULT NULL,
  CONSTRAINT `login_block_flag_code_id` FOREIGN KEY (`login_block_flag_code_id`) REFERENCES `code` (`code_id`),
  CONSTRAINT `user_gender_code_id` FOREIGN KEY (`user_gender_code_id`) REFERENCES `code` (`code_id`),
  CONSTRAINT `user_nonmember_flag_code_id` FOREIGN KEY (`user_nonmember_flag_code_id`) REFERENCES `code` (`code_id`)
);

CREATE TABLE `user_shipping_address` (
  `user_shipping_address_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_id` int NOT NULL,
  `building_management_number` char(25) NOT NULL,
  `detail_user_address` varchar(100) NOT NULL,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletion_date_time` datetime DEFAULT NULL,
  CONSTRAINT `building_management_number` FOREIGN KEY (`building_management_number`) REFERENCES `address` (`building_management_number`),
  CONSTRAINT `user_shipping_address_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

ALTER TABLE `user` ADD CONSTRAINT `default_user_shipping_address_id`
FOREIGN KEY (`default_user_shipping_address_id`) REFERENCES `user_shipping_address` (`user_shipping_address_id`);

CREATE TABLE `user_login_category` (
  `user_login_category_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_id` int NOT NULL,
  `user_login_category_code_id` int NOT NULL,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deletion_date_time` datetime DEFAULT NULL,
  CONSTRAINT `user_login_category_code_id` FOREIGN KEY (`user_login_category_code_id`) REFERENCES `code` (`code_id`),
  CONSTRAINT `user_login_category_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

CREATE TABLE `user_login_credentials` (
  `user_login_credentials_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_login_category_id` int NOT NULL,
  `login_user_name` varchar(30) DEFAULT NULL,
  `login_user_password` varchar(72) DEFAULT NULL,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletion_date_time` datetime DEFAULT NULL,
  CONSTRAINT `user_login_category_id` FOREIGN KEY (`user_login_category_id`) REFERENCES `user_login_category` (`user_login_category_id`)
);

CREATE TABLE `naver_user` (
  `naver_user_id` int NOT NULL PRIMARY KEY,
  `user_login_category_id` int NOT NULL,
  CONSTRAINT `naver_user_user_login_category_id` FOREIGN KEY (`user_login_category_id`) REFERENCES `user_login_category` (`user_login_category_id`)
);

CREATE TABLE `kakao_user` (
  `kakao_user_id` int NOT NULL PRIMARY KEY,
  `user_login_category_id` int NOT NULL,
  CONSTRAINT `kakao_user_user_login_category_id` FOREIGN KEY (`user_login_category_id`) REFERENCES `user_login_category` (`user_login_category_id`)
);

CREATE TABLE `user_agreement_item` (
  `user_agreement_item_id` int NOT NULL PRIMARY KEY,
  `user_id` int NOT NULL,
  `user_agreement_item_code_id` int NOT NULL,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletion_date_time` datetime DEFAULT NULL,
  CONSTRAINT `user_agreement_item_code_id` FOREIGN KEY (`user_agreement_item_code_id`) REFERENCES `code` (`code_id`),
  CONSTRAINT `user_agreement_item_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

CREATE TABLE `user_login_block_period` (
  `user_login_block_period_id` int NOT NULL PRIMARY KEY,
  `user_id` int NOT NULL,
  `user_login_block_reason_code_id` int NOT NULL,
  `user_login_block_detail_reason` varchar(200) NOT NULL,
  `block_start_date_time` datetime NOT NULL,
  `block_end_date_time` datetime NOT NULL,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletion_date_time` datetime DEFAULT NULL,
  CONSTRAINT `user_login_block_period_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `user_login_block_reason_code_id` FOREIGN KEY (`user_login_block_reason_code_id`) REFERENCES `code` (`code_id`)
);

CREATE TABLE `user_privacy_usage_period` (
  `user_privacy_usage_period_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_id` int NOT NULL,
  `usage_start_date_time` datetime NOT NULL,
  `usage_end_date_time` datetime NOT NULL,
  `creation_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletion_date_time` datetime DEFAULT NULL,
  CONSTRAINT `user_privacy_usage_period_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);
