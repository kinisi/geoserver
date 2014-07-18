CREATE TABLE `device_location_unique` (
  `altitude` double DEFAULT NULL,
  `climb` double DEFAULT NULL,
  `device_id` varchar(255) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `measure_time` datetime NOT NULL,
  `receive_time` datetime NOT NULL,
  `speed` double DEFAULT NULL,
  `track` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_lat_long` (`device_id`,`latitude`,`longitude`),
  KEY `device_time` (`device_id`,`measure_time`)
) ENGINE=InnoDB AUTO_INCREMENT=606811 DEFAULT CHARSET=utf8;

insert into device_location_unique
select * from device_location where id in (
  select max_id from (select device_id, latitude, longitude, max(id) max_id from device_location group by 1, 2, 3) t1
);

rename table device_location to device_location_dupes;
rename table device_location_unique to device_location;
