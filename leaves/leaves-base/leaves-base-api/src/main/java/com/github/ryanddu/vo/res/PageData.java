package com.github.ryanddu.vo.res;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.ryanddu.vo.req.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页信息
 * @author: ryan
 * @date: 2023/3/27 9:56
 **/
@Data
@Schema(description = "分页信息")
public class PageData<T> {

    /**
     * 数据列表
     */
    @Schema(description = "数据列表")
    private List<T> resultData;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数")
    private long totalNum;

    /**
     * 当前页，从1开始
     */
    @Schema(description = "当前页，从1开始")
    private long pageNo;

    /**
     * 分页大小
     */
    @Schema(description = "分页大小")
    private long pageSize;

    public PageData() {

    }

    public PageData(IPage<T> page) {
        this.setResultData(page.getRecords());
        this.setPageNo(page.getCurrent());
        this.setPageSize(page.getSize());
        this.setTotalNum(page.getTotal());
    }

    public PageData(PageRequest request){
        this.setPageNo(request.getPageNo());
        this.setPageSize(request.getPageSize());
    }


    public PageData(List<T> resultData, IPage page) {
        this.setResultData(resultData);
        this.setPageNo(page.getCurrent());
        this.setPageSize(page.getSize());
        this.setTotalNum(page.getTotal());
    }

    public <E> PageData(IPage<E> page, Function<E, T> mapper) {

        this.setPageNo(page.getCurrent());
        this.setPageSize(page.getSize());
        this.setTotalNum(page.getTotal());

        if (CollUtil.isEmpty(page.getRecords())) {
            this.setResultData(Collections.emptyList());
        } else {
            this.setResultData(page.getRecords().stream().map(mapper).collect(Collectors.toList()));
        }

    }

    public PageData(List<T> resultData, long totalNum, int pageNo, int pageSize) {
        this.resultData = resultData;
        this.totalNum = totalNum;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public <E> PageData(IPage<E> page, List<T> resultData) {
        this.resultData = resultData;
        this.setPageNo(page.getCurrent());
        this.setPageSize(page.getSize());
        this.setTotalNum(page.getTotal());
    }

    public <E> PageData(PageData<E> pageData, List<T> resultData) {
        this.resultData = resultData;
        this.totalNum = pageData.totalNum;
        this.pageNo = pageData.pageNo;
        this.pageSize = pageData.pageSize;
    }

}
