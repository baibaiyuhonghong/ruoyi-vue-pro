package cn.iocoder.yudao.module.book.service.book;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.BookPageReqVO;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.BookSaveReqVO;
import cn.iocoder.yudao.module.book.dal.mysql.book.BookMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@Import(BookServiceImpl.class)
class BookPersistenceTest extends BaseDbUnitTest {
    @Resource
    private BookService service;
    @Resource
    private BookMapper mapper;

    @Test
    void roundTripPageFiltersAndLogicalDelete() {
        BookSaveReqVO request = new BookSaveReqVO();
        request.setName("Java 编程");
        request.setAuthor("作者甲");
        request.setIsbn("9787300000000");
        request.setPublisher("出版社");
        request.setPublishDate(LocalDate.of(2026, 1, 1));
        request.setPrice(1980);
        request.setStock(10);
        request.setStatus(0);
        Long firstId = service.createBook(request);
        request.setName("Java 实践");
        Long secondId = service.createBook(request);
        request.setId(secondId);
        request.setStatus(1);
        service.updateBook(request);
        assertEquals(1, service.getBook(secondId).getStatus());
        assertEquals(LocalDate.of(2026, 1, 1), service.getBook(firstId).getPublishDate());
        assertEquals(1980, service.getBook(firstId).getPrice());

        BookPageReqVO page = new BookPageReqVO();
        page.setName("Java");
        page.setAuthor("作者");
        page.setIsbn(request.getIsbn());
        page.setPublisher("出版");
        page.setPublishDate(new LocalDate[]{LocalDate.of(2025, 12, 31), LocalDate.of(2026, 1, 2)});
        page.setPageSize(1);
        var result = service.getBookPage(page);
        assertEquals(2L, result.getTotal());
        assertEquals(secondId, result.getList().getFirst().getId());
        page.setStatus(0);
        assertEquals(firstId, service.getBookPage(page).getList().getFirst().getId());
        page.setAuthor("不存在的作者");
        assertEquals(0L, service.getBookPage(page).getTotal());

        service.deleteBook(firstId);
        assertNull(service.getBook(firstId));
        assertNull(mapper.selectById(firstId));
    }
}
