package com.wmz.test.bean;

/**
 * ����bean
 * 
 * @author wmz
 * 
 */
public class UpdateBean {
	// �ο�http://www.oschina.net/MobileAppVersion.xml
	private int versionCode;
	private String versionName;
	private String downloadUrl;
	private String updateLog;

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}

	@Override
	public String toString() {
		return "UpgradBean [versionCode=" + versionCode + ", versionName="
				+ versionName + ", downloadUrl=" + downloadUrl + ", updateLog="
				+ updateLog + "]";
	}

}
