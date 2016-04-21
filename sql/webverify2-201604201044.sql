CREATE TABLE `configuration`( `id` INT NOT NULL AUTO_INCREMENT, `key` VARCHAR(254) NOT NULL, `name` VARCHAR(254) NOT NULL, `description` VARCHAR(1000) NOT NULL, `value` VARCHAR(254) NOT NULL, `status` INT(1) NOT NULL DEFAULT 1, PRIMARY KEY (`id`) ); 

INSERT INTO `configuration` (`key`, `name`, `description`, `value`) VALUES ('max_scan_threads', 'Max scan threads', 'Maximum number of scans that can be running before start a new scan', '5'); 
INSERT INTO `configuration` (`key`, `name`, `description`, `value`) VALUES ('gmail_oauth2_credentials', 'GMAIL OAuth2 Credentials', 'OAuth2 access information to use GMAIL', 'username:;access_token:'); 

DELIMITER $$

DROP PROCEDURE IF EXISTS `getConfigurationByKey`$$

CREATE PROCEDURE `getConfigurationByKey`(IN varKey VARCHAR(254))
BEGIN
	SELECT `id`, `key`, `name`, `description`, `value`, `status` FROM `configuration` WHERE `key` = varKey AND `status` <> 0;
END$$

DELIMITER ;