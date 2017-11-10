package com.tools.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * 基础类服务
 * 
 * @author LMC
 *
 */
public abstract class AbsBaseService extends Service {

	private static final String TAG = AbsBaseService.class.getSimpleName();
	private static final boolean D = true;

	protected Context context = null;
	
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}

}
