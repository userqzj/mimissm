package com.bjpowernode.controller;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    //每页显示的记录数
    private static final int PAGE_SIZE = 5;

    //异步上传的图片的名称
    private String saveFileName = "";

    //业务逻辑层对象
    @Autowired
    private ProductInfoService productInfoService;

    //显示全部商品不分页
    @RequestMapping("/getAll.action")
    public ModelAndView getAll() {
        ModelAndView mv = new ModelAndView();
        List<ProductInfo> list = productInfoService.getAll();

        mv.addObject("list", list);
        mv.setViewName("product");
        return mv;
    }

    //显示第1页的5条记录
    @RequestMapping("/split.action")
    public ModelAndView split(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("prodVo");

        if (vo !=null) {
            info=productInfoService.splitPageVo((ProductInfoVo) vo,PAGE_SIZE);
            request.getSession().removeAttribute("prodVo");
        }else {
            //得到第一页的数据
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }

        mv.addObject("info", info);
        mv.setViewName("product");
        return mv;
    }

    //ajax分页翻页处理
    @RequestMapping("/ajaxSplit.action")
    @ResponseBody
    public void ajaxSplit(ProductInfoVo vo, HttpSession session) {

        //取得当前page参数的页面的数据
        PageInfo info = productInfoService.splitPageVo(vo, PAGE_SIZE);

        session.setAttribute("info", info);
    }

    //多条件查询功能实现
    @RequestMapping("/condition.action")
    @ResponseBody
    public void condition(ProductInfoVo vo, HttpSession session) {
        List<ProductInfo> list = productInfoService.selectCondition(vo);
        session.setAttribute("list", list);
    }

    //异步ajax文件上传处理
    @RequestMapping("/ajaxImg.action")
    @ResponseBody
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request) {

        //提取生成文件名UUID+上传图片后缀 .jpg .png
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中图片存储的路径
        String path = request.getServletContext().getRealPath("/image_big");
        //转存 D:\mimissm\image_big\feqwwqegqwtewq.jpg
        try {
            pimage.transferTo(new File(path + File.separator + saveFileName));
            System.out.println(path + File.separator + saveFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回客户端JSON对象，封装图片的路径，为了在页面实现立即回显
        JSONObject object = new JSONObject();
        object.put("imgurl", saveFileName);

        return object.toString();
    }

    @RequestMapping("/save.action")
    public ModelAndView save(ProductInfo info) {
        ModelAndView mv = new ModelAndView();
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        //info对象中有表单提交上来的5个数据，由异步ajax上来的图片名称数据 有时间数据
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            mv.addObject("msg", "增加成功!");
        } else {
            mv.addObject("msg", "增加失败！");
        }

        //清空saveFileName内容，为了下次增加或修改的异步ajax的上传处理
        saveFileName = "";

        //增加成功后应该重新访问数据库，所以跳转到分页显示的action上
        mv.setViewName("forward:/prod/split.action");

        return mv;
    }

    @RequestMapping("/one.action")
    public ModelAndView one(Integer pid, ProductInfoVo vo,HttpSession session) {
        ModelAndView mv = new ModelAndView();

        ProductInfo info = productInfoService.getById(pid);
        mv.addObject("prod", info);
        //将多条件及页码放入session中，更新处理结束后读取条件和页码进行处理
        session.setAttribute("prodVo", vo);
        mv.setViewName("update");

        return mv;
    }

    @RequestMapping("/update.action")
    public ModelAndView update(ProductInfo info) {
        ModelAndView mv = new ModelAndView();

        //因为ajax的一部图片上传，如果有上传过，则saveFileName里有上传上来的图片的名称，
        // 如果没使用ajax上传过图片，则saveFileName="",实体类info使用隐藏表单域提供上来的pImage原始图片的名称;
        if (!saveFileName.equals("")) {
            info.setpImage(saveFileName);
        }
        //完成更新操作
        int num = -1;
        //对增删改操作一定要添加try catch
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (num > 0) {
            //此时说明更新成功
            mv.addObject("msg", "更新成功!");
        } else {
            //更新失败
            mv.addObject("msg", "更新失败!");
        }

        //处理完更新后，saveFileName有可能有数据，
        // 而下一次更新时要使用这个变量作为判断的依据 就会出错，所以必须清空saveFileNaame；
        saveFileName = "";

        mv.setViewName("forward:/prod/split.action");
        return mv;
    }

    @RequestMapping("/delete.action")
    public ModelAndView delete(Integer pid,ProductInfoVo vo,HttpSession session) {
        ModelAndView mv = new ModelAndView();

        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (num > 0) {
            mv.addObject("msg", "删除成功！");
            session.setAttribute("deleteProdVo",vo);
        } else {
            mv.addObject("msg", "删除失败！");
        }

        mv.setViewName("forward:/prod/deleteAjaxSplit.action");
        //删除结束后跳到分页显示
        return mv;
    }

    @RequestMapping(value = "/deleteAjaxSplit", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public Object deleteAjaxSplit(HttpServletRequest request) {
        PageInfo info=null;
        Object vo=request.getSession().getAttribute("deleteProdVo");
        if (vo!=null){
            info=productInfoService.splitPageVo((ProductInfoVo) vo,PAGE_SIZE);
        }else {
            //取得第一页的数据
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.getSession().removeAttribute("deleteProdVo");
        request.getSession().setAttribute("info", info);

        return request.getAttribute("msg");
    }

    @RequestMapping("/deleteBatch.action")
    public ModelAndView deleteBatch(String pids) {
        ModelAndView mv = new ModelAndView();

        //将上传上来的字符串切割，形成商品id的字符数组
        String[] ps = pids.split(",");
        try {

            int num = productInfoService.deleteBatch(ps);
            if (num > 0) {
                mv.addObject("msg", "批量删除成功！");
            } else {
                mv.addObject("msg", "批量删除失败！");
            }
        } catch (Exception e) {
            mv.addObject("msg", "商品不可删除！");
        }

        mv.setViewName("forward:/prod/deleteAjaxSplit.action");

        return mv;
    }


}
