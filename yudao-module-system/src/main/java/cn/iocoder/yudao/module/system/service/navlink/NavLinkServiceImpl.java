package cn.iocoder.yudao.module.system.service.navlink;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.navlink.vo.NavLinkPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.navlink.vo.NavLinkSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.navlink.NavLinkDO;
import cn.iocoder.yudao.module.system.dal.mysql.navlink.NavLinkMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.NAV_LINK_NOT_EXISTS;

/**
 * 常用网址导航 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class NavLinkServiceImpl implements NavLinkService {

    @Resource
    private NavLinkMapper navLinkMapper;

    @Override
    public Long createNavLink(NavLinkSaveReqVO createReqVO) {
        NavLinkDO navLink = BeanUtils.toBean(createReqVO, NavLinkDO.class);
        navLinkMapper.insert(navLink);
        return navLink.getId();
    }

    @Override
    public void updateNavLink(NavLinkSaveReqVO updateReqVO) {
        // 校验是否存在
        validateNavLinkExists(updateReqVO.getId());
        // 更新常用网址导航
        NavLinkDO updateObj = BeanUtils.toBean(updateReqVO, NavLinkDO.class);
        navLinkMapper.updateById(updateObj);
    }

    @Override
    public void deleteNavLink(Long id) {
        // 校验是否存在
        validateNavLinkExists(id);
        // 删除常用网址导航
        navLinkMapper.deleteById(id);
    }

    @Override
    public void deleteNavLinkList(List<Long> ids) {
        navLinkMapper.deleteByIds(ids);
    }

    @Override
    public NavLinkDO getNavLink(Long id) {
        return navLinkMapper.selectById(id);
    }

    @Override
    public PageResult<NavLinkDO> getNavLinkPage(NavLinkPageReqVO pageReqVO) {
        return navLinkMapper.selectPage(pageReqVO);
    }

    private void validateNavLinkExists(Long id) {
        if (navLinkMapper.selectById(id) == null) {
            throw exception(NAV_LINK_NOT_EXISTS);
        }
    }

}
