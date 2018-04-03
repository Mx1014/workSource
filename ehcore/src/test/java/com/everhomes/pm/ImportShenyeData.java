package com.everhomes.pm;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.region.Region;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRegionsDao;
import com.everhomes.util.ConvertHelper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


@Controller
public class ImportShenyeData {
	
	@Autowired
    private DbProvider dbProvider;
	
	private static Long communityId = 56641L;
	private static Long eh_community_geopointsId = 52545L;

	private static Long regionId = 16002L;
	
	BufferedWriter communitywriter = null;
	BufferedWriter logwriter = null;
	BufferedWriter otherwriter = null;

	
	@RequestMapping("createImportShenyeData")
	public void create(@RequestParam()String path) {
		try {
			communitywriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("C:\\Users\\Administrator\\Desktop\\shenyesql\\community.sql")));
			logwriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("C:\\Users\\Administrator\\Desktop\\shenyesql\\log.sql")));
			otherwriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("C:\\Users\\Administrator\\Desktop\\shenyesql\\other.sql")));
			File file = new File(path);
			readData(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				communitywriter.close();
				logwriter.close();
				otherwriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void readData(File file) throws Exception{
		
		if(!file.exists()) {
//			logwriter.write("文件不存在：\t"+file.getAbsolutePath()+ "\n");
			return;
		}
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File f: files) {
				readData(f);
			}
		}
		createData(file);
	}
	
	public void createData(File file) {
		Workbook wb = null;
		try{
		
		if(null == file)
			return;
		
		if(!file.exists()) {
//			logwriter.write("文件不存在："+file.getAbsolutePath()+ "\n");
			return;
		}
		
		if(file.isDirectory()) {
//			logwriter.write("文件是目录\t"+file.getAbsolutePath()+ "\n");
			return;
		}
		if(file.getName().indexOf(".xlsx") == -1 && file.getName().indexOf(".xls") == -1) {
//			logwriter.write("文件是不是excel\t"+file.getAbsolutePath()+ "\n");
			return;
		}
		if(file.getName().indexOf("~$") != -1) {
//			logwriter.write("打开excel生成的临时文件\t"+file.getAbsolutePath()+ "\n");
			return;
		}
		
		if(file.getName().indexOf(".xlsx") != -1) {
			wb = new XSSFWorkbook(file);
		}else{
			wb = new HSSFWorkbook(new FileInputStream(file));
	
		}
		
		
		Sheet sheet = wb.getSheetAt(0);
		
		if(file.getName().indexOf("小区信息.xlsx") != -1 || file.getName().indexOf("小区信息.xls") != -1) {

			int rowLength = sheet.getLastRowNum();
			
			for(int i=0; i <= rowLength; i++) {
				Row row = sheet.getRow(i);
				if(null == row) {
					continue;
				}
				int cellLength = row.getLastCellNum();
					Cell cell0 = row.getCell(0);
					Cell cell1 = row.getCell(1);
					Cell cell2 = row.getCell(2);
					Cell cell3 = row.getCell(3);
					Cell cell4 = row.getCell(4);
					Cell cell5 = row.getCell(5);
					Cell cell6 = row.getCell(6);
					
					if(null == cell0 ) {
						continue;
					}
					if(null == cell1 ) {
						otherwriter.write("----------------创建数据失败,城市名称为空\t"+file.getAbsolutePath()+ "\n");
						continue;
					}
					if(null == cell2 ) {
						otherwriter.write("----------------创建数据失败,区县名称为空\t"+file.getAbsolutePath()+ "\n");
						continue;
					}
					if(null == cell3 ) {
						otherwriter.write("----------------创建数据失败,地址为空\t"+file.getAbsolutePath()+ "\n");
						continue;
					}
					if(null == cell4 || null == cell5 ) {
						otherwriter.write("----------------创建数据失败,经纬度数据错误\t"+file.getAbsolutePath()+ "\n");
						continue;
					}
					
					String cellValue1 = cell0.getStringCellValue();
					if(null == cellValue1|| cellValue1.trim().indexOf("小区名称") != -1 || "".equals(cellValue1.trim())
							|| "XXX".equals(cell0.getStringCellValue().trim())) {
						continue;
					}
					
					communityId ++;
					regionId ++;
					eh_community_geopointsId++;

					Region region = findRegionByPath(999992, cell1.getStringCellValue());
					Long cityId  = null;
					String cityName = null;
					Long areaId  = null;
					String areaName = null;
					if(null != region){
						cityId = region.getId();
						cityName = region.getName();
						
						Region area = findRegionByPath(999992, cell2.getStringCellValue());
						if(null == area) {
							//创建区域
							Region area1 = findRegionByPath(0, cell2.getStringCellValue());
							
							if(null == area1) {
								otherwriter.write("----------------创建数据失败,找不到"+cell2.getStringCellValue()+"地区\t"+file.getAbsolutePath()+ "\n");
								continue;
							}
							
							area1.setParentId(region.getId());
							Map<String, String> temp = createRegionSql(area1);
							String areaSql = temp.get("sql");
							
							//插入数据库
							DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
							context.execute(areaSql);
							
							areaId = Long.valueOf(temp.get("regionId"));
							areaName = temp.get("name");
						}else {
							areaId = area.getId();
							areaName = area.getName();
						}
						
					}else {
						region = findRegionByPath(0, cell1.getStringCellValue());
						
						if(null == region) {
							otherwriter.write("----------------创建数据失败,城市不存在\t"+file.getAbsolutePath()+ "\n");
							continue;
						}

						Region parent = findRegionById(region.getParentId());
						Region parent2 = findRegionByPath(999992, parent.getName());
						//创建省
						if(null == parent2) {
							Map<String, String> temp = createRegionSql(parent);
							String provinceSql = temp.get("sql");
							
							String provinceId = temp.get("regionId");
							region.setParentId(Long.valueOf(provinceId));
							//插入数据库
							DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
							context.execute(provinceSql);
						}else{
							region.setParentId(parent2.getId());
						}
						
						//创建市
						Map<String, String> temp = createRegionSql(region);
						String citySql = temp.get("sql");
						//插入数据库
						DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
						context.execute(citySql);
						
						cityId = Long.valueOf(temp.get("regionId"));
						cityName = temp.get("name");
						
						Region area = findRegionByPath(999992, cell2.getStringCellValue());
						if(null == area) {
							//创建区域
							Region area1 = findRegionByPath(0, cell2.getStringCellValue());
							
							area1.setParentId(cityId);
							Map<String, String> areaTemp = createRegionSql(area1);
							String areaSql = areaTemp.get("sql");
							
							//插入数据库
							context.execute(areaSql);
							
							areaId = Long.valueOf(areaTemp.get("regionId"));
							areaName = areaTemp.get("name");
						}else {
							areaId = area.getId();
							areaName = area.getName();
						}
					}
   
					String description = null==cell6?"":cell6.getStringCellValue();
					String communitySql = "INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)\n"
	+ "\tVALUES(2401110443310"+ communityId +", UUID(), "+cityId+", '"+cityName+"',  "+areaId+", '"+areaName+"', '"+ cell0.getStringCellValue() +"', '"+ cell0.getStringCellValue() +"', '"+ cell3.getStringCellValue() +"', NULL, '"+ description +"',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 31, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 179900, 179901, UTC_TIMESTAMP(), 999992);";
					String geohash = null;
					try{
						geohash = GeoHashUtils.encode(cell5.getNumericCellValue(), cell4.getNumericCellValue());
					}catch(Exception e) {
						otherwriter.write("----------------创建数据失败,经纬度数据错误\t"+file.getAbsolutePath()+ "\n");
						continue;
					}
					String eh_community_geopoints = "INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`)\n"
	+ "\tVALUES(2401110443310"+eh_community_geopointsId+", 2401110443310"+communityId+", '', "+cell4.getNumericCellValue()+", "+cell5.getNumericCellValue()+", '"+geohash+"');";
					
					String eh_organization_communities = "INSERT INTO `eh_organization_communities`(organization_id, community_id)"
	+ "VALUES(1000750, 2401110443310"+communityId+");";
					communitywriter.write(communitySql+ "\n");
					communitywriter.write(eh_community_geopoints+ "\n");
					communitywriter.write(eh_organization_communities+ "\n");
					communitywriter.write("\r\n");
			}
			
			logwriter.write("已经创建的小区：\t"+file.getAbsolutePath()+ "\n");

		}else {
			otherwriter.write("其他读取文件：\t"+file.getAbsolutePath()+"\n");
		}
		}catch(Exception e){
			try {
				otherwriter.write("----------------创建数据失败\t"+file.getAbsolutePath()+ "\n");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			}
		}finally{
			try {
				if(null != wb)
				wb.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public Region findRegionByPath(Integer namespaceId, String name) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		//fetchOne()没查到记录会返回null
		try {
			return context.select().from(Tables.EH_REGIONS).where(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId))
					.and(Tables.EH_REGIONS.NAME.eq(name)).fetchOne().map(t->ConvertHelper.convert(t, Region.class));
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public Region findRegionById(long regionId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRegionsDao dao = new EhRegionsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(regionId), Region.class);
	}
	
	private Map<String, String> createRegionSql(Region r) {
		String isoCode = null==r.getIsoCode()?"":r.getIsoCode();
		String telCode = null==r.getTelCode()?"":r.getTelCode();
		String sql = "INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)\n"
	+ "\tVALUES ("+regionId+", '"+r.getParentId()+"', '"+r.getName()+"', '"+r.getPinyinName()+"', '"+r.getPinyinPrefix()+"', '"+r.getPath()+"', '"+r.getLevel()+"', '"+r.getScopeCode()+"', '"+isoCode+"', '"+telCode+"', '"+r.getStatus()+"', '"+r.getHotFlag()+"', 999992);";
		
		Map<String, String> result = new HashMap<>();
		result.put("regionId", String.valueOf(regionId));
		result.put("sql", sql);
		result.put("name", r.getName());
		return result;
	}
	
//	public static final String url = "jdbc:mysql://127.0.0.1/ehcore";  
//    public static final String name = "com.mysql.jdbc.Driver";  
//    public static final String user = "ehcore";  
//    public static final String password = "ehcore";  
//  
//    public static Connection conn = null;  
//  
//    public void close() {  
//        try {  
//            this.conn.close(); 
//        } catch (SQLException e) {  
//            e.printStackTrace();  
//        }  
//    }
//	
//	
//	static {
//		try {  
//			Class.forName(name);//指定连接类型  
//			conn = DriverManager.getConnection(url, user, password);//获取连接  
//	    } catch (Exception e) {  
//	    	e.printStackTrace();  
//	    } 	
//	}
	
	
	
	public static void replaceFile2(){
		int id = 170;
		BufferedWriter writer = null;
		BufferedReader br = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("C:\\Users\\Administrator\\Desktop\\weixin.data.sql")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream("C:\\Users\\Administrator\\Desktop\\data1.txt")));
			String s;
			while((s = br.readLine()) != null) {
				byte[] bom = s.getBytes();
				if((bom[0] == (byte)0xEF) && (bom[1] == (byte)0xBB) &&
	            (bom[2] == (byte)0xBF) ) {
					s = new String(bom, 3, bom.length- 3);
				}
				
	            
					String[] arr = s.split(",");
					
						
					id++;
//					String hh = "INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)" 
//	+ "VALUES ("+id+", 1002756, 240111044331053516, "+arr[0]+", "+arr[7]+", '0');";
					String ss = "INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)\n" 
    + "\tVALUES(239825274387135"+id+",UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'"+arr[0]+"-"+arr[1]+"','"+arr[0]+"','"+arr[1]+"','2','0',UTC_TIMESTAMP(), 999982);";
					writer.write(ss+"\n");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				br.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

}
