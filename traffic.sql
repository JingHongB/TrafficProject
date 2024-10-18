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

 Date: 18/10/2024 18:14:28
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
INSERT INTO `car` VALUES (1847219126321258496, 3, 106.328609, 29.708383, '空闲');
INSERT INTO `car` VALUES (1847219126321258497, 2, 108.020767, 28.504098, '空闲');
INSERT INTO `car` VALUES (1847219126321258498, 3, 102.323872, 26.5243, '空闲');
INSERT INTO `car` VALUES (1847219126321258499, 2, 105.466733, 28.234332, '空闲');
INSERT INTO `car` VALUES (1847219126321258500, 1, 100.573299, 29.543171, '空闲');
INSERT INTO `car` VALUES (1847219126321258501, 3, 100.108491, 33.505063, '空闲');
INSERT INTO `car` VALUES (1847219126321258502, 2, 107.093824, 27.75261, '空闲');
INSERT INTO `car` VALUES (1847219126321258503, 2, 98.550603, 26.837958, '空闲');
INSERT INTO `car` VALUES (1847219126321258504, 2, 106.166702, 28.458029, '空闲');
INSERT INTO `car` VALUES (1847219126321258505, 2, 101.040451, 26.774938, '空闲');

-- ----------------------------
-- Table structure for car_type
-- ----------------------------
DROP TABLE IF EXISTS `car_type`;
CREATE TABLE `car_type`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `load_capacity` double NULL DEFAULT NULL,
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
INSERT INTO `goods_type` VALUES (5, '生铁', 15, 20, NULL, 6, 80);
INSERT INTO `goods_type` VALUES (6, '熟铁', 13, 18, 5, 7, 80);
INSERT INTO `goods_type` VALUES (7, '铁板', 11, 16, 6, 8, 80);
INSERT INTO `goods_type` VALUES (8, '铁制品', 11, 16, 7, NULL, 80);

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
INSERT INTO `poi` VALUES (1846450881562320896, 2, '白玉县金诺图文广告加工厂', '河东上街213-215号', 98.830082, 31.205861, '缺货');
INSERT INTO `poi` VALUES (1846450881600069632, 2, '八角楼木材加工厂', '八角楼乡卡仁布村318国道旁', 101.128291, 30.073322, '缺货');
INSERT INTO `poi` VALUES (1846451223104495616, 3, '御美家衣柜工厂', '高阳街道大石板社区兴回路二段9号', 102.148179, 28.53127, '缺货');
INSERT INTO `poi` VALUES (1846451223117078528, 3, '金鑫匠整装定制工厂', '市城南街道城南社区十二组', 102.250098, 26.637793, '缺货');
INSERT INTO `poi` VALUES (1846451223129661441, 3, '九O九设备制造厂', '德怡桥头火锅店西北侧120米', 103.540676, 29.683982, '缺货');
INSERT INTO `poi` VALUES (1846815811947888640, 3, '艺居尚全屋定制厂', '永利街384号', 105.157111, 31.61571, '缺货');
INSERT INTO `poi` VALUES (1846816499243405313, 3, '四川启鸿家具制造有限公司', '阳光林森路与帝标路交叉口西240米', 104.000336, 30.856272, '缺货');
INSERT INTO `poi` VALUES (1846819650738475008, 4, '居然之家(昆明路店)', '昆明路世贸城C区', 106.941794, 27.709404, '缺货');
INSERT INTO `poi` VALUES (1846820006763581440, 4, '红星美凯龙达州金龙商场', '西外镇金龙大道666号中迪广场', 107.461151, 31.234754, '缺货');
INSERT INTO `poi` VALUES (1846820006830690305, 4, '唐老鸭家具城', '任市镇正南街107号', 107.784645, 30.896672, '缺货');
INSERT INTO `poi` VALUES (1846820277996638208, 4, '居然之家国际馆', '巴州区经济开发区西部国际商贸城', 106.790468, 31.847112, '缺货');
INSERT INTO `poi` VALUES (1846820278072135680, 4, '明珠家具城', '南江镇县城内大堂坝', 106.843132, 32.35271, '缺货');
INSERT INTO `poi` VALUES (1846820461547769856, 4, '文川百合家具城', '文川镇南街文川移民新村公交站牌处', 107.175375, 33.205623, '缺货');
INSERT INTO `poi` VALUES (1846820693488586752, 4, '麒麟家具城(曲靖三江大道永安路店)', '三江大道建宁街道办事处旁', 103.78972, 25.513698, '缺货');
INSERT INTO `poi` VALUES (1846820868051324928, 4, '鸿广家具城', '061乡道与060乡道交叉口北240米', 104.266094, 26.106031, '缺货');
INSERT INTO `poi` VALUES (1846821413822554112, 4, '梦圆家具批发商场(景隆欣苑店)', '	\r\n景隆欣苑西门旁', 105.290676, 27.312539, '缺货');
INSERT INTO `poi` VALUES (1846821689254109184, 4, '宜家家居(远东·西景花苑店)', '城关镇钟楼滩兴朝美居广场四楼', 104.914939, 33.398453, '缺货');
INSERT INTO `poi` VALUES (1846822472515239936, 3, '福盛木工厂', '德吉路东北侧', 95.59333, 31.412437, '缺货');
INSERT INTO `poi` VALUES (1846822825004548097, 3, '得鹿全屋定制工厂', '梁家寨二组61号', 106.495973, 26.568277, '缺货');
INSERT INTO `poi` VALUES (1846823141682888704, 3, '	\r\n阿坝县木材加工厂', '崇拉街与阿曲路交叉口东北140米', 101.694377, 32.903033, '缺货');
INSERT INTO `poi` VALUES (1846823552678543360, 3, '鑫发柏木家具厂', '光良再生资源回收(玉清店)东侧100米', 105.169377, 29.930826, '缺货');
INSERT INTO `poi` VALUES (1846823953054220289, 3, '豪峰定制家居工厂', '南城街道', 106.046639, 29.829682, '缺货');
INSERT INTO `poi` VALUES (1846825104944324608, 2, '盐源鸿一木业制品厂', '柳树湾村三组27号', 101.430766, 27.466874, '缺货');
INSERT INTO `poi` VALUES (1846825371291017217, 2, '雄森家具厂', '青龙南路与文华段交叉口西北460米', 100.206, 26.844173, '缺货');
INSERT INTO `poi` VALUES (1846837904525680640, 2, '定制家具厂', '沿川木工加工厂北侧', 108.388735, 28.441682, '缺货');
INSERT INTO `poi` VALUES (1846838605930749952, 2, '四玥衣柜厂', '西溪坪国际建材城对面打鼓台', 110.52326, 29.109778, '缺货');
INSERT INTO `poi` VALUES (1846839035167432704, 2, '森予全屋定制加工厂', '谭家塝路与夷兴大道交叉口西北260米', 111.352394, 30.793266, '缺货');
INSERT INTO `poi` VALUES (1846839655156826112, 2, '博凡尼全屋定制工厂', '职业中等专业学校南侧270米', 109.135479, 27.368953, '缺货');
INSERT INTO `poi` VALUES (1846840017511137280, 2, '文河竹木加工厂', '大福镇民利村8号', 111.858013, 28.312332, '缺货');
INSERT INTO `poi` VALUES (1846840463210463234, 2, '谭坝镇王氏木材加工厂', '家家乐明田超市西南侧', 108.969391, 32.910122, '缺货');
INSERT INTO `poi` VALUES (1846850191722455040, 1, '博雅林场', '狮山镇禄金庄村内', 102.436646, 25.379701, '缺货');
INSERT INTO `poi` VALUES (1846850284580151296, 1, '西林县国有王子山林场古障分场', '321省道与357国道交叉口西500米', 104.715075, 24.497412, '缺货');
INSERT INTO `poi` VALUES (1846850363407900672, 1, '交翁林场', '龙胜小九寨瀑布山庄西侧230米', 109.951053, 25.705957, '缺货');
INSERT INTO `poi` VALUES (1846850468995309568, 1, '谷城县国营跃进林场', '石昝路67号', 111.41521, 32.243664, '缺货');
INSERT INTO `poi` VALUES (1846850686654521344, 1, '苏江林场', '宏羽加油站西南侧130米', 98.284924, 25.337348, '缺货');
INSERT INTO `poi` VALUES (1846850812429115392, 1, '李子园林场娘娘坝营林区', '娘娘坝镇李子村上店子1号', 105.837086, 34.284889, '缺货');
INSERT INTO `poi` VALUES (1846850975243608064, 1, '道孚国有林保护管理局麻孜林场', '中国熊猫大道', 100.957447, 31.125197, '缺货');
INSERT INTO `poi` VALUES (1846851126511181824, 1, '昌都林场', '林业加油站西北侧270米', 97.183924, 31.171271, '缺货');
INSERT INTO `poi` VALUES (1846851278084939776, 1, '太安林场', '太安镇马坊村', 109.002573, 35.388506, '缺货');
INSERT INTO `poi` VALUES (1846851458477760512, 1, '重庆市巫溪县白果林场', '城厢镇太平路56号', 109.615626, 31.395642, '缺货');
INSERT INTO `poi` VALUES (1846872686940807168, 5, '平川镇平川铁矿', '[]', 101.858199, 27.63431, '缺货');
INSERT INTO `poi` VALUES (1846872686940807169, 5, '朱兰铁矿分公司', '倮密路与五湖路交叉口南140米', 101.765655, 26.617234, '缺货');
INSERT INTO `poi` VALUES (1846872686945001473, 5, '喜德县明洋铁矿', '[]', 102.335579, 28.326305, '缺货');
INSERT INTO `poi` VALUES (1846872686949195777, 5, '宏达矿业有限责任公司李家河铁矿', '水磨镇白玉村二组', 106.661706, 32.465291, '缺货');
INSERT INTO `poi` VALUES (1846873174948077568, 5, '接龙铁矿重钢集团矿业有限公司', '渝道路北50米', 106.77631, 29.255168, '缺货');
INSERT INTO `poi` VALUES (1846873672623218688, 5, '洋县钒钛磁铁矿有限责任公司', '桑溪镇', 108.013801, 33.202093, '缺货');
INSERT INTO `poi` VALUES (1846874446799462400, 5, '蓉谊商贸华兴团林采矿场', '团林村村民委员会东南侧270米', 106.101755, 29.575718, '缺货');
INSERT INTO `poi` VALUES (1846877862560997376, 5, '贵州众泰工贸有限公司选矿厂', '	\r\n930县道', 107.393842, 27.023083, '缺货');
INSERT INTO `poi` VALUES (1846879871884271616, 6, '都江堰市德胜锻造厂', '蒲阳镇拦厢村6组16号', 103.697563, 31.036616, '缺货');
INSERT INTO `poi` VALUES (1846879871888465920, 6, '欧瑞铁艺锻造厂', '仁筑路西南40米', 103.983963, 30.798696, '缺货');
INSERT INTO `poi` VALUES (1846880259454738432, 6, '自贡市森泰锻造有限公司', '班中路东南侧', 104.64873, 29.341489, '缺货');
INSERT INTO `poi` VALUES (1846880383568388097, 6, '綦江全益锻造厂', '136乡道南50米', 106.661249, 28.948457, '缺货');
INSERT INTO `poi` VALUES (1846880728264679424, 6, '安顺市西秀区七眼桥春喜锻造厂', '安紫高速', 106.044373, 26.288366, '缺货');
INSERT INTO `poi` VALUES (1846880851099066368, 6, '兴盛锻造厂', '103省道西50米', 103.871703, 30.255647, '缺货');
INSERT INTO `poi` VALUES (1846882759197007872, 7, '兔宝宝健康板材全屋定制', '文南路店24号副42号', 103.472351, 30.407805, '缺货');
INSERT INTO `poi` VALUES (1846883167348924417, 7, '福森建材批发(美吉特家居建材科技博览城店)', '旭阳镇荣州大道三段美吉特家居建材城刘栋101号', 104.399069, 29.454549, '缺货');
INSERT INTO `poi` VALUES (1846883343241256961, 7, '友盛板材批发部', '247乡道与金象路交叉口东南180米', 105.339185, 29.150487, '缺货');
INSERT INTO `poi` VALUES (1846883529610960896, 7, '智阁板材(奥韵康城店)', '清泉街与怡兴街交叉口西20米', 106.290821, 30.336849, '缺货');
INSERT INTO `poi` VALUES (1846883754954137600, 7, '金顺不锈钢板材折弯加工', '御善桥旁', 104.241484, 28.093139, '缺货');
INSERT INTO `poi` VALUES (1846883934269022211, 7, '板材五金批发中心', '城市供排水南侧330米', 105.987467, 31.552355, '缺货');
INSERT INTO `poi` VALUES (1846884082059517952, 7, '长华板材', '达外路与三星路交叉口西北120米', 106.959347, 30.861284, '缺货');
INSERT INTO `poi` VALUES (1846884510419591168, 7, '明宇轩门窗加工厂', '顺丰速运(永善店)西南侧240米', 103.643125, 28.224375, '缺货');
INSERT INTO `poi` VALUES (1846885578608156672, 7, '莫干山板材(中央领御店)', '中央领御', 104.358403, 28.821814, '缺货');
INSERT INTO `poi` VALUES (1846885954434572289, 7, '德阳东远建材厂', '四川通力线缆有限公司东北侧190米', 104.183439, 31.312783, '缺货');
INSERT INTO `poi` VALUES (1846886405007679489, 8, '标件五金工具', '普州大道264号2幢2单元504晶盛花园2期', 105.351738, 30.110897, '缺货');
INSERT INTO `poi` VALUES (1846886678627295235, 8, '舒氏商贸五金电器批发', '文林镇英雄广场', 104.148274, 30.013035, '缺货');
INSERT INTO `poi` VALUES (1846886821460123650, 8, '日用五金店', '重龙镇中东街86号', 104.855035, 29.775579, '缺货');
INSERT INTO `poi` VALUES (1846887034446880768, 8, '未来万家五金机电(大英店)', '蓬乐街136号', 105.24759, 30.582182, '缺货');
INSERT INTO `poi` VALUES (1846887187614474240, 8, '舒氏商贸五金电器批发', '文林镇英雄广场', 104.148274, 30.013035, '缺货');
INSERT INTO `poi` VALUES (1846887337929940992, 8, '联升五金(杜家坝安置房店)', '昌州街道联升大桥安置房3栋', 105.594798, 29.422951, '缺货');
INSERT INTO `poi` VALUES (1846887487310077952, 8, '园林五金机电(荷塘晓月南区店)', '红河路185-187号', 105.695209, 28.581964, '缺货');
INSERT INTO `poi` VALUES (1846887618314969088, 8, '渝北区国际五金机电城', '回兴街道兰馨大道2号', 106.604178, 29.696305, '缺货');
INSERT INTO `poi` VALUES (1846887812075036674, 8, '金牛管金海五金', '希望城7一19门面', 105.055078, 28.724433, '缺货');
INSERT INTO `poi` VALUES (1846887998365048832, 8, '大奔机电五金超市(柏亚·滨江城店)', '柏亚·滨江城2期', 106.427893, 31.01903, '缺货');

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
INSERT INTO `poi_type` VALUES (1, 1, '林场', NULL, 2);
INSERT INTO `poi_type` VALUES (2, 2, '加工厂', 1, 3);
INSERT INTO `poi_type` VALUES (3, 3, '制造厂', 2, 4);
INSERT INTO `poi_type` VALUES (4, 4, '家具城', 3, NULL);
INSERT INTO `poi_type` VALUES (5, 5, '铁矿', NULL, 6);
INSERT INTO `poi_type` VALUES (6, 6, '锻造厂', 5, 7);
INSERT INTO `poi_type` VALUES (7, 7, '板材厂', 6, 8);
INSERT INTO `poi_type` VALUES (8, 8, '五金店', 7, NULL);

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
