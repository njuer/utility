package org.minnie.utility.util;

public class Constant {
	
//	public static final String URL_XINYINGBA_MOVIE_2014_FILE = "C:\\dianying2014.html";
	public static final String URL_XINYINGBA = "http://www.xinyingba.com";
	public static final String URL_TIANTIANYULE = "http://www.ttyl5.com";
	public static final String URL_XINYINGBA_MOVIE_2014 = "http://www.xinyingba.com/dianying/2014-nian.htm";
	public static final String URL_SOHU_LOTTERY_DOUBLE_COLOR = "http://trend.sohu.lecai.com/ssq/redBaseTrend.action";
	
	public static final String CATEGORY_XINYINGBA_MOVIE = "dianying";
	public static final String CATEGORY_XINYINGBA_TV = "dianshi";
	public static final String CATEGORY_XINYINGBA_VARIETY_SHOW = "zongyi";
	public static final String CATEGORY_XINYINGBA_ANIMATION = "dongman";
	public static final String CATEGORY_XINYINGBA_UPDATE_TODAY = "jinri";
	public static final String CATEGORY_XINYINGBA_TRAILER = "yugao";
//	public static final String URL_XINYINGBA_UPDATE_TODAY = "http://www.xinyingba.com/jinri.htm";
	/**
	 * 电影分区
	 */
	public static final String MOVIE_REGION_EUROPE_AMERICA = "oumei";
	public static final String MOVIE_REGION_CHINESE_MAINLAND = "dalu";
	public static final String MOVIE_REGION_HONGKONG = "xianggang";
	public static final String MOVIE_REGION_CHINESE_TAIPEI = "taiwan";
	public static final String MOVIE_REGION_SOUTH_KOREA = "hanguo";
	public static final String MOVIE_REGION_JAPAN = "riben";
	/**
	 * 电视剧分区
	 */
	public static final String TV_REGION_CHINESE_MAINLAND = "guochan";
	public static final String TV_REGION_HONGKONG_TAIPEI = "gangtai";
	public static final String TV_REGION_JAPAN_TAIPEI = "rihan";
	public static final String TV_REGION_HONGKONG_KOREA = "oumeiju";
	public static final String TV_REGION_SINGAPORE_MALAYSIA_THAILAND = "xinmatai";
	public static final String TV_REGION_OTHER = "qitapian";

	
	public static final String URL_STRING_JAVASCRIPT = "javascript:;";
	public static final String ENCODING_UTF8 = "UTF-8";
	public static final String REG_DL = "[a-zA-z]+-\\d+";
	public static final String REG_NUMBER = "\\d+";
	public static final String REG_URL = "(http|ftp|https)://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
	public static final String REG_DOMAIN = "(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)";
	public static final String REG_HOOPCHINA_COUNT_STRING = "/&nbsp;\\d+\\)&nbsp;&nbsp;";
	public static final String REG_ILLEGAL_CHARACTERS = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）\"\"《》\\\\——+|{}【】‘；：”“’。，、？]";
	public static final String REG_DOUBLE_COLOR_RED_BALL = "\\d{2}\\,\\d{2}\\,\\d{2}\\,\\d{2}\\,\\d{2}\\,\\d{2}";
	public static final String REG_GD_FIVE_IN_ELEVEN = "\\d{8}";
	public static final String REG_BALL = "\\d{2}";
	public static final String REG_NETEASE_BBS_DATETIME = "\\d{4}\\-\\d{1,2}-\\d{1,2}\\s([0-1]{1}\\d{1}|[2]{1}[0-3]{1})(?::)?([0-5]{1}\\d{1})(?::)?([0-5]{1}\\d{1}){0,1}";
	//保留两位小数
	public static final String REG_SP = "\\d+\\.\\d{2}";
	
	public static final String REG_MAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	
	public static final String REG_SOCCER_SCORE = "\\d{1,2}:\\d{1,2}";

	
	public static final String DOMAIN_163 = "163.com";
	public static final String DOMAIN_HUPU = "hupu.com";
	public static final String DOMAIN_HOOPCHINA = "hoopchina.com";
	public static final String DOMAIN_39YANGSHENGSUO = "39yss.com";
	public static final String DOMAIN_TIANTIANYULE = "www.ttyl5.com";

	
	
	public static final String TAG_DD_ATTRIBUTE_CLASS_VIDEO_COVER = "video-cover";
	public static final String TAG_DD_ATTRIBUTE_CLASS_VIDEO_INTRO = "video-intro";
	public static final String TAG_DD_ATTRIBUTE_CLASS_VIDEO_GRADE = "video-grade";
	public static final String TAG_DD_ATTRIBUTE_CLASS_VIDEO_INFORMATION = "video-information";
	public static final String TAG_LINK_ATTRIBUTE_CLASS_GRADE_SCORE = "grade-score";
	public static final String TAG_EM_VALUE_CATEGORY = "类型:";
	public static final String TAG_EM_VALUE_STARRING = "主演:";
	public static final String TAG_EM_VALUE_YEAR = "年代:";
	
	//数据连接池--c3p0配置文件--Mysql
	public static final String DB_POOL_CONFIGURE_C3P0_MYSQL = "\\resource\\dbpool\\c3p0mysql.properties";
	
	//日志配置文件
	public static final String LOG_LOG4J_PARAM_FILE = "\\resource\\log\\log4j.properties";
	
	//系统参数
	public static final String SYS_PARAM_FILE = "\\resource\\sys\\param.properties";
	
	//log4j输出文件路径
	public static final String LOGGING_FILE_PATH = "log4j.appender.R.File";
	
	//启动mysql批处理文件路径
	public static final String MYSQL_START_BAT_PATH = "\\resource\\cmd\\MysqlStart.bat";
	//关闭mysql批处理文件路径
	public static final String MYSQL_STOP_BAT_PATH = "\\resource\\cmd\\MysqlStop.bat";
	
	
	
	public static final String BLANK = "";
	public static final String IMG_NO_PIC = "/img/nopic.gif";
	
	public static final int CONNECTION_TIMEOUT = 15000;
	public static final int READ_TIMEOUT = 15000;

	
	
	public static final String PATTERN_PERCENTAGE = "##.00%";
	public static final String FORMAT_BALL_VALUE = "%02d";
	
	public static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0;.NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30;.NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";
	public static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.76 Safari/537.36";
	public static final String USER_AGENT_FIREFOX = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0";
	public static final String USER_AGENT_IE = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";
	
	public static final int BUFFER_SIZE_1024 = 1024;
	
	public static final String DATE_FORMAT_STANDARD = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_NETEASE_LOTTERY_FIVE_IN_ELEVEN = "yyMMdd";
	public static final String DATE_FORMAT_WITHOUT_HH_MM_SS = "yyyy-MM-dd";
	public static final String TIMESTAMP_FORMAT = "\\d{13}";
	
	/**
	 * 彩票
	 */
	public static final String LOTTERY_FIVE_IN_ELEVEN_GUANGDONG = "gdd11";//广东11选5
	public static final String LOTTERY_FIVE_IN_ELEVEN_OLD = "jxd11";//老11选5
	public static final String LOTTERY_FIVE_IN_ELEVEN_LUCKY = "hljd11";//好运11选5

	/**
	 * 新时时彩"杀号"类别
	 */
	public static final String JXSSC_GEWEI = "gewei";//个位
	public static final String JXSSC_SHIWEI = "shiwei";//十位
	public static final String JXSSC_BAIWEI = "baiwei";//百位
	public static final String JXSSC_QIANWEI = "qianwei";//千位
	public static final String JXSSC_WANWEI = "wanwei";//万位
	
	public static final String HOST_NETEASE_LOTTERY = "zx.caipiao.163.com";
	
	/**
	 * 好运11选5"杀号"类别
	 */
	public static final String HLJ11XUAN5_FIRST = "first1";
	public static final String HLJ11XUAN5_SECOND = "first2";
	public static final String HLJ11XUAN5_THIRD = "first3";
	public static final String HLJ11XUAN5_FOURTH = "first4";
	public static final String HLJ11XUAN5_FFIFTH = "first5";
	public static final String HLJ11XUAN5_FIRST_TWO = "qian2";
	public static final String HLJ11XUAN5_FIRST_THREE = "qian3";
	public static final String HLJ11XUAN5_ANY_FIVE = "ren5";

	


	

}
