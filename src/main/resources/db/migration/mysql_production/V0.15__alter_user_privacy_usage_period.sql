ALTER TABLE `user_privacy_usage_period`
    ADD COLUMN `usage_end_date_time` datetime NOT NULL;

UPDATE `user_privacy_usage_period`
	SET `usage_end_date_time` = CASE user_privacy_usage_period_code
			WHEN 0
            THEN '9999-12-31 23:59:59'
            WHEN 31536000
            THEN DATE_ADD(usage_start_date_time, INTERVAL 1 YEAR)
            WHEN 94608000
            THEN DATE_ADD(usage_start_date_time, INTERVAL 3 YEAR)
            WHEN 157680000
            THEN DATE_ADD(usage_start_date_time, INTERVAL 5 YEAR)
            ELSE DATE_ADD(usage_start_date_time, INTERVAL user_privacy_usage_period_code SECOND)
		END
    WHERE `user_privacy_usage_period_id` > 0;

CREATE INDEX `usage_end_date_time_idx` ON user_privacy_usage_period (usage_end_date_time);