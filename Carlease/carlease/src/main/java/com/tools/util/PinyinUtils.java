package com.tools.util;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 封装公用的字符转换操作类，如中文转拼音
 * 注意：添加多音字判断 如 重庆市首字母C 重量首字母Z
 * @author luman
 *
 */

public class PinyinUtils {

	/**
	 *  字母Z使用了两个标签，这里有２７个值
	 *   i, u, v都不做声母, 跟随前面的字母
	 *
    */
    private static char[] chartable =
            {
                '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈',
                '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然',
                '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝', '座'
            };

    /**
     *
     */
    private static char[] alphatable =
            {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',

                'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            };

    /**
    * 所有地名多音字列表
    且末jūmî县名，在新疆维吾尔自治区。
    百色bïsâ县名，在广西壮族自治区。
    丽江lìjiāng县名（纳西族自治县），在云南省。
    丽水líshuǐ县名，在浙江省。将乐jiànglâ县名，在福建省。
    月氏yuâzhī我国古代西部民族名。
    乐陵lâlíng县名，在山东省。
    乐山lâshān县名，在四川省。
    乐亭lâtíng县名，在河北省乐清yuâqīng县名，
    六安lùān县名，在安徽省
    六合lùhã县名，在江苏省
    冠县guānxiàn县名，在山东省
    单县shànxiàn县名，在山东省
    什邡shìfāng县名，在四川省
    任丘rãnqiū县名，在河北省
    侗族dîngzú我国少数民族之一，分布在贵州、湖南和广西等省区
    闽侯mǐnhïu县名，在福建省
    旬xún周代国名，在今山西省临猗县一带
    哨shào周代诸侯国名，在今陕西省凤翔县东南
    泌阳bìyáng县名，在河南省
    七里沋qīlǐlïng地名，在浙江省
    沋水镇shuāngshuǐzhân地名，在广东省
    泺水luîshuǐ水名，在山东省
    浒湾hǔwān地名，在河南省
    浒墅关xǔshùguān地名，在江苏省
    浒浦xǔpǔ地名，在江苏省
    浒湾xǔwān地名，在江西省
    涡河guōhâ水名，发源于河南省
    浚县xúnxiàn县名，在河南省
    渑池miǎnchì县名，在河南省
    渑水shãngshuǐ古水名，在今山东临淄县一带
    溱头河zhãntïuhã水名，在河南省
    溱潼qíntïng镇名，在江苏省
    滃江wēngjiāng水名，在广东省
    漯河luîhã城市名，在河南省
    漯河tàhã水名，在山东省
    澄江chēngjiāng县名，在云南
    澄海chēnghǎi县名，在广东省
    瀑河bàohã水名，在河北省，
    宁níng宁夏回族自治区的简称，南京市的简称
    应县yìngxiàn县名，在山西省
    尉氏wâishì县名，在河南省
    尉犁yùlí县名，在新疆维吾尔自治区
    掖县yâxiàn县名，在山东省
    掸族shǎnzú缅甸民族之一
    掸邦shànbāng缅甸的一个自治州
    垌塚tïngzhōng地名，
    堡子bǔzi有城墙的村镇
    莎车shāchē县名，在新疆维吾尔自治区
    莘县shēnxiàn县名，在山东省
    莘庄xīnzhuāng地名，在上海市
    蒙古族měnggǔzú我国少数民族之一，分布在内蒙古
    蔚县yùxiàn县名，河北省
    奓山zhāshān地名，在湖北省
    崆峒kōngtïng山名，在甘肃省
    蠡县lǐxiàn县名，在河北省
    珲春húnchūn县名，在吉林省
    瑷珲àihuī县名，在黑龙江
    枞阳zōngyáng县名，在安徽省
    柞水zhàshuǐ县名，在陕西省
    柏乡bǎixiāng县名，河北省
    栎阳yuâyáng地名，在陕西省
    栟茶bēnchá地名，在江苏省
    穆棱mùlíng县名，在黑龙江省
    槟椥bīnzhī越南地名
    荥阳xíngyáng县名，在河南省
    荥经yíngjīng县名，在四川
    牟平mùpíng县名，在山东省
    犍为qiánwãi县名，在四川省
    歙县shēxiàn县名，在安徽省
    畹町wǎndīng镇名，在云南省
    番禺pānyú县名，在广东省
    铅山yánshān县名，在江西省
    秘鲁bìlǔ国名，在南美洲
    筠连jūnlián县名，在四川省
    解县xiâxiàn旧县名，在山西省
    解池xiâchí湖名，在山西省
    剡溪shànxī水名，在浙江省。
    剡界岭shànjièlǐng地名，在浙江省
    会稽kuàijī山名，在浙江省
    句容jùróng县名，在江苏省
    */
	public static List<String[]> polyphonicStrsList = new ArrayList<String[]>();
	static{
		//------------------------------- 多音字 如： ["重庆","chongqin","C"]
		polyphonicStrsList.add(new String[]{"重庆","chongqin","C"});
		polyphonicStrsList.add(new String[]{"沈阳","shenyang","S"});
		polyphonicStrsList.add(new String[]{"浏阳","liuyang","L"});
		polyphonicStrsList.add(new String[]{"沌口","zhuanko","Z"});	//zhuan (第四声): 武汉有个地方叫“沌口”。
		polyphonicStrsList.add(new String[]{"硚口","qiaoko","Q"});

		polyphonicStrsList.add(new String[]{"且末","jumi","J"});
		polyphonicStrsList.add(new String[]{"百色","bisa","B"});
		polyphonicStrsList.add(new String[]{"丽江","lijiang","L"});
		polyphonicStrsList.add(new String[]{"丽水","lishui","L"});
		polyphonicStrsList.add(new String[]{"月氏","yuazhi","Y"});
		polyphonicStrsList.add(new String[]{"乐陵","laling","L"});
		polyphonicStrsList.add(new String[]{"乐亭","lating","L"});
		polyphonicStrsList.add(new String[]{"六安","luan","L"});
		polyphonicStrsList.add(new String[]{"六合","luha","L"});
		polyphonicStrsList.add(new String[]{"冠县","guanxian","G"});
		polyphonicStrsList.add(new String[]{"单县","shanxian","S"});
		polyphonicStrsList.add(new String[]{"什邡","shifang","S"});
		polyphonicStrsList.add(new String[]{"任丘","ranqiu","R"});
		polyphonicStrsList.add(new String[]{"侗族","dingzu","D"});
		polyphonicStrsList.add(new String[]{"闽侯","minhiu","M"});
		polyphonicStrsList.add(new String[]{"泌阳","biyang","B"});
		polyphonicStrsList.add(new String[]{"七里沋","qiling","Q"});
		polyphonicStrsList.add(new String[]{"沋水镇","shuangshuizhan","S"});
		polyphonicStrsList.add(new String[]{"泺水","luishui","L"});
		polyphonicStrsList.add(new String[]{"浒湾","huwan","H"});
		polyphonicStrsList.add(new String[]{"浒浦","xupu","X"});
		polyphonicStrsList.add(new String[]{"涡河","guoha","G"});
		polyphonicStrsList.add(new String[]{"浚县","xunxian","X"});
		polyphonicStrsList.add(new String[]{"渑池","mianchi","M"});
		polyphonicStrsList.add(new String[]{"溱头河","zhantiuha","Z"});
		polyphonicStrsList.add(new String[]{"溱潼","qinting","Q"});
		polyphonicStrsList.add(new String[]{"滃江","wengjiang","W"});
		polyphonicStrsList.add(new String[]{"漯河","taha","T"});
		polyphonicStrsList.add(new String[]{"澄江","chengjiang","C"});
		polyphonicStrsList.add(new String[]{"澄海","chenghai","C"});
		polyphonicStrsList.add(new String[]{"瀑河","baoha","B"});
		polyphonicStrsList.add(new String[]{"尉氏","waishi","W"});
		polyphonicStrsList.add(new String[]{"掖县","yaxian","Y"});
		polyphonicStrsList.add(new String[]{"掸邦","shanbang","S"});
		polyphonicStrsList.add(new String[]{"垌塚","tingzhong","T"});
		polyphonicStrsList.add(new String[]{"堡子","buzi","B"});
		polyphonicStrsList.add(new String[]{"莎车","shache","S"});
		polyphonicStrsList.add(new String[]{"莘县","shenxian","S"});
		polyphonicStrsList.add(new String[]{"莘庄","xinzhuang","X"});
		polyphonicStrsList.add(new String[]{"蔚县","yuxian","Y"});
		polyphonicStrsList.add(new String[]{"奓山","zhashan","Z"});
		polyphonicStrsList.add(new String[]{"崆峒","kongting","K"});
		polyphonicStrsList.add(new String[]{"蠡县","lixian","L"});
		polyphonicStrsList.add(new String[]{"瑷珲","aihui","A"});
		polyphonicStrsList.add(new String[]{"枞阳","zongyang","Z"});
		polyphonicStrsList.add(new String[]{"柞水","zhashui","Z"});
		polyphonicStrsList.add(new String[]{"柏乡","baixiang","B"});
		polyphonicStrsList.add(new String[]{"栎阳","yuayang","Y"});
		polyphonicStrsList.add(new String[]{"栟茶","bencha","B"});
		polyphonicStrsList.add(new String[]{"穆棱","muling","M"});
		polyphonicStrsList.add(new String[]{"荥阳","xingyang","X"});
		polyphonicStrsList.add(new String[]{"荥经","yingjing","Y"});
		polyphonicStrsList.add(new String[]{"牟平","muping","M"});
		polyphonicStrsList.add(new String[]{"犍为","qianwai","Q"});
		polyphonicStrsList.add(new String[]{"歙县","shexian","S"});
		polyphonicStrsList.add(new String[]{"畹町","wanding","W"});
		polyphonicStrsList.add(new String[]{"番禺","panyu","P"});
		polyphonicStrsList.add(new String[]{"铅山","yanshan","Y"});
		polyphonicStrsList.add(new String[]{"秘鲁","bilu","B"});
		polyphonicStrsList.add(new String[]{"筠连","junlian","J"});
		polyphonicStrsList.add(new String[]{"解县","xianxian","X"});
		polyphonicStrsList.add(new String[]{"解池","xiachi","X"});
		polyphonicStrsList.add(new String[]{"剡溪","shanxi","S"});
		polyphonicStrsList.add(new String[]{"剡界岭","shanjieling","S"});
		polyphonicStrsList.add(new String[]{"会稽","kuaiji","K"});
		polyphonicStrsList.add(new String[]{"句容","jurong","J"});
	}

    private static int[] table = new int[27];

    //初始化
    static{
        for (int i = 0; i < 27; ++i) {
            table[i] = gbValue(chartable[i]);
        }
    }

	/**
	 * 输入汉字字符得到声母
	 * 英文字母则返回大写字母
	 * 其他返回'0'
	 * @param ch
	 * @return
	 */
    public static char Char2Alpha(char ch) {

        if (ch >= 'a' && ch <= 'z')
            return (char) (ch - 'a' + 'A');
        if (ch >= 'A' && ch <= 'Z')
            return ch;


        int gb = gbValue(ch);
        if (gb < table[0])
            return '0';

        int i;
        for (i = 0; i < 26; ++i) {
            if (match(i, gb))
                break;
        }

        if (i >= 26)
            return '0';
        else
            return alphatable[i];
    }

    //根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串
    public static String String2Alpha(String SourceStr) {
        String Result = "";
        int StrLength = SourceStr.length();
        int i;
		for(String[] pStrs: polyphonicStrsList){	//遍历查找是否有多音字
			if(SourceStr.startsWith(pStrs[0])){		//查找到开头字符 包含多音字词（如：重庆市 包含 重庆）
				//Log.d("PinyinUtils", "add SourceStr:"+SourceStr+" pStrs[0]:"+pStrs[0]+" pStrs[2]:"+pStrs[2]);
				return pStrs[2];		//多音字母出现的位置 （如： 重庆的字母C）
			}
		}
        try {
//            for (i = 0; i < StrLength; i++) {
                Result += Char2Alpha(SourceStr.charAt(0));
//            }
        } catch (Exception e) {
            Result = "";
        }
        return Result;
    }

    private static boolean match(int i, int gb) {
        if (gb < table[i])
            return false;

        int j = i + 1;

        //字母Z使用了两个标签
        while (j < 26 && (table[j] == table[i]))
            ++j;

        if (j == 26)
            return gb <= table[j];
        else
            return gb < table[j];

    }

    //取出汉字的编码
    private static int gbValue(char ch) {
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GBK");
            if (bytes.length < 2)
                return 0;
            return (bytes[0] << 8 & 0xff00) + (bytes[1] &
                    0xff);
        } catch (Exception e) {
            return 0;
        }

    }
	/**
	 * 获取汉字串拼音，英文字符不变
	 * @param chinese
	 *            汉字串
	 * @return 汉语拼音
	 */
	public static String getFullSpell(String chinese) {
		char[] t1 = null;
		t1 = chinese.toCharArray();
		String[] t2 = new String[t1.length];
		net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat t3 = new net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断是否为汉字字符
				if (java.lang.Character.toString(t1[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4 += t2[0];
				} else {
					t4 += java.lang.Character.toString(t1[i]);
				}
			}
			return t4;
		} catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
	}


	/**
	 * list重新按中文排序
	 * @param strList 中文字符串List
	 * @return 中文按拼音排序的list
	 */
		public static List<String> sort(List<String> strList)
		 {
			List<String> sortList=new ArrayList<String>();
	        String[] str0= new String[strList.size()];
	        strList.toArray(str0);
			//对简体字有效，对繁体字无效
			Comparator comp = Collator.getInstance(java.util.Locale.CHINA);
			Arrays.sort(str0, comp);
			sortList.clear();
			for(String str:str0){
				sortList.add(str);//去除前后空字符
			}
		  return sortList; //返回排序后的列表
		 }



		// [A~Z]拼音首字母 在列表第一次出现的的index
		public static int[] alphaCharIndex = new int[27];
		/**
		 * 获取[A~Z]出现的index
		 * @param alphaChar
		 * @return index
		 */
		public static int getAlphaCharIndex(char alphaChar){
			int index = 0;
			for(char c:alphatable){
				if(alphaChar == c){
					break;
				}
				index++;
			}
			return alphaCharIndex[index];
		}

		// [A~Z]拼音首字母 在列表出现的的数量
		public static int[] alphaCharNumber = new int[27];


		public static List<List<String>> pinyinLists = new ArrayList<List<String>>();	//所有排序好的拼音

		/**
		 * 重置拼音首字母列表
		 */
		public static void resetPinyinLists(){
			pinyinLists = new ArrayList<List<String>>();	//所有排序好的拼音
			for(int j=0; j<alphatable.length; j++){
				List<String> pList = new ArrayList<String>();
				pinyinLists.add(pList);			//用于保存不同字母开头的 拼音列表
			}
		}

	/**
	 * 获取排序好的拼音首字母 首次出现的index
	 */
	public static void getListCharIndex(List<String> list){
		resetPinyinLists();
		char currChar = alphatable[0];	//遍历第一个字母‘A’开始
		int aIndex = 0;
		alphaCharIndex[aIndex] = 0;		//'A'第一次出现的index为0
		for(int i=0; i<list.size(); i++){
			String str = list.get(i);
			char firstChar = String2Alpha(str).charAt(0);		//拼音的第一个字母
			for(int j=0; j<alphatable.length; j++){
				if(alphatable[j] == firstChar){				//第一个字母出现的位置
					alphaCharNumber[j]++;					//同一个字母保存的次数
					List<String> pList = pinyinLists.get(j);		//当前字母已保存的
					pList.add(str);
				}
			}
		}
		for(aIndex=1; aIndex<alphatable.length; aIndex++){
			alphaCharIndex[aIndex] += alphaCharNumber[aIndex-1]+alphaCharIndex[aIndex-1] ;
			//Log.d("", "alphaCharIndex["+aIndex+"]:"+alphaCharIndex[aIndex]);
		}

	}

	/**
	 * list重新按中文排序
	 * @param strList 中文字符串List
	 * @return 中文按拼音排序的list
	 */
	public static List<String> sortByPinyin(List<String> strList) {
		List<String> sortList = new ArrayList<String>();
		String[] str0 = new String[strList.size()];
		strList.toArray(str0);
		// 对简体字有效，对繁体字无效
//		Comparator comp = Collator.getInstance(java.util.Locale.CHINA);
//		Arrays.sort(str0, comp);
//		sortList.clear();
		for (String str : str0) {
			sortList.add(str);// 去除前后空字符
		}
		getListCharIndex(sortList);		//获得排序后的 所有首次出现字母的index

//		for (int i=0; i<sortList.size(); i++) {
//			String str = sortList.get(i);
//			for(String[] pStrs: polyphonicStrsList){	//遍历查找是否有多音字
//				if(str.startsWith(pStrs[0])){		//查找到开头字符 包含多音字词（如：重庆市 包含 重庆）
//					int aIndex = getAlphaCharIndex(pStrs[2].charAt(0));		//多音字母出现的位置 （如： 重庆的字母C）
//					Log.d("PinyinUtils", "add str:"+str+" pStrs[0]:"+pStrs[0]+" add Index:"+aIndex+ " remove:"+i);
//					sortList.remove(i);
//					sortList.add(aIndex, str);		//插入列表中
//				}
//			}
//		}
		sortList.clear();
		for(List<String> pList:pinyinLists){		//遍历所有排序好的拼音列表
			List<String> sList = sort(pList);		//每个分组再进行排序
			for(String s:sList){

				sortList.add(s);	//从[A~Z]按顺序添加
			}
		}


		return sortList; // 返回排序后的列表
	}

		/**
		 * 判断一个字符串是否中文(不含特殊字符)
		 * @param chineseStr 中文字符
		 * @return
		 */
		 public static  boolean isChineseCharacter(String chineseStr) {
		        char[] charArray = chineseStr.toCharArray();
		        for (int i = 0; i < charArray.length; i++) {
		            if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
		                return true;
		            }
		        }
		        return false;
		    }


}
