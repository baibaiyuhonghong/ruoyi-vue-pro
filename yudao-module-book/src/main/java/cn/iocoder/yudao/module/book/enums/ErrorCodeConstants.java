package cn.iocoder.yudao.module.book.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/** 图书管理错误码，使用 1-070-000-000 段。 */
public interface ErrorCodeConstants {
    ErrorCode BOOK_NOT_EXISTS = new ErrorCode(1_070_000_000, "图书不存在");
}
