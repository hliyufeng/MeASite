package com.mea.site.common.utils;

import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RexUtil {


	/**
	 * ******************************************
	 * getChinese<br>
	 * 去掉不需要的字符保留中文<br>
	 * @since v1.0.0
	 * @return
	 * String
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2015年3月30日::x08::创建此方法<br>
	 ********************************************
	 */
	public static String getChinese(String word) {
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(word);
		String c = "";
		while (mat.find()) {
			c += mat.group();// 取到中文
		}
		return c;
	}

	public static String getFormat(String s) {
		if (StringUtils.isEmpty(s)) {
			return null;
		}
		String regEx = "-?\\d+(\\.\\d+)?";// 浮点数
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(s.replaceAll(",", ""));
		String shuzi = "";
		if (mat.find()) {
			shuzi = mat.group();// 取到数字
		}
		String danwei = mat.replaceAll("");// 货币单位
		DecimalFormat df = new DecimalFormat("0.00");
		String a;
		if (!shuzi.contains(".0000")) {
			a = shuzi + danwei;
		} else {
			a = df.format(new BigDecimal(shuzi)) + danwei;
		}
		return a;
	}

	/**
	 * ******************************************
	 * getChinese<br>
	 * 去掉不需要的字符保留数字<br>
	 * @since v1.0.0
	 * @return
	 * String
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2015年3月30日::x08::创建此方法<br>
	 ********************************************
	 */
	public static String getNumber(String word) {
		String regEx = "\\d+(\\.\\d+)?";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(word);
		String c = "";
		while (mat.find()) {
			c += mat.group();// 取到数字
		}
		return c;
	}

	/**
	 * ******************************************
	 * getBZ<br>
	 * 同时有数字与单位的用此方法<br>
	 * @since v1.0.0
	 * @param bz
	 * @return
	 * Map<String,String>
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2015年3月17日::x08::创建此方法<br>
	 ********************************************
	 */
	public static Map<String, String> getBZ(String bz) {

		Map<String, String> map = new HashMap();
		if (StringUtils.isEmpty(bz)) {
			map.put("shuzi", "");
			map.put("danwei", "");
			return map;
		}
		String regEx = "\\d+(\\.\\d+)?";// 正浮点数
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(bz.replaceAll(",", ""));
		String shuzi = "";
		if (mat.find()) {
			shuzi = mat.group();// 取到数字
		}

		String danwei = mat.replaceAll("").trim().replace("　", "");// 货币单位
		if (danwei.indexOf("万") > -1) {
			danwei = danwei.replace("万", "").trim();
			if (danwei.equals("元") || danwei.equals("")) {
				danwei = "元人民币";
			}
			if (!"".equals(shuzi)) {
				shuzi = String.valueOf(new BigDecimal(shuzi).multiply(new BigDecimal(10000)));
			}

		}
		DecimalFormat df = new DecimalFormat("0.00");
		if ("0".equals(shuzi)) {
			map.put("shuzi", "0");
			map.put("danwei", danwei);
		} else if ("".equals(shuzi)) {
			map.put("shuzi", "");
			map.put("danwei", danwei);
		} else {
			map.put("shuzi", df.format(new BigDecimal(shuzi)));
			map.put("danwei", danwei);
		}

		return map;
	}

	/**
	 * ******************************************
	 * getBZ<br>
	 * 只有数字没有单位的用此方法<br>
	 * @since v1.0.0
	 * @param shuzi
	 * @param danwei
	 * @return
	 * String
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2015年3月17日::x08::创建此方法<br>
	 ********************************************
	 */
	public static Map<String, String> getBZ(String shuzi, String danwei) {
		Map<String, String> map = new HashMap();
		if (danwei.indexOf("万") > -1) {
			shuzi = String.valueOf(Float.parseFloat(shuzi) * 10000);
			if (danwei.indexOf("万元") > -1) {
				danwei = "元人民币";
			} else {
				danwei = danwei.replace("万", "");
			}
		}
		map.put("shuzi", shuzi);
		map.put("danwei", danwei);

		return map;
	}

	/**
	 * ******************************************
	 * changBZ<br>
	 * 只有单位的用此方法<br>
	 * @since v1.0.0
	 * @param bz
	 * @return
	 * String
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2015年3月17日::x08::创建此方法<br>
	 ********************************************
	 */
	public static String changBZ(String bz) {
		if (bz.indexOf("万元") > -1) {
			bz = "元人民币";
		}
		if (bz.indexOf("万") > -1) {
			bz = bz.replaceAll("万", "");
		}
		return bz;
	}

	/********************************************
	 * timeFormat<br>
	 * 时间格式转换，年月日转为xxxx-xx-xx<br>
	 * @since v1.0.0
	 * @param time
	 * @return
	 * String
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2015年7月16日::x08::创建此方法<br>
	 *********************************************/
	public static String timeFormat(String time) {
		Date formatDate = DateUtils.formatDate(time);
		if (formatDate != null) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			time = sf.format(formatDate);
		}
		return time;
	}

	/********************************************
	 * full2HalfChange<br>
	 * 全角转半角<br>
	 * @since v1.0.0
	 * @param QJstr
	 * @return
	 * String
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2015年7月16日::x09::创建此方法<br>
	 *********************************************/
	public static String full2HalfChange(String QJstr) {
		if (org.apache.commons.lang.StringUtils.isEmpty(QJstr)) {
			return QJstr;
		}
		StringBuffer outStrBuf = new StringBuffer("");
		String Tstr = "";
		byte[] b = null;
		for (int i = 0; i < QJstr.length(); i++) {
			Tstr = QJstr.substring(i, i + 1);
			// 全角空格转换成半角空格
			if (Tstr.equals("　")) {
				outStrBuf.append(" ");
				continue;
			}
			try {
				b = Tstr.getBytes("unicode");
				// 得到 unicode 字节数据
				if (b[2] == -1) {
					// 表示全角
					b[3] = (byte) (b[3] + 32);
					b[2] = 0;
					outStrBuf.append(new String(b, "unicode"));
				} else {
					outStrBuf.append(Tstr);
				}
			} catch (UnsupportedEncodingException e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
			}

		} // end for.
		return outStrBuf.toString();

	}

	/********************************************
	 * full2HalfChange<br>
	 * 半角转全角<br>
	 * @since v1.0.0
	 * @param QJstr
	 * @return
	 * String
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2015年7月16日::x09::创建此方法<br>
	 *********************************************/
	public static String half2Fullchange(String QJstr) {
		if (org.apache.commons.lang.StringUtils.isEmpty(QJstr)) {
			return QJstr;
		}
		StringBuffer outStrBuf = new StringBuffer("");
		String Tstr = "";
		byte[] b = null;
		for (int i = 0; i < QJstr.length(); i++) {
			Tstr = QJstr.substring(i, i + 1);
			if (Tstr.equals(" ")) {
				// 半角空格
				outStrBuf.append("　");
				continue;
			}
			try {
				b = Tstr.getBytes("unicode");
				if (b[2] == 0) {

					// 半角?
					b[3] = (byte) (b[3] - 32);
					b[2] = -1;
					outStrBuf.append(new String(b, "unicode"));
				} else {
					outStrBuf.append(Tstr);
				}
			} catch (Exception e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
			}
		}
		return outStrBuf.toString();

	}

	/********************************************
	 * 方法简述加英文符号.<br>
	 * 是否包含全角<br>
	 * @since v1.0.0
	 * @param
	 * @return 返回类型 返回类型描述
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2016年3月8日::x09::创建此方法<br>
	 *********************************************/
	public static boolean containsFull(String keyword) {
		if (StringUtils.isEmpty(keyword)) {
			return false;
		}
		String Tstr = "";
		byte[] b = null;
		for (int i = 0; i < keyword.length(); i++) {
			Tstr = keyword.substring(i, i + 1);
			// 全角空格转换成半角空格
			if (Tstr.equals("　")) {
				return true;
			}
			try {
				b = Tstr.getBytes("unicode");
				// 得到 unicode 字节数据
				if (b[2] == -1) {// 表示全角
					return true;
				}
			} catch (UnsupportedEncodingException e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
			}
		}
		return false;
	}

	/********************************************
	 * 方法简述加英文符号.<br>
	 * 替换全角文字<br>
	 * @since v1.0.0
	 * @param
	 * @return 返回类型 返回类型描述
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2016年3月8日::x09::创建此方法<br>
	 *********************************************/
	public static String replaceFull(String QJstr, String to) {
		if (org.apache.commons.lang.StringUtils.isEmpty(QJstr)) {
			return QJstr;
		}
		StringBuffer outStrBuf = new StringBuffer("");
		String Tstr = "";
		byte[] b = null;
		for (int i = 0; i < QJstr.length(); i++) {
			Tstr = QJstr.substring(i, i + 1);
			// 全角空格转换成半角空格
			if (Tstr.equals("　")) {
				outStrBuf.append(to);
				continue;
			}
			try {
				b = Tstr.getBytes("unicode");
				// 得到 unicode 字节数据
				if (b[2] == -1) {
					// 表示全角
					outStrBuf.append(to);
				} else {
					outStrBuf.append(Tstr);
				}
			} catch (UnsupportedEncodingException e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
			}
		} // end for.
		return outStrBuf.toString();
	}

	/********************************************
	 * 方法简述加英文符号.<br>
	 * 格式化注册资本<br>
	 * @since v1.0.0
	 * @param
	 * @return 返回类型 返回类型描述
	 * 描述 过亿的保留到千万，写为X亿X千万；千万位是0，就写为为“X亿”。千万的到十万的保留到万位，写为“XXXX万，XXX万，XX万”，如果有千万位，百万位为0，就写为“X千万”。万元的保留到千，写为“X万X千”；千位为0，写为“X万”。其他写为元。
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2016年6月1日::cl::创建此方法<br>
	 *********************************************/
	public static String rexZczb(String zczb) {
		if (null == zczb) {
			return null;
		}
		String result = "";
		int i = zczb.indexOf(".");
		if (i > 0) {
			zczb = zczb.substring(0, i);
		}
		if (zczb.length() >= 9) {
			// 千万位
			if ("0".equals(zczb.charAt(zczb.length() - 8) + "")) {
				result = zczb.substring(0, zczb.length() - 8) + "亿";
			} else {
				result = zczb.substring(0, zczb.length() - 8) + "亿" + zczb.charAt(zczb.length() - 8) + "千万";
			}
		} else if (zczb.length() >= 6) {
			if (zczb.length() == 8 && "0".equals(zczb.charAt(zczb.length() - 7) + "")) {
				result = zczb.charAt(zczb.length() - 8) + "千万";
			} else {
				result = zczb.substring(0, zczb.length() - 4) + "万";
			}
		} else if (zczb.length() == 5) {
			if ("0".equals(zczb.charAt(zczb.length() - 4) + "")) {
				result = zczb.charAt(zczb.length() - 5) + "万";
			} else {
				result = zczb.charAt(zczb.length() - 5) + "万" + zczb.charAt(zczb.length() - 4) + "千";
			}
		} else {
			result = zczb + "元";
		}
		return result;
	}

	/********************************************
	 * 方法简述加英文符号.<br>
	 * 格式化注册资本<br>
	 * @since v1.0.0
	 * @param
	 * @return 返回类型 返回类型描述
	 * 描述 计算企业成立的年限
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2016年6月1日::wanggaoshuai::创建此方法<br>
	 *********************************************/
	public static String getclnf(String clsj) {
		try {
			clsj = clsj.replace("年", "-").replace("月", "-").replace("日", "");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			long l = System.currentTimeMillis() - df.parse(clsj).getTime();
			int day = (int) (l / (24 * 60 * 60 * 1000));
			if (day == 0) {
				return "不足1天";
			} else if (day < 30) {
				return day + " 天";
			} else {
				int month = day / 30;
				if (month < 12) {
					return month + " 个月";
				} else {
					int year = month / 12;
					month = month - year * 12;
					if (month > 5) {
						return year + " 年半";
					} else {
						return year + " 年";
					}
				}
			}
		} catch (Exception e) {
			return null;
		}

	}

	/********************************************
	 * 方法简述加英文符号.<br>
	 * 用于对比公示平台搜索的名称匹配的名称格式化,将非中英文字符替换掉,并将半角英文替换为全角英文<br>
	 * @since v1.0.0
	 * @return 返回类型 返回类型描述
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2016年7月15日::x09::创建此方法<br>
	 *********************************************/
	public static String getHalfMc(String qymc) {
		if (StringUtils.isEmpty(qymc)) {
			return qymc;
		}
		String reg = "[^\u4E00-\u9FA5\uF900-\uFA2Da-zA-ZＡ-Ｚａ-ｚ]";
		// 将非中文非英文的字符替换掉
		qymc = qymc.trim().replaceAll(reg, "_");
		Pattern p_full = Pattern.compile("[Ａ-Ｚａ-ｚ]");
		Matcher m_full = p_full.matcher(qymc);
		// 如果包含全角英文,将其替换为半角英文
		if (m_full.find()) {
			StringBuffer sf = new StringBuffer();
			for (int i = 0; i < qymc.length(); i++) {
				String sub = qymc.substring(i, i + 1);
				m_full = p_full.matcher(sub);
				if (m_full.find()) {
					sub = RexUtil.full2HalfChange(sub);
				}
				sf.append(sub);
			}
			qymc = sf.toString();
		}
		return qymc;
	}

	/********************************************
	 * 方法简述加英文符号.<br>
	 * 将以  (.+)结尾且括号中不含合伙的公司名去掉那段内容<br>
	 * 青岛农村商业银行股份有限公司（简称：青岛农商银行）
	 * @since v1.0.0
	 * @return 返回类型 返回类型描述
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2016年11月28日::x09::创建此方法<br>
	 *********************************************/
	public static String getSearchKey(String mc) {
		if (StringUtils.isEmpty(mc)) {
			return mc;
		}
		Integer start = null;// 最后左括号
		Integer end = null;// 最后右括号
		if (mc.endsWith(")")) {// 英文括号结尾
			end = mc.lastIndexOf(")");
		} else if (mc.endsWith("）")) {// 中文括号结尾
			end = mc.lastIndexOf("）");
		}
		if (end != null) {
			Integer zw = mc.lastIndexOf("（");
			Integer yw = mc.lastIndexOf("(");
			start = zw > yw ? zw : yw;
			if (start < 0) {
				start = null;
			}
			if (start != null && end != null && start < end) {
				String sub = mc.substring(start + 1, mc.length() - 1);
				Pattern pattern = Pattern.compile("[\\(\\)（）]");
				Matcher matcher = pattern.matcher(sub);
				if (!matcher.find() && !sub.contains("合伙")) {
					mc = mc.substring(0, start);
				}
			}
		}
		return mc;
	}

	public static BigDecimal toBigDecimal(String text) {
		if (StringUtils.isEmpty(text)) {
			return null;
		}
		try {
			BigDecimal bigDecimal = new BigDecimal(text.replaceAll("[^0-9-.]", ""));
			return bigDecimal;
		} catch (Exception e) {
		}
		return null;
	}
}
