package com.wmz.test.utils;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

public class CursorUtils {
	public static List<String> getList(Cursor cursor, String columnName) {
		if (cursor == null)
			return null;
		List<String> list = new ArrayList<String>();
		while (cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex(columnName)));
		}
		cursor.close();
		return list.size() > 0 ? list : null;
	}
}
