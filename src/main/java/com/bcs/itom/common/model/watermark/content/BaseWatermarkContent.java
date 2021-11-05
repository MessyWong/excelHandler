package com.bcs.itom.common.model.watermark.content;

import java.io.Serializable;

/**
 * 水印包含的内容
 */
public abstract class BaseWatermarkContent implements Serializable {

    /**
     * 将所有内容转换为一个字符串
     * @return
     */
    abstract public String toString();
}
