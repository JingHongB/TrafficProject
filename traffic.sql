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

 Date: 16/10/2024 16:13:46
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of car
-- ----------------------------
INSERT INTO `car` VALUES (1846464374478077952, 2, 103.320696, 26.321811, '空闲');
INSERT INTO `car` VALUES (1846464374478077953, 2, 106.272307, 27.798087, '空闲');
INSERT INTO `car` VALUES (1846464374478077954, 3, 104.832953, 30.479594, '空闲');
INSERT INTO `car` VALUES (1846464374478077955, 2, 104.613243, 28.385766, '空闲');
INSERT INTO `car` VALUES (1846464374478077956, 2, 97.650874, 30.587228, '空闲');

-- ----------------------------
-- Table structure for car_type
-- ----------------------------
DROP TABLE IF EXISTS `car_type`;
CREATE TABLE `car_type`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `load` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

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
  `father_node` bigint(20) NULL DEFAULT NULL,
  `child_node` bigint(20) NULL DEFAULT NULL,
  `time` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of goods_type
-- ----------------------------
INSERT INTO `goods_type` VALUES (1, '原木', 12.7, 5.6, NULL, 2, 40);
INSERT INTO `goods_type` VALUES (2, '木板', 11.7, 4.6, 1, 3, 40);
INSERT INTO `goods_type` VALUES (3, '家具', 10.7, 3.6, 2, 4, 40);
INSERT INTO `goods_type` VALUES (4, '商品', 10.7, 3.6, 3, NULL, 40);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of poi
-- ----------------------------
INSERT INTO `poi` VALUES (1846394619181076480, 1, '康定市孔玉乡伐木场', '峨日村村民委员会西南侧240米', 101.988816, 30.411755, '缺货');
INSERT INTO `poi` VALUES (1846394619189465088, 1, '盐边县红果彝族乡伐木厂', '[]', 101.795675, 26.804525, '缺货');
INSERT INTO `poi` VALUES (1846394619189465089, 1, '米易县得石镇伐木厂', '[]', 101.799325, 26.809075, '缺货');
INSERT INTO `poi` VALUES (1846394619193659392, 1, '荥经县龙苍沟镇泡草湾伐木场', '龙苍沟叠翠溪景区(西北角)', 102.884775, 29.625425, '缺货');
INSERT INTO `poi` VALUES (1846394619197853696, 1, '五月沟伐木场', '149县道东100米', 103.045595, 28.782853, '缺货');
INSERT INTO `poi` VALUES (1846394619197853697, 1, '二八四伐木厂', '锦城镇县交警队旁', 103.581195, 28.266253, '缺货');
INSERT INTO `poi` VALUES (1846450881562320896, 2, '白玉县金诺图文广告加工厂', '河东上街213-215号', 98.830082, 31.205861, '缺货');
INSERT INTO `poi` VALUES (1846450881600069632, 2, '八角楼木材加工厂', '八角楼乡卡仁布村318国道旁', 101.128291, 30.073322, '缺货');
INSERT INTO `poi` VALUES (1846450881600069633, 2, '西昌市诚信塑料加工厂', '袁马路64号', 102.22801, 27.859198, '缺货');
INSERT INTO `poi` VALUES (1846450881604263936, 2, '雅通玻璃加工厂', '建设路与步行街交叉口北160米', 102.765618, 30.061962, '缺货');
INSERT INTO `poi` VALUES (1846450881608458240, 2, '茗源茶叶加工厂', '麻柳湾23号', 103.118247, 30.489333, '缺货');
INSERT INTO `poi` VALUES (1846450881608458241, 2, '名山区马坝精米加工厂', '车岭镇马坝精米加工厂', 103.243991, 30.082901, '缺货');
INSERT INTO `poi` VALUES (1846450881616846848, 2, '成学竹木粗加工厂', '4组23号', 103.453267, 30.635029, '缺货');
INSERT INTO `poi` VALUES (1846450881621041152, 2, '云松木材加工厂', '将军乡杨场村2组', 103.394434, 29.874195, '缺货');
INSERT INTO `poi` VALUES (1846450881625235456, 2, '怀远志明木器加工厂', '[]', 103.553962, 30.759685, '缺货');
INSERT INTO `poi` VALUES (1846450881650401280, 2, '崇州市元通顺超机械加工厂', '禹王村十组15号', 103.605272, 30.735378, '缺货');
INSERT INTO `poi` VALUES (1846451223104495616, 3, '御美家衣柜工厂', '高阳街道大石板社区兴回路二段9号', 102.148179, 28.53127, '缺货');
INSERT INTO `poi` VALUES (1846451223108689920, 3, '柏里居全屋定制家具工厂', '易发木门厂东南侧', 102.20978, 27.95214, '缺货');
INSERT INTO `poi` VALUES (1846451223117078528, 3, '金鑫匠整装定制工厂', '市城南街道城南社区十二组', 102.250098, 26.637793, '缺货');
INSERT INTO `poi` VALUES (1846451223121272832, 3, '四川华蜀机械制造有限公司', '金沙村3组', 103.412744, 29.858516, '缺货');
INSERT INTO `poi` VALUES (1846451223125467136, 3, '中念家具工厂', '石羊镇中崇路', 103.636489, 30.865657, '缺货');
INSERT INTO `poi` VALUES (1846451223125467137, 3, '四川岷阳精机制造有限公司', '九鼎大道与拥军路交叉口东南220米', 103.661314, 31.030792, '缺货');
INSERT INTO `poi` VALUES (1846451223129661440, 3, '四川禾芯机械制造有限公司', '都江堰市经济开发区堰华路623号', 103.675131, 31.03471, '缺货');
INSERT INTO `poi` VALUES (1846451223129661441, 3, '九O九设备制造厂', '德怡桥头火锅店西北侧120米', 103.540676, 29.683982, '缺货');
INSERT INTO `poi` VALUES (1846451223133855744, 3, '明珠家具集团公司制造二厂', '明珠路与学府路交叉口东南240米', 103.716202, 30.686136, '缺货');
INSERT INTO `poi` VALUES (1846451223142244352, 3, '雅庄家具制造厂', '甘江镇大石桥社区11社', 103.613285, 29.719783, '缺货');
INSERT INTO `poi` VALUES (1846451593981632512, 4, '双桥家具商场(双桥子专卖店)', '经华北路93号', 104.107666, 30.647225, '缺货');
INSERT INTO `poi` VALUES (1846451593985826816, 4, '红星美凯龙成都至尊MALL', '二环路西一段6号', 104.029842, 30.63863, '缺货');
INSERT INTO `poi` VALUES (1846451593994215424, 4, '宜家家居(成都高新商场店)', '益州大道北段9号', 104.062078, 30.599759, '缺货');
INSERT INTO `poi` VALUES (1846451593998409728, 4, '红星美凯龙成都佳灵商场', '佳灵路9号1栋', 104.027131, 30.630542, '缺货');
INSERT INTO `poi` VALUES (1846451593998409729, 4, '居然之家(金牛店)', '一品天下大街399号', 104.028109, 30.695579, '缺货');
INSERT INTO `poi` VALUES (1846451594002604032, 4, '太平园家私广场', '佳灵路215号', 104.017651, 30.619552, '缺货');
INSERT INTO `poi` VALUES (1846451594002604033, 4, '居然之家成都琉璃总店', '工业园区锦华路三段88号', 104.093952, 30.592729, '缺货');
INSERT INTO `poi` VALUES (1846451594006798336, 4, '成都八益家具城', '成双大道北段', 104.011206, 30.615705, '缺货');
INSERT INTO `poi` VALUES (1846451594010992640, 4, '八益家具城', '成双大道北段69号', 104.012437, 30.613926, '缺货');
INSERT INTO `poi` VALUES (1846451594010992641, 4, '宜家家居(成华店)', '蓉都大道将军碑路9号(川陕立交西北角)', 104.10475, 30.721206, '缺货');

-- ----------------------------
-- Table structure for poi_type
-- ----------------------------
DROP TABLE IF EXISTS `poi_type`;
CREATE TABLE `poi_type`  (
  `id` bigint(20) NOT NULL,
  `goods_type_id` bigint(20) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `father_node` bigint(20) NULL DEFAULT NULL,
  `child_node` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of poi_type
-- ----------------------------
INSERT INTO `poi_type` VALUES (1, 1, '伐木场', NULL, 2);
INSERT INTO `poi_type` VALUES (2, 2, '加工厂', 1, 3);
INSERT INTO `poi_type` VALUES (3, 3, '制造厂', 2, 4);
INSERT INTO `poi_type` VALUES (4, 4, '家具城', 3, NULL);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task
-- ----------------------------

-- ----------------------------
-- Triggers structure for table task
-- ----------------------------
DROP TRIGGER IF EXISTS `task_status`;
delimiter ;;
CREATE TRIGGER `task_status` AFTER UPDATE ON `task` FOR EACH ROW BEGIN
	IF NEW.status = '完成' THEN
		UPDATE car SET status = '空闲'
		WHERE id = NEW.car_id;

	  UPDATE goods SET status = '已到达'
    WHERE id = NEW.goods_id;

	  UPDATE poi SET status = '有货'
    WHERE id = NEW.end_id;
	END IF;

	IF NEW.status = '任务中' THEN
		UPDATE car SET status = '运输'
		WHERE id = NEW.car_id;

	  UPDATE goods SET status = '运输中'
    WHERE id = NEW.goods_id;

	  UPDATE poi SET status = '缺货'
    WHERE id = NEW.start_id;
	END IF;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
