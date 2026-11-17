package cn.iocoder.yudao.module.book.service.book;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.BookPageReqVO;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.BookSaveReqVO;
import cn.iocoder.yudao.module.book.dal.dataobject.book.BookDO;
import jakarta.validation.Valid;

/**
 * 图书 Service 接口
 */
public interface BookService {

    Long createBook(@Valid BookSaveReqVO createReqVO);

    void updateBook(@Valid BookSaveReqVO updateReqVO);

    void deleteBook(Long id);

    BookDO getBook(Long id);

    PageResult<BookDO> getBookPage(BookPageReqVO pageReqVO);

}
