package cn.iocoder.yudao.module.system.dal.mysql.todo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.todo.TodoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 个人待办 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface TodoMapper extends BaseMapperX<TodoDO> {

    default PageResult<TodoDO> selectPage(TodoPageReqVO reqVO, Long userId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TodoDO>()
                .eq(TodoDO::getUserId, userId)
                .likeIfPresent(TodoDO::getTitle, reqVO.getTitle())
                .eqIfPresent(TodoDO::getStatus, reqVO.getStatus())
                .eqIfPresent(TodoDO::getPriority, reqVO.getPriority())
                // 截止时间升序（最紧急在前），未填截止时间的排最后，同组内新建在前
                .last("ORDER BY deadline IS NULL, deadline ASC, id DESC"));
    }

    default Long selectCountByUserIdAndStatus(Long userId, Integer status) {
        return selectCount(new LambdaQueryWrapperX<TodoDO>()
                .eq(TodoDO::getUserId, userId)
                .eq(TodoDO::getStatus, status));
    }

}