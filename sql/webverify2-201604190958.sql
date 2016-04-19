-- Added support for site ip address verification

ALTER TABLE `site` ADD COLUMN `ip` VARCHAR(254) NOT NULL AFTER `url`; 

ALTER TABLE `scan_entry` ADD COLUMN `ip` VARCHAR(254) NOT NULL AFTER `end`; 

DELIMITER $$

DROP PROCEDURE IF EXISTS `getSiteById`$$

CREATE PROCEDURE `getSiteById`(IN varId INT)
BEGIN
	SELECT id, `name`, url, ip, description, `user`, created, `status` FROM site WHERE id = varId;
END$$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `updateSiteById`$$

CREATE PROCEDURE `updateSiteById`(IN varId INT, IN varName VARCHAR(254), IN varUrl VARCHAR(254), IN varIp VARCHAR(254), IN varDescription VARCHAR(1000), IN varLastBuild TIMESTAMP, IN varStatus INT, OUT varResult INT)
BEGIN
	UPDATE site SET `name` = varName, `url` = varUrl, ip = varIp, `description` = varDescription, `lastbuild` = varLastBuild, `status` = varStatus WHERE id = varId;
	SET varResult = 1;
END$$

DELIMITER ;