-- 新建用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE sys_user(
 `user_id` VARCHAR(50) PRIMARY KEY NOT NULL COMMENT '用户id',
 `phone` VARCHAR(11) DEFAULT NULL COMMENT '手机号',
 `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
 `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
 `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
 `unit` VARCHAR(50) DEFAULT NULL COMMENT '工作单位',
 `type` INT(1) DEFAULT 0 COMMENT '用户类型',
 `state` INT(1) DEFAULT 0 COMMENT '用户状态 0未激活 1正常 2封禁',
 `join_time` DATETIME DEFAULT NULL COMMENT '注册时间',
 `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
 `readnews_time` DATETIME DEFAULT NULL COMMENT '查看消息时间',
 `has_msg` INT(1) DEFAULT 0 COMMENT '是否有新消息 0无1有',
`verification_code` VARCHAR(6) DEFAULT NULL COMMENT '验证码'
);
-- 2017-09-07 新建文章表
DROP TABLE IF EXISTS `sys_article`;
CREATE TABLE `sys_article` (
  `ARTICLE_ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `ANTHOLOGY_ID` VARCHAR(50) DEFAULT NULL COMMENT '文集id',
  `ARTICLE_CONTENT` TEXT DEFAULT NULL COMMENT '内容',
  `CREATE_DATE` DATETIME DEFAULT NULL COMMENT '创建日期',
  `CREATE_BY` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `UPDATE_DATE` DATETIME DEFAULT NULL COMMENT '修改日期',
  `UPDATE_BY` VARCHAR(50) DEFAULT NULL COMMENT '修改人',
  `ARTICLE_TITLE` VARCHAR(500) DEFAULT NULL COMMENT '主题',
  `READING_NUMBER` INT(11) DEFAULT 0 COMMENT '阅读数',
  `IS_TOP` INT(2) DEFAULT 0 COMMENT '置顶',
  `TYPE` INT(1) DEFAULT 0 COMMENT '类型',
  `IS_DEL` INT(1) DEFAULT 0 COMMENT '是否有效 0有效 1无效',
  PRIMARY KEY (`ARTICLE_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 2017-09-09 新建消息表  
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message` (
  `message_id` VARCHAR(50) PRIMARY KEY COMMENT '消息id',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `title` VARCHAR(50) DEFAULT NULL COMMENT '消息标题',
  `content` VARCHAR(2000) DEFAULT NULL COMMENT '消息内容'
);
-- 2017-09-09 新建文集表
DROP TABLE IF EXISTS `sys_anthology`;
CREATE TABLE `sys_anthology` (
  `anthology_id` VARCHAR(50) PRIMARY KEY COMMENT '文集id',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT'创建人',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `title` VARCHAR(50) DEFAULT NULL COMMENT '文集名字',
   `is_del` INT(1) DEFAULT 0 COMMENT '是否删除 0已删 1未删'
);
-- 评论表 包括问答 评论 和讨论
DROP TABLE IF EXISTS `sys_comment`;
CREATE TABLE `sys_comment` (
  `comment_id` VARCHAR(50) PRIMARY KEY COMMENT 'id',
  `parent_id` VARCHAR(50) DEFAULT NULL COMMENT '上级id',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `content` TEXT DEFAULT NULL COMMENT '内容',
  `collect_num` INT(4) DEFAULT 0 COMMENT '收藏数',
  `praise_num` INT(4) DEFAULT 0 COMMENT '赞数',
   `comment_type` INT(1) DEFAULT 0 COMMENT '类型 0问题回答 1文章评论 2讨论'
) COMMENT '回复表';
-- 2017-09-11 文章表添加字段
ALTER TABLE sys_article ADD `IS_PUBLISH` INT(1) DEFAULT 0 COMMENT '是否发布 0未发布 1已发布';
-- 2017-09-12 用户表添加头像字段
alter table sys_user add avatar varchar(100) default null comment '头像地址';

-- 2017-09-12 新建表
DROP TABLE IF EXISTS `user_action`;
CREATE TABLE user_action(
  user_action_id VARCHAR(50) PRIMARY KEY COMMENT"主键",
  obj_id VARCHAR(50) DEFAULT NULL COMMENT "作用对象id",
  user_id VARCHAR(50) DEFAULT NULL COMMENT "操作人",
  action_type INT(1) DEFAULT 0 COMMENT "操作类型 0关注 1收藏 2赞同 3反对"
) COMMENT "用户操作表";

-- 20117-09-13 用户表新增字段
ALTER TABLE sys_user ADD `introduction` VARCHAR(50) DEFAULT NULL COMMENT '一句话自我介绍';
-- 20117-09-13 用户操作表新增字段
ALTER TABLE user_action ADD `obj_belong` VARCHAR(50) DEFAULT NULL COMMENT '作用对象的拥有人';

-- 2017-09-13 新建表
DROP TABLE IF EXISTS `sys_content`;
CREATE TABLE `sys_content` (
  `content_id` VARCHAR(50) NOT NULL COMMENT '主键',
  `parent_id` VARCHAR(50) DEFAULT NULL COMMENT '上级id',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `title` VARCHAR(50) DEFAULT NULL COMMENT '标题',
  `content` TEXT DEFAULT NULL COMMENT '正文内容',
  `state` INT(1) DEFAULT 1 COMMENT '状态 1正常 2加精 3置顶 4加精并置顶',
  `type` INT(1) DEFAULT 1 COMMENT '类型 1文章 2问题',
  `is_del` INT(1) DEFAULT 0 COMMENT '是否有效 0有效 1有效',
  `is_publish` INT(1) DEFAULT 0 COMMENT '是否发布 0未发布 1已发布',
  PRIMARY KEY (`content_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
-- 2017-09-13 更改操作表
ALTER TABLE user_action MODIFY COLUMN action_type  INT(1) DEFAULT 1 COMMENT '操作类型 1关注 2收藏 3赞同 4反对 5阅读',
ADD COLUMN `creat_time` DATETIME DEFAULT NULL COMMENT '操作时间';
-- 2017-09-13 内容表新增字段
ALTER TABLE sys_content ADD `publish_type` INT(1) DEFAULT 1 COMMENT '发布类型 1真名 2昵称 3匿名';
ALTER TABLE sys_content ADD `anthology_id` VARCHAR(50) DEFAULT NULL COMMENT '文集ID';

-- 2017-09-14 评论表更改
ALTER TABLE sys_comment ADD COLUMN `publish_type` INT(1) DEFAULT NULL COMMENT '发布类型 1真名 2昵称 3匿名',
MODIFY COLUMN comment_type INT(1) DEFAULT 1 COMMENT '回复类型 1文章评论 2问题回答 3讨论';
