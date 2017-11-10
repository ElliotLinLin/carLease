package com.hst.Carlease.util;

import java.io.File;

import android.content.Context;

import com.hst.Carlease.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tools.os.storage.SDRam;
import com.tools.os.storage.StorageTool;
import com.tools.util.Log;

public class MyImageLoader {
	private static final String TAG = MyImageLoader.class.getSimpleName();

	/**
	 * 初始化ImageLoader
	 * 
	 * @param context
	 */
	public static ImageLoaderConfiguration initImageLoader(Context context) {
		if (context == null) {
			return null;
		}

		// 创建图片选项
		DisplayImageOptions imageOptions = null;
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		// 设置图片在下载期间显示的图片
		builder.showStubImage(R.drawable.image_default).
		// 设置图片Uri为空或是错误的时候显示的图片
		showImageForEmptyUri(R.drawable.image_default)
		// 设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.drawable.image_default)
				// 设置图片在下载前是否重置，复位
				.resetViewBeforeLoading(false)
		// 设置下载的图片是否缓存在内存中
		 .cacheInMemory(false)
		// 设置下载的图片是否缓存在SD卡中
		 .cacheOnDisc(true)
		 .cacheOnDisk(true)
//		 .
		// 设置图片的解码类型
//		 .bitmapConfig(Bitmap.Config.RGB_565)
		// 设置图片的解码配置
//		 .decodingOptions(android.graphics.BitmapFactory.Options
//		 decodingOptions)
		// 设置图片下载前的延迟
		 .delayBeforeLoading(300);
		// 设置额外的内容给ImageDownloader
		// .extraForDownloader(Object extra)
		// 设置图片加入缓存前，对bitmap进行设置
//		 .preProcessor(BitmapProcessor preProcessor)
		// 设置显示前的图片，显示后这个图片一直保留在缓存中
		// .postProcessor(BitmapProcessor postProcessor)
		// 设置图片以如何的编码方式显示
		// .imageScaleType(ImageScaleType imageScaleType)
		// 设置图片显示方式：这里是圆角RoundedBitmapDisplayer
		// .displayer(new RoundedBitmapDisplayer(20))
		// .build();
		// 判断是否有存储卡（包括内置或是外置）
		StorageTool tool = new StorageTool(context);
		// if (tool.getMountedPath() == null) {
		builder.cacheInMemory(true).cacheOnDisc(false);
		// }else {
		// builder.cacheInMemory(false).cacheOnDisc(true);
		// }

		imageOptions = builder.build();

		ImageLoaderConfiguration config = null;
		ImageLoaderConfiguration.Builder builder2 = new ImageLoaderConfiguration.Builder(
				context);
		// 设置线程的优先级
		builder2.threadPriority(Thread.NORM_PRIORITY - 2)
		// 线程池大小
				.threadPoolSize(5)
				// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.denyCacheImageMultipleSizesInMemory()
				// 设置缓存文件的名字
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 设置图片下载和显示的工作队列排序
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 设置显示图片选项
				.defaultDisplayImageOptions(imageOptions);
		// 设置硬盘缓冲目录
		// .discCache(new UnlimitedDiscCache( new File(SD.getPath(true) +
		// SDRam.getImageCacheForNews()) )) // You can pass your own disc cache
		// implementation
		// .build();

		if (tool.getMountedPath() != null) {
			// 得到外置存储卡路径
			String storage = tool.getExternalStoragePath();
			// 判断外置存储卡是否挂载
			if (tool.isMounted(storage)) {
				builder2.discCache(new UnlimitedDiscCache(new File(storage
						+ SDRam.getImageCacheForNews()))); // You can pass your
															// own disc cache
															// implementation
			} else {
				storage = tool.getInternalStoragePath();
				builder2.discCache(new UnlimitedDiscCache(new File(storage
						+ SDRam.getImageCacheForNews()))); // You can pass your
															// own disc cache
															// implementation
			}
			Log.e(TAG,
					"image cache path:" + storage
							+ SDRam.getImageCacheForNews());
		}

		config = builder2.build();
		return config;
	}
}