package cn.iocoder.yudao.module.book.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Book 错误码枚举类
 *
 * book 系统，使用 1-061-000-000 段
 */
public interface ErrorCodeConstants {

    ErrorCode BOOK_NOT_EXISTS = new ErrorCode(1_061_000_000, "图书不存在");
    ErrorCode BOOK_ISBN_DUPLICATE = new ErrorCode(1_061_000_001, "ISBN 已存在");

}
