package com.hst.Carlease.widget.viewImage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.hst.Carlease.R;
import com.hst.Carlease.util.TakePhotoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GalleryAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<MyImageView> imageViews = new ArrayList<MyImageView>();

	private ImageCacheManager imageCache;

	private List<String> mItems;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bitmap bitmap = (Bitmap) msg.obj;
			Bundle bundle = msg.getData();
			String url = bundle.getString("url");
			for (int i = 0; i < imageViews.size(); i++) {
				if (imageViews.get(i).getTag().equals(url)) {
					imageViews.get(i).setImageBitmap(bitmap);
				}
			}
		}
	};

	public void setData(List<String> data) {
		this.mItems = data;
		notifyDataSetChanged();
	}

	public GalleryAdapter(Context context) {
		this.context = context;
		imageCache = ImageCacheManager.getInstance(context);
	}

	@Override
	public int getCount() {
		return mItems != null ? mItems.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyImageView view = new MyImageView(context);
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		String pathString = mItems.get(position);
		if (pathString != null) {
			Bitmap bmp;
			// try {
			// bmp = imageCache.get(item);
			// bmp = TakePhotoUtils.getimage(item, 320, 480);
			// bmp = ImageLoader.getInstance().loadImageSync(item);
			if (pathString.contains("http:")) {// 网络图片，非默认图片
				bmp = ImageLoader.getInstance().loadImageSync(pathString);
				if (bmp != null) {
					view.setImageBitmap(bmp);// 显示图片
				} else {
					// 显示默认图片
					view.setImageBitmap(ImageLoader.getInstance()
							.loadImageSync(
									"drawable://" + R.drawable.image_default));
				}
			} else if (pathString.equals("one")) {// 默认的四张图片
				view.setImageBitmap(ImageLoader.getInstance().loadImageSync(
						"drawable://" + R.drawable.one));
			} else if (pathString.equals("two")) {
				view.setImageBitmap(ImageLoader.getInstance().loadImageSync(
						"drawable://" + R.drawable.two));
			} else if (pathString.equals("there")) {
				view.setImageBitmap(ImageLoader.getInstance().loadImageSync(
						"drawable://" + R.drawable.there));
			} else if (pathString.equals("four")) {
				view.setImageBitmap(ImageLoader.getInstance().loadImageSync(
						"drawable://" + R.drawable.there));
			} else if (pathString.equals("default")) {
				view.setImageBitmap(ImageLoader.getInstance().loadImageSync(
						"drawable://" + R.drawable.image_default));
			} else {
				// 对图片进行压缩
				bmp = TakePhotoUtils.getimage(pathString, 320, 480);
				if (bmp != null) {
					view.setImageBitmap(bmp);// 显示图片
				} else {
					// 显示默认图片
					view.setImageBitmap(ImageLoader.getInstance()
							.loadImageSync(
									"drawable://" + R.drawable.image_default));
				}
			}

			view.setTag(pathString);
			if (!this.imageViews.contains(view)) {
				imageViews.add(view);
			}
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		return view;
	}
}
