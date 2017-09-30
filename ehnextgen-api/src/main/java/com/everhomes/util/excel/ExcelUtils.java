package com.everhomes.util.excel;

import com.everhomes.user.UserContact;
import com.everhomes.user.UserContext;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.test.IntegrationTest;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
    //备注内容
    private String titleRemark;
    //必填项设置列表
    private List<Integer> mandatoryTitle;
    //表头字体
    private String titleFontType = "Arial Unicode MS";
    //表头字号
    private short titleFontSize = 12;
    //正文字体
    private String contentFontType = "Arial Unicode MS";
    //正文字号
    private short contentFontSize = 12;
    //首行备注字体
    private String titleRemarkFontType = "宋体";
    //首行备注字号
    private short titleRemarkFontSize = 18;
    //首行备注高度
    private short titleRemarkCellHeight = 18;
    //是否需要序号列
    private boolean needSequenceColumn = true;
    //是否需要首行备注
    private boolean needTitleRemark = false;
    //是否需要标注必填项
    private boolean needMandatoryTitle = false;

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

    public ExcelUtils setNeedTitleRemark(boolean needTitleRemark) {
        this.needTitleRemark = needTitleRemark;
        return this;
    }

    public ExcelUtils setNeedMandatoryTitle(boolean needMandatoryTitle) {
        this.needMandatoryTitle = needMandatoryTitle;
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
     * 设置首行备注信息
     *
     */
    public ExcelUtils setTitleRemark(String titleRemark, short titleRemarkFontSize, short titleRemarkCellHeight) {
        this.titleRemark = titleRemark;
        this.titleRemarkFontSize = titleRemarkFontSize;
        this.titleRemarkCellHeight = titleRemarkCellHeight;
        return this;
    }

    public ExcelUtils setMandatoryTitle(List<Integer> mandatoryTitle) {
        this.mandatoryTitle = mandatoryTitle;
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
        // 设置样式
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        setTitleFont(titleStyle);
        // 若含有必填项的样式
        XSSFCellStyle mandatoryTitleStyle = workbook.createCellStyle();
        setMandatoryTitleFont(mandatoryTitleStyle);

        if (needSequenceColumn) {
            Cell numberColumn = titleNameRow.createCell(0);
            numberColumn.setCellStyle(titleStyle);
            numberColumn.setCellValue(StringEscapeUtils.unescapeJava("\\u5e8f\\u53f7"));
        }

        for (int i = 0; i < titleNames.length; i++) {
            sheet.setColumnWidth(needSequenceColumn ? i + 1 : i, titleSize[i] * 256);    //设置宽度
            Cell cell = titleNameRow.createCell(needSequenceColumn ? i + 1 : i);
            if (needMandatoryTitle) {
                if (mandatoryTitle.get(i) == 1)
                    cell.setCellStyle(mandatoryTitleStyle);
            } else
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
        if(needTitleRemark)
            addTitleRemark(workbook,titleRemarkCellHeight);
        workbook.write(out);
        return out;
    }

    /**
     * 设置首行备注
     */
    private void addTitleRemark(XSSFWorkbook workbook,short titleRemarkCellHeight){
        Sheet sheet = workbook.getSheet(this.sheetName);
        sheet.shiftRows(0,sheet.getLastRowNum(),1,true,false);
        // 合并首行单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,12));
        // 表头
        Row titleRemarkRow = sheet.createRow(0);
        titleRemarkRow.setHeight(titleRemarkCellHeight);
        // 设置样式
        XSSFCellStyle titleRemarkStyle = workbook.createCellStyle();
        setTitleRemarkFont(titleRemarkStyle);
        // 加入内容
        Cell cell = titleRemarkRow.createCell(0);
        cell.setCellStyle(titleRemarkStyle);
        cell.setCellValue(titleRemark);
/*        for (int i = 0; i < titleNames.length; i++) {
            sheet.setColumnWidth(needSequenceColumn ? i + 1 : i, titleSize[i] * 256);    //设置宽度
            Cell cell = titleNameRow.createCell(needSequenceColumn ? i + 1 : i);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(titleNames[i]);
        }*/
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

    /**
     * 设置表头字体（包含必填项）
     */
    private void setMandatoryTitleFont(CellStyle style) {
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(this.titleFontSize);
        font.setFontName(this.titleFontType);
        font.setColor(IndexedColors.RED.index);
        font.setBold(true);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font);
    }

    /**
     * 设置首行备注字体
     */
    private void setTitleRemarkFont(CellStyle style) {
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(this.titleRemarkFontSize);
        font.setFontName(this.titleRemarkFontType);
        font.setBold(true);
        style.setFont(font);
        style.setWrapText(true);
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
     * @Author from internet
     * @param workbook
     * @param sheetNum (sheet的位置，0表示第一个表格中的第一个sheet)
     * @param sheetTitle  （sheet的名称）
     * @param headers    （表格的标题）
     * @param result   （表格的数据）
     * @throws Exception
     */
    public void exportExcel(HSSFWorkbook workbook, int sheetNum,
                            String sheetTitle, String[] headers, List<List<String>> result,String[] mandatory) throws Exception {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetTitle);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
//        style.setFillForegroundColor(HSSFColor.GREEN.index);
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个内容字体
        HSSFCellStyle style_content = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        style_content.setFont(font);
        //非必填的标题的样式
        HSSFFont font2 = workbook.createFont();
        font2.setColor(HSSFColor.BLACK.index);
        font2.setFontHeightInPoints((short) 16);
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font2);
        //必填的字体和样式

        HSSFCellStyle style_m = workbook.createCellStyle();
        // 设置这些样式
//        style.setFillForegroundColor(HSSFColor.GREEN.index);
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style_m.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFFont font4 = workbook.createFont();
        font4.setColor(HSSFColor.RED.index);
        font4.setFontHeightInPoints((short) 16);
        font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        style_m.setFont(font4);
        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);
        //产生说明
        HSSFFont font3 = workbook.createFont();
        font3.setColor(HSSFColor.BLACK.index);
        font3.setFontHeightInPoints((short) 18);
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFCellStyle introStyle = workbook.createCellStyle();
        introStyle.setWrapText(true);
        introStyle.setAlignment(HorizontalAlignment.LEFT);
        introStyle.setFillBackgroundColor(HSSFColor.YELLOW.index);
        introStyle.setFont(font3);
        CellRangeAddress cra = new CellRangeAddress(0,0,0,11);
        sheet.addMergedRegion(cra);
        HSSFRow introRow = sheet.createRow(0);
        introRow.setHeightInPoints(130);
        HSSFCell introCell = introRow.createCell(0);
        introCell.setCellStyle(introStyle);
        introCell.setCellValue("填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）\n" +
                "1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照要求输入。\n" +
                "2、请在表格里面逐行录入数据，建议一次最多导入400条信息。\n" +
                "3、请不要随意复制单元格，这样会破坏字段规则校验。\n" +
                "4、红色字段为必填项。");

        // 产生表格标题行
        HSSFRow row = sheet.createRow(1);
        // 把字体应用到当前的样式,标题为加粗的

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            if(mandatory[i].equals("1")){
                cell.setCellStyle(style_m);
            }else{
                cell.setCellStyle(style);
            }
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text.toString());
        }
        style.setFont(font);
        // 遍历集合数据，产生数据行
        if (result != null) {
            int index = 1;
            for (List<String> m : result) {
                row = sheet.createRow(index+1);
                int cellIndex = 0;
                for (String str : m) {
                    HSSFCell cell = row.createCell((short) cellIndex);
                    cell.setCellStyle(style_content);
                    cell.setCellValue(str.toString());
                    cellIndex++;
                }
                index++;
            }
        }
    }
    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(".xls".equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(".xlsx".equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell){
        String value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());
                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
                    value = sdf.format(cell.getDateCellValue());
                }else{
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                Boolean booleanCellValue = cell.getBooleanCellValue();
                value = booleanCellValue.toString();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

    public ExcelUtils() {
    }
}
