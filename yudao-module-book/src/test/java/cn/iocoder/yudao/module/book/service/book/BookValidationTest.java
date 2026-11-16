package cn.iocoder.yudao.module.book.service.book;

import cn.iocoder.yudao.module.book.controller.admin.book.vo.BookSaveReqVO;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BookValidationTest {
    @Test
    void rejectsNegativeAmountsAndUnknownStatus() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            BookSaveReqVO request = new BookSaveReqVO();
            request.setName("图书");
            request.setAuthor("作者");
            request.setIsbn("9787300000000");
            request.setPublisher("出版社");
            request.setPublishDate(LocalDate.of(2026, 1, 1));
            request.setPrice(-1);
            request.setStock(-1);
            request.setStatus(2);
            var validator = factory.getValidator();
            var properties = validator.validate(request).stream()
                    .map(v -> v.getPropertyPath().toString()).toList();
            assertEquals(3, properties.size());
            assertTrue(properties.containsAll(java.util.List.of("price", "stock", "status")));
            request.setPrice(0);
            request.setStock(0);
            request.setStatus(1);
            assertTrue(validator.validate(request).isEmpty());
            request.setName(" ");
            assertFalse(validator.validate(request).isEmpty());
        }
    }
}
