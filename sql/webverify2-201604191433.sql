DELIMITER $$

DROP PROCEDURE IF EXISTS `createScanEntry`$$

CREATE PROCEDURE `createScanEntry`(IN varSite INT, IN varStart TIMESTAMP, IN varIp VARCHAR(254), IN varOcurrences INT, IN varStatus INT, OUT varId INT)
BEGIN
	INSERT INTO scan_entry (site, `start`, `ip`, `ocurrences`, `status`) VALUES (varSite, varStart, varIp, varOcurrences, varStatus);
	SET varId = LAST_INSERT_ID();
END$$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `updateScanEntry`$$

CREATE PROCEDURE `updateScanEntry`(IN varId INT, IN varSite INT, IN varStart TIMESTAMP, IN varEnd TIMESTAMP, IN varIp VARCHAR(254), IN varOcurrences INT, IN varStatus INT, OUT varResult INT)
BEGIN
	UPDATE scan_entry SET site = varSite, `start` = varStart, `end` = varEnd, `ip` = varIp, `ocurrences` = varOcurrences, `status` = varStatus WHERE id = varId;
	SET varResult = 1;
END$$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `createScanAsset`$$

CREATE PROCEDURE `createScanAsset`(IN varScan INT, IN varType INT, IN varLocation VARCHAR(254), IN varUrl VARCHAR(254), IN varSize INT, OUT varId INT)
BEGIN
	INSERT INTO scan_asset (scan, `type`, location, url, size) VALUES (varScan, varType, varLocation, varUrl, varSize);
	SET varId = LAST_INSERT_ID();
END$$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `updateScanAssetById`$$

CREATE PROCEDURE `updateScanAssetById`(IN varId INT, IN varType INT, IN varLocation VARCHAR(254), IN varUrl VARCHAR(254), IN varSize INT IN varStatus INT, OUT varResult INT)
BEGIN
	UPDATE scan_asset SET `type` = varType, location = varLocation, url = varUrl, size = varSize, `status` = varStatus WHERE id = varId;
	SET varResult = 1;
END$$

DELIMITER ;