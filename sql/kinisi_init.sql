CREATE TABLE `api_token` (
  `email` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` smallint(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
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
  `epx` double DEFAULT NULL,
  `epy` double DEFAULT NULL,
  `epv` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_time_lat_long` (`device_id`,`measure_time`,`latitude`,`longitude`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

CREATE TABLE `device_interface` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `device_configuration_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_name` (`device_id`, `name`),
  CONSTRAINT `device_interface_ibfk_1` FOREIGN KEY (`device_configuration_id`) REFERENCES `device_configuration` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
;
