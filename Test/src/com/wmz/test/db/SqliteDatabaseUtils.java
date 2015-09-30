package com.wmz.test.db;

import java.nio.channels.ClosedByInterruptException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqliteDatabaseUtils {
	public static SQLiteDatabase db = null;

	public static void openOrCreateDatabase(String path) {
		db = SQLiteDatabase.openOrCreateDatabase(path, null);
	}

	public static void execSQL(String sql) {
		if (db != null && db.isOpen()) {
			db.execSQL(sql);
		}
	}

	public static void execSQL(String sql, Object[] bindArgs) {
		if (db != null && db.isOpen()) {
			db.execSQL(sql, bindArgs);
		}
		
	}

	public static Cursor rawQuery(String sql) {
		return rawQuery(sql, null);
	}

	public static Cursor rawQuery(String sql, String[] selectionArgs) {
		if (db != null && db.isOpen()) {
			db.beginTransaction();
			Cursor cursor = db.rawQuery(sql, selectionArgs);
			db.setTransactionSuccessful();
			db.endTransaction();
			return cursor;
		}
		return null;
	}

	public static int getCount() {
		if (db != null && db.isOpen()) {
			Cursor cursor = db.query("table_fingerprint", null, null, null,
					null, null, null);
			if (cursor.moveToFirst()) {
				return cursor.getCount();
			}
		}
		return -1;
	}

	public static void closeDB() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	public static long insert(String table, ContentValues values) {
		if (db != null && db.isOpen()) {
			return db.insert(table, null, values);
		}
		return -1;
	}
}
