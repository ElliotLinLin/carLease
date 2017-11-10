package com.tools.video;


import android.media.CamcorderProfile;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OutputFormat;
import android.media.MediaRecorder.VideoEncoder;
import android.media.MediaRecorder.VideoSource;

import com.tools.app.AbsUI;
import com.tools.util.Log;

/**
 * 录像配置信息
 *
 * @author Liaojp
 *
 */
public class VideoRecorderConfig {
	private static final String TAG = VideoRecorderConfig.class.getSimpleName();

	private boolean useAudio = true;	//是否打开录音
	// 设置录制视频源为Camera(相机)
	private int videoSource = VideoSource.CAMERA;
	// 从麦克风获取音频
	private int audioSource = AudioSource.MIC;
	// 使用高质量视频录制
	private CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);

	// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
	private int outPutFormat = OutputFormat.MPEG_4;
	// 设置录制的视频编码h263 h264
	private int videoEncoder = VideoEncoder.H264;
	// 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
	private int videoWidth = 1280;	//1280
	private int videoHeight = 720;	//720
	// 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
	private int videoFrameRate = 15;//
	// 设置视频文件输出的路径
	private String outPutFilePath = "";
	// 视频文件后缀
	private String outPutFileSuffix = ".mp4";
	// 设置视频的最大持续时间 ms
	private int maxDuration = 30 * 60 * 1000;
	//设置单个视频的最大持续时间ms
	private int oneMaxDuration = 5 * 60 * 1000;
	// 设置视频的最大文件 byte
	private long maxFileSize = 1024 * 1024 * 1024;
	// 音频编码格式
	private int audioEncoder = AudioEncoder.AMR_NB;

	private String startTimeFileName = "";//以时间做文件名的前缀
	private String videoRecordPath = "";//中间路径
	private String path;

	/**
	 * 获取录制视频源
	 * @return VideoSource.CAMERA(相机)
	 */
	public int getVideoSource() {
		return videoSource;
	}

	/**
	 * 设置录制视频源
	 * @param videoSource VideoSource.CAMERA(相机)
	 */
	public void setVideoSource(int videoSource) {
		this.videoSource = videoSource;
	}

	/**
	 * 录制完成后视频的封装格式 THREE_GPP为3gp.MPEG_4为mp4
	 * @return default MPEG_4
	 */
	public int getOutPutFormat() {
		return outPutFormat;
	}

	/**
	 * 设置录制完成后视频的封装格式
	 * @param outPutFormat THREE_GPP为3gp.MPEG_4为mp4
	 */
	public void setOutPutFormat(int outPutFormat) {
		this.outPutFormat = outPutFormat;
	}

	/**
	 * 录制的视频编码 h263或h264
	 * @return default h263
	 */
	public int getVideoEncoder() {
		return videoEncoder;
	}

	/**
	 * 设置录制的视频编码 h263或h264
	 * @param videoEncoder  h263或h264 (default h263)
	 */
	public void setVideoEncoder(int videoEncoder) {
		this.videoEncoder = videoEncoder;
	}

	/**
	 * 视频录制的分辨率宽
	 * @return 宽default 1280
	 */
	public int getVideoWidth() {
		return videoWidth;
	}

	/**
	 * 设置视频录制的分辨率
	 * @param videoWidth 宽default 1280
	 */
	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}

	/**
	 * 视频录制的分辨率
	 * @return 高default 720
	 */
	public int getVideoHeight() {
		return videoHeight;
	}

	/**
	 * 设置视频录制的分辨率
	 * @param videoHeight 高default 720
	 */
	public void setVideoHeight(int videoHeight) {
		this.videoHeight = videoHeight;
	}

	/**
	 * 录制的视频帧率
	 * @return	default 15
	 */
	public int getVideoFrameRate() {
		return videoFrameRate;
	}

	/**
	 * 设置录制的视频帧率
	 * @param videoFrameRate default 15
	 */
	public void setVideoFrameRate(int videoFrameRate) {
		this.videoFrameRate = videoFrameRate;
	}

	/**
	 * 获取输出文件路径
	 * @return
	 */
	public String getOutPutFilePath() {
		return outPutFilePath;
	}

	/**
	 * 设置输出文件路径
	 * @param outPutFilePath 文件路径+文件名
	 */
	public void setOutPutFilePath(String outPutFilePath) {
		this.outPutFilePath = outPutFilePath;
	}

	/**
	 * 返回相对路径，用于保存到数据库
	 * void
	 * 2014-1-23
	 * @author MoSQ
	 */
	public String getRelativePath(){
		if (AbsUI.isEmptyString(videoRecordPath)) {
			return null;
		}
		if (AbsUI.isEmptyString(startTimeFileName)) {
			return null;
		}
		return videoRecordPath + "/"+getReplaceWord(startTimeFileName) + getOutPutFileSuffix();
	}

	/**
	 * 替换字符
	 * 有的符号不能作为视频的文件名出现（有的手机出现黑屏），因此需要转换
	 * @param url
	 * @return
	 * String
	 *2014-1-23
	 */
	public String getReplaceWord(String url){
		return url.replaceAll("[:/?.#%=&*, ]", "+").replaceAll("[+]+", "_");
	}

	/**
	 * 视频的最大持续时间 ms
	 * @return default 30 * 60 * 1000秒
	 */
	public int getMaxDuration() {
		return maxDuration;
	}

	/**
	 * 设置视频的最大持续时间 ms
	 * @param maxDuration default 30 * 60 * 1000秒
	 */
	public void setMaxDuration(int maxDuration) {
		this.maxDuration = maxDuration;
	}
	
	
	/**
	 * 得到单个视频的最大持续时间
	 * @return
	 * int
	 * 2014-3-17
	 * @author MoSQ
	 */
	public int getOneMaxDuration() {
		return oneMaxDuration;
	}

	/**
	 * 设置单个视频的最大持续时间
	 * @param oneMaxDuration
	 * void
	 * 2014-3-17
	 * @author MoSQ
	 */
	public void setOneMaxDuration(int oneMaxDuration) {
		this.oneMaxDuration = oneMaxDuration;
	}

	/**
	 * 视频的最大文件的大小
	 * @return
	 */
	public long getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * 设置视频的最大文件的大小
	 * @param maxFileSize
	 */
	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	/**
	 * 音频编码格式
	 * @return default AudioEncoder.AMR_NB
	 */
	public int getAudioEncoder() {
		return audioEncoder;
	}

	/**
	 * 设置音频编码格式
	 * @param audioEncoder default  AudioEncoder.AMR_NB
	 */
	public void setAudioEncoder(int audioEncoder) {
		this.audioEncoder = audioEncoder;
	}

	/**
	 * 从麦克风获取音频源
	 * @return default AudioSource.MIC麦克风
	 */
	public int getAudioSource() {
		return audioSource;
	}

	/**
	 * 从麦克风获取音频源
	 * @param audioSource AudioSource.MIC麦克风
	 */
	public void setAudioSource(int audioSource) {
		this.audioSource = audioSource;
	}

	/**
	 * 获取输出文件的格式
	 * @return	OutputFormat.THREE_GPP为.3gp  OutputFormat.MPEG_4返回.mp4
	 */
	public String getOutPutFileSuffix() {
		if (outPutFormat == OutputFormat.THREE_GPP) {
			outPutFileSuffix = ".3gp";
		} else if (outPutFormat == OutputFormat.MPEG_4) {
			outPutFileSuffix = ".mp4";
		}
		return outPutFileSuffix;
	}

	/**
	 * 设置文件格式
	 * @param outPutFileSuffix OutputFormat.THREE_GPP为.3gp  OutputFormat.MPEG_4返回.mp4
	 */
	public void setOutPutFileSuffix(String outPutFileSuffix) {
		this.outPutFileSuffix = outPutFileSuffix;
	}

	/**
	 * 获取录像质量配置类
	 * @return CamcorderProfile 高质量CamcorderProfile.QUALITY_HIGH
	 */
	public CamcorderProfile getCamcorderProfile() {
		Log.d(TAG, "CamcorderProfile fileFormat:"+camcorderProfile.fileFormat);
		Log.d(TAG, "CamcorderProfile videoBitRate:"+camcorderProfile.videoBitRate);
		Log.d(TAG, "CamcorderProfile videoFrameRate:"+camcorderProfile.videoFrameRate);
		Log.d(TAG, "CamcorderProfile videoFrameWidth:"+camcorderProfile.videoFrameWidth);
		Log.d(TAG, "CamcorderProfile videoFrameHeight:"+camcorderProfile.videoFrameHeight);
		Log.d(TAG, "CamcorderProfile videoCodec:"+camcorderProfile.videoCodec);
		return camcorderProfile;
	}

	/**
	 * 设置录像质量配置类
	 * @param camcorderProfile 高质量CamcorderProfile.QUALITY_HIGH
	 */
	public void setCamcorderProfile(CamcorderProfile camcorderProfile) {
		this.camcorderProfile = camcorderProfile;
	}

	/**
	 * 得到保存的文件夹的路径
	 * void
	 * 2014-1-24
	 * @author MoSQ
	 */
	public String getFolderPath(){
		return path;
	}

	/**
	 * 是否使用录音
	 * @return 使用true 不使用false
	 */
	public boolean isUseAudio() {
		return useAudio;
	}

	/**
	 * 设置是否使用录音
	 * @param useAudio 使用true 不使用false
	 */
	public void setUseAudio(boolean useAudio) {
		if (useAudio) {
			// 从麦克风获取音频
			setAudioSource(AudioSource.MIC);
		} else {
			//不进行录音
			Log.d(TAG, "useAudio:" + useAudio);
			setAudioSource(AudioSource.VOICE_CALL);
		}
		this.useAudio = useAudio;
	}

	public String getStartTimeFileName() {
		return startTimeFileName;
	}

	public void setStartTimeFileName(String startTimeFileName) {
		this.startTimeFileName = startTimeFileName;
	}

	public String getVideoRecordPath() {
		return videoRecordPath;
	}

	public void setVideoRecordPath(String videoRecordPath) {
		this.videoRecordPath = videoRecordPath;
	}

}
