import java.text.SimpleDateFormat;
import java.util.*;

import com.messy.common.model.entity.ExcelDataEntity;
import com.messy.common.model.util.ExcelWriterWithEasyExcel;
import com.messy.common.model.util.ExcelWriterWithPOI;

public class ExcelMain {
    public static void main(String[] args) throws Exception {
        ExcelWriterWithEasyExcel.WriteExcelWithEasyExcel("E:\\test_easyexcel.xlsx",
                "download on "+new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                ExcelDataEntity.class,
                generateData());
        ExcelWriterWithPOI.WriteExcelWithPOI("E:\\test_poi.xlsx",
                "download on "+new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                generateData());
    }


    private static List<ExcelDataEntity> generateData() {
        List<ExcelDataEntity> excelDataList = new ArrayList<>();
        ExcelDataEntity excelDataEntity;
        for (int i = 1; i <= 20; i++) {
            excelDataEntity = new ExcelDataEntity();
            excelDataEntity.setName("Robert 0" + i);
            excelDataEntity.setAge(i+20);
            excelDataEntity.setPhoneNumber("13932802374");
            excelDataEntity.setAddress("Yuelu Boulevard");
            excelDataEntity.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            excelDataList.add(excelDataEntity);
        }
        return excelDataList;
    }
}
