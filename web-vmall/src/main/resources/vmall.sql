
drop Database if exists shopping;

Create Database shopping Character Set UTF8;

use shopping;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for evaluation
-- ----------------------------
DROP TABLE IF EXISTS `evaluation`;
CREATE TABLE `evaluation`  (
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`user_id`, `product_id`, `time`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  CONSTRAINT `evaluation_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_main` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `evaluation_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
  `id` int(11) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `key_word` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `price` int(11) NOT NULL,
  `counts` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES (1, 'Mate20 RS', 'HUAWEI Mate 20 RS 保时捷设计', 'Mate;20;手机;RS', 12999, 10000000, 1);
INSERT INTO `products` VALUES (2, 'Mate20 X', 'HUAWEI Mate 20 X 6GB+128GB 全网通版（宝石蓝）', 'Mate;20;手机;X', 4999, 1000, 1);
INSERT INTO `products` VALUES (3, 'Magic2 ', '荣耀Magic2 移动4G+ 8GB+128GB 渐变蓝', 'Magic;2;手机', 4299, 100, 1);
INSERT INTO `products` VALUES (4, 'MateBook 13', 'HUAWEI MateBook 13 全面屏轻薄性能本 皓月银 i7 8GB 512GB 独显', 'MatebBook;笔记本', 6399, 98, 2);
INSERT INTO `products` VALUES (5, '荣耀Waterplay', '荣耀Waterplay 防水影音平板 3GB+32GB WiFi版（皓月银）', '平板;Waterplay;荣耀', 1499, 200, 2);
INSERT INTO `products` VALUES (6, 'Smartwatch', 'PORSCHE DESIGN | HUAWEI Smartwatch 华为智能手表 保时捷联合设计（曜石黑）', '智能手表;保时捷', 4988, 20, 3);
INSERT INTO `products` VALUES (7, '荣耀手表', '荣耀手表 S1 表体（深空灰）+长腕带（黑色）', '手表;荣耀', 449, 999, 3);
INSERT INTO `products` VALUES (8, '空气净化器', 'KARCHER卡赫双滤芯多功能空气净化器KA5 白色（支持HUAWEI HiLink）', '空气净化器', 4490, 100, 4);

-- ----------------------------
-- Table structure for shopping_car
-- ----------------------------
DROP TABLE IF EXISTS `shopping_car`;
CREATE TABLE `shopping_car`  (
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_price` int(11) NOT NULL,
  `counts` int(11) NOT NULL,
  PRIMARY KEY (`user_id`, `product_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  CONSTRAINT `shopping_car_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_main` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `shopping_car_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for shopping_record
-- ----------------------------
DROP TABLE IF EXISTS `shopping_record`;
CREATE TABLE `shopping_record`  (
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `time` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_status` int(11) NOT NULL,
  `product_price` int(11) NOT NULL,
  `counts` int(11) NOT NULL,
  PRIMARY KEY (`user_id`, `product_id`, `time`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  CONSTRAINT `shopping_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_main` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `shopping_record_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_detail
-- ----------------------------
DROP TABLE IF EXISTS `user_detail`;
CREATE TABLE `user_detail`  (
  `id` int(11) NOT NULL,
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sex` int(11) NOT NULL,
  `birthday` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `post_number` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `address` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `register_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `user_detail_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user_main` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_detail
-- ----------------------------
INSERT INTO `user_detail` VALUES (1, 'admin', '', 0, '', '', '', '2019-02-20 11:18:54');
INSERT INTO `user_detail` VALUES (2, 'user01', '', 0, '', '', '', '2019-02-20 11:19:25');

-- ----------------------------
-- Table structure for user_main
-- ----------------------------
DROP TABLE IF EXISTS `user_main`;
CREATE TABLE `user_main`  (
  `id` int(11) NOT NULL,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `nick_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_main
-- ----------------------------
INSERT INTO `user_main` VALUES (1, 'admin', 'admin', '管理员', 1);
INSERT INTO `user_main` VALUES (2, 'user01', 'user01', '用户1', 0);

SET FOREIGN_KEY_CHECKS = 1;
