package cn.iocoder.yudao.module.book.service.book;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.BookSaveReqVO;
import cn.iocoder.yudao.module.book.dal.dataobject.book.BookDO;
import cn.iocoder.yudao.module.book.dal.mysql.book.BookMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.module.book.enums.ErrorCodeConstants.BOOK_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceImplTest extends BaseMockitoUnitTest {
    @InjectMocks
    private BookServiceImpl service;
    @Mock
    private BookMapper mapper;

    @Test
    void createIgnoresClientIdAndKeepsPriceInCents() {
        BookSaveReqVO request = new BookSaveReqVO();
        request.setId(99L);
        request.setName("图书");
        request.setPrice(1980);
        request.setStock(3);
        request.setStatus(0);
        doAnswer(invocation -> {
            BookDO book = invocation.getArgument(0);
            assertNull(book.getId());
            assertEquals("图书", book.getName());
            assertEquals(1980, book.getPrice());
            assertEquals(3, book.getStock());
            assertNull(book.getTenantId()); // 租户由框架注入
            book.setId(1L);
            return 1;
        }).when(mapper).insert(any(BookDO.class));
        assertEquals(1L, service.createBook(request));
    }

    @Test
    void updateMissingBookDoesNotWrite() {
        BookSaveReqVO request = new BookSaveReqVO();
        request.setId(42L);
        assertServiceException(() -> service.updateBook(request), BOOK_NOT_EXISTS);
        verify(mapper, never()).updateById(any(BookDO.class));
    }

    @Test
    void updateWithoutIdDoesNotWrite() {
        assertServiceException(() -> service.updateBook(new BookSaveReqVO()), BOOK_NOT_EXISTS);
        verifyNoInteractions(mapper);
    }

    @Test
    void deleteMissingBookDoesNotWrite() {
        assertServiceException(() -> service.deleteBook(42L), BOOK_NOT_EXISTS);
        verify(mapper, never()).deleteById(any(Long.class));
    }

    @Test
    void deleteExistingBook() {
        when(mapper.selectById(42L)).thenReturn(new BookDO());
        service.deleteBook(42L);
        verify(mapper).deleteById(42L);
    }
}
