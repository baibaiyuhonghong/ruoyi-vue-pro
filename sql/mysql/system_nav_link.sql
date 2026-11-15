-- ----------------------------
-- 常用网址导航：增量脚本
--
-- 包含三部分：表结构、字典、菜单与按钮权限。
-- 直接对已初始化的 ruoyi-vue-pro 库执行即可，重复执行前请先自行确认（建表语句带 DROP）。
-- ----------------------------

-- ----------------------------
-- 1. 表结构
-- ----------------------------
DROP TABLE IF EXISTS `system_nav_link`;
CREATE TABLE `system_nav_link`  (
  `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name`        varchar(50)  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `url`         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '网址',
  `category`    tinyint      NOT NULL COMMENT '分类，参见 system_nav_link_category 字典',
  `icon`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '图标',
  `sort`        int          NOT NULL DEFAULT 0 COMMENT '排序',
  `status`      tinyint      NOT NULL DEFAULT 0 COMMENT '状态，参见 CommonStatusEnum 枚举（0正常 1停用）',
  `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator`     varchar(64)  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater`     varchar(64)  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`     bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id`   bigint       NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '常用网址导航表';

-- ----------------------------
-- 2. 字典：网址导航分类
-- ----------------------------
INSERT INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES ('网址导航分类', 'system_nav_link_category', 0, '常用网址导航的分类', 'admin', NOW(), 'admin', NOW(), b'0', NULL);

INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (1, '公司系统', '1', 'system_nav_link_category', 0, 'primary', '', NULL, 'admin', NOW(), 'admin', NOW(), b'0'),
       (2, '开发工具', '2', 'system_nav_link_category', 0, 'success', '', NULL, 'admin', NOW(), 'admin', NOW(), b'0'),
       (3, '文档资料', '3', 'system_nav_link_category', 0, 'warning', '', NULL, 'admin', NOW(), 'admin', NOW(), b'0'),
       (4, '其他',     '4', 'system_nav_link_category', 0, 'info',    '', NULL, 'admin', NOW(), 'admin', NOW(), b'0');

-- ----------------------------
-- 3. 菜单与按钮权限：挂在「系统管理」（id = 1）下
-- ----------------------------
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('常用网址导航', '', 2, 20, 1, 'nav-link', 'ep:link', 'system/navlink/index', 'SystemNavLink', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 按钮的父菜单编号
SELECT @navLinkParentId := LAST_INSERT_ID();

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('常用网址导航查询', 'system:nav-link:query',  3, 1, @navLinkParentId, '', '', '', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
       ('常用网址导航创建', 'system:nav-link:create', 3, 2, @navLinkParentId, '', '', '', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
       ('常用网址导航更新', 'system:nav-link:update', 3, 3, @navLinkParentId, '', '', '', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
       ('常用网址导航删除', 'system:nav-link:delete', 3, 4, @navLinkParentId, '', '', '', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
       ('常用网址导航导出', 'system:nav-link:export', 3, 5, @navLinkParentId, '', '', '', 0, 'admin', NOW(), 'admin', NOW(), b'0');

-- 说明：超级管理员（super_admin）默认拥有全部权限；其它角色需在「系统管理 - 角色管理 - 菜单权限」中勾选后生效。
