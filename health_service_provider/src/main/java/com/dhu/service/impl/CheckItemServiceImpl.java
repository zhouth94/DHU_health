package com.dhu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dhu.entity.PageResult;
import com.dhu.entity.QueryPageBean;
import com.dhu.entity.Result;
import com.dhu.mapper.CheckItemMapper;
import com.dhu.pojo.CheckItem;
import com.dhu.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhou
 * @create 2020/4/27
 */

@Service(interfaceClass = CheckItemService.class) //加了事务注解，需要明确当前服务实现哪个服务接口
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemMapper checkItemMapper;
    @Override
    public int add(CheckItem ci) {
        int res = checkItemMapper.add(ci);
        return res;
    }

    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //使用PageHelper的静态方法来设置分页参数,基于Mybatis提供的分页助手插件
        if(queryString != null && currentPage != 1) {
            currentPage = 1;
        }
        PageHelper.startPage(currentPage, pageSize);
        /*List<CheckItem> checkItems = checkItemMapper.queryByCondition(queryString);
        PageInfo<CheckItem> pageInfo = new PageInfo<>(checkItem);
        System.out.println("当前页:"+pageInfo.getPageNum());//得到当前页
        System.out.println("总页数:"+pageInfo.getPages());//得到总页数
        System.out.println("总记录数:"+pageInfo.getTotal());//表中的总记录数
        System.out.println("下一页:"+pageInfo.getNextPage());
        System.out.println("上一页:"+pageInfo.getPrePage());
        System.out.println("所有导航页号:"+pageInfo.getNavigatepageNums());
        List<CheckItem> list = pageInfo.getList();//当前页所得到的记录的集合*/

        Page<CheckItem> page = checkItemMapper.queryByCondition(queryString);
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        PageResult pg = new PageResult(total, rows);
        return pg;
    }

    @Override
    public int deleteById(Integer id) {
        long count = checkItemMapper.findCountByCheckItemId(id);
        if(count > 0) {
            throw new RuntimeException("该检查项已关联检查组，无法删除");
        }
        int res = checkItemMapper.deleteById(id);
        return res;
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemMapper.findById(id);
    }

    @Override
    public void editCheckItem(CheckItem checkItem) {
        checkItemMapper.update(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemMapper.queryAll();
    }

}
