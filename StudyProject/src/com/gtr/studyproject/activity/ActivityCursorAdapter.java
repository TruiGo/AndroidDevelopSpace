package com.gtr.studyproject.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.frameworkxt.android.model.SQLColumn;
import com.xiaotian.frameworkxt.android.model.SQLDataBaseHelper;
import com.xiaotian.frameworkxt.android.model.SQLEntity;
import com.xiaotian.frameworkxt.android.model.SQLException;
import com.xiaotian.frameworkxt.android.model.SQLId;
import com.xiaotian.frameworkxt.android.model.SQLPersister;
import com.xiaotian.frameworkxt.android.model.SQLTable;
import com.xiaotian.frameworkxt.android.model.SQLTable.DatabaseNameType;
import com.xiaotian.frameworkxt.android.model.UtilSQLEntityAnnotation;
import com.xiaotian.frameworkxt.android.model.provider.SQLContentProvider;
import com.xiaotian.frameworkxt.android.model.provider.SQLTableContentProvider;
import com.xiaotian.frameworkxt.android.model.provider.UtilSQLContentProviderAnnotation;

/**
 * @version 1.0.0
 * @author mac
 * @name ActivityCursorAdapter
 * @description Cursor Adapter
 * @date 2014-10-13
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityCursorAdapter extends BaseFragmentActivity {
	private static final int LOADER_ID = 0X001;
	ListView listView;
	SQLPersister persister;
	BaseAdapter adapter;
	String dataBaseName = "xiaotiantian-com.db";
	UtilSQLEntityAnnotation<TestAccount> utilEntity = new UtilSQLEntityAnnotation<TestAccount>() {
		@Override
		public Class<?> getExtendsedClass() {
			return getClass();
		}
	};

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		persister = new SQLPersister(this);
		setContentView(R.layout.activity_cursor_adapter);
		listView = (ListView) findViewById(R.id.id_0);
		TextView textEmpty = (TextView) findViewById(R.id.id_1);
		listView.setEmptyView(textEmpty);
		adapter = new BaseCursorAdapter(this, null, 0);
		listView.setAdapter(adapter);
		// 供应者模式的适配器[3.0 以上]
		getLoaderManager().initLoader(LOADER_ID, null, new LoaderCallbacks<Cursor>() {
			@Override
			public Loader<Cursor> onCreateLoader(int id, Bundle args) {
				Uri uri = UtilSQLContentProviderAnnotation.getContentURI(TestAccount.class, dataBaseName);
				String[] projects = UtilSQLEntityAnnotation.getSQLEntityProjects(TestAccount.class);
				return new CursorLoader(getApplicationContext(), uri, projects, null, null, "_id");
			}

			@Override
			public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
				((CursorAdapter) adapter).swapCursor(data);
			}

			@Override
			public void onLoaderReset(Loader<Cursor> loader) {
				((CursorAdapter) adapter).swapCursor(null);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				listView.setTag(R.id.id_0, view.getTag(R.id.id_0));
				listView.setTag(R.id.id_position, position);
				toast(view.getTag(R.id.id_0).toString());
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				listView.setTag(R.id.id_0, view.getTag(R.id.id_0));
				listView.setTag(R.id.id_position, position);
				toast(view.getTag(R.id.id_0).toString());
				return false;
			}
		});
		registerForContextMenu(listView);
		// 多线程并发写数据库
		ExecutorService executors = Executors.newFixedThreadPool(10);
		executors.execute(new AddColumnRunnable());
		executors.execute(new AddColumnRunnable());
		executors.execute(new AddColumnRunnable());
		executors.execute(new AddColumnRunnable());
		executors.execute(new AddColumnRunnable());
		executors.execute(new AddColumnRunnable());
		executors.execute(new AddColumnRunnable());
		executors.execute(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					try {
						Thread.sleep(Math.round(1000) + 2000);
					} catch (InterruptedException e1) {}
					// 清空数据
					// getContentResolver().delete(Uri.parse(TestAccountContentProvider.CONTENT_URI), null, null);
					// Uri uri = UtilSQLContentProviderAnnotation.getContentURI(TestAccount.class, dataBaseName);
					// getContentResolver().delete(uri, null, null);
				}
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("选择Item的操作选项:").setHeaderIcon(R.drawable.icon_en);
		menu.addSubMenu(0x001, R.id.id_0, 0, "查看详情");
		menu.addSubMenu(0x001, R.id.id_1, 1, "更新详情");
		menu.addSubMenu(0x001, R.id.id_2, 2, "删除详情");
		menu.addSubMenu(0x002, R.id.id_3, 3, "操作1-查看详情");
		menu.addSubMenu(0x002, R.id.id_4, 4, "操作2-更新详情");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Uri url = UtilSQLContentProviderAnnotation.getContentURI(TestAccount.class, dataBaseName);
		int id = (Integer) listView.getTag(R.id.id_0);
		switch (item.getItemId()) {
		case R.id.id_0:
			// 获取详情
			url = Uri.withAppendedPath(url, String.valueOf(id));
			try {
				Cursor cursor = getContentResolver().query(url, null, null, null, null);
				if (cursor.moveToFirst()) {
					TestAccount ta = utilEntity.deSerialize(cursor);
					toast("Get The Item Id=" + " " + ta.id + ta.account + " " + ta.password);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case R.id.id_1:
			// 更新
			url = Uri.withAppendedPath(url, String.valueOf(id));
			try {
				Cursor cursor = getContentResolver().query(url, null, null, null, null);
				if (cursor.moveToFirst()) {
					TestAccount ta = utilEntity.deSerialize(cursor);
					ta.account = "Modify Account Name";
					ta.password = "Password Account";
					int row = getContentResolver().update(url, utilEntity.serialize(ta), null, null);
					toast("更新记录数 : " + " " + row);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case R.id.id_2:
			// 删除
			url = Uri.withAppendedPath(url, String.valueOf(id));
			getContentResolver().delete(url, null, null);
			break;
		case R.id.id_3:
			// 批量操作
			break;
		case R.id.id_4:

			break;
		}
		return true;
	}

	//
	class AddColumnRunnable implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				try {
					Thread.sleep(Math.round(1000) + 500);
				} catch (InterruptedException e1) {}
				Thread thread = Thread.currentThread();
				//
				TestAccount account = new TestAccount();
				account.account = "Account Type i=" + i + " threadid=" + thread.getId();
				account.password = "Account Password i=" + i;
				//
				ContentValues values = utilEntity.serialize(account);
				// getContentResolver().insert(Uri.parse(TestAccountContentProvider.CONTENT_URI), values);

				Uri uri = UtilSQLContentProviderAnnotation.getContentURI(TestAccount.class, dataBaseName);
				getContentResolver().insert(uri, values);
			}
		}
	};

	class BaseCursorAdapter extends CursorAdapter {

		@SuppressWarnings("deprecation")
		public BaseCursorAdapter(Context context, Cursor c) {
			super(context, c);
		}

		public BaseCursorAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public BaseCursorAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
		}

		// 创建View
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			return inflater.inflate(R.layout.item_listview_activity_cursoradapter, parent, false);
		}

		// 设置Item的数据
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			try {
				TestAccount account = utilEntity.deSerialize(cursor);
				TextView textAccount = (TextView) view.findViewById(R.id.id_0);
				TextView textPassword = (TextView) view.findViewById(R.id.id_1);
				textAccount.setText(account.account);
				textPassword.setText(account.password);
				view.setTag(R.id.id_0, account.id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@SQLContentProvider(authorities = "com.gtr.studyproject.activity.AnnotationProvider", contentPath = "AnnotationAccount")
	@SQLEntity
	@SQLTable(databaseName = "xiaotian_cursor_adapter.db", databaseType = DatabaseNameType.DYNAMIC, name = "TestAccount", version = 1)
	public static class TestAccount {
		@SQLId
		Integer id;
		@SQLColumn(name = "Account")
		String account;
		@SQLColumn(name = "Password")
		String password;

		public TestAccount() {}
	}

	public static class TestAccountContentProvider extends ContentProvider {
		public static final String TABLE = "TestAccount";
		public static final String AUTHORITY = "com.gtr.studyproject.activity.TestAccountContentProvider";
		public static final String CONTENT_URI = "content://" + AUTHORITY + "/" + TABLE;
		public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		public static final Map<String, String> projectionMap = new HashMap<String, String>();
		SQLDataBaseHelper dbHelper;
		static {
			sUriMatcher.addURI(AUTHORITY, TABLE, 0X001); // 查询Table集合
			sUriMatcher.addURI(AUTHORITY, TABLE + "/#", 0X002);// 查询一条记录
			projectionMap.put("_id", "_id");
			projectionMap.put("Account", "Account");
			projectionMap.put("Password", "Password");
		}

		@Override
		public boolean onCreate() {
			dbHelper = new SQLDataBaseHelper(getContext(), TestAccount.class);
			return true;
		}

		// 查询
		@Override
		public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			switch (sUriMatcher.match(uri)) {
			case 0x001:
				qb.setTables(TABLE);
				qb.setProjectionMap(projectionMap);
				break;
			case 0x002:
				qb.setTables(TABLE);
				qb.setProjectionMap(projectionMap);
				qb.appendWhere("_id=" + uri.getPathSegments().get(1)); // 获取参数/#
				break;
			}
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
			c.setNotificationUri(getContext().getContentResolver(), uri); // 查询Notify用于CursorAdapter的NotifyDataSetChange
			return c;
		}

		@Override
		public String getType(Uri uri) {
			return null;
		}

		// 插入
		@Override
		public Uri insert(Uri uri, ContentValues values) {
			SQLiteDatabase db = dbHelper.getWritableDatabase(); // 不用打开open,不产生锁数据库问题
			long rowId = db.insert(TABLE, null, values);
			// 绑定的CursorAdapter会接受到Notify消息,自动更新Adapter
			getContext().getContentResolver().notifyChange(Uri.withAppendedPath(uri, String.valueOf(rowId)), null); // NotifyDataSetChange
			return uri;
		}

		@Override
		public int delete(Uri uri, String selection, String[] selectionArgs) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			int rows = db.delete(TABLE, null, null);
			getContext().getContentResolver().notifyChange(uri, null);
			return rows;
		}

		@Override
		public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

			return 0;
		}
	}

	public static class TestAccountAnnotationContentProvider extends SQLTableContentProvider<TestAccount> {

	}
}
