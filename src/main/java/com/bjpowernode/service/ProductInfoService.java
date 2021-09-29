package com.bjpowernode.service;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductInfoService {
    //显示全部商品（不分页）
    List<ProductInfo> getAll();

    //分页功能实现
    //select * from pro limit 起始记录数=（当前页-1）*每页条数，每页取几条
    PageInfo splitPage(int pageNum,int pageSize);

    //增加商品
    int save(ProductInfo info);

    //按主键id查询商品
    ProductInfo getById(Integer id);

    //更新商品
    int update(ProductInfo info);

    //单个商品删除
    int delete(Integer pid);

    //批量删除商品
    int deleteBatch(String[] ids);

    //多条件商品查询
    List<ProductInfo> selectCondition(ProductInfoVo vo);

    //多条件查询分页
     PageInfo splitPageVo(ProductInfoVo vo,Integer pageSize);
}
