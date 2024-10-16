/*
 Navicat Premium Data Transfer

 Source Server         : bjb
 Source Server Type    : MySQL
 Source Server Version : 50729 (5.7.29-log)
 Source Host           : localhost:3306
 Source Schema         : traffic

 Target Server Type    : MySQL
 Target Server Version : 50729 (5.7.29-log)
 File Encoding         : 65001

 Date: 16/10/2024 11:20:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for car
-- ----------------------------
DROP TABLE IF EXISTS `car`;
CREATE TABLE `car`  (
  `id` bigint(20) NOT NULL,
  `type_id` bigint(20) NULL DEFAULT NULL,
  `longitude` double NULL DEFAULT NULL,
  `latitude` double NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '空闲',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of car
-- ----------------------------
INSERT INTO `car` VALUES (1846363605384986624, 3, 107.742521, 28.132483, '空闲');
INSERT INTO `car` VALUES (1846363605384986625, 3, 100.470017, 29.247678, '空闲');
INSERT INTO `car` VALUES (1846363605384986626, 2, 105.770497, 30.233086, '空闲');
INSERT INTO `car` VALUES (1846363605384986627, 2, 102.211117, 31.927672, '空闲');
INSERT INTO `car` VALUES (1846363605384986628, 2, 103.448397, 31.954889, '空闲');

-- ----------------------------
-- Table structure for car_type
-- ----------------------------
DROP TABLE IF EXISTS `car_type`;
CREATE TABLE `car_type`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `load_capacity` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of car_type
-- ----------------------------
INSERT INTO `car_type` VALUES (1, '大车型', 25);
INSERT INTO `car_type` VALUES (2, '中车型', 15);
INSERT INTO `car_type` VALUES (3, '小车型', 10);

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint(20) NOT NULL,
  `type_id` bigint(20) NULL DEFAULT NULL,
  `owner_id` bigint(20) NULL DEFAULT NULL,
  `weight` double NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '待委托',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------

-- ----------------------------
-- Table structure for goods_type
-- ----------------------------
DROP TABLE IF EXISTS `goods_type`;
CREATE TABLE `goods_type`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `max_weight` double NULL DEFAULT NULL,
  `min_weight` double NULL DEFAULT NULL,
  `father_node` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `child_node` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `time` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_type
-- ----------------------------
INSERT INTO `goods_type` VALUES (1, '原木', 5.6, 12.7, 'NULL', '木板', 40);
INSERT INTO `goods_type` VALUES (2, '木板', 4.6, 11.7, '原木', '家具', 40);
INSERT INTO `goods_type` VALUES (3, '家具', 3.6, 10.7, '木板', '商品', 40);
INSERT INTO `goods_type` VALUES (4, '商品', 3.6, 10.7, '家具', 'NULL', 40);

-- ----------------------------
-- Table structure for poi
-- ----------------------------
DROP TABLE IF EXISTS `poi`;
CREATE TABLE `poi`  (
  `id` bigint(20) NOT NULL,
  `type_id` bigint(20) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `longitude` double NULL DEFAULT NULL,
  `latitude` double NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '缺货',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of poi
-- ----------------------------
INSERT INTO `poi` VALUES (1, 1, '康定市孔玉乡伐木场', '峨日村村民委员会西南侧240米', 101.988816, 30.411755, '缺货');
INSERT INTO `poi` VALUES (2, 2, '八角楼木材加工厂', '八角楼乡卡仁布村318国道旁', 101.128291, 30.073322, '缺货');
INSERT INTO `poi` VALUES (3, 3, '纯伦家具厂', '凉山州纯伦教学设备有限公司西侧', 102.186323, 27.91118, '缺货');
INSERT INTO `poi` VALUES (4, 4, '双桥家具商场(双桥子专卖店)', '经华北路93号', 104.107666, 30.647225, '缺货');

-- ----------------------------
-- Table structure for poi_type
-- ----------------------------
DROP TABLE IF EXISTS `poi_type`;
CREATE TABLE `poi_type`  (
  `id` bigint(20) NOT NULL,
  `goods_type_id` bigint(20) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `father_node` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `child_node` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of poi_type
-- ----------------------------
INSERT INTO `poi_type` VALUES (1, 1, '伐木场', NULL, '加工厂');
INSERT INTO `poi_type` VALUES (2, 2, '加工厂', '伐木场', '制造厂');
INSERT INTO `poi_type` VALUES (3, 3, '制造厂', '加工厂', '家具城');
INSERT INTO `poi_type` VALUES (4, 4, '家具城', '制造厂', NULL);

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` bigint(20) NOT NULL,
  `car_id` bigint(20) NULL DEFAULT NULL,
  `goods_id` bigint(20) NULL DEFAULT NULL,
  `start_id` bigint(20) NULL DEFAULT NULL,
  `end_id` bigint(20) NULL DEFAULT NULL,
  `distance` double NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '待接取',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
