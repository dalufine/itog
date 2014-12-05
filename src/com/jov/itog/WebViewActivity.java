package com.jov.itog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.Toast;

import com.jov.bean.BlogBean;
import com.jov.db.DBHelper;
import com.jov.net.HTMLGetThread;
import com.jov.net.ThreadPoolUtils;
import com.jov.util.Common;
import com.jov.util.StringUtil;

public class WebViewActivity extends Activity {
	private WebView myWebView;
	private Intent intent;
	private View progress_bar;
	private DBHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_content);

		myWebView = (WebView) findViewById(R.id.webview_content);
		progress_bar = (View) findViewById(R.id.progress_bar);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		int scale = dm.densityDpi;
		if (scale == 240) { //
			myWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		} else if (scale == 160) {
			myWebView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		} else {
			myWebView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		}
		intent = getIntent();
		initView();
	}

	private void initView() {
		if (!Common.isNetworkConnected(this)) {
			Toast.makeText(WebViewActivity.this, "�ף�ľ������Ŷ����", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		}
		String url = intent.getStringExtra("url");
		if (StringUtil.isEmpty(url)) {
			finish();
			return;
		}
		ThreadPoolUtils.execute(new HTMLGetThread(resourceHand, url));
	}

	private Handler resourceHand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 200:
				String result = (String) msg.obj;
				if (result != null) {
					Log.v("html", result);
					myWebView.loadDataWithBaseURL(null, result, "text/html",
							"utf-8", null);
					progress_bar.setVisibility(View.GONE);
					return;
				}
				Toast.makeText(WebViewActivity.this, "Sorry��û�н���������",
						Toast.LENGTH_SHORT).show();
				progress_bar.setVisibility(View.GONE);
				break;
			default:
				Toast.makeText(WebViewActivity.this, "��������쳣��Sorry������ʧ���ˣ�",
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
		};
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detail_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_addfav) {
			if (intent != null) {
				db = new DBHelper(this);
				BlogBean bean = new BlogBean();
				bean.setAuthor(intent.getStringExtra("author"));
				bean.setComment(intent.getStringExtra("comment"));
				bean.setRead(intent.getStringExtra("read"));
				bean.setDate(intent.getStringExtra("date"));
				bean.setLink(intent.getStringExtra("url"));
				bean.setShortDesc(intent.getStringExtra("shortDesc"));
				bean.setSortType(intent.getStringExtra("sortType"));
				bean.setTitle(intent.getStringExtra("title"));
				db.insertBlog(bean);
				Toast.makeText(WebViewActivity.this, "���ղ�", Toast.LENGTH_SHORT)
						.show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
