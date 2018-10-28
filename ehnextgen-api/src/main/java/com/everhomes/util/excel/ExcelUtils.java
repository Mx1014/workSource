package com.everhomes.util.excel;

import com.everhomes.dynamicExcel.DynamicField;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2016/9/8.
 */
public class ExcelUtils {

    private HttpServletResponse response;
    private HttpServletRequest request;

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
    //说明的背景颜色
    private Short titleRemarkBackGroundColorIndex = null;
    //标题的北京颜色
    private Short headerBackGroundColorIndex = null;

    private XSSFWorkbook workbook = null;

    // 导出的格式均为文本 added by wentian 2018/4/9
    private boolean isCellStylePureString = false;

    public ExcelUtils setIsCellStylePureString(boolean flag){
        this.isCellStylePureString = flag;
        return this;
    }

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
    
    public ExcelUtils(HttpServletRequest request, HttpServletResponse response, String fileName, String sheetName) {
    	this.request = request;
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

    public ExcelUtils setTitleRemarkColorIndex(Short index){
        this.titleRemarkBackGroundColorIndex = index;
        return this;
    }

    public ExcelUtils setHeaderColorIndex(Short index){
        this.headerBackGroundColorIndex = index;
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
            ByteArrayOutputStream excelStream = buildExcel(propertyNames, titleName, false, columnSizes, dataList);
            out.write(excelStream.toByteArray());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 写excel.
     *
     * @param propertyNames 对应bean的属性名
     * @param titleName   列名
     * @param columnSizes   列宽
     * @param dataList    数据
     */
    public ByteArrayOutputStream writeExcelOutStream(String[] propertyNames, String[] titleName, int[] columnSizes, List<?> dataList) {
        try (OutputStream out = getOutputStream();) {
            ByteArrayOutputStream excelStream = buildExcel(propertyNames, titleName, false, columnSizes, dataList);
            return excelStream;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    // 重载，可以在必填项前加星号
    public void writeExcel(String[] propertyNames, String[] titleName, boolean withStar, int[] columnSizes, List<?> dataList) {
        try (OutputStream out = getOutputStream();) {
            ByteArrayOutputStream excelStream = buildExcel(propertyNames, titleName, withStar, columnSizes, dataList);
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

	public OutputStream getOutputStream(List<String> propertyNames, List<String> titleName, List<Integer> columnSizes, List<?> dataList){
        try {
            ByteArrayOutputStream excelStream = buildExcel(propertyNames.toArray(new String[propertyNames.size()]), titleName.toArray(new String[titleName.size()]), false, ArrayUtils.toPrimitive(columnSizes.toArray(new Integer[columnSizes.size()])), dataList);
            return excelStream;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

	public OutputStream getOutputStream(String[] propertyNames, String[] titleNames, int[] titleSize, List<?> dataList){
        try {
            ByteArrayOutputStream excelStream = buildExcel(propertyNames, titleNames, false, titleSize, dataList);
            return excelStream;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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

		// 设置文件返回类型
		fileName = fileName + ".xlsx";
		response.setContentType("application/msexcel");

		// 默认处理
		response.setHeader("Content-Disposition",
				"attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));

		if (null == request) {
			return;
		}
		
		String Agent = request.getHeader("User-Agent");
		if (null != Agent && (Agent.toLowerCase().indexOf("firefox") != -1)) {
			//对火狐做特殊处理
			response.setHeader("Content-Disposition",
					String.format("attachment;filename*=utf-8'zh_cn'%s", URLEncoder.encode(fileName, "utf-8")));
		}
	}

    private ByteArrayOutputStream buildExcel(String[] propertyNames, String[] titleNames, boolean withStarAhead, int[] titleSize, List<?> dataList) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        Sheet sheet = workbook.createSheet(this.sheetName);
        // 表头
        Row titleNameRow = sheet.createRow(0);
        // 设置样式
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        setTitleFont(titleStyle);
        // 设置为纯文本 by wentian
        XSSFDataFormat format = workbook.createDataFormat();
        if(isCellStylePureString == true){
            titleStyle.setDataFormat(format.getFormat("@"));
        }


        if(headerBackGroundColorIndex != null){
            titleStyle.setFillBackgroundColor(headerBackGroundColorIndex.shortValue());
        }
        // 若含有必填项的样式
        XSSFCellStyle mandatoryTitleStyle = workbook.createCellStyle();
        setMandatoryTitleFont(mandatoryTitleStyle);
        if(headerBackGroundColorIndex != null){
            mandatoryTitleStyle.setFillBackgroundColor(headerBackGroundColorIndex.shortValue());
        }
        if(isCellStylePureString == true){
            mandatoryTitleStyle.setDataFormat(format.getFormat("@"));
        }


        if (needSequenceColumn) {
            Cell numberColumn = titleNameRow.createCell(0);
            numberColumn.setCellStyle(titleStyle);
            numberColumn.setCellValue(StringEscapeUtils.unescapeJava("\\u5e8f\\u53f7"));
        }
        for (int i = 0; i < titleNames.length; i++) {
            sheet.setColumnWidth(needSequenceColumn ? i + 1 : i, titleSize == null ? 20 * 256 : titleSize[i] * 256);    //设置宽度
            Cell cell = titleNameRow.createCell(needSequenceColumn ? i + 1 : i);
            if (needMandatoryTitle) {
                if (mandatoryTitle.get(i) == 1){
                    cell.setCellStyle(mandatoryTitleStyle);
                    if(withStarAhead){
                        titleNames[i] = "*"+titleNames[i];
                    }
                }
            } else
                cell.setCellStyle(titleStyle);
            cell.setCellValue(titleNames[i]);
        }

        // 内容样式
        XSSFCellStyle contentStyle = workbook.createCellStyle();
        setContentFont(contentStyle);
        if(isCellStylePureString == true){
            contentStyle.setDataFormat(format.getFormat("@"));
            //每一列都做个默认文本
            for(int i = 0; i < titleNames.length; i ++){
                sheet.setDefaultColumnStyle(i, contentStyle);
            }

        }

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
                        //由于导出的账单组费项是动态的，原有的对象属性get/set不满足要求，采用map处理动态字段
                        if(obj instanceof Map) {
                        	Map map = (Map) obj;
                        	String data = (String) map.get(title);
                        	cell.setCellValue(data);
                        }else {
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
    private void addTitleRemark(XSSFWorkbook workbook,short titleRemarkCellHeight ){
        Sheet sheet = workbook.getSheet(this.sheetName);
        sheet.shiftRows(0,sheet.getLastRowNum(),1,true,false);
        // 合并首行单元格
//        sheet.addMergedRegion(new CellRangeAddress(0,0,0,12));
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,13));
        // 表头
        Row titleRemarkRow = sheet.createRow(0);
        titleRemarkRow.setHeight(titleRemarkCellHeight);
        // 设置样式
        XSSFCellStyle titleRemarkStyle = workbook.createCellStyle();
        setTitleRemarkFont(titleRemarkStyle);
        if(titleRemarkBackGroundColorIndex != null){
//			设置背景色没有起作用，设置前景色和填充模式（这里我们设置为全填充），才能使单元格变颜色
//          titleRemarkStyle.setFillBackgroundColor(titleRemarkBackGroundColorIndex.shortValue());
            titleRemarkStyle.setFillForegroundColor(titleRemarkBackGroundColorIndex.shortValue());
            titleRemarkStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        }
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
        //  日期格式
        XSSFDataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("yyyy-MM-dd"));
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
        //  日期格式
        XSSFDataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("yyyy-MM-dd"));
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
        //  日期格式
        XSSFDataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("yyyy-MM-dd"));
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
    public void exportExcel(org.apache.poi.ss.usermodel.Workbook workbook, int sheetNum,
                            String sheetTitle, String[] headers, List<List<String>> result,String[] mandatory) throws Exception {
        // 生成一个表格
        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetTitle);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 30);
        //sheet.autoSizeColumn(1, true);
        // 生成一个样式
        CellStyle style_non_m = workbook.createCellStyle();
        // 设置这些样式
//        style.setFillForegroundColor(HSSFColor.GREEN.index);
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style_non_m.setBorderBottom(CellStyle.BORDER_THIN);
        style_non_m.setBorderLeft(CellStyle.BORDER_THIN);
        style_non_m.setBorderRight(CellStyle.BORDER_THIN);
        style_non_m.setBorderTop(CellStyle.BORDER_THIN);
        style_non_m.setAlignment(CellStyle.ALIGN_CENTER);
        // 生成一个内容字体
        CellStyle style_content = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        style_content.setFont(font);
        //非必填的标题的样式
        Font font2 = workbook.createFont();
        font2.setColor(HSSFColor.BLACK.index);
        font2.setFontHeightInPoints((short) 16);
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style_non_m.setFont(font2);
        //必填的字体和样式

        CellStyle style_m = workbook.createCellStyle();
        // 设置这些样式
//        style.setFillForegroundColor(HSSFColor.GREEN.index);
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style_m.setBorderBottom(CellStyle.BORDER_THIN);
        style_m.setBorderLeft(CellStyle.BORDER_THIN);
        style_m.setBorderRight(CellStyle.BORDER_THIN);
        style_m.setBorderTop(CellStyle.BORDER_THIN);
        style_m.setAlignment(CellStyle.ALIGN_CENTER);

        Font font4 = workbook.createFont();
        font4.setColor(HSSFColor.RED.index);
        font4.setFontHeightInPoints((short) 16);
        font4.setBoldweight(Font.BOLDWEIGHT_BOLD);

        style_m.setFont(font4);
        // 指定当单元格内容显示不下时自动换行
        style_non_m.setWrapText(true);
        style_content.setWrapText(true);
        style_m.setWrapText(true);
        //产生说明
        Font font3 = workbook.createFont();
        font3.setColor(HSSFColor.BLACK.index);
        font3.setFontHeightInPoints((short) 12);
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        CellStyle introStyle = workbook.createCellStyle();
        introStyle.setWrapText(true);
        introStyle.setAlignment(HorizontalAlignment.LEFT);
        introStyle.setFillBackgroundColor(HSSFColor.YELLOW.index);
        introStyle.setFont(font3);
        CellRangeAddress cra = new CellRangeAddress(0,0,0,11);
        sheet.addMergedRegion(cra);
        Row introRow = sheet.createRow(0);
        //这里改成170 原：130
        introRow.setHeightInPoints(170);
        Cell introCell = introRow.createCell(0);
        introCell.setCellStyle(introStyle);
        //这里可以根据sheet决定怎么显示枚举，晚上搞这个
        String instruction = "";
        switch (sheetTitle){
            case "人才团队信息":
                instruction =
                            "性别: 男、女 \n" +
                            "技术职称: 高级、中级、初级 \n" +
                            "是否海归: 是、否 \n"  +
                            "个人评定: 否、上海（千人批次）、中央（千人批次）、浦江人才、两院院士、高层次人才、其他 \n";
                break;
            case "申报项目":
                instruction =
                            "项目来源: 国家、上海市、嘉定区、其他（可多选） \n" +
                            "项目状态: 进行中、已完结 \n";
                break;
            case "工商信息":
                instruction =
                            "企业类型: 企业、事业单位、政府机关、社会团体、民办非企业单位、基金会、其他组织机构 \n" +
                            "企业控股情况: 国有控股、集体控股、私人控股、港澳台商控股、外商投资、其他 \n";
                break;
            case "投融情况":
                break;
            case "经济指标":
                break;
            case "商标信息":
                instruction =
                            "商标类型: 文字商标、图片商标、品牌商标、著名商标 \n" +
                            "企业控股情况: 国有控股、集体控股、私人控股、港澳台商控股、外商投资、其他 \n";

                break;
            case "专利信息":
                instruction =
                        "专利类型: 发明专利、实用新型、外观设计、集成电路布图、软件著作权、证书 \n" +
                        "专利状态: 申请、授权 \n";
                break;
            case "证书":
                break;
            case "跟进信息":
                break;
            case "计划信息":
                break;
            case "离场信息":
                break;
            case "入驻信息":
                break;
            //物业巡检中添加 备注信息 暂时在注意事项中协商select的byte值  列出
            default:
                instruction =
                        "二维码状态： 停用 、启用 \n"+
                        "当前状态：不完整 、使用中 、维修中 、报废 、停用  、备用 \n"+
                        "设备类型：消防 、强电 、弱电 、电梯 、空调 、给排水、空置房、装修、安保、日常工作检查、公共设施检查、周末值班、安全检查、其他 \n"+
                        "日期格式:  yyyy-MM-dd \n";
        }
        introCell.setCellValue("填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）\n" +
                "1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照要求输入。\n" +
                "2、请在表格里面逐行录入数据，建议一次最多导入400条信息。\n" +
                "3、请不要随意复制单元格，这样会破坏字段规则校验。\n" +
                "4、红色字段为必填项,不填将导致导入失败。\n" +
                "以下字段请按枚举值填写，否则导入将失败 \n" +
                instruction
        );
//        "户型: 单间、一室一厅、两室一厅、两室两厅、三室一厅、三室两厅、三室三厅、四室两厅、其他 \n" +
//                "资产类型: 住宅、写字楼、酒店式公寓、厂房、库房、车位、其他 \n" +
//                "资产来源: 自管、业主放盘、大业主交管、其他 \n" +
//                "资产状态: 已租、已售、自用、待租、待售、其他 \n" +
//                "装修状态: 已装修、未装修 \n" +
//                "朝向: 坐北朝南、坐南朝北、坐东朝西、坐西朝东、其他 \n" +
//                "产权归属: 自有、出售、非产权 \n" +
//                "入住状况: 已入住、未入住 \n" +
//                "资产来源: 自管、业主放盘、大业主交管、其他 \n" +
//                "门牌类型: 住宅、写字楼、商铺，酒店式公寓、厂房、库房、车位、其他 \n" +
//                "门牌来源: 自管、业主放盘、大业主交管、其他 \n" +
//                "门牌状态: 已租、已售、自用、待租、待售、其他 \n" +
//                "表计类型: 应收部分、应付部分、自用部分、公共计量部分、其他 \n"

        // 产生表格标题行
        Row row = sheet.createRow(1);
        // 把字体应用到当前的样式,标题为加粗的

        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell((short) i);
            if(mandatory[i].equals("1")){
                cell.setCellStyle(style_m);
            }else{
                cell.setCellStyle(style_non_m);
            }
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text.toString());
        }

        // 遍历集合数据，产生数据行
        if (result != null) {
            int index = 1;
            for (List<String> m : result) {
                row = sheet.createRow(index+1);
                int cellIndex = 0;
                for (String str : m) {
                    Cell cell = row.createCell((short) cellIndex);
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
//        String fileType = fileName.substring(fileName.lastIndexOf("."));
        //现在导出的模板都是xssf了

//        if(".xls".equals(fileType)){
//            wb = new HSSFWorkbook(inStr);  //2003-
//        }else if(".xlsx".equals(fileType)){
//            wb = new XSSFWorkbook(inStr);  //2007+
//        }else{
//            throw new Exception("解析的文件格式有误！");
//        }
        try{
            wb = new XSSFWorkbook(inStr);
        }catch (Exception e){
            try{
                wb = new HSSFWorkbook(inStr);
            }catch (Exception e1){
                throw e1;
            }
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell, DynamicField ds){
        String value = ds.getDefaultValue();
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        String dateFormat = ds.getDateFormat();
        SimpleDateFormat sdf = null;
        if(null == dateFormat){
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }else{
            try{
                sdf = new SimpleDateFormat(dateFormat);  //日期格式化
            }catch (Exception e){
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
        }
//
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字
        if(cell == null){
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());
                }else if(cell.getCellStyle().getDataFormatString().startsWith("m/d/yy")){
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

    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell){
        String value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字
        if(cell == null){
            return "";
        }
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

    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    public static String getRealCellValue(Cell cell){
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
