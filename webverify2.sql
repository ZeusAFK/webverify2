/*
SQLyog Community compiled by ZeusAFK v11.0 (32 bit)
MySQL - 5.6.17 : Database - webverify2
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `asset_type` */

CREATE TABLE `asset_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(254) NOT NULL,
  `mime` varchar(50) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `download` int(1) NOT NULL DEFAULT '0',
  `binary` int(11) NOT NULL DEFAULT '0',
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Table structure for table `authority_type` */

CREATE TABLE `authority_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `handler` varchar(50) NOT NULL,
  `name` varchar(254) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `impact_type` */

CREATE TABLE `impact_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(254) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `weight` int(11) NOT NULL DEFAULT '1',
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `ocurrence_type` */

CREATE TABLE `ocurrence_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(254) NOT NULL,
  `impact` int(11) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_ocurrence_type_impact` (`impact`),
  CONSTRAINT `fk_ocurrence_type_impact` FOREIGN KEY (`impact`) REFERENCES `impact_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `report_configuration` */

CREATE TABLE `report_configuration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `target` varchar(5000) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_report_configuration_site` (`site`),
  KEY `fk_report_configuration_type` (`type`),
  CONSTRAINT `fk_report_configuration_site` FOREIGN KEY (`site`) REFERENCES `site` (`id`),
  CONSTRAINT `fk_report_configuration_type` FOREIGN KEY (`type`) REFERENCES `report_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `report_entry` */

CREATE TABLE `report_entry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scan` int(11) NOT NULL,
  `configuration` int(11) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_report_entry_scan` (`scan`),
  KEY `fk_report_entry_configuration` (`configuration`),
  CONSTRAINT `fk_report_entry_configuration` FOREIGN KEY (`configuration`) REFERENCES `report_configuration` (`id`),
  CONSTRAINT `fk_report_entry_scan` FOREIGN KEY (`scan`) REFERENCES `scan_entry` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `report_type` */

CREATE TABLE `report_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `handler` varchar(50) NOT NULL,
  `name` varchar(254) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `scan_asset` */

CREATE TABLE `scan_asset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scan` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `location` varchar(254) NOT NULL,
  `url` varchar(254) NOT NULL,
  `size` int(11) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_scan_asset_scan` (`scan`),
  KEY `fk_scan_asset_type` (`type`),
  CONSTRAINT `fk_scan_asset_scan` FOREIGN KEY (`scan`) REFERENCES `scan_entry` (`id`),
  CONSTRAINT `fk_scan_asset_type` FOREIGN KEY (`type`) REFERENCES `asset_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `scan_entry` */

CREATE TABLE `scan_entry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site` int(11) NOT NULL,
  `start` timestamp NOT NULL,
  `end` timestamp NOT NULL,
  `ocurrences` int(11) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_scan_entry_site` (`site`),
  CONSTRAINT `fk_scan_entry_site` FOREIGN KEY (`site`) REFERENCES `site` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `scan_ocurrence` */

CREATE TABLE `scan_ocurrence` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scan` int(11) NOT NULL,
  `asset` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_scan_ocurrence_scan` (`scan`),
  KEY `fk_scan_ocurrence_asset` (`asset`),
  KEY `fk_scan_ocurrence_type` (`type`),
  CONSTRAINT `fk_scan_ocurrence_asset` FOREIGN KEY (`asset`) REFERENCES `scan_asset` (`id`),
  CONSTRAINT `fk_scan_ocurrence_scan` FOREIGN KEY (`scan`) REFERENCES `scan_entry` (`id`),
  CONSTRAINT `fk_scan_ocurrence_type` FOREIGN KEY (`type`) REFERENCES `ocurrence_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `scan_schedule` */

CREATE TABLE `scan_schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site` int(11) NOT NULL,
  `interval` int(11) NOT NULL,
  `limit` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `priority` int(11) NOT NULL,
  `user` int(11) NOT NULL,
  `build` int(1) NOT NULL DEFAULT '1',
  `crawl` int(1) NOT NULL DEFAULT '1',
  `lastscan` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_scan_schedule_user` (`user`),
  KEY `fk_scan_schedule_site` (`site`),
  CONSTRAINT `fk_scan_schedule_site` FOREIGN KEY (`site`) REFERENCES `site` (`id`),
  CONSTRAINT `fk_scan_schedule_user` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `site` */

CREATE TABLE `site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(254) NOT NULL,
  `url` varchar(254) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `user` int(11) NOT NULL,
  `lastbuild` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_site_user` (`user`),
  CONSTRAINT `fk_site_user` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `site_asset` */

CREATE TABLE `site_asset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `location` varchar(254) NOT NULL,
  `url` varchar(254) NOT NULL,
  `size` int(11) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_site_asset_site` (`site`),
  KEY `fk_site_asset_type` (`type`),
  CONSTRAINT `fk_site_asset_site` FOREIGN KEY (`site`) REFERENCES `site` (`id`),
  CONSTRAINT `fk_site_asset_type` FOREIGN KEY (`type`) REFERENCES `asset_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Table structure for table `user` */

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `firstname` varchar(254) NOT NULL,
  `lastname` varchar(254) NOT NULL,
  `password` varchar(40) NOT NULL,
  `authority` int(11) NOT NULL,
  `lastaccess` timestamp NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_user_authority` (`authority`),
  CONSTRAINT `fk_user_authority` FOREIGN KEY (`authority`) REFERENCES `authority_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/* Procedure structure for procedure `createSiteAsset` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `createSiteAsset`(IN varSite INT, IN varType INT, IN varLocation VARCHAR(254), IN varUrl VARCHAR(254), IN varSize INT, OUT varId INT)
BEGIN
	INSERT INTO site_asset (site, `type`, location, url, size) VALUES (varSite, varType, varLocation, varUrl, varSize);
	SET varId = LAST_INSERT_ID();
END */$$
DELIMITER ;

/* Procedure structure for procedure `deleteSiteAssetById` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteSiteAssetById`(IN varId INT, OUT varResult INT)
BEGIN
	UPDATE site_asset SET `status` = 0 WHERE id = varId;
	SET varResult = 1;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getAssetTypeById` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getAssetTypeById`(IN varId INT)
BEGIN
	SELECT id, `name`, mime, description, download, `binary`, `status` FROM asset_type WHERE id = varId;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getAssetTypeByMime` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getAssetTypeByMime`(IN varMime VARCHAR(50))
BEGIN
	SELECT id, `name`, mime, description, download, `binary`, `status` FROM asset_type WHERE varMime LIKE CONCAT(mime, '%') LIMIT 1;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getAuthorityTypeById` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getAuthorityTypeById`(IN varId INT)
BEGIN
	SELECT id, `handler`, `name`, description, `status` FROM `authority_type` WHERE id = varId;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getScanScheduleActiveList` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getScanScheduleActiveList`()
BEGIN
	SELECT id, site, `interval`, `limit`, priority, `user`, build, crawl, lastscan, created, `status` FROM scan_schedule WHERE `status` <> 0 ORDER BY priority DESC;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getSiteAssetBySiteAndLocation` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getSiteAssetBySiteAndLocation`(IN varSite INT, IN varLocation VARCHAR(254))
BEGIN
	SELECT id, site, `type`, location, url, size, created, `status` FROM site_asset WHERE site = varSite AND location = varLocation AND `status` <> 0;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getSiteAssets` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getSiteAssets`(IN varSite INT)
BEGIN
	SELECT id, site, `type`, location, url, size, created, `status` FROM site_asset WHERE site = varSite AND `status` <> 0;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getSiteById` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getSiteById`(IN varId INT)
BEGIN
	SELECT id, `name`, url, description, `user`, created, `status` FROM site WHERE id = varId;
END */$$
DELIMITER ;

/* Procedure structure for procedure `getUserById` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getUserById`(IN varId INT)
BEGIN
	SELECT id, `name`, firstname, lastname, `password`, authority, lastaccess, created, `status` FROM `user` WHERE id = varId;
END */$$
DELIMITER ;

/* Procedure structure for procedure `updateSchanScheduleById` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `updateSchanScheduleById`(IN varId INT, IN varInterval INT, IN varLastScan TIMESTAMP, IN varLimit TIMESTAMP, IN varPriority INT, IN varBuild INT, IN varCrawl INT, IN varStatus INT, OUT varResult INT)
BEGIN
	UPDATE scan_schedule SET `interval` = varInterval, `lastscan` = varLastScan, `limit` = varLimit, `priority` = varPriority, `build` = varBuild, `crawl` = varCrawl, `status` = varStatus WHERE id = varId;
	SET varResult = 1;
END */$$
DELIMITER ;

/* Procedure structure for procedure `updateSiteById` */

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `updateSiteById`(IN varId INT, IN varName VARCHAR(254), IN varUrl VARCHAR(254), IN varDescription VARCHAR(1000), IN varLastBuild TIMESTAMP, IN varStatus INT, OUT varResult INT)
BEGIN
	UPDATE site SET `name` = varName, `url` = varUrl, `description` = varDescription, `lastbuild` = varLastBuild, `status` = varStatus WHERE id = varId;
	SET varResult = 1;
END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
