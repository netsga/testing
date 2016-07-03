/*
-- Query: SELECT * FROM limerc.tbl_parameter_restriction
LIMIT 0, 500

-- Date: 2016-06-30 08:49
*/
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('deviceId','.{1,100}',1,'STRING',NULL,'^[0-9A-Za-z-_]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('deviceType','.{1,100}',1,'STRING',NULL,'^[0-9A-Za-z.]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('location','.{0,255}',1,'STRING',NULL,'^[0-9A-Za-z-_.,! 가-힣]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('logicalDeviceId','.{1,50}',1,'STRING',NULL,'^[0-9A-Za-z-]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('modelId','.{1,100}',1,'STRING',NULL,'^[0-9A-Za-z]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('modelName','.{1,30}',1,'STRING',NULL,'^[0-9A-Za-z-_/]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('nameTag','.{0,50}',1,'STRING',NULL,'^[0-9A-Za-z-_.,! 가-힣]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('serviceType','.{5}',1,'STRING',NULL,'^[0-9]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('subNameTag','.{0,50}',1,'STRING',NULL,'^[0-9A-Za-z-_.,! 가-힣]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('subSubNameTag','.{0,50}',1,'STRING',NULL,'^[0-9A-Za-z-_.,! 가-힣]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('tagName','.{5,100}',1,'STRING',NULL,'^[0-9A-Za-z._]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('testInt',NULL,1,'NUMBER','0-100',NULL);
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('testInt1',NULL,1,'NUMBER','0-100',NULL);
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('testInt2',NULL,1,'NUMBER','0-100',NULL);
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('testInt3',NULL,1,'NUMBER','50',NULL);
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('userId','.{5,30}',1,'STRING',NULL,'^[0-9A-Za-z_.]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('vendor','.{0,50}',1,'STRING',NULL,'^[0-9A-Za-z-_., ]*$');
INSERT INTO `tbl_parameter_restriction` (`parameter_name`,`length`,`need_check`,`parameter_type`,`number_range`,`regex`) VALUES ('version','.{1,30}',1,'STRING',NULL,'^[0-9A-Za-z-_.]*$');
