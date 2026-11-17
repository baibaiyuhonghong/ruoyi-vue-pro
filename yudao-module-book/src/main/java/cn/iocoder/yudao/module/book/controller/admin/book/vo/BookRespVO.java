package cn.iocoder.yudao.module.book.controller.admin.book.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 图书 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BookRespVO {

    @Schema(description = "图书编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("图书编号")
    private Long id;

    @Schema(description = "书名", requiredMode = Schema.RequiredMode.REQUIRED, example = "红楼梦")
    @ExcelProperty("书名")
    private String name;

    @Schema(description = "作者", requiredMode = Schema.RequiredMode.REQUIRED, example = "曹雪芹")
    @ExcelProperty("作者")
    private String author;

    @Schema(description = "ISBN", requiredMode = Schema.RequiredMode.REQUIRED, example = "9787020002207")
    @ExcelProperty("ISBN")
    private String isbn;

    @Schema(description = "出版社", requiredMode = Schema.RequiredMode.REQUIRED, example = "人民文学出版社")
    @ExcelProperty("出版社")
    private String publisher;

    @Schema(description = "出版日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2008-07-01")
    @ExcelProperty("出版日期")
    private LocalDate publishDate;

    @Schema(description = "价格（分）", requiredMode = Schema.RequiredMode.REQUIRED, example = "5990")
    @ExcelProperty("价格（分）")
    private Integer price;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @ExcelProperty("库存")
    private Integer stock;

    @Schema(description = "状态（0 上架，1 下架）", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("状态（0 上架，1 下架）")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026-11-17 12:00:00")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
