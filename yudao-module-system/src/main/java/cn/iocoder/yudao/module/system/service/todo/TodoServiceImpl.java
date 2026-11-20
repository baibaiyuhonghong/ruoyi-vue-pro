package cn.iocoder.yudao.module.system.service.todo;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoStatusCountRespVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoUpdateStatusReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.todo.TodoDO;
import cn.iocoder.yudao.module.system.dal.mysql.todo.TodoMapper;
import cn.iocoder.yudao.module.system.enums.todo.TodoStatusEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.TODO_NOT_EXISTS;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.TODO_NOT_OWNER;

/**
 * 个人待办 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class TodoServiceImpl implements TodoService {

    @Resource
    private TodoMapper todoMapper;

    @Override
    public Long createTodo(TodoSaveReqVO createReqVO) {
        TodoDO todo = BeanUtils.toBean(createReqVO, TodoDO.class);
        todo.setUserId(getLoginUserId());
        todo.setFinishTime(resolveFinishTime(createReqVO.getStatus(), null));
        todoMapper.insert(todo);
        return todo.getId();
    }

    @Override
    public void updateTodo(TodoSaveReqVO updateReqVO) {
        TodoDO dbTodo = validateTodoOwner(updateReqVO.getId());
        TodoDO updateObj = BeanUtils.toBean(updateReqVO, TodoDO.class);
        updateFinishTime(updateObj, resolveFinishTime(updateReqVO.getStatus(), dbTodo.getFinishTime()));
    }

    @Override
    public void updateTodoStatus(TodoUpdateStatusReqVO updateReqVO) {
        TodoDO dbTodo = validateTodoOwner(updateReqVO.getId());
        TodoDO updateObj = new TodoDO();
        updateObj.setId(updateReqVO.getId());
        updateObj.setStatus(updateReqVO.getStatus());
        updateFinishTime(updateObj, resolveFinishTime(updateReqVO.getStatus(), dbTodo.getFinishTime()));
    }

    @Override
    public void deleteTodo(Long id) {
        validateTodoOwner(id);
        todoMapper.deleteById(id);
    }

    @Override
    public TodoDO getTodo(Long id) {
        return validateTodoOwner(id);
    }

    @Override
    public PageResult<TodoDO> getTodoPage(TodoPageReqVO pageReqVO) {
        return todoMapper.selectPage(pageReqVO, getLoginUserId());
    }

    @Override
    public TodoStatusCountRespVO getTodoStatusCount() {
        Long userId = getLoginUserId();
        TodoStatusCountRespVO respVO = new TodoStatusCountRespVO();
        respVO.setPendingCount(todoMapper.selectCountByUserIdAndStatus(userId, TodoStatusEnum.PENDING.getStatus()));
        respVO.setProcessingCount(todoMapper.selectCountByUserIdAndStatus(userId, TodoStatusEnum.PROCESSING.getStatus()));
        respVO.setFinishedCount(todoMapper.selectCountByUserIdAndStatus(userId, TodoStatusEnum.FINISHED.getStatus()));
        respVO.setTotalCount(respVO.getPendingCount() + respVO.getProcessingCount() + respVO.getFinishedCount());
        return respVO;
    }

    /**
     * 校验待办存在且属于当前登录用户，通过后返回库中记录
     */
    private TodoDO validateTodoOwner(Long id) {
        TodoDO todo = todoMapper.selectById(id);
        if (todo == null) {
            throw exception(TODO_NOT_EXISTS);
        }
        if (!ObjUtil.equal(todo.getUserId(), getLoginUserId())) {
            throw exception(TODO_NOT_OWNER);
        }
        return todo;
    }

    /**
     * 推导完成时间：置为已完成时写入（原本已完成则保留原值），改回未完成时清空
     */
    private LocalDateTime resolveFinishTime(Integer status, LocalDateTime oldFinishTime) {
        if (!TodoStatusEnum.isFinished(status)) {
            return null;
        }
        return oldFinishTime != null ? oldFinishTime : LocalDateTime.now();
    }

    /**
     * 写入完成时间。必须走 UpdateWrapper 显式 set：updateById 默认忽略 null 字段，清不掉 finish_time
     */
    private void updateFinishTime(TodoDO updateObj, LocalDateTime finishTime) {
        todoMapper.update(updateObj, new LambdaUpdateWrapper<TodoDO>()
                .eq(TodoDO::getId, updateObj.getId())
                .set(TodoDO::getFinishTime, finishTime));
    }

}