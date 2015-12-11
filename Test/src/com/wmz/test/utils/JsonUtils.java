package com.wmz.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wmz.test.bean.UpdateBean;

public class JsonUtils {
	public static List<String> getListFromJsonByGetad(String json) {
		if (json == null || "".equals(json))
			return null;
		List<String> list = new ArrayList<String>();
		try {
			String key = "url";
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				list.add(object.get(key).toString());
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> getMapFormJson(String json) {

		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject object = new JSONObject(json);
			String key = "success";
			String value = object.getString(key);
			map.put(key, value);
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static UpdateBean getUpgradBeanFromJsonByCheckVersion(String json) {
		if (json == null || "".equals(json))
			return null;
		UpdateBean bean = null;
		try {
			JSONObject object = new JSONObject(json);
			String key = "status";
			String value = object.getString(key);
			if ("true".equals(value)) {
				bean = new UpdateBean();
				JSONObject object2 = object.getJSONObject("data");
				bean.setDownloadUrl(object2.getString("downloadUrl"));
				bean.setUpdateLog(object2.getString("updateLog"));
				bean.setVersionCode(object2.getInt("versionCode"));
				bean.setVersionName(object2.getString("versionName"));
			}
			return bean;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
