package com.bcs.itom.common.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelDataEntity {
    @ExcelProperty(value = "姓名")
    private String name;

    @ExcelProperty(value = "年龄")
    private int age;

    @ExcelProperty(value = "手机号")
    private String phoneNumber;

    @ExcelProperty(value = "住址")
    private String address;

    @ExcelProperty(value = "生日")
    private String birthday;
}
