package com.gaea.common.result;

/**
 * Created by Fe on 15/1/19.
 */
public class AjaxResult {

    /**
     * 返回状态
     */
    private AjaxResultStatusEnum status;
    /**
     * 异常提示
     */
    private String               message;
    /**
     * 业务数据
     */
    private Object               data;

    public AjaxResult(Object data) {
        this.data = data;
        this.status = AjaxResultStatusEnum.OK;
    }

    public AjaxResult() {
        this.status = AjaxResultStatusEnum.OK;
    }

    public int getStatus() {
        if (status != null) {
            return status.getCode();
        }
        return AjaxResultStatusEnum.ERROR.getCode();
    }

    public void setStatus(AjaxResultStatusEnum status) {
        this.status = status;
    }

    public <T> T getData() {
        return (T) data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
