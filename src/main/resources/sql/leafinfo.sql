/*
-- Query: SELECT * FROM limerc.tbl_leaf_information
LIMIT 0, 500

-- Date: 2016-06-30 08:49
*/
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`) VALUES ('getDeviceData','hems','http_get',NULL,'http_get.getDeviceData(localhems,$params, ...)','Content-Type:application/json;charset=utf-8|user-id:#{userId}','http://localhost:8080/devices/#{ldId}/data?tags=#{tags}');
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`) VALUES ('getDeviceData','hems','instance',NULL,NULL,NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`) VALUES ('getDeviceData','lime','datamanager',NULL,'datamanager.getDeviceData(plug.dynamicinfo.status.t)',NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`) VALUES ('setDeviceData','hems','http_post','{\\\"request\\\":{\\\"#{tagName}\\\":\"#{value}\"}}','http_post.setDeviceData(localhems, $params, ...)',NULL,'http://localhost:8080/devices/#{ldId}');
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`) VALUES ('setDeviceData','hems','instance',NULL,NULL,NULL,NULL);
INSERT INTO `tbl_leaf_information` (`method`,`source_name`,`type`,`body`,`description`,`header`,`url`) VALUES ('setDeviceData','lime','datamanager',NULL,'datamanager.setDeviceData(typecode,datasheet)',NULL,NULL);
