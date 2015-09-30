package com.wmz.test.activity;

import java.io.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.wmz.test.R;
import com.wmz.test.utils.EnvironmentUtils;

public class SQLiteActivity extends Activity implements OnClickListener {

	private String TAG = "SQLiteActivity";
	private Context mContext = SQLiteActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sqlite);
		onCreateOrOpenDB();
		initView();
	}

	private void onCreateOrOpenDB() {

	}

	private void initView() {
		findViewById(R.id.btn_sqlite_create).setOnClickListener(this);
		findViewById(R.id.btn_sqlite_insert).setOnClickListener(this);
		findViewById(R.id.btn_sqlite_update).setOnClickListener(this);
		findViewById(R.id.btn_sqlite_query).setOnClickListener(this);
		findViewById(R.id.btn_sqlite_delete).setOnClickListener(this);
		findViewById(R.id.btn_sqlite_insertmore).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sqlite_create:
			createDB();
			break;
		case R.id.btn_sqlite_insert:
			insertDB();
			break;
		case R.id.btn_sqlite_update:
			updateDB();
			break;
		case R.id.btn_sqlite_query:
			queryDB();
			break;
		case R.id.btn_sqlite_delete:
			deleteDB();
			break;
		case R.id.btn_sqlite_insertmore:
			insertDBmore();
			break;
		}
	}

	Long startTime;

	private void insertDBmore() {
		startTime = System.currentTimeMillis();
		Log.d(TAG, "wmz:startTime=0");
		for (int i = 0; i < 10000; i++) {
			db.execSQL("insert into person values(null,?,?)", new Object[] {
					"wmz_" + i, i + "" });
		}
		Log.d(TAG, "wmz:time=" + (System.currentTimeMillis() - startTime));
	}

	private void deleteDB() {
		db.delete("person", "age<?", new String[] { "35" });
	}

	private void queryDB() {
		Cursor cursor = db.rawQuery("select * from person where age>=?",
				new String[] { "23" });
		while (cursor.moveToNext()) {
			int _id = cursor.getInt(cursor.getColumnIndex("name"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			int age = cursor.getInt(cursor.getColumnIndex("age"));
			Log.d(TAG, "wmz:_id=" + _id + ",name=" + name + ",age=" + age);
		}
		cursor.close();
	}

	private void updateDB() {
		ContentValues cValues = new ContentValues();
		cValues.put("age", 23);

		db.update("person", cValues, "name=?", new String[] { "john" });

	}

	private void insertDB() {

		person = new Person();
		person.name = "john";
		person.age = 20;
		// 插入数据
		db.execSQL("insert into person values(null,?,?)", new Object[] {
				person.name, person.age });

		person.name = "david";
		person.age = 33;
		ContentValues cValues = new ContentValues();
		cValues.put("name", person.name);
		cValues.put("age", person.age);
		// 插入contentValues数据
		db.insert("person", null, cValues);
	}

	SQLiteDatabase db = null;
	Person person = null;

	private void createDB() {
		String path = EnvironmentUtils.getExternalStoragePath()
				+ File.separator + "test.db";
		boolean isCreate = false;
		if (!new File(path).exists())
			isCreate = true;
		db = openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
		if (isCreate) {
			db.execSQL("drop table if exists person");
			db.execSQL("create table person (_id integer primary key autoincrement,name varchar,age smallint)");
		}
	}

	class Person {
		private String name;
		private int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}
}
