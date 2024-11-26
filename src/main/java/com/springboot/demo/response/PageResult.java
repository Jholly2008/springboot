package com.springboot.demo.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageResult<T> extends ApiResult<T> {
    /**
     * 当前页码
     */
    private long current;

    /**
     * 每页大小
     */
    private long pageSize;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 创建分页成功响应
     */
    public static <T> PageResult<T> ok() {
        PageResult<T> result = new PageResult<>();
        result.setStatus(200);
        result.setSuccess(true);
        return result;
    }

    /**
     * 设置分页数据
     */
    @Override
    public PageResult<T> body(T data) {
        super.body(data);
        return this;
    }

    /**
     * 设置分页信息
     */
    public PageResult<T> pagination(long current, long pageSize, long total) {
        this.current = current;
        this.pageSize = pageSize;
        this.total = total;
        return this;
    }

    /**
     * 快捷方法：一次性设置分页数据和分页信息
     */
    public PageResult<T> body(T data, long current, long pageSize, long total) {
        super.body(data);
        return pagination(current, pageSize, total);
    }
}
