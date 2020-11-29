# springboot-project
SpringBoot微服务整合WebSocket页面聊天项目

---------------------------------------------------------------------------------------------------------------
1、sql脚本：
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t1_user
-- ----------------------------
DROP TABLE IF EXISTS `t1_user`;
CREATE TABLE `t1_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `passwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t1_user
-- ----------------------------
INSERT INTO `t1_user` VALUES (1, '1', 'xgx');
INSERT INTO `t1_user` VALUES (2, '1', 'myn');
INSERT INTO `t1_user` VALUES (3, '1', 'xhz');

SET FOREIGN_KEY_CHECKS = 1;
---------------------------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------------------------
2、修改application.properties配置文件为你自己的数据库配置即可
---------------------------------------------------------------------------------------------------------------
