package cn.iocoder.yudao.module.system.service.navlink;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.navlink.vo.NavLinkPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.navlink.vo.NavLinkSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.navlink.NavLinkDO;

import java.util.List;

/**
 * 常用网址导航 Service 接口
 *
 * @author 芋道源码
 */
public interface NavLinkService {

    /**
     * 创建常用网址导航
     *
     * @param createReqVO 常用网址导航
     * @return 编号
     */
    Long createNavLink(NavLinkSaveReqVO createReqVO);

    /**
     * 更新常用网址导航
     *
     * @param updateReqVO 常用网址导航
     */
    void updateNavLink(NavLinkSaveReqVO updateReqVO);

    /**
     * 删除常用网址导航
     *
     * @param id 编号
     */
    void deleteNavLink(Long id);

    /**
     * 批量删除常用网址导航
     *
     * @param ids 编号列表
     */
    void deleteNavLinkList(List<Long> ids);

    /**
     * 获得常用网址导航
     *
     * @param id 编号
     * @return 常用网址导航
     */
    NavLinkDO getNavLink(Long id);

    /**
     * 获得常用网址导航分页
     *
     * @param pageReqVO 分页条件
     * @return 常用网址导航分页
     */
    PageResult<NavLinkDO> getNavLinkPage(NavLinkPageReqVO pageReqVO);

}
