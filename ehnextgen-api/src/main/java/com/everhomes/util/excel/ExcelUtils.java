package com.everhomes.util.excel;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.test.IntegrationTest;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by xq.tian on 2016/9/8.
 */
public class ExcelUtils {

    private HttpServletResponse response;

    // 文件名
    private String fileName = "export";
    //文件保存路径
    private String fileDir;
    //sheet名
    private String sheetName = "sheet1";
    //表头字体
    private String titleFontType = "Arial Unicode MS";
    //表头字号
    private short titleFontSize = 12;
    //正文字体
    private String contentFontType = "Arial Unicode MS";
    //正文字号
    private short contentFontSize = 12;
    // 是否需要序号列
    private boolean needSequenceColumn = true;

    private XSSFWorkbook workbook = null;

    public ExcelUtils(String fileDir, String sheetName) {
        this.fileDir = fileDir;
        this.sheetName = sheetName;
        workbook = new XSSFWorkbook();
    }

    public ExcelUtils(HttpServletResponse response, String fileName, String sheetName) {
        this.response = response;
        this.sheetName = sheetName;
        this.fileName = fileName;
        workbook = new XSSFWorkbook();
    }

    /**
     * 设置表头字体.
     *
     * @param titleFontType
     */
    public ExcelUtils setTitleFontType(String titleFontType) {
        this.titleFontType = titleFontType;
        return this;
    }

    public ExcelUtils setNeedSequenceColumn(boolean needSequenceColumn) {
        this.needSequenceColumn = needSequenceColumn;
        return this;
    }

    /**
     * 设置表头字体大小.
     *
     * @param titleFontSize
     */
    public ExcelUtils setTitleFontSize(short titleFontSize) {
        this.titleFontSize = titleFontSize;
        return this;
    }

    /**
     * 设置正文字体.
     *
     * @param contentFontType
     */
    public ExcelUtils setContentFontType(String contentFontType) {
        this.contentFontType = contentFontType;
        return this;
    }

    /**
     * 设置正文字号.
     *
     * @param contentFontSize
     */
    public ExcelUtils setContentFontSize(short contentFontSize) {
        this.contentFontSize = contentFontSize;
        return this;
    }

    /**
     * 写excel.
     *
     * @param propertyNames 对应bean的属性名
     * @param titleName   列名
     * @param columnSizes   列宽
     * @param dataList    数据
     */
    public void writeExcel(String[] propertyNames, String[] titleName, int[] columnSizes, List<?> dataList) {
        try (OutputStream out = getOutputStream();) {
            ByteArrayOutputStream excelStream = buildExcel(propertyNames, titleName, columnSizes, dataList);
            out.write(excelStream.toByteArray());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	public void writeExcel(List<String> propertyNames, List<String> titleName, List<Integer> columnSizes,
			List<?> dataList) {
		writeExcel(propertyNames.toArray(new String[propertyNames.size()]), titleName.toArray(new String[titleName.size()]), ArrayUtils.toPrimitive(columnSizes.toArray(new Integer[columnSizes.size()])), dataList);
	}

    private OutputStream getOutputStream() throws IOException {
        if (fileDir != null) {
            // 保存文件
            return new FileOutputStream(fileDir);
        } else {
            // 从response中获取输出流
            buildResponse();
            return response.getOutputStream();
        }
    }

    private void buildResponse() throws UnsupportedEncodingException {
        fileName = fileName + ".xlsx";
        response.setContentType("application/msexcel");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
    }

    private ByteArrayOutputStream buildExcel(String[] propertyNames, String[] titleNames, int[] titleSize, List<?> dataList) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        Sheet sheet = workbook.createSheet(this.sheetName);
        // 表头
        Row titleNameRow = sheet.createRow(0);
        //设置样式
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        setTitleFont(titleStyle);

        if (needSequenceColumn) {
            Cell numberColumn = titleNameRow.createCell(0);
            numberColumn.setCellStyle(titleStyle);
            numberColumn.setCellValue(StringEscapeUtils.unescapeJava("\\u5e8f\\u53f7"));
        }

        for (int i = 0; i < titleNames.length; i++) {
            sheet.setColumnWidth(needSequenceColumn ? i + 1 : i, titleSize[i] * 256);    //设置宽度
            Cell cell = titleNameRow.createCell(needSequenceColumn ? i + 1 : i);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(titleNames[i]);
        }

        // 内容样式
        XSSFCellStyle contentStyle = workbook.createCellStyle();
        setContentFont(contentStyle);

        if (dataList != null && dataList.size() > 0 && propertyNames.length > 0) {
            for (int rowIndex = 1; rowIndex <= dataList.size(); rowIndex++) {
                Object obj = dataList.get(rowIndex - 1);     //获得该对象
                Class clazz = obj.getClass();     //获得该对对象的class实例
                Row dataRow = sheet.createRow(rowIndex);
                // dataRow.setRowStyle(contentStyle);
                for (int propertyIndex = 0; needSequenceColumn ? (propertyIndex <= propertyNames.length) : (propertyIndex < propertyNames.length); propertyIndex++) {
                    Cell cell = dataRow.createCell(propertyIndex);
                    cell.setCellStyle(contentStyle);
                    if (needSequenceColumn && propertyIndex == 0) {
                        cell.setCellValue(rowIndex);
                        continue;
                    }
                    String title = propertyNames[needSequenceColumn ? propertyIndex-1 : propertyIndex].trim();
                    if (!"".equals(title)) {  //字段不为空
                        //使首字母大写
                        String UTitle = StringUtils.capitalize(title); // 使其首字母大写;
                        String methodName = "get" + UTitle;
                        // 设置要执行的方法
                        Method method = clazz.getMethod(methodName);
                        //获取返回类型
                        String returnType = method.getReturnType().getName();

                        Object ret = method.invoke(obj);
                        String data = ret == null ? "" : ret.toString();
                        if (!"".equals(data)) {
                            switch (returnType) {
                                case "int":
                                    cell.setCellValue(Integer.parseInt(data));
                                    break;
                                case "long":
                                    cell.setCellValue(Long.parseLong(data));
                                    break;
                                case "float":
                                    cell.setCellValue(Float.parseFloat(data));
                                    break;
                                case "double":
                                    cell.setCellValue(Double.parseDouble(data));
                                    break;
                                default:
                                    cell.setCellValue(data);
                            }
                        }
                    }
                }
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out;
    }

    /**
     * 设置正文字体
     */
    private void setContentFont(CellStyle style) {
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(this.contentFontSize);
        font.setFontName(this.contentFontType);
        // font.setBold(true);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font);
    }

    /**
     * 设置表头字体
     */
    private void setTitleFont(CellStyle style) {
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(this.titleFontSize);
        font.setFontName(this.titleFontType);
        font.setBold(true);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font);
    }

    /*public static void main(String[] args) {
        ExcelUtils u = new ExcelUtils("D://ss.xlsx", "sheet1");
        u.setContentFontSize(Short.valueOf("15"));
        u.setTitleFontSize(Short.valueOf("20"));
        // u.setNeedSequenceColumn(false);
        String[] titleColumn = {"name", "age", "time"};
        String[] titleName = {"标题一", "标题二", "dsadas"};
        int[] titleSize = {30, 50, 30};
        List<User> users = Arrays.asList(new User("xiaoxiao", 244, 44L), new User("fs", 87654, 44L), new User("发生地方", 2, 44L), new User("发送到", 2, 44L));
        u.writeExcel(titleColumn, titleName, titleSize, users);
    }

    static class User {
        String name;
        int age;
        Long time;

        public User(String name, int age, Long time) {
            this.name = name;
            this.age = age;
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }
    }*/
    /**
     * @Title: exportExcel
     * @Description: 导出Excel的方法
     * @author: evan @ 2014-01-09
     * @param workbook
     * @param sheetNum (sheet的位置，0表示第一个表格中的第一个sheet)
     * @param sheetTitle  （sheet的名称）
     * @param headers    （表格的标题）
     * @param result   （表格的数据）
     * @throws Exception
     */
    public void exportExcel(HSSFWorkbook workbook, int sheetNum,
                            String sheetTitle, String[] headers, List<List<String>> result) throws Exception {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetTitle);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
//        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);

        // 产生表格标题行
        HSSFRow row = sheet.createRow(2);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text.toString());
        }
        // 遍历集合数据，产生数据行
        if (result != null) {
            int index = 1;
            for (List<String> m : result) {
                row = sheet.createRow(index+2);
                int cellIndex = 0;
                for (String str : m) {
                    HSSFCell cell = row.createCell((short) cellIndex);
                    cell.setCellValue(str.toString());
                    cellIndex++;
                }
                index++;
            }
        }
    }

    public ExcelUtils() {
    }
}
