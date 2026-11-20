-- ----------------------------
-- 个人待办：增量脚本
--
-- 包含三部分：表结构、字典、菜单与按钮权限。
-- 直接对已初始化的 ruoyi-vue-pro 库执行即可，重复执行前请先自行确认（建表语句带 DROP）。
-- ----------------------------

-- ----------------------------
-- 1. 表结构
-- ----------------------------
DROP TABLE IF EXISTS `system_todo`;
CREATE TABLE `system_todo`  (
  `id`          bigint        NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id`     bigint        NOT NULL COMMENT '所属用户编号',
  `title`       varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `priority`    tinyint       NOT NULL DEFAULT 2 COMMENT '优先级，参见 system_todo_priority 字典（1高 2中 3低）',
  `status`      tinyint       NOT NULL DEFAULT 0 COMMENT '状态，参见 system_todo_status 字典（0待处理 1进行中 2已完成）',
  `deadline`    datetime      NULL DEFAULT NULL COMMENT '截止时间',
  `finish_time` datetime      NULL DEFAULT NULL COMMENT '完成时间',
  `remark`      varchar(500)  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator`     varchar(64)   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater`     varchar(64)   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`     bit(1)        NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id`   bigint        NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_deadline`(`deadline`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '个人待办表';

-- ----------------------------
-- 2. 字典：待办状态、待办优先级
-- ----------------------------
INSERT INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES ('待办状态',   'system_todo_status',   0, '个人待办的状态',   'admin', NOW(), 'admin', NOW(), b'0', NULL),
       ('待办优先级', 'system_todo_priority', 0, '个人待办的优先级', 'admin', NOW(), 'admin', NOW(), b'0', NULL);

INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (1, '待处理', '0', 'system_todo_status',   0, 'info',    '', NULL, 'admin', NOW(), 'admin', NOW(), b'0'),
       (2, '进行中', '1', 'system_todo_status',   0, 'primary', '', NULL, 'admin', NOW(), 'admin', NOW(), b'0'),
       (3, '已完成', '2', 'system_todo_status',   0, 'success', '', NULL, 'admin', NOW(), 'admin', NOW(), b'0'),
       (1, '高',     '1', 'system_todo_priority', 0, 'danger',  '', NULL, 'admin', NOW(), 'admin', NOW(), b'0'),
       (2, '中',     '2', 'system_todo_priority', 0, 'warning', '', NULL, 'admin', NOW(), 'admin', NOW(), b'0'),
       (3, '低',     '3', 'system_todo_priority', 0, 'info',    '', NULL, 'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- 3. 菜单与按钮权限：挂在「系统管理」（id = 1）下
-- ----------------------------
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('个人待办', '', 2, 21, 1, 'todo', 'ep:finished', 'system/todo/index', 'SystemTodo', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 按钮的父菜单编号
SELECT @todoParentId := LAST_INSERT_ID();

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('个人待办查询', 'system:todo:query',  3, 1, @todoParentId, '', '', '', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
       ('个人待办创建', 'system:todo:create', 3, 2, @todoParentId, '', '', '', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
       ('个人待办更新', 'system:todo:update', 3, 3, @todoParentId, '', '', '', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
       ('个人待办删除', 'system:todo:delete', 3, 4, @todoParentId, '', '', '', 0, 'admin', NOW(), 'admin', NOW(), b'0');

-- 说明：状态变更接口复用 system:todo:update 权限，不单独建按钮。
-- 超级管理员（super_admin）默认拥有全部权限；其它角色需在「系统管理 - 角色管理 - 菜单权限」中勾选后生效。