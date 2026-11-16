package cn.iocoder.yudao.module.book.controller.admin.book.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 图书分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class BookPageReqVO extends PageParam {
    @Schema(description = "书名，模糊匹配")
    private String name;
    @Schema(description = "作者，模糊匹配")
    private String author;
    @Schema(description = "ISBN，精确匹配")
    private String isbn;
    @Schema(description = "出版社，模糊匹配")
    private String publisher;
    @Schema(description = "状态：0 上架，1 下架")
    @Min(0)
    @Max(1)
    private Integer status;
    @Schema(description = "出版日期范围")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate[] publishDate;
    @Schema(description = "创建时间范围")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}
