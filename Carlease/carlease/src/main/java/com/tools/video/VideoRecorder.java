package com.tools.video;

import java.io.IOException;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.util.Log;
import android.view.SurfaceHolder;

import com.tools.util.DatetimeUtil;
import com.tools.widget.Prompt;

/**
 * 录像封装类
 * @author Liaojp
 *
 */
public class VideoRecorder {
	private static final String TAG = VideoRecorder.class.getSimpleName();
	private MediaRecorder mediaRecorder = null;
	private VideoRecorderConfig config = null;
	private OnDurationInfoListener durationInfoListener;
	private Camera camera = null;
	

	Prompt prompt = null;

	public VideoRecorderConfig getConfig() {
		return config;
	}
	/**
	 * 设置配置信息
	 * @param config 配置
	 */
	public void setConfig(VideoRecorderConfig config) {
		this.config = config;
	}

	public boolean isPreView = false;	//是否在预览
	public static boolean isRecording = false;	//是否正在录制
	public static boolean isRelease = false;//判断Camera是否已经释放
	private String startTime = "";		//录制开始时间
	private String currTime = "";		//录制当前时间
	private long startMills ;		//录制开始毫秒
	private long endMills ;		//录制结束毫秒
	private String endTime = "";		//录制结束时间
	private String recordTime = "";	//录制了多少时间
	private String pathString = "";//当前文件路径
	private String oldPathString = "";//上一个文件路径

	/**
	 * 视屏录制 默认保存文件路径
	 */
	public VideoRecorder(){
		mediaRecorder = new MediaRecorder();
		config = new VideoRecorderConfig();
		
	}


	/**
	 * 开始录制
	 * @param surfaceView 实时预览的surfaceView  需要传递一个已初始化好的SurfaceHolder对象
	 */
	public void startRecord(SurfaceHolder holder) {

		if (holder == null) {
			Log.i(TAG, "holder == null");
			return;
		}
		if (mediaRecorder == null) {
			Log.i(TAG, "mediaRecorder == null");
			return;
		}
		
		if (camera != null) {
			// 第1步：解锁并将摄像头指向MediaRecorder 设置录制视频源为Camera(相机)
			camera.unlock();
			mediaRecorder.setCamera(camera); 
		}

		/**
		 * 是否录制声音的选择(在设置声音的时候，只能设置AudioSource.MIC,有的手机厂商因为法律问题，禁用了其他的AudioSource)
		 * 比如，如果想不设置声音，而把AudioSource设置为AudioSource.VOICE_CALL,那么有的手机有效，有的手机直接崩溃。
		 * http://www.zhihu.com/question/21851683
		 */
		// 设置录制视频源为Camera(相机)
		mediaRecorder.setAudioSource(config.getAudioSource());
		mediaRecorder.setVideoSource(config.getVideoSource());
		
		/**
		 * 不同的SDK版本配置需要不同，低于API Level 8的版本，不能设置setProfile，而应设置视频格式，视频编码，音频编码
		 * 高于API Level 8的版本，需要设置setProfile.
		 * 但是现在发现我的手机2.3.7的版本（Level 10）也不能设置setProfile（如果不设置预览则可以） ？？？
		 * 所以设置高于Level10，那么低于此设置版本的手机选择质量无效，当前还没有更好的其他办法。
		 * 
		 */
		Log.i(TAG, "com.tools.os.Build.VERSION.SDK_INT--->"+com.tools.os.Build.VERSION.SDK_INT);
		if ( com.tools.os.Build.VERSION.SDK_INT > com.tools.os.Build.VERSION_CODES.Level10 ) {
			//构造CamcorderProfile，使用高质量视频录制
			mediaRecorder.setProfile(config.getCamcorderProfile());
		}else {

			//设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
			mediaRecorder.setOutputFormat(config.getOutPutFormat());

			//设置录制的视频编码h263 h264
			// 从麦克风获取音频
			mediaRecorder.setAudioEncoder(config.getAudioEncoder());
			mediaRecorder.setVideoEncoder(config.getVideoEncoder());

		}
		
		Log.i(TAG, "config.getVideoWidth()-->"+config.getVideoWidth());
		Log.i(TAG, "config.getVideoHeight()-->"+config.getVideoHeight());
		// 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错(如果加上这一句，则有的手机会卡主不动，录制不了，不同的手机的分辨率不同，不能固定死)
		mediaRecorder.setVideoSize(config.getVideoWidth(), config.getVideoHeight());
		
		// 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
//		mediaRecorder.setVideoFrameRate(config.getVideoFrameRate());

		// 设置视频文件输出的路径
		pathString = config.getOutPutFilePath();
		Log.i(TAG, "config.getOutPutFilePath()--->"+pathString);
		mediaRecorder.setOutputFile(config.getOutPutFilePath());
		
		//设置视频单个持续录制时间
		mediaRecorder.setMaxDuration(config.getOneMaxDuration());
		mediaRecorder.setOnInfoListener(new OnInfoListener() {
			
			//onInfo方法经常一次性调用好几次，所以需要限制。否则有可能造成保存多次的问题和刚开始下一个录制又马上结束的问题
			@Override
			public void onInfo(MediaRecorder mr, int what, int extra) {
				// TODO Auto-generated method stub
				Log.i(TAG, "---onInfo--->>"+"config.getOneMaxDuration()"+config.getOneMaxDuration());
				
				//当录制时间等于设置好的最大录制时间时
				if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED && !pathString.equals(oldPathString)) {
					oldPathString = pathString;
					Log.i(TAG, "!pathString.equals(oldPathString)");
					durationInfoListener.durationInfoListener(what);//监听录制时长
					
					
				}
			}
		});

		// 准备实时预览
		mediaRecorder.setPreviewDisplay(holder.getSurface());	//需要传递一个已初始化好的SurfaceHolder对象
		
		prepare();	//缓冲
		// 开始录制
		try {
			mediaRecorder.start();
			isRecording = true;
			startTime = DatetimeUtil.getCurrentDatetime();
			startMills = DatetimeUtil.getCurrentMilliseconds();
			VideoRecorderCaption caption = new VideoRecorderCaption();	//字幕
			String captionStr = "";	//字幕
			caption.setCaption(currTime, captionStr);
		} catch (IllegalStateException e) {
			mediaRecorder.release();
			mediaRecorder = null;
			e.printStackTrace();
		}


	}

	/**
	 * 停止录制
	 */
	public void stopRecord() {
		
		endTime = DatetimeUtil.getCurrentDatetime();
		endMills = DatetimeUtil.getCurrentMilliseconds();
		if (isRecording == true) {
			try {
				mediaRecorder.stop();
				mediaRecorder.reset();
				mediaRecorder.release();
				mediaRecorder = null;
				isRecording = false;
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				mediaRecorder.reset();
				mediaRecorder.release();
				mediaRecorder = null;
				isRecording = false;
			}
		}

	}


	/**
	 * 准备录制
	 */
	public void prepare(){
		try {
			mediaRecorder.prepare();
		} catch (IOException e) {
			try {
				Thread.sleep(1000);
				mediaRecorder.prepare();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}catch (IOException e1) {
				mediaRecorder.reset();
				mediaRecorder.release();
				mediaRecorder = null;
				camera.release();
				camera = null;
			} 
			
		}
	}


	/**
	 * 保存录制好的视频
	 */
	public void saveRecord(){
		if(isRecording && mediaRecorder != null){
			Log.i(TAG, "---------------saveRecord()---------------");
		
			endTime = DatetimeUtil.getCurrentDatetime();
			endMills = DatetimeUtil.getCurrentMilliseconds();
			try {
				mediaRecorder.stop();//经常在这一句报错，但是还不知道为什么？？？
				//4.0以上设备不需要自己加锁，但是4.0一下的却需要，否则录制第二次的时候就报Camera.unlock失败异常；
				camera.lock();
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
					mediaRecorder.stop();
				} catch (Exception e1) {
					mediaRecorder.reset();
					mediaRecorder.release();
					if (camera != null) {
						camera.release();
						camera = null;
					}
				}
			}finally{
				isRecording = false;
				if (mediaRecorder != null) {
					mediaRecorder.release();
					mediaRecorder = null;
				}
			}
		}
	}
	
	/**
	 * 监听录制时间的接口
	 * @author MoSQ
	 *2014-3-17上午10:29:05
	 */
	public interface OnDurationInfoListener{
		/**
		 *  
		 *  int what 表示录制的最大时长
		 *  当 what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED时调用此方法
		 * void
		 * 2014-3-18
		 * @author MoSQ
		 */
		public void durationInfoListener(int what);
	}
	
	/**
	 * 设置录制时间的监听器
	 * void
	 * 2014-3-17
	 * @author MoSQ
	 */
	public void setOnDurationInfoListener(OnDurationInfoListener durationInfoListener){
		this.durationInfoListener = durationInfoListener;
	}
	
	public MediaRecorder getMediaRecorder() {
		return mediaRecorder;
	}
	public void setMediaRecorder(MediaRecorder mediaRecorder) {
		this.mediaRecorder = mediaRecorder;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	public String getCurrTime() {
		this.currTime = DatetimeUtil.getCurrentDatetime();
		return currTime;
	}

	public void setCurrTime(String currTime) {
		this.currTime = currTime;
	}
	public Camera getCamera() {
		return camera;
	}
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public long getStartMills() {
		return startMills;
	}
	public void setStartMills(long startMills) {
		this.startMills = startMills;
	}
	public long getEndMills() {
		return endMills;
	}
	public void setEndMills(long endMills) {
		this.endMills = endMills;
	}
	public static boolean isRelease() {
		return isRelease;
	}
	public static void setRelease(boolean isRelease) {
		VideoRecorder.isRelease = isRelease;
	}

}
