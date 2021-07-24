package wending;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import util.common.NumberUtils;

import java.io.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ctl
 * @date 2020/10/29
 */
public class WendingArticleTextProcess {

//	private static final Pattern P1 = Pattern.compile("^第.+章\\s*[^\\s]+$");
//	private static final Pattern P2 = Pattern.compile("^第.+章.*$");

	private static final Pattern P3_1 = Pattern.compile("^第[零一二三四五六七八九十百千万].*章.+$");
	private static final Pattern P3_2 = Pattern.compile("^[零一二三四五六七八九十百千万]+章.+$");

	private static String[] exclude;
	private static String[] special;
	private static Map<String, String> specialReplace;
	static {
		exclude = new String[]{
//				"第二天是周末，一大早乔梁就去了章梅爸妈家。",
//				"第二天早上，乔梁吃过早饭刚回到病房，章梅醒了。",
//				"第二天中午，乔梁在家里忙着收拾东西，明天就要公开和章梅离婚的消息了，该彻底离开这个家了。",
//				"第二天一上班，乔梁和章梅就去了民政局，顺利办了离婚手续。",
//				"第二天是周日，上午9点，章梅和方小雅又坐在那个咖啡厅的角落里。",
//				"第二天上午，乔梁一觉睡到8点，起床后看到卧室门开着，章梅不在家里，不知去哪了。",
//				"第二天乔梁醒来的时候，章梅不在家里，餐桌上有做好的早饭，还有一张字条：“看你睡得沉，没打扰你，我去妈家了，记得吃早饭。”",
//				"第二天早上，乔梁起床洗漱完正要出门，章梅从厨房里出来叫住他：“早饭做好了，吃完再走。”",
//				"第二天早上，章梅去上班了，乔梁正在蒙头大睡，手机响了，睁眼一看来电，叶心仪的号码。",
				"第二天名贸市官方开始高调预热PX项目，《名贸日报》在头版刊发《名贸石化绿色高端产品走进千家万户》的文章，主要介绍了名贸石化绿色低碳、精细管理、社会责任等，紧接着《名贸日报》接连发表《PX到底有没有危害》、《揭开PX的神秘面纱》、《PX项目还要不要继续发展》、《PX项目的真相》等一系列文章",
		};
		special = new String[]{
				"第一千零七十二章 署名文章",
				"第一千零七十二章署名文章",
				"第一千八百七十五章 拿赵晓兰做文章",
				"第一千八百七十五章拿赵晓兰做文章"
		};
		specialReplace = Maps.newHashMap();
		specialReplace.put("第四百八十四章（上）出谋划策", "出谋划策");
		specialReplace.put("第四百八十四章（下）公共自行车租赁系统", "公共自行车租赁系统");
	}

	@Test
	public void replace2() throws Exception {
		String path = "C:\\Users\\ctl\\Desktop\\上位-首章序号从一千二百七十六开始.txt";
		String outPath = "C:\\Users\\ctl\\Desktop\\上位-改标题序号.txt";
		File outFile = new File(outPath);
		if (!outFile.exists()) {
			outFile.createNewFile();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
		int count = 0;
		int index = 1276;
//		boolean write = true;
//		String last = null;
		while (true) {
			String line = br.readLine();
			if (line == null) {
				break;
			}
			if (StringUtils.isBlank(line)) {
				writeLine(bw, line);
				continue;
			}
			if (specialReplace.containsKey(line)) {
				count++;
				String modifyTitle = "第" + NumberUtils.toChineseLower(index) + "章" + " " + specialReplace.get(line);
				writeLine(bw, modifyTitle);
				index++;
				continue;
			}
			if (StringUtils.equalsAny(line, exclude)) {
				writeLine(bw, line);
				continue;
			}
//			if (StringUtils.equalsAny(line, special)) {
////				if (write) {
//					count++;
//					String[] split = StringUtils.split(line, " ");
//					if (split.length != 2) {
//						throw new RuntimeException("split error:" + line);
//					}
////					System.out.println(split[0]);
//					String modifyTitle = "第" + NumberUtils.toChineseLower(index) + "章" + " " + split[1].trim();
//					index++;
//					writeLine(bw, modifyTitle);
////				}
////				write = !write;
//				continue;
//			}
			if (match(P3_1, line) || match(P3_2, line)) {
//				if (write) {
					count++;
					if (StringUtils.countMatches(line, "章") != 1) {
						throw new RuntimeException("count error:" + line);
					}
					String[] split = StringUtils.split(line, "章");
					if (split.length != 2) {
						throw new RuntimeException("split error:" + line);
					}
//					System.out.println(split[0]);
					String modifyTitle = "第" + NumberUtils.toChineseLower(index) + "章" + " " + split[1].trim();
					index++;
					writeLine(bw, modifyTitle);
//				}
//				write = !write;
				/*if (last != null) {
					if (last.equals(split[0])) {
						last = null;
					} else {
						throw new RuntimeException("not double:" + line);
					}
				} else {
					last = split[0];
				}*/
				continue;
			}
			writeLine(bw, line);
		}
		bw.close();
		System.out.println("count:" + count);
	}



	/**
	 * —
	 * ^第.+章\s*[^\s]+$
	 * ^第[零一二三四五六七八九十百千万].*章\s*[^\s]+$ || ^[零一二三四五六七八九十百千万]+章\s*[^\s]+$
	 *
	 * ^第[零|一|二|三|四|五|六|七|八|九|十|百|千|万|（|\d|）]+章\s*[^\s]+$
	 *
	 *
	 *
	 */
	@Test
	public void replaceTitleNum() throws Exception {
		String path = "C:\\Users\\ctl\\Desktop\\二狗.txt";
		String outPath = "C:\\Users\\ctl\\Desktop\\out.txt";
		File outFile = new File(outPath);
		if (!outFile.exists()) {
			outFile.createNewFile();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
		while (true) {
			String line = br.readLine();
			if (line == null) {
				break;
			}
			if (StringUtils.isBlank(line)) {
				writeLine(bw, line);
				continue;
			}
			if (match(PATTERN1, line)) {
//				System.out.println(line + " --- " + processPattern1(line));
				writeLine(bw, processPattern1(line));
				continue;
			}
			if (match(PATTERN2, line)) {
//				System.out.println(line + " --- " + processPattern2(line));
				writeLine(bw, processPattern2(line));
				continue;
			}
			if (match(PATTERN3, line)) {
//				System.out.println(line + " --- " + processPattern3(line));
				writeLine(bw, processPattern3(line));
				continue;
			}
			if (match(PATTERN4, line)) {
//				System.out.println(line + " --- " + processPattern4(line));
				writeLine(bw, processPattern4(line));
				continue;
			}
			writeLine(bw, line);
		}
		bw.close();
	}

	private boolean match(Pattern pattern, String line) {
		return pattern.matcher(line).matches();
	}

	private void writeLine(BufferedWriter bw, String line) throws IOException {
		bw.write(line);
		bw.newLine();
	}

	private static final int INCR = 886;

	private static final Pattern PATTERN1 = Pattern.compile("^\\d+$");
	private String processPattern1(String line) {
		return String.valueOf(Integer.parseInt(line) + INCR);
	}
	private static final Pattern PATTERN2 = Pattern.compile("^\\d+-\\d+$");
	private String processPattern2(String line) {
		String[] split = line.split("-");
		return (Integer.parseInt(split[0]) + INCR) + "-" + (Integer.parseInt(split[1]) + INCR);
	}
	private static final Pattern PATTERN3 = Pattern.compile("^\\d+[：:]$");
	private String processPattern3(String line) {
		return (Integer.parseInt(line.replaceAll("[：:]", "")) + INCR) + "：";
	}
	private static final Pattern PATTERN4 = Pattern.compile("^\\d+[：:].+$");
	private String processPattern4(String line) {
		String[] split = line.split("[：:]");
		if (split.length > 2) {
			throw new IllegalArgumentException(line + "---length > 2");
		}
		return (Integer.parseInt(split[0]) + INCR) + "：" + split[1];
	}

	@Test
	public void oneTest() {
		String s = "829:美容";
		System.out.println(PATTERN4.matcher(s).matches());
		System.out.println(processPattern4(s));
	}

	private String commonReplace(String line) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(line);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String numStr = matcher.group();
			int i = Integer.parseInt(numStr) + 5;
			matcher.appendReplacement(sb, String.valueOf(i));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}
