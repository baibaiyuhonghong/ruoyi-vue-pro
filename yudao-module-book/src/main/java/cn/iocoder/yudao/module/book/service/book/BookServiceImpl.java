package cn.iocoder.yudao.module.book.service.book;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.*;
import cn.iocoder.yudao.module.book.dal.dataobject.book.BookDO;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.book.dal.mysql.book.BookMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.book.enums.ErrorCodeConstants.BOOK_NOT_EXISTS;

@Service
@Validated
public class BookServiceImpl implements BookService {
    @Resource
    private BookMapper bookMapper;

    @Override
    public Long createBook(BookSaveReqVO createReqVO) {
        BookDO book = BeanUtils.toBean(createReqVO, BookDO.class);
        book.setId(null);
        bookMapper.insert(book);
        return book.getId();
    }

    @Override
    public void updateBook(BookSaveReqVO updateReqVO) {
        validateBookExists(updateReqVO.getId());
        bookMapper.updateById(BeanUtils.toBean(updateReqVO, BookDO.class));
    }

    @Override
    public void deleteBook(Long id) {
        validateBookExists(id);
        bookMapper.deleteById(id);
    }

    private void validateBookExists(Long id) {
        if (id == null || bookMapper.selectById(id) == null) {
            throw exception(BOOK_NOT_EXISTS);
        }
    }

    @Override
    public BookDO getBook(Long id) {
        return bookMapper.selectById(id);
    }

    @Override
    public PageResult<BookDO> getBookPage(BookPageReqVO pageReqVO) {
        return bookMapper.selectPage(pageReqVO);
    }
}
