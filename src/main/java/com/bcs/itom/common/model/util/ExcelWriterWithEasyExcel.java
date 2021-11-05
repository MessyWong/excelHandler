package com.bcs.itom.common.model.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.property.ColumnWidthProperty;
import com.bcs.itom.common.model.entity.ExcelDataEntity;
import com.bcs.itom.common.model.watermark.handler.ExcelWatermarkHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author wangyw
 * @date 2021/11/5
 */
public class ExcelWriterWithEasyExcel {

    /**
     * 利用EasyExcel写Excel文件
     *
     * @param excelPathName     excel文件路径
     * @param content           水印内容
     * @param clazz             数据类
     * @param dataList          数据
     * @throws IOException
     */
    public static void WriteExcelWithEasyExcel(String excelPathName, String content,Class<?> clazz, List<?> dataList) throws IOException {
        FileOutputStream excelWriteStream = new FileOutputStream(excelPathName);
        EasyExcel.write(excelPathName, clazz)
                .inMemory(true)
                .registerWriteHandler(new ExcelWatermarkHandler(content))
                .sheet("Sheet1").doWrite(dataList);
        excelWriteStream.flush();
        excelWriteStream.close();
    }
}
