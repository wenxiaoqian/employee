/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50630
 Source Host           : localhost
 Source Database       : employee

 Target Server Type    : MySQL
 Target Server Version : 50630
 File Encoding         : utf-8

 Date: 12/15/2016 23:34:50 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `roleId` varchar(500) DEFAULT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `department`
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `departmentId` varchar(50) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `departmentName` varchar(100) DEFAULT NULL,
  `departmentOrder` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `educate`
-- ----------------------------
DROP TABLE IF EXISTS `educate`;
CREATE TABLE `educate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field1` varchar(500) DEFAULT NULL,
  `field2` varchar(500) DEFAULT NULL,
  `field3` varchar(500) DEFAULT NULL,
  `field4` varchar(500) DEFAULT NULL,
  `field5` varchar(500) DEFAULT NULL,
  `field6` varchar(500) DEFAULT NULL,
  `field7` varchar(500) DEFAULT NULL,
  `field8` varchar(500) DEFAULT NULL,
  `field9` varchar(500) DEFAULT NULL,
  `field10` varchar(500) DEFAULT NULL,
  `field11` varchar(500) DEFAULT NULL,
  `field12` varchar(500) DEFAULT NULL,
  `field13` varchar(500) DEFAULT NULL,
  `field14` varchar(500) DEFAULT NULL,
  `field15` varchar(500) DEFAULT NULL,
  `field16` varchar(500) DEFAULT NULL,
  `field17` varchar(500) DEFAULT NULL,
  `field18` varchar(500) DEFAULT NULL,
  `createtime` varchar(20) DEFAULT NULL,
  `updatetime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `educate`
-- ----------------------------
BEGIN;
INSERT INTO `educate` VALUES ('1', '33', '33', '33', '33', '33', '33', '33', '33', '33', null, null, null, null, null, null, null, null, null, '2016-12-13 11:10:10', '2016-12-13 11:10:10');
COMMIT;

-- ----------------------------
--  Table structure for `employee`
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field1` varchar(100) DEFAULT NULL,
  `field2` varchar(100) DEFAULT NULL,
  `field3` varchar(100) DEFAULT NULL,
  `field4` varchar(100) DEFAULT NULL,
  `field5` varchar(100) DEFAULT NULL,
  `field6` varchar(100) DEFAULT NULL,
  `field7` varchar(100) DEFAULT NULL,
  `field8` varchar(100) DEFAULT NULL,
  `field9` varchar(100) DEFAULT NULL,
  `field10` varchar(100) DEFAULT NULL,
  `field11` varchar(100) DEFAULT NULL,
  `field12` varchar(100) DEFAULT NULL,
  `field13` varchar(100) DEFAULT NULL,
  `field14` varchar(100) DEFAULT NULL,
  `field15` varchar(100) DEFAULT NULL,
  `field16` varchar(100) DEFAULT NULL,
  `field17` varchar(100) DEFAULT NULL,
  `field18` varchar(100) DEFAULT NULL,
  `field19` varchar(100) DEFAULT NULL,
  `field20` int DEFAULT 0,
  `field21` varchar(100) DEFAULT NULL,
  `field22` varchar(100) DEFAULT NULL,
  `field23` varchar(100) DEFAULT NULL,
  `field24` varchar(100) DEFAULT NULL,
  `field25` varchar(100) DEFAULT NULL,
  `field26` varchar(100) DEFAULT NULL,
  `field27` varchar(100) DEFAULT NULL,
  `field28` varchar(100) DEFAULT NULL,
  `field29` varchar(100) DEFAULT NULL,
  `field30` varchar(100) DEFAULT NULL,
  `field31` varchar(100) DEFAULT NULL,
  `field32` varchar(100) DEFAULT NULL,
  `field35` int DEFAULT 0,
  `field33` varchar(100) DEFAULT NULL,
  `field34` varchar(100) DEFAULT NULL,
  `field36` varchar(100) DEFAULT NULL,
  `field37` varchar(100) DEFAULT NULL,
  `field38` varchar(100) DEFAULT NULL,
  `field39` varchar(100) DEFAULT NULL,
  `field40` varchar(100) DEFAULT NULL,
  `field41` varchar(100) DEFAULT NULL,
  `field42` varchar(100) DEFAULT NULL,
  `field43` varchar(100) DEFAULT NULL,
  `field44` varchar(100) DEFAULT NULL,
  `field45` varchar(100) DEFAULT NULL,
  `field46` varchar(100) DEFAULT NULL,
  `field47` varchar(100) DEFAULT NULL,
  `field48` varchar(100) DEFAULT NULL,
  `field49` varchar(100) DEFAULT NULL,
  `field50` varchar(100) DEFAULT NULL,
  `field51` varchar(100) DEFAULT NULL,
  `field52` varchar(100) DEFAULT NULL,
  `field53` varchar(100) DEFAULT NULL,
  `field54` varchar(100) DEFAULT NULL,
  `field55` varchar(100) DEFAULT NULL,
  `field56` varchar(100) DEFAULT NULL,
  `field57` varchar(100) DEFAULT NULL,
  `field58` varchar(100) DEFAULT NULL,
  `field59` varchar(100) DEFAULT NULL,
  `field60` varchar(100) DEFAULT NULL,
  `field61` varchar(100) DEFAULT NULL,
  `field62` varchar(100) DEFAULT NULL,
  `field63` varchar(100) DEFAULT NULL,
  `field64` varchar(100) DEFAULT NULL,
  `field65` varchar(100) DEFAULT NULL,
  `field66` varchar(100) DEFAULT NULL,
  `field67` varchar(100) DEFAULT NULL,
  `field68` varchar(100) DEFAULT NULL,
  `field69` varchar(100) DEFAULT NULL,
  `field70` varchar(100) DEFAULT NULL,
  `field72` varchar(100) DEFAULT NULL,
  `field71` varchar(100) DEFAULT NULL,
  `field73` varchar(100) DEFAULT NULL,
  `field74` varchar(100) DEFAULT NULL,
  `field75` varchar(100) DEFAULT NULL,
  `field76` varchar(100) DEFAULT NULL,
  `field77` varchar(100) DEFAULT NULL,
  `field78` varchar(100) DEFAULT NULL,
  `field79` varchar(100) DEFAULT NULL,
  `field80` varchar(100) DEFAULT NULL,
  `field81` varchar(100) DEFAULT NULL,
  `field82` varchar(100) DEFAULT NULL,
  `field83` varchar(100) DEFAULT NULL,
  `field84` varchar(100) DEFAULT NULL,
  `field85` varchar(100) DEFAULT NULL,
  `field86` varchar(100) DEFAULT NULL,
  `field87` varchar(100) DEFAULT NULL,
  `field88` varchar(100) DEFAULT NULL,
  `field89` varchar(100) DEFAULT NULL,
  `field90` varchar(100) DEFAULT NULL,
  `field91` varchar(100) DEFAULT NULL,
  `field92` varchar(100) DEFAULT NULL,
  `field93` varchar(100) DEFAULT NULL,
  `createtime` varchar(20) DEFAULT NULL,
  `updatetime` varchar(20) DEFAULT NULL,
  `deletetime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ----------------------------
--  Table structure for `family`
-- ----------------------------
DROP TABLE IF EXISTS `family`;
CREATE TABLE `family` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field1` varchar(500) DEFAULT NULL,
  `field2` varchar(500) DEFAULT NULL,
  `field3` varchar(500) DEFAULT NULL,
  `field4` varchar(500) DEFAULT NULL,
  `field5` varchar(500) DEFAULT NULL,
  `field6` varchar(500) DEFAULT NULL,
  `field7` varchar(500) DEFAULT NULL,
  `field8` varchar(500) DEFAULT NULL,
  `field9` varchar(500) DEFAULT NULL,
  `field10` varchar(500) DEFAULT NULL,
  `field11` varchar(500) DEFAULT NULL,
  `field12` varchar(500) DEFAULT NULL,
  `field13` varchar(500) DEFAULT NULL,
  `field14` varchar(500) DEFAULT NULL,
  `field15` varchar(500) DEFAULT NULL,
  `field16` varchar(500) DEFAULT NULL,
  `field17` varchar(500) DEFAULT NULL,
  `field18` varchar(500) DEFAULT NULL,
  `createtime` varchar(20) DEFAULT NULL,
  `updatetime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `family`
-- ----------------------------
BEGIN;
INSERT INTO `family` VALUES ('1', '111', '111', '1111', '111111', '11111', '11111', '11111', '1111', null, null, null, null, null, null, null, null, null, null, '2016-12-13 11:10:10', '2016-12-13 11:10:10');
COMMIT;

-- ----------------------------
--  Table structure for `lend`
-- ----------------------------
DROP TABLE IF EXISTS `lend`;
CREATE TABLE `lend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field1` varchar(500) DEFAULT NULL,
  `field2` varchar(500) DEFAULT NULL,
  `field3` varchar(500) DEFAULT NULL,
  `field4` varchar(500) DEFAULT NULL,
  `field5` varchar(500) DEFAULT NULL,
  `field6` varchar(500) DEFAULT NULL,
  `field7` varchar(500) DEFAULT NULL,
  `field8` varchar(500) DEFAULT NULL,
  `field9` varchar(500) DEFAULT NULL,
  `field10` varchar(500) DEFAULT NULL,
  `field11` varchar(500) DEFAULT NULL,
  `field12` varchar(500) DEFAULT NULL,
  `field13` varchar(500) DEFAULT NULL,
  `field14` varchar(500) DEFAULT NULL,
  `field15` varchar(500) DEFAULT NULL,
  `field16` varchar(500) DEFAULT NULL,
  `field17` varchar(500) DEFAULT NULL,
  `field18` varchar(500) DEFAULT NULL,
  `createtime` varchar(20) DEFAULT NULL,
  `updatetime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `lend`
-- ----------------------------
BEGIN;
INSERT INTO `lend` VALUES ('1', '44', '44', '44', '44', '44', '44', '44', '444', null, null, null, null, null, null, null, null, null, null, '2016-12-13 11:10:10', '2016-12-13 11:10:10');
COMMIT;

-- ----------------------------
--  Table structure for `works`
-- ----------------------------
DROP TABLE IF EXISTS `works`;
CREATE TABLE `works` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field1` varchar(500) DEFAULT NULL,
  `field2` varchar(500) DEFAULT NULL,
  `field3` varchar(500) DEFAULT NULL,
  `field4` varchar(500) DEFAULT NULL,
  `field5` varchar(500) DEFAULT NULL,
  `field6` varchar(500) DEFAULT NULL,
  `field7` varchar(500) DEFAULT NULL,
  `field8` varchar(500) DEFAULT NULL,
  `field9` varchar(500) DEFAULT NULL,
  `field10` varchar(500) DEFAULT NULL,
  `field11` varchar(500) DEFAULT NULL,
  `field12` varchar(500) DEFAULT NULL,
  `field13` varchar(500) DEFAULT NULL,
  `field14` varchar(500) DEFAULT NULL,
  `field15` varchar(500) DEFAULT NULL,
  `field16` varchar(500) DEFAULT NULL,
  `field17` varchar(500) DEFAULT NULL,
  `field18` varchar(500) DEFAULT NULL,
  `createtime` varchar(20) DEFAULT NULL,
  `updatetime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `works`
-- ----------------------------
BEGIN;
INSERT INTO `works` VALUES ('1', '222', '22', '22', '22', '22', '22', '22', '22', '22', null, null, null, null, null, null, null, null, null, '2016-12-13 11:10:10', '2016-12-13 11:10:10');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
