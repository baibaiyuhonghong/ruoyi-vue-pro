-- ----------------------------
-- 图书管理目录、菜单与按钮权限
-- 在已初始化 system_menu 的数据库执行，可重复执行。
-- ----------------------------
START TRANSACTION;

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `creator`, `updater`)
SELECT '图书管理', '', 1, 90, 0, '/book', 'ep:reading', 'admin', 'admin'
WHERE NOT EXISTS (
    SELECT 1 FROM `system_menu` WHERE `type` = 1 AND `parent_id` = 0 AND `path` = '/book' AND `deleted` = b'0'
);

SET @bookDirectoryId = (
    SELECT MIN(`id`) FROM `system_menu`
    WHERE `type` = 1 AND `parent_id` = 0 AND `path` = '/book' AND `deleted` = b'0'
);

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `creator`, `updater`)
SELECT '图书列表', '', 2, 1, @bookDirectoryId, 'book', 'ep:notebook', 'book/book/index', 'BookBook', 'admin', 'admin'
WHERE NOT EXISTS (
    SELECT 1 FROM `system_menu` WHERE `type` = 2 AND `parent_id` = @bookDirectoryId AND `path` = 'book' AND `deleted` = b'0'
);

SET @bookMenuId = (
    SELECT MIN(`id`) FROM `system_menu`
    WHERE `type` = 2 AND `parent_id` = @bookDirectoryId AND `path` = 'book' AND `deleted` = b'0'
);

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `creator`, `updater`)
SELECT button.name, button.permission, 3, button.sort, @bookMenuId, '', '', '', 'admin', 'admin'
FROM (
    SELECT '图书查询' AS name, 'book:book:query' AS permission, 1 AS sort
    UNION ALL SELECT '图书创建', 'book:book:create', 2
    UNION ALL SELECT '图书修改', 'book:book:update', 3
    UNION ALL SELECT '图书删除', 'book:book:delete', 4
    UNION ALL SELECT '图书导出', 'book:book:export', 5
) AS button
WHERE NOT EXISTS (
    SELECT 1 FROM `system_menu`
    WHERE `type` = 3 AND `parent_id` = @bookMenuId AND `permission` = button.permission AND `deleted` = b'0'
);

COMMIT;

-- 其它角色可在「系统管理 - 角色管理 - 菜单权限」中按需授权。
