package com.messy.common.model.watermark.handler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.messy.common.model.watermark.content.CommonWatermarkContent;
import com.messy.common.model.watermark.constant.WatermarkParam;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author wangyw
 * @Since 2021/11/5
 *
 * excel添加水印策略类.
 * 可在多种场景下使用，提供示例如下：
 * （1）easyexcel使用场景：
 *          EasyExcel.write(excelPathName,clazz)
 *                   .inMemory(true)
 *                   .registerWriteHandler(new ExcelWatermarkHandler("watermark content"))
 *                   .sheet("Sheet1")
 *                   .doWrite(dataList);
 * （2）POI excel使用场景（可为workbook的各个Sheet添加水印；只支持XSSFWorkbook）：
 *          ExcelWatermarkHandler.putWatermarkToWorkbook(workbook,watermarkContent);
 */

@Slf4j
@RequiredArgsConstructor
public class ExcelWatermarkHandler implements SheetWriteHandler {

    private final String watermarkContent;

    /**
     * 根据用户姓名，工号，创建日期信息生成水印图片
     *
     * @param contentObj    用户姓名，工号，创建日期信息
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream createWaterMark(CommonWatermarkContent contentObj) throws IOException {
        return createWaterMark(contentObj.toString());
    }

    /**
     *
     * @param content                   水印内容
     * @return byteArrayOutputStream    水印图片流，使用后请关闭
     * @throws IOException
     */
    public static ByteArrayOutputStream createWaterMark(String content) throws IOException {

        BufferedImage waterMarkImage = new BufferedImage(WatermarkParam.width, WatermarkParam.height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2d = waterMarkImage.createGraphics();
        waterMarkImage = graphics2d.getDeviceConfiguration().createCompatibleImage(WatermarkParam.width, WatermarkParam.height, Transparency.TRANSLUCENT);
        graphics2d.dispose();
        graphics2d = waterMarkImage.createGraphics();
        graphics2d.setColor(WatermarkParam.color);
        graphics2d.setStroke(new BasicStroke(1));
        graphics2d.setFont(WatermarkParam.font);
        graphics2d.rotate(WatermarkParam.theta, (double) waterMarkImage.getWidth() / 2, (double) waterMarkImage.getHeight() / 2);
        FontRenderContext context = graphics2d.getFontRenderContext();
        Rectangle2D bounds = WatermarkParam.font.getStringBounds(content, context);
        double x = (WatermarkParam.width - bounds.getWidth()) / 2;
        double y = (WatermarkParam.height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;

        graphics2d.drawString(content, (int) x, (int) baseY);

        graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        graphics2d.dispose();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(waterMarkImage, WatermarkParam.format, byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    /**
     * 水印策略，为XSSFSheet添加水印
     *
     * @param sheet     待添加水印的sheet
     * @param bytes     水印图片字节
     */
    public static void putWatermarkToExcel(XSSFSheet sheet, byte[] bytes) {
        XSSFWorkbook workbook = sheet.getWorkbook();
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        PackageRelationship pr = sheet.getPackagePart().
                addRelationship(workbook.getAllPictures().get(pictureIdx).getPackagePart().getPartName(),
                        TargetMode.INTERNAL, XSSFRelation.IMAGES.getRelation(), null);

        sheet.getCTWorksheet().addNewPicture().setId(pr.getId());
    }

    /**
     * 为XSSFWorkbook的每个Sheet添加水印
     *
     * @param workbook          待添加水印的workbook
     * @param watermarkContent  水印内容
     */
    public static void putWatermarkToWorkbook(XSSFWorkbook workbook,String watermarkContent) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = createWaterMark(watermarkContent)) {
            int pictureIdx = workbook.addPicture(byteArrayOutputStream.toByteArray(), Workbook.PICTURE_TYPE_PNG);
            for(int i = 0;i < workbook.getNumberOfSheets();i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                PackageRelationship pr = sheet.getPackagePart().addRelationship(workbook.getAllPictures().get(pictureIdx).getPackagePart().getPartName(), TargetMode.INTERNAL, XSSFRelation.IMAGES.getRelation(), null);
                sheet.getCTWorksheet().addNewPicture().setId(pr.getId());
            }
        }
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @SneakyThrows
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        ByteArrayOutputStream watermark = null;
        try {
            watermark = createWaterMark(watermarkContent);
            XSSFSheet sheet = (XSSFSheet) writeSheetHolder.getSheet();
            putWatermarkToExcel(sheet, watermark.toByteArray());
        } catch(Exception e) {
            log.error("[ExcelWatermarkHandler] afterSheetCreate()",e);
        } finally {
            if(watermark != null)
                watermark.close();
        }
    }
}