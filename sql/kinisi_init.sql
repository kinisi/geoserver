CREATE TABLE `api_token` (
  `email` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` smallint(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8
;

CREATE TABLE `device_location` (
  `altitude` double DEFAULT NULL,
  `climb` double DEFAULT NULL,
  `device_id` varchar(255) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `measure_time` datetime NOT NULL,
  `receive_time` datetime NOT NULL,
  `speed` double DEFAULT NULL,
  `track` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `device_time` (`device_id`,`measure_time`)
) ENGINE=InnoDB AUTO_INCREMENT=131240 DEFAULT CHARSET=utf8
;

CREATE TABLE `device_configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `api_token_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_id` (`device_id`),
  KEY `api_token_id` (`api_token_id`),
  CONSTRAINT `device_configuration_ibfk_1` FOREIGN KEY (`api_token_id`) REFERENCES `api_token` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8
;
