-- ----------------------------
-- 图书管理目录、菜单与按钮权限
-- 在已初始化 system_menu 的数据库执行一次。
-- ----------------------------
START TRANSACTION;

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `creator`, `updater`)
VALUES ('图书管理', '', 1, 90, 0, '/book', 'ep:reading', 'admin', 'admin');

SET @bookDirectoryId = LAST_INSERT_ID();

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `creator`, `updater`)
VALUES ('图书列表', '', 2, 1, @bookDirectoryId, 'book', 'ep:notebook', 'book/book/index', 'BookBook', 'admin', 'admin');

SET @bookMenuId = LAST_INSERT_ID();

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `creator`, `updater`)
VALUES ('图书查询', 'book:book:query',  3, 1, @bookMenuId, '', '', '', 'admin', 'admin'),
       ('图书创建', 'book:book:create', 3, 2, @bookMenuId, '', '', '', 'admin', 'admin'),
       ('图书修改', 'book:book:update', 3, 3, @bookMenuId, '', '', '', 'admin', 'admin'),
       ('图书删除', 'book:book:delete', 3, 4, @bookMenuId, '', '', '', 'admin', 'admin'),
       ('图书导出', 'book:book:export', 3, 5, @bookMenuId, '', '', '', 'admin', 'admin');

COMMIT;

-- 其它角色可在「系统管理 - 角色管理 - 菜单权限」中按需授权。
