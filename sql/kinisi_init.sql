SET FOREIGN_KEY_CHECKS=0
;

DROP TABLE IF EXISTS `api_token` CASCADE
;

SET FOREIGN_KEY_CHECKS=1
;

SET FOREIGN_KEY_CHECKS=0
;

DROP TABLE IF EXISTS `device_location` CASCADE
;

SET FOREIGN_KEY_CHECKS=1
;

CREATE TABLE `device_location` (
  `altitude` double DEFAULT NULL,
  `climb` double DEFAULT NULL,
  `device_id` varchar(255) NOT NULL,
  `id` int(11) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `measure_time` datetime NOT NULL,
  `receive_time` datetime NOT NULL,
  `speed` double DEFAULT NULL,
  `track` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_time` (`device_id`,`measure_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
;

CREATE TABLE `api_token` (
  `email` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `status` smallint(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
;

DROP TABLE IF EXISTS AUTO_PK_SUPPORT
;

CREATE TABLE AUTO_PK_SUPPORT (  TABLE_NAME CHAR(100) NOT NULL,  NEXT_ID BIGINT NOT NULL, UNIQUE (TABLE_NAME))
;

DELETE FROM AUTO_PK_SUPPORT WHERE TABLE_NAME IN ('api_token', 'device_location')
;

INSERT INTO AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('api_token', 200)
;

INSERT INTO AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('device_location', 200)
;

