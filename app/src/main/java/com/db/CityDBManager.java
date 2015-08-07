package com.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.express.R;
import com.example.express.bean.CityInfo;

public class CityDBManager {
	private final int BUFFER_SIZE = 1024;

	public static final String DB_NAME = "city_cn.s3db";

	public static final String PACKAGE_NAME = "com.example.express";

	public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME;

	private SQLiteDatabase database;

	private Context context;

	private File file = null;

	public CityDBManager(Context context) {

		this.context = context;
	}

	public void openDatabase() {

		this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
	}

	public SQLiteDatabase getDatabase() {

		return this.database;
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {

			file = new File(dbfile);
			if (!file.exists()) {

				InputStream is = context.getResources().openRawResource(R.raw.city);
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
//					L.i("cc", "while");
					fos.flush();
				}
				fos.close();
				is.close();
			}
			
			database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);

			return database;
		} catch (FileNotFoundException e) {
			Log.e("cc", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("cc", "IO exception");
			e.printStackTrace();
		} catch (Exception e) {
			Log.e("cc", "exception " + e.toString());
		}
		return null;
	}

	public void closeDatabase() {
		Log.i("cc", "closeDatabase()");
		if (this.database != null)
			this.database.close();
	}
	
	public List<CityInfo> queryAllinfo(){
		String sql = null;
		sql = "select code,name from city ";
		List<CityInfo> cityInfos = new ArrayList<CityInfo>();
		Cursor cursor = null;
		try {
			this.openDatabase();
			cursor = database.rawQuery(sql, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					CityInfo cif = new CityInfo();
					cif.setCode(cursor.getString(cursor.getColumnIndex("code")));
					cif.setName(cursor.getString(cursor.getColumnIndex("name")));
					//cif.setPcode(cursor.getString(cursor.getColumnIndex("pcode")));
					//cif.setNum(cursor.getString(cursor.getColumnIndex("num")));
					cityInfos.add(cif);
					//city_code = cursor.getString(cursor.getColumnIndex("code"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		closeDatabase();
		return cityInfos;
	}

	public String queryAreaCodeByCityCode(String code) {
		String city_code = "";
		String sql = null;
		sql = "select code from city where num = '" + code + "'";

		Cursor cursor = null;
		try {
			this.openDatabase();
			cursor = database.rawQuery(sql, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					city_code = cursor.getString(cursor.getColumnIndex("code"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		closeDatabase();
		return city_code;
	}

	public String queryCityNameByCityCode(String city_code) {
		String city_name = null;
		String sql = "select name from city where code = '" + city_code + "'";
		Cursor cursor = null;
		try {
			this.openDatabase();
			cursor = database.rawQuery(sql, null);

			Log.v("--->>", "count=" + cursor.getCount());

			if (cursor != null) {
				while (cursor.moveToNext()) {
					city_name = new String(cursor.getBlob(cursor.getColumnIndex("name")), "gbk");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		closeDatabase();

		return city_name;
	}

	/**
	 * <通过省份code查询省份名称> <功能详细描述>
	 * 
	 * @param
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String[] queryCity(String cityCode, String provinceCode) {
		String provinceSql = "select name from province where code =" + provinceCode;
		String citySql = "select name from city where code =" + cityCode;

		this.openDatabase();

		String provinceName = null;
		String cityName = null;

		Cursor proCursor = database.rawQuery(provinceSql, null);

		if (proCursor.getCount() > 0) {
			proCursor.moveToFirst();

			try {
				byte bytes[] = proCursor.getBlob(0);
				provinceName = new String(bytes, "gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} finally {

				if (proCursor != null) {

					proCursor.close();

				}

			}
		} else {

			if (proCursor != null) {

				proCursor.close();

			}
		}

		Cursor cityCursor = database.rawQuery(citySql, null);

		if (cityCursor.getCount() > 0) {
			cityCursor.moveToFirst();

			try {
				byte bytes[] = cityCursor.getBlob(0);
				cityName = new String(bytes, "gbk");

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} finally {

				if (cityCursor != null) {

					cityCursor.close();

				}

			}

		} else {
			if (cityCursor != null) {

				cityCursor.close();

			}
		}

		String[] name = new String[2];
		name[0] = provinceName;
		name[1] = cityName;
		this.closeDatabase();
		return name;
	}

}