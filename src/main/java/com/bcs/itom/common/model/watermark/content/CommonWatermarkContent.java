package com.bcs.itom.common.model.watermark.content;

import lombok.Data;

@Data
public class CommonWatermarkContent extends BaseWatermarkContent {
    private static final long serialVersionUID = -3839564064939731210L;
    /**
     * 姓名
     */
    private String name;

    /**
     * 工号
     */
    private String userName;

    /**
     * 时间
     */
    private String time;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getName()).append("  ")
                .append(this.getUserName()).append("  ")
                .append(this.getTime());
        return sb.toString();
    }
}
