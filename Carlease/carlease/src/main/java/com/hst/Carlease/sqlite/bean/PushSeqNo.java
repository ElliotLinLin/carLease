package com.hst.Carlease.sqlite.bean;

import com.tools.sqlite.annotation.Column;

/**
 * 保存推送消息的SeqNo
 * 
 * @author LMC
 *
 */
public class PushSeqNo {
	
	@Column(key=true, notNull=true)
	private int SeqNo;
	
	public int getSeqNo() {
		return SeqNo;
	}
	
	public void setSeqNo(int seqNo) {
		SeqNo = seqNo;
	}
	
}
