package cn.iocoder.yudao.module.book.dal.mysql.book;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.BookPageReqVO;
import cn.iocoder.yudao.module.book.dal.dataobject.book.BookDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapperX<BookDO> {

    default PageResult<BookDO> selectPage(BookPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BookDO>()
                .likeIfPresent(BookDO::getName, reqVO.getName())
                .likeIfPresent(BookDO::getAuthor, reqVO.getAuthor())
                .eqIfPresent(BookDO::getIsbn, reqVO.getIsbn())
                .likeIfPresent(BookDO::getPublisher, reqVO.getPublisher())
                .eqIfPresent(BookDO::getPublishDate, reqVO.getPublishDate())
                .eqIfPresent(BookDO::getStatus, reqVO.getStatus())
                .orderByDesc(BookDO::getId));
    }

}
