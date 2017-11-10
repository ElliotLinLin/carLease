package com.hst.Carlease.adapter;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;

	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		if (mDatas == null) mDatas = new ArrayList<>();
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount() {
		return mDatas == null?0:mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommonViewHolder viewHolder = getViewHolder(position, convertView, parent);
		fillItemData(viewHolder, getItem(position),position);
		return  viewHolder.getConvertView();

	}

	/**
	 * 填充数据到convertView
	 * 
	 * @param holder通过holder.getView(id)来获取你需要的view
	 * @param data数据
	 * @param position当前项
	 */
	public abstract void fillItemData(CommonViewHolder holder, T data, int position);

	private CommonViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
		return CommonViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
	}

	public List<T> getDatas() {
		return mDatas;
	}

	/**
	 * 添加一个条目
	 * @param elem
	 */
	public void add(T elem) {
		mDatas.add(elem);
		notifyDataSetChanged();
	}

	public void addAt(int index, T elem) {
		mDatas.add(index, elem);
		notifyDataSetChanged();
	}
	/**
	 * 添加一个集合数据
	 * @param elem
	 */
	public void addAll(List<T> elem) {
		mDatas.addAll(elem);
		notifyDataSetChanged();
	}

	/**
	 * 替换某个位置的数据
	 * @param oldElem
	 * @param newElem
	 */
	public void set(T oldElem, T newElem) {
		setAt(mDatas.indexOf(oldElem), newElem);
	}

	/**
	 * 替换某个位置的数据
	 * @param index
	 * @param elem
	 */
	public void setAt(int index, T elem) {
		mDatas.set(index, elem);
		notifyDataSetChanged();
	}

	public void remove(T elem) {
		mDatas.remove(elem);
		notifyDataSetChanged();
	}

	/**
	 * 移除某个条目
	 * @param index  在datas中的角标
	 */
	public void removeAt(int index) {
		mDatas.remove(index);
		notifyDataSetChanged();
	}

	/**
	 * 替换全部
	 * @param elem
	 */
	public void replaceAll(List<T> elem) {
		mDatas.clear();
		mDatas.addAll(elem);
		notifyDataSetChanged();
	}

	/**
	 * 是否包含某个条目
	 * @param elem
	 * @return
	 */
	public boolean contains(T elem) {
		return mDatas.contains(elem);
	}

	/**
	 * Clear data list
	 */
	public void clear() {
		mDatas.clear();
		notifyDataSetChanged();
	}

}
