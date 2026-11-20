package cn.iocoder.yudao.module.system.service.todo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoStatusCountRespVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoUpdateStatusReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.todo.TodoDO;

/**
 * 个人待办 Service 接口
 *
 * 所有读写都以当前登录用户为归属边界，只能操作自己创建的待办
 *
 * @author 芋道源码
 */
public interface TodoService {

    /**
     * 创建个人待办
     *
     * @param createReqVO 待办
     * @return 编号
     */
    Long createTodo(TodoSaveReqVO createReqVO);

    /**
     * 更新个人待办
     *
     * @param updateReqVO 待办
     */
    void updateTodo(TodoSaveReqVO updateReqVO);

    /**
     * 变更个人待办状态
     *
     * @param updateReqVO 编号与新状态
     */
    void updateTodoStatus(TodoUpdateStatusReqVO updateReqVO);

    /**
     * 删除个人待办
     *
     * @param id 编号
     */
    void deleteTodo(Long id);

    /**
     * 获得个人待办
     *
     * @param id 编号
     * @return 待办
     */
    TodoDO getTodo(Long id);

    /**
     * 获得个人待办分页
     *
     * @param pageReqVO 分页条件
     * @return 待办分页
     */
    PageResult<TodoDO> getTodoPage(TodoPageReqVO pageReqVO);

    /**
     * 获得当前登录用户的待办按状态统计
     *
     * @return 各状态数量
     */
    TodoStatusCountRespVO getTodoStatusCount();

}