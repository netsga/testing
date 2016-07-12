/*
-- Query: SELECT * FROM hemsdb.tbl_leaf_information
LIMIT 0, 1000

-- Date: 2016-07-12 17:56
*/
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`,`certification_key`,`password`) VALUES ('getDeviceData','hems','http_get',NULL,'http_get.getDeviceData(localhems,$params, ...)','Content-Type:application/json;charset=utf-8|user-id:#{userId}','http://localhost:8080/devices/#{ldId}/data?tags=#{tags}',NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`,`certification_key`,`password`) VALUES ('getDeviceData','hems','instance',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`,`certification_key`,`password`) VALUES ('getDeviceData','kiwigrid','http_get',NULL,'http_get.getDeviceData(localhems,$params, ...)','Content-Type:application/json|LG-USER-ID:#{userId}','https://lgservice-lg.appdev.kiwigrid.com/rest/deviceservice/devices/#{deviceId}','ad4-lg.p12','kiwigrid');
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`,`certification_key`,`password`) VALUES ('getDeviceData','lime','datamanager',NULL,'datamanager.getDeviceData(plug.dynamicinfo.status.t)',NULL,NULL,NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`,`certification_key`,`password`) VALUES ('getDeviceHistoryData','hems','http_get',NULL,'http_get.getDeviceHistoryData(localhems,$params, ...)','Content-Type:application/json;charset=utf-8|user-id:#{userId}','http://localhost:8080/devices/#{ldId}/data?tags=#{tags}',NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`,`certification_key`,`password`) VALUES ('setDeviceData','hems','http_post','{\\\"request\\\":{\\\"#{tagName}\\\":\"#{value}\"}}','http_post.setDeviceData(localhems, $params, ...)',NULL,'http://localhost:8080/devices/#{ldId}',NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`,`certification_key`,`password`) VALUES ('setDeviceData','hems','instance',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`,`certification_key`,`password`) VALUES ('setDeviceData','lime','datamanager',NULL,'datamanager.setDeviceData(typecode,datasheet)',NULL,NULL,NULL,NULL);
