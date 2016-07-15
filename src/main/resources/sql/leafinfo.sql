/*
-- Query: SELECT * FROM hemsdb.tbl_leaf_information
LIMIT 0, 1000

-- Date: 2016-07-14 17:51
*/
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`certification_key`,`description`,`header`,`password`,`url`) VALUES ('getDeviceData','hems','http_get',NULL,NULL,'http_get.getDeviceData(localhems,$params, ...)','Content-Type:application/json;charset=utf-8|user-id:#{userId}',NULL,'http://localhost:8080/devices/#{ldId}/data?tags=#{tags}');
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`certification_key`,`description`,`header`,`password`,`url`) VALUES ('getDeviceData','hems','instance',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`certification_key`,`description`,`header`,`password`,`url`) VALUES ('getDeviceData','kiwigrid','http_get',NULL,'ad4-lg.p12','http_get.getDeviceData(localhems,$params, ...)','Content-Type:application/json|LG-USER-ID:#{userId}','kiwigrid','https://lgservice-lg.appdev.kiwigrid.com/rest/deviceservice/devices/#{deviceId}');
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`certification_key`,`description`,`header`,`password`,`url`) VALUES ('getDeviceData','lime','datamanager',NULL,NULL,'datamanager.getDeviceData(plug.dynamicinfo.status.t)',NULL,NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`certification_key`,`description`,`header`,`password`,`url`) VALUES ('getDeviceHistoryData','hems','http_get',NULL,NULL,'http_get.getDeviceHistoryData(localhems,$params, ...)','Content-Type:application/json;charset=utf-8|user-id:#{userId}',NULL,'http://localhost:8080/devices/#{ldId}/data?tags=#{tags}');
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`certification_key`,`description`,`header`,`password`,`url`) VALUES ('setDeviceData','hems','http_post','{\\\"request\\\":{\\\"#{tagName}\\\":\"#{value}\"}}',NULL,'http_post.setDeviceData(localhems, $params, ...)',NULL,NULL,'http://localhost:8080/devices/#{ldId}');
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`certification_key`,`description`,`header`,`password`,`url`) VALUES ('setDeviceData','hems','instance',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`certification_key`,`description`,`header`,`password`,`url`) VALUES ('setDeviceData','kiwigrid','kiwigrid_patch','{\"tagValues\":{\"#{tagValue}\":{\"value\": #{value}, \"oca\": #{oca}}}}','ad4-lg.p12','http_get.getDeviceData(localhems,$params, ...)','Content-Type:application/json|LG-USER-ID:#{userId}','kiwigrid','https://lgservice-lg.appdev.kiwigrid.com/rest/deviceservice/devices/#{deviceId}');
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`certification_key`,`description`,`header`,`password`,`url`) VALUES ('setDeviceData','lime','datamanager',NULL,NULL,'datamanager.setDeviceData(typecode,datasheet)',NULL,NULL,NULL);
