package cn.iocoder.yudao.module.book.service.book;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.*;
import cn.iocoder.yudao.module.book.dal.dataobject.book.BookDO;
import jakarta.validation.Valid;

/** 图书 Service */
public interface BookService {
    Long createBook(@Valid BookSaveReqVO createReqVO);
    void updateBook(@Valid BookSaveReqVO updateReqVO);
    void deleteBook(Long id);
    BookDO getBook(Long id);
    PageResult<BookDO> getBookPage(BookPageReqVO pageReqVO);
}
