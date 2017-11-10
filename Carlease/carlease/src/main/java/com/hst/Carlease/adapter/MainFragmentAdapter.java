package com.hst.Carlease.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


public class MainFragmentAdapter {
	 private List<Fragment> fragments;
	    private FragmentActivity fragmentActivity; // Fragment所属的Activity

	    private int fragmentContentId; // Activity中所要被替换的区域的id

	    private int currentTab; // 当前Tab页面索引

	    private int index;

	    public MainFragmentAdapter(FragmentActivity fragmentActivity,
	                               List<Fragment> fragments, int fragmentContentId, int index) {
	        this.fragmentActivity = fragmentActivity;
	        this.fragments = fragments;
	        this.fragmentContentId = fragmentContentId;
	        this.index = index;

	        // 默认显示第一页
	        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
	                .beginTransaction();

	        ft.add(fragmentContentId, fragments.get(index));
	        ft.commit();
	    }

	    public void onChange(int index) {
	        Fragment fragment = fragments.get(index);
	        FragmentTransaction ft = obtainFragmentTransaction(index);

	        getCurrentFragment().onPause(); // 暂停当前tab
	        getCurrentFragment().onStop(); // 暂停当前tab

	        if (fragment.isAdded()) {
	            fragment.onStart(); // 启动目标tab的onStart()
	            fragment.onResume(); // 启动目标tab的onResume()
	        } else {
	            ft.add(fragmentContentId, fragment);
	        }
	        showTab(index); // 显示目标tab
	        ft.commit();

	        currentTab = index;
	    }

	    /**
	     * 切换tab
	     *
	     * @param idx
	     */
	    private void showTab(int idx) {
	        for (int i = 0; i < fragments.size(); i++) {
	            Fragment fragment = fragments.get(i);
	            FragmentTransaction ft = obtainFragmentTransaction(idx);
	            if (idx == i) {
	                ft.show(fragment);
	            } else {
	                ft.hide(fragment);
	            }
	            ft.commit();
	        }
	        currentTab = idx; // 更新目标tab为当前tab
	    }

	    /**
	     * 获取一个带动画的FragmentTransaction
	     *
	     * @param index
	     * @return
	     */
	    private FragmentTransaction obtainFragmentTransaction(int index) {
	        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
	                .beginTransaction();
	        // 设置切换动画
	        // 玉巧你可以自己设置其他动画
//	        if (index > currentTab) {
//	            ft.setCustomAnimations(R.anim.slide_in_from_right,
//	                    R.anim.slide_in_from_left);
//	        } else {
//	            ft.setCustomAnimations(R.anim.slide_in_from_left,
//	                    R.anim.slide_in_from_left);
//	        }
	        return ft;
	    }

	    public Fragment getCurrentFragment() {
	        return fragments.get(currentTab);
	    }
}
