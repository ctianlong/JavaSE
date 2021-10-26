package util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author ctl
 * @date 2021/10/19
 */
public class EasyExcelUtil {

	/**
	 * 独取excel第一个sheet数据，model需要使用 @ExcelProperty 来标注表头信息，首行默认为表头，不返回
	 */
	public static <T> List<T> extractModelList(InputStream in, Class<T> model) {
		List<T> res = new ArrayList<>();
		EasyExcel.read(in).head(model).sheet().registerReadListener(new AnalysisEventListener<T>() {
			@Override
			public void invoke(T data, AnalysisContext context) {
				res.add(data);
			}
			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {}
		}).doRead();
		return res;
	}

	/**
	 * 独取excel第一个sheet数据，每行数据用map接收，首行默认为表头，不返回
	 */
	public static List<Map<Integer, String>> extractMapList(InputStream in) {
		List<Map<Integer, String>> res = new ArrayList<>();
		EasyExcel.read(in).sheet().registerReadListener(new AnalysisEventListener<Map<Integer, String>>() {
			@Override
			public void invoke(Map<Integer, String> data, AnalysisContext context) {
				res.add(data);
			}
			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {}
		}).doRead();
		return res;
	}

	/**
	 * web导出excel，model需要使用 @ExcelProperty 来标注表头信息
	 */
	public static <T> void webDownload(HttpServletResponse response, String fileName, Class<T> model, List<T> data) throws IOException {
		fileName = new String((fileName + ".xlsx").getBytes(), StandardCharsets.ISO_8859_1);
		response.setHeader("Cache-Control", "private");
		response.addHeader("Content-disposition", "attachment;filename="+fileName);
		response.setContentType("application/x-download");
		EasyExcel.write(response.getOutputStream())
				.excelType(ExcelTypeEnum.XLSX)
				.head(model)
				.sheet("sheet1")
				.doWrite(data);
	}


	@Test
	public void testReadByMap() {
		String path = "C:\\Users\\ctl\\Desktop\\test1.xlsx";
		EasyExcel.read(path).sheet().registerReadListener(new AnalysisEventListener<Map<Integer, String>>() {
			@Override
			public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
				System.out.println("---header: " + headMap);
			}
			@Override
			public void invoke(Map<Integer, String> data, AnalysisContext context) {
				System.out.println("---data: " + data);
			}
			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
				System.out.println("---end");
			}
		}).doRead();
	}

	@Test
	public void testReadByDTO() {
		String path = "C:\\Users\\ctl\\Desktop\\test1.xlsx";
		EasyExcel.read(path).head(TitleDTO.class).sheet().registerReadListener(new AnalysisEventListener<TitleDTO>() {
			@Override
			public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
				System.out.println("---header: " + headMap);
			}
			@Override
			public void invoke(TitleDTO data, AnalysisContext context) {
				System.out.println("---data: " + data);
			}
			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
				System.out.println("---end");
			}
		}).doRead();
	}

	@Test
	public void testWrite() {
		EasyExcel.write("C:\\Users\\ctl\\Desktop\\out1.xlsx")
				.head(TitleDTO.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet()
				.doWrite(new ArrayList<TitleDTO>());
	}

	public static class TitleDTO {

		@ExcelProperty(value = "名称")
		private String name;//标题名称

		@ExcelProperty(value = "标题")
		private String titleContent;//标题内容

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitleContent() {
			return titleContent;
		}

		public void setTitleContent(String titleContent) {
			this.titleContent = titleContent;
		}

		@Override
		public String toString() {
			return "TitleDTO{" +
					"name='" + name + '\'' +
					", titleContent='" + titleContent + '\'' +
					'}';
		}
	}
}
