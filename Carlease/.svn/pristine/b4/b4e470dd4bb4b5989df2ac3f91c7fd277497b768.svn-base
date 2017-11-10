package com.tools.video;

import java.util.HashMap;
import java.util.Map;

/**
 *	视频字幕
 * @author Liaojp
 *
 */
public class VideoRecorderCaption {

	private Map<String, String> captionMap = null;	//保存视频字幕

	public VideoRecorderCaption(){
		captionMap = new HashMap<String, String>();		//保存视频字幕
	}

	/**
	 * 清空视频字幕
	 */
	public void clearCaptionMap() {
		captionMap.clear();
	}

	/**
	 * 设置当前时间的字幕
	 * @param currTime 字幕出显示的时间
	 * @param captionStr 字幕
	 */
	public void setCaption(String currTime, String captionStr) {
		captionMap.put(currTime, captionStr);
	}

	/**
	 *	获得当前时间的字幕
	 * @param currTime	字幕出显示的时间
	 * @return 字幕
	 */
	public String getCaptionByTime(String currTime) {
		return captionMap.get(currTime);
	}

	/**
	 * 获取字幕map
	 * @return Map<String currTime, String captionStr>	Map<当前时间, 显示的字幕>
	 */
	public Map<String, String> getCaptionMap() {
		return captionMap;
	}

	/**
	 * 设置获取字幕map
	 * @param captionMap Map<String currTime, String captionStr>	Map<当前时间, 显示的字幕>
	 */
	public void setCaptionMap(Map<String, String> captionMap) {
		this.captionMap = captionMap;
	}

}
