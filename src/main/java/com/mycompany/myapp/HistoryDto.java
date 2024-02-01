package com.mycompany.myapp;

public class HistoryDto {

	private int seq;
	private String boardSeq;
	private String signDate;
	private String signer;
	private String signStatus;
	private String historySeq;
	private String name;
	private String apperId;
	private String apperName;
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getBoardSeq() {
		return boardSeq;
	}
	public void setBoardSeq(String boardSeq) {
		this.boardSeq = boardSeq;
	}
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	public String getSigner() {
		return signer;
	}
	public void setSigner(String signer) {
		this.signer = signer;
	}
	public String getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
	public String getHistorySeq() {
		return historySeq;
	}
	public void setHistorySeq(String historySeq) {
		this.historySeq = historySeq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getApperId() {
		return apperId;
	}
	public void setApperId(String apperId) {
		this.apperId = apperId;
	}
	public String getApperName() {
		return apperName;
	}
	public void setApperName(String apperName) {
		this.apperName = apperName;
	}
}
