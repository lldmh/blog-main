package com.mh.Controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.CategoryDto;
import com.mh.domain.entity.Category;
import com.mh.domain.vo.ExcelCategoryVo;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.service.CategoryService;
import com.mh.utils.BeanCopyUtils;
import com.mh.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author dmh
 * @Date 2023/7/26 17:15
 * @Version 1.0
 * @introduce
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //查询所有类别
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

    /**
     * 查询文章类别列表
     * @param pageNum     页面num
     * @param pageSize    页面大小
     * @param categoryDto 类别dto
     * @return {@link ResponseResult}
     */
    @GetMapping("/list")
    public ResponseResult getArticleCategoryList(Integer pageNum, Integer pageSize, CategoryDto categoryDto) {
        return categoryService.getArticleCategoryList(pageNum, pageSize, categoryDto);
    }

    /**
     * 分类导出EAXYEXCEL插件：https://github.com/alibaba/easyexcel
     * @param response
     */
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    /**
     * 添加文章类别
     *
     * @param categoryDto
     * @return
     */
    @PostMapping("")
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    /**
     * 根据id查询分类,回显
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable("id") Long id){
        return categoryService.getCategoryById(id);
    }

    /**
     * 更新分类
     *
     * @param categoryDto
     * @return
     */
    @PutMapping("")
    public ResponseResult updateCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(categoryDto);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategoryById(@PathVariable("id") Long id){
        return categoryService.deleteCategoryById(id);
    }
}
