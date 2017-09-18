/*
SQLyog v10.2 
MySQL - 5.5.40 : Database - zxk
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`zxk` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `zxk`;

/*Table structure for table `sys_anthology` */

DROP TABLE IF EXISTS `sys_anthology`;

CREATE TABLE `sys_anthology` (
  `anthology_id` varchar(50) NOT NULL COMMENT '文集id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `title` varchar(50) DEFAULT NULL COMMENT '文集名字',
  `is_del` int(1) DEFAULT '0' COMMENT '是否删除 0未删 1已删',
  PRIMARY KEY (`anthology_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sys_comment` */

DROP TABLE IF EXISTS `sys_comment`;

CREATE TABLE `sys_comment` (
  `comment_id` varchar(50) NOT NULL COMMENT 'id',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '上级id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `content` mediumtext COMMENT '内容',
  `collect_num` int(4) DEFAULT '0' COMMENT '收藏数',
  `praise_num` int(4) DEFAULT '0' COMMENT '赞数',
  `comment_type` int(1) DEFAULT '1' COMMENT '回复类型 1文章评论 2问题回答 3讨论',
  `publish_type` int(1) DEFAULT '1' COMMENT '发布类型 1真名 2昵称 3匿名',
  `publish_time` datetime DEFAULT NULL COMMENT '发表时间',
  `is_del` int(1) DEFAULT '0' COMMENT '是否删除 0未删 1已删',
  `is_publish` int(1) DEFAULT '0' COMMENT '是否发布 0未发布 1已发布',
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='回复表';

/*Table structure for table `sys_content` */

DROP TABLE IF EXISTS `sys_content`;

CREATE TABLE `sys_content` (
  `content_id` varchar(50) NOT NULL COMMENT '主键',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '上级id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `title` varchar(50) DEFAULT NULL COMMENT '标题',
  `content` varchar(2000) DEFAULT NULL COMMENT '正文内容',
  `state` int(1) DEFAULT '1' COMMENT '状态 1正常 2加精 3置顶 4加精并置顶',
  `type` int(1) DEFAULT '1' COMMENT '类型 1文章 2问题',
  `is_del` int(1) DEFAULT '0' COMMENT '是否有效 0有效 1无效',
  `is_publish` int(1) DEFAULT '0' COMMENT '是否发布 0未发布 1已发布',
  `publish_type` int(1) DEFAULT '1' COMMENT '发布类型 1真名 2昵称 3匿名',
  `anthology_id` varchar(50) DEFAULT NULL COMMENT '文集ID',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sys_message` */

DROP TABLE IF EXISTS `sys_message`;

CREATE TABLE `sys_message` (
  `message_id` varchar(50) NOT NULL COMMENT '消息id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `title` varchar(50) DEFAULT NULL COMMENT '消息标题',
  `content` varchar(2000) DEFAULT NULL COMMENT '消息内容',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `user_id` varchar(50) NOT NULL COMMENT '用户id',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `unit` varchar(50) DEFAULT NULL COMMENT '工作单位',
  `type` int(1) DEFAULT '0' COMMENT '用户类型',
  `state` int(1) DEFAULT '0' COMMENT '用户状态 0未激活 1正常 2封禁',
  `join_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `readnews_time` datetime DEFAULT NULL COMMENT '查看消息时间',
  `has_msg` int(1) DEFAULT '0' COMMENT '是否有新消息 0无1有',
  `verification_code` varchar(6) DEFAULT NULL COMMENT '验证码',
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像地址',
  `introduction` varchar(50) DEFAULT NULL COMMENT '一句话自我介绍',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `user_action` */

DROP TABLE IF EXISTS `user_action`;

CREATE TABLE `user_action` (
  `user_action_id` varchar(50) NOT NULL COMMENT '主键',
  `obj_id` varchar(50) DEFAULT NULL COMMENT '作用对象id',
  `user_id` varchar(50) DEFAULT NULL COMMENT '操作人',
  `action_type` int(1) DEFAULT '1' COMMENT '操作类型 1关注 2收藏 3赞同 4反对 5阅读',
  `creat_time` datetime DEFAULT NULL COMMENT '操作时间',
  `obj_parentid` varchar(50) DEFAULT '' COMMENT '作用对象的上级id',
  `obj_belong` varchar(50) DEFAULT NULL COMMENT '作用对象的拥有人',
  PRIMARY KEY (`user_action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户操作表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
