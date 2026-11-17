package cn.iocoder.yudao.module.book.service.book;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.BookPageReqVO;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.BookSaveReqVO;
import cn.iocoder.yudao.module.book.dal.dataobject.book.BookDO;
import cn.iocoder.yudao.module.book.dal.mysql.book.BookMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.module.book.enums.ErrorCodeConstants.BOOK_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(BookServiceImpl.class)
class BookServiceImplTest extends BaseDbUnitTest {

    @Resource
    private BookService bookService;
    @Resource
    private BookMapper bookMapper;

    @Test
    void testCreateAndGetBook() {
        BookSaveReqVO reqVO = buildBook("红楼梦", 0);
        reqVO.setId(1024L);

        Long id = bookService.createBook(reqVO);

        assertNotNull(id);
        assertNotEquals(reqVO.getId(), id);
        BookDO book = bookService.getBook(id);
        assertPojoEquals(reqVO, book, "id");
        assertNotNull(book.getCreateTime());
        assertNotNull(book.getUpdateTime());
    }

    @Test
    void testUpdateBook() {
        BookSaveReqVO reqVO = buildBook("红楼梦", 0);
        reqVO.setId(bookService.createBook(reqVO));
        reqVO.setPrice(6990);
        reqVO.setStock(12);
        reqVO.setStatus(1);

        bookService.updateBook(reqVO);

        assertPojoEquals(reqVO, bookMapper.selectById(reqVO.getId()));
    }

    @Test
    void testDeleteBook() {
        Long id = bookService.createBook(buildBook("红楼梦", 0));

        bookService.deleteBook(id);

        assertNull(bookMapper.selectById(id));
        assertEquals(0L, bookService.getBookPage(new BookPageReqVO()).getTotal());
        assertServiceException(() -> bookService.getBook(id), BOOK_NOT_EXISTS);
        assertServiceException(() -> bookService.deleteBook(id), BOOK_NOT_EXISTS);
    }

    @Test
    void testBookNotExists() {
        Long id = 1024L;
        BookSaveReqVO reqVO = buildBook("红楼梦", 0);
        reqVO.setId(id);

        assertServiceException(() -> bookService.updateBook(reqVO), BOOK_NOT_EXISTS);
        assertServiceException(() -> bookService.deleteBook(id), BOOK_NOT_EXISTS);
        assertServiceException(() -> bookService.getBook(id), BOOK_NOT_EXISTS);
    }

    @Test
    void testGetBookPage() {
        Long firstId = bookService.createBook(buildBook("红楼梦 上册", 0));
        Long secondId = bookService.createBook(buildBook("红楼梦 下册", 0));
        bookService.createBook(buildBook("红楼梦 纪念版", 1));
        bookService.createBook(buildBook("石头记", 0));

        BookPageReqVO pageReqVO = new BookPageReqVO();
        pageReqVO.setName("红楼梦");
        pageReqVO.setPublishDate(LocalDate.of(2008, 7, 1));
        pageReqVO.setStatus(0);
        pageReqVO.setPageSize(1);
        PageResult<BookDO> page = bookService.getBookPage(pageReqVO);
        assertEquals(2L, page.getTotal());
        assertEquals(List.of(secondId), page.getList().stream().map(BookDO::getId).toList());

        pageReqVO.setPageNo(2);
        page = bookService.getBookPage(pageReqVO);
        assertEquals(2L, page.getTotal());
        assertEquals(List.of(firstId), page.getList().stream().map(BookDO::getId).toList());

        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        page = bookService.getBookPage(pageReqVO);
        assertEquals(List.of(secondId, firstId), page.getList().stream().map(BookDO::getId).toList());
    }

    private BookSaveReqVO buildBook(String name, int status) {
        BookSaveReqVO book = new BookSaveReqVO();
        book.setName(name);
        book.setAuthor("曹雪芹");
        book.setIsbn("9787020002207");
        book.setPublisher("人民文学出版社");
        book.setPublishDate(LocalDate.of(2008, 7, 1));
        book.setPrice(5990);
        book.setStock(20);
        book.setStatus(status);
        return book;
    }

}
