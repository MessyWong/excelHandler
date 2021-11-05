package com.messy.common.model.watermark.constant;

import java.awt.*;

public final class WatermarkParam {
    /**
     * 水印图片尺寸
     */
    public final static int  width = 300;
    public final static int height = 250;

    /**
     * 水印字体
     */
    static final String fontType = "微软雅黑";
    static final int fontStyle = Font.BOLD;
    static final int fontSize = 20;
    public final static Font font = new Font(fontType, fontStyle, fontSize);

    /**
     * 水印颜色，不透明度
     */
    public final static Color color = new Color(0,0,0,20);

    /**
     * 水印线宽
     */
    public final static BasicStroke stroke = new BasicStroke(1);

    /**
     * 水印旋转角度
     */
    public final static double theta = -0.5;

    /**
     * 水印图片流格式
     */
    public final static String format = "png";
}
