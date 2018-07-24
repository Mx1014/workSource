//@formatter:off
package com.everhomes.assetPayment;

import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/13.
 */

public class ExcelMultipleSheetTest {
    @Test
    public void foo(){
        Node a = new Node("a", 1);
        Node b = new Node("b", 2);
        a.setNode(b);
        Node conver = ConvertHelper.convert(a, Node.class);
        System.out.println(conver);
    }
    class Node{
        private String gg;
        private int wp;
        private Node node;

        @Override
        public String toString() {
            return StringHelper.toJsonString(this);
        }

        public Node(String gg, int wp) {
            this.gg = gg;
            this.wp = wp;
        }

        public String getGg() {
            return gg;
        }

        public void setGg(String gg) {
            this.gg = gg;
        }

        public int getWp() {
            return wp;
        }

        public void setWp(int wp) {
            this.wp = wp;
        }

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
        }
    }
    @Test
    @SuppressWarnings("unchecked")
    public void fun() {
        try {
            OutputStream out = new FileOutputStream("D:\\test.xls");
            List<List<String>> data = new ArrayList<List<String>>();
            for (int i = 1; i < 5; i++) {
                List rowData = new ArrayList();
                rowData.add(String.valueOf(i));
                rowData.add("东霖柏鸿");
                data.add(rowData);
            }
            String[] headers = { "ID", "用户名" };
            ExcelUtils eeu = new ExcelUtils();
            HSSFWorkbook workbook = new HSSFWorkbook();
//            eeu.exportExcel(workbook, 0, "上海", headers, data);
//            eeu.exportExcel(workbook, 1, "深圳", headers, data);
//            eeu.exportExcel(workbook, 2, "广州", headers, data);
            //原理就是将所有的数据一起写入，然后再关闭输入流。
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
