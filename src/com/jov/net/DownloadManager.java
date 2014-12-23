package com.jov.net;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.jov.bean.BlogBean;
import com.jov.db.DBHelper;
import com.jov.util.Common;
import com.jov.util.Constants;

public class DownloadManager {
	private Context context;
	private DBHelper db;

	public DownloadManager(Context context) {
		this.context = context;

	}

	public void doLoadThread() {
		if (!Common.isNetworkConnected(context)) {
			Toast.makeText(context, "����δ�������޷�����", Toast.LENGTH_SHORT).show();
			return;
		} else {
			db = new DBHelper(context);
			Toast.makeText(context, "��ʼ���沩��", Toast.LENGTH_SHORT).show();
			ThreadPoolUtils.execute(new HTMLParser(csdnHand,
					Constants.CSDN_BLOG, true));
			ThreadPoolUtils.execute(new HTMLParser(cnHand, Constants.CN_BLOGS,
					true));
			ThreadPoolUtils.execute(new HTMLParser(iteyeHand,
					Constants.ITEYE_BLOG, true));
			ThreadPoolUtils.execute(new HTMLParser(oscHand,
					Constants.OSCHINA_BLOG, true));
		}
	}

	private Handler csdnHand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 200:
				List<BlogBean> result = (List<BlogBean>) msg.obj;
				if (result != null) {
					// ֻ����һ������
					db.deleteAll(false, Constants.CSDN_FLAG_2);
					doInsert(result);
					Toast.makeText(context, "CSDN���������", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				Toast.makeText(context, "CSDN���߻���ʧ��", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};
	private Handler cnHand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 200:
				List<BlogBean> result = (List<BlogBean>) msg.obj;
				if (result != null) {
					db.deleteAll(false, Constants.CNBLOG_FLAG_3);
					doInsert(result);
					Toast.makeText(context, "����԰���������", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				Toast.makeText(context, "����԰���߻���ʧ��", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	private Handler iteyeHand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 200:
				List<BlogBean> result = (List<BlogBean>) msg.obj;
				if (result != null) {
					db.deleteAll(false, Constants.ITEYE_FLAG_4);
					doInsert(result);
					Toast.makeText(context, "ITEYE���������", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				Toast.makeText(context, "ITEYE���߻���ʧ��", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};
	private Handler oscHand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 200:
				List<BlogBean> result = (List<BlogBean>) msg.obj;
				if (result != null) {
					db.deleteAll(false, Constants.OSCHINA_FLAG_5);
					doInsert(result);
					Toast.makeText(context, "��Դ�й����������", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				Toast.makeText(context, "��Դ�й����߻���ʧ��", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};

	private void doInsert(List<BlogBean> list) {
		for (BlogBean bean : list) {
			db.insertBlog(bean);
		}
	}
}
