package cn.iocoder.yudao.module.book.dal.dataobject.book;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import java.time.LocalDate;

/** 图书 DO */
@TableName("book_book")
@KeySequence("book_book_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BookDO extends TenantBaseDO {
    /** 图书编号 */
    @TableId
    private Long id;

    /** 书名 */
    private String name;

    /** 作者 */
    private String author;

    /** ISBN */
    private String isbn;

    /** 出版社 */
    private String publisher;

    /** 出版日期 */
    private LocalDate publishDate;

    /** 价格，单位：分 */
    private Integer price;

    /** 库存 */
    private Integer stock;

    /** 状态：0 上架，1 下架 */
    private Integer status;

}
