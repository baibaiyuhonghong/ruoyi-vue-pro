-- 用于已建表的数据库。先核对并处理历史重复 ISBN（含逻辑删除记录），再执行 ALTER。
SELECT `isbn`, COUNT(*) AS `book_count`
FROM `book_book`
GROUP BY `isbn`
HAVING COUNT(*) > 1;

ALTER TABLE `book_book` ADD UNIQUE KEY `uk_book_isbn` (`isbn`);
