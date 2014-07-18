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
  UNIQUE KEY `device_time_lat_long` (`device_id`,`measure_time`,`latitude`,`longitude`)
) ENGINE=InnoDB AUTO_INCREMENT=606811 DEFAULT CHARSET=utf8;

create temporary table dmax select device_id, measure_time, latitude, longitude, max(id) max_id from device_location group by 1, 2, 3, 4;

insert into device_location_unique
select t1.altitude, t1.climb, t1.device_id, t1.id, t1.latitude, t1.longitude, t1.measure_time, t1.receive_time, t1.speed, t1.track from device_location t1 join dmax t2 on t1.id = t2.max_id;

rename table device_location to device_location_dupes;
rename table device_location_unique to device_location;

drop table dmax;