package cn.iocoder.yudao.module.book.service.book;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.BookPageReqVO;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.BookSaveReqVO;
import cn.iocoder.yudao.module.book.dal.dataobject.book.BookDO;
import cn.iocoder.yudao.module.book.dal.mysql.book.BookMapper;
import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.book.enums.ErrorCodeConstants.BOOK_ISBN_DUPLICATE;
import static cn.iocoder.yudao.module.book.enums.ErrorCodeConstants.BOOK_NOT_EXISTS;

/**
 * 图书 Service 实现类
 */
@Service
@Validated
public class BookServiceImpl implements BookService {

    @Resource
    private BookMapper bookMapper;

    @Override
    public Long createBook(BookSaveReqVO createReqVO) {
        validateIsbnUnique(null, createReqVO.getIsbn());
        BookDO book = BeanUtils.toBean(createReqVO, BookDO.class);
        book.setId(null);
        try {
            bookMapper.insert(book);
        } catch (DuplicateKeyException ex) {
            throw exception(BOOK_ISBN_DUPLICATE);
        }
        return book.getId();
    }

    @Override
    public void updateBook(BookSaveReqVO updateReqVO) {
        if (updateReqVO.getId() == null) {
            throw exception(BOOK_NOT_EXISTS);
        }
        getBook(updateReqVO.getId());
        validateIsbnUnique(updateReqVO.getId(), updateReqVO.getIsbn());
        try {
            if (bookMapper.updateById(BeanUtils.toBean(updateReqVO, BookDO.class)) == 0) {
                throw exception(BOOK_NOT_EXISTS);
            }
        } catch (DuplicateKeyException ex) {
            throw exception(BOOK_ISBN_DUPLICATE);
        }
    }

    private void validateIsbnUnique(Long id, String isbn) {
        Long existingId = bookMapper.selectIdByIsbn(isbn);
        if (existingId != null && !existingId.equals(id)) {
            throw exception(BOOK_ISBN_DUPLICATE);
        }
    }

    @Override
    public void deleteBook(Long id) {
        if (bookMapper.deleteById(id) == 0) {
            throw exception(BOOK_NOT_EXISTS);
        }
    }

    @Override
    public BookDO getBook(Long id) {
        BookDO book = bookMapper.selectById(id);
        if (book == null) {
            throw exception(BOOK_NOT_EXISTS);
        }
        return book;
    }

    @Override
    public PageResult<BookDO> getBookPage(BookPageReqVO pageReqVO) {
        return bookMapper.selectPage(pageReqVO);
    }

}
