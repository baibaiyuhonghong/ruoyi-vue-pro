-- ----------------------------
-- Table structure for book_book
-- ----------------------------
CREATE TABLE `book_book` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '图书编号',
  `name` varchar(200) NOT NULL COMMENT '书名',
  `author` varchar(100) NOT NULL COMMENT '作者',
  `isbn` varchar(32) NOT NULL COMMENT 'ISBN',
  `publisher` varchar(200) NOT NULL COMMENT '出版社',
  `publish_date` date NOT NULL COMMENT '出版日期',
  `price` int NOT NULL COMMENT '价格（分）',
  `stock` int NOT NULL COMMENT '库存',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0 上架，1 下架）',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_deleted_id` (`tenant_id`, `deleted`, `id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '图书表';
