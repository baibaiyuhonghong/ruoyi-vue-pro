-- ----------------------------
-- 图书管理：增量脚本（在已初始化的 ruoyi-vue-pro 库执行一次）
-- 不删除已有表或数据。菜单对应 book/book/index，前端页面需另行实现。
-- ----------------------------
CREATE TABLE IF NOT EXISTS `book_book` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '图书编号',
  `name` varchar(200) NOT NULL COMMENT '书名',
  `author` varchar(100) NOT NULL COMMENT '作者',
  `isbn` varchar(32) NOT NULL COMMENT 'ISBN',
  `publisher` varchar(200) NOT NULL COMMENT '出版社',
  `publish_date` date NOT NULL COMMENT '出版日期',
  `price` int NOT NULL COMMENT '价格，单位：分',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0上架 1下架）',
  `creator`     varchar(64)  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater`     varchar(64)  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`     bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id`   bigint       NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '图书表';

-- 菜单与按钮权限：使用自增编号，避免与已有菜单冲突。
START TRANSACTION;
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`)
VALUES ('图书管理', '', 1, 70, 0, '/book', 'ep:reading', NULL, NULL, 0);
SET @bookRootId = LAST_INSERT_ID();
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`)
VALUES ('图书列表', '', 2, 1, @bookRootId, 'book', 'ep:reading', 'book/book/index', 'BookBook', 0);
SET @bookMenuId = LAST_INSERT_ID();
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `status`)
VALUES ('图书查询', 'book:book:query', 3, 1, @bookMenuId, '', 0),
       ('图书创建', 'book:book:create', 3, 2, @bookMenuId, '', 0),
       ('图书更新', 'book:book:update', 3, 3, @bookMenuId, '', 0),
       ('图书删除', 'book:book:delete', 3, 4, @bookMenuId, '', 0),
       ('图书导出', 'book:book:export', 3, 5, @bookMenuId, '', 0);
COMMIT;
-- 超级管理员默认拥有全部权限；其他角色通过「角色管理 - 菜单权限」授权。
-- 开启 SaaS 时，还需将新增菜单加入对应租户套餐，再为租户角色授权。
