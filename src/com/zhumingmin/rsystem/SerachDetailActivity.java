package com.zhumingmin.rsystem;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.minxing.util.News;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.jersey.core.util.Base64;

public class SerachDetailActivity extends Activity {
	private ImageButton jiafen, koufen;
	private TextView title, category, readnumber, tuijian, butuijian, body;
	private LinearLayout ly_fanhui;
	private static final String SERVICE_URL = "http://192.168.191.1:8080/RestWebServiceDemo/rest/news";
	String picturepath = null;
	private static final String TAG = "SerachDetailActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);

		title = (TextView) findViewById(R.id.title);
		category = (TextView) findViewById(R.id.category);
		readnumber = (TextView) findViewById(R.id.readnumber);
		tuijian = (TextView) findViewById(R.id.tuijiannumber);
		butuijian = (TextView) findViewById(R.id.butuijiannumber);
		body = (TextView) findViewById(R.id.body);
		jiafen = (ImageButton) findViewById(R.id.jiafen);
		koufen = (ImageButton) findViewById(R.id.koufen);
		ly_fanhui = (LinearLayout) findViewById(R.id.ly_xiangqing);
		ly_fanhui.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		Intent intent = getIntent();
		if (intent != null) {
			String sampleURL = SERVICE_URL + "/1";
			WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK,
					SerachDetailActivity.this, "正在加载，请稍候...");
			wst.execute(new String[] { sampleURL });
		}
		intent.getExtras();
		Bundle data = intent.getExtras();
		int position = data.getInt("news_id");
		News news = SerachListActivity.newslist.get(position);
		title.setText(news.getBiaoTi());
		category.setText(news.getLeiBie());
		readnumber.setText(String.valueOf(Integer.parseInt(news
				.getYueDuLiang2()) + 1));
		body.setText(news.getBody());

		jiafen.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				jiafen.setBackgroundResource(R.drawable.like_pressed);
				DisplayToast("感谢您的反馈！");
				int a = Integer.parseInt(tuijian.getText().toString());
				a = a + 1;
				tuijian.setText(String.valueOf(a));
				// 这里后续的操作是上传阅读量，推荐数，不推荐数至后台

			}
		});
		koufen.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				koufen.setBackgroundResource(R.drawable.unlike_pressed);
				DisplayToast("感谢您的反馈！");
				int a = Integer.parseInt(butuijian.getText().toString());
				a = a + 1;
				butuijian.setText(String.valueOf(a));
			}
		});
	}

	public void handleResponse(String response) {
		final ImageView imageViewOne = (ImageView) findViewById(R.id.news_detail_iv);
		try {

			JSONObject jso = new JSONObject(response);

			String biaoti = jso.getString("title");
			String neirong = jso.optString("body");

			title.setText(biaoti);
			body.setText(neirong);
			/*
			 * 形式一：获取后台传来的图片
			 */
			final String tupian = jso.optString("picturepath");
			if (tupian == null) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						final Bitmap bitmap = getHttpBitmap("http://img.tvb.com/inews_web/web/generic_thumbnail.jpg");
						imageViewOne.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								imageViewOne.setImageBitmap(bitmap);
							}
						});
					}
				}).start();
			} else {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						final Bitmap bi = getBitmap(tupian);// 这里调用的是后台传来的图片的base64字符串编码
						imageViewOne.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								imageViewOne.setImageBitmap(bi);
							}
						});
					}
				}).start();

			}
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
		}

	}

	/**
	 * 获取网落图片资源
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 这句可有可无，没有影响
			// conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/*
	 * 解码后台传过来的已经编码的图片
	 */
	public Bitmap getBitmap(String iconBase64) {
		if (iconBase64 == null)
			return null;
		try {
			// Base64解码
			BASE64Decoder decoder = new BASE64Decoder();
			iconBase64 = iconBase64.replace("data:image/jpeg;base64,", "");

			byte[] b = decoder.decodeBuffer(iconBase64);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					// 调整异常数据
					b[i] += 256;
				}
			}
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} catch (Exception e) {
			return null;
		}
		// byte[] bitmapArray;
		// bitmapArray = Base64.decode(iconBase64);
		// return BitmapFactory
		// .decodeByteArray(bitmapArray, 0, bitmapArray.length);
	}

	// 主要操作部分
	private class WebServiceTask extends AsyncTask<String, Integer, String> {

		public static final int POST_TASK = 1;
		public static final int GET_TASK = 2;

		private static final String TAG = "WebServiceTask";

		// connection timeout, in milliseconds (waiting to connect)
		private static final int CONN_TIMEOUT = 3000;

		// socket timeout, in milliseconds (waiting for data)
		private static final int SOCKET_TIMEOUT = 5000;

		private int taskType = GET_TASK;
		private Context mContext = null;
		private String processMessage = "Processing...";

		private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		private ProgressDialog pDlg = null;

		public WebServiceTask(int taskType, Context mContext,
				String processMessage) {

			this.taskType = taskType;
			this.mContext = mContext;
			this.processMessage = processMessage;
		}

		public void addNameValuePair(String name, String value) {

			params.add(new BasicNameValuePair(name, value));
		}

		@SuppressWarnings("deprecation")
		private void showProgressDialog() {

			pDlg = new ProgressDialog(mContext);
			pDlg.setMessage(processMessage);
			pDlg.setProgressDrawable(mContext.getWallpaper());
			pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDlg.setCancelable(false);
			pDlg.show();

		}

		@Override
		protected void onPreExecute() {

			showProgressDialog();

		}

		protected String doInBackground(String... urls) {

			String url = urls[0];
			String result = "";

			HttpResponse response = doResponse(url);

			if (response == null) {
				return result;
			} else {

				try {

					result = inputStreamToString(response.getEntity()
							.getContent());

				} catch (IllegalStateException e) {
					Log.e(TAG, e.getLocalizedMessage(), e);

				} catch (IOException e) {
					Log.e(TAG, e.getLocalizedMessage(), e);
				}

			}

			return result;
		}

		@Override
		protected void onPostExecute(String response) {

			handleResponse(response);
			if (response != null) {
				Toast.makeText(getApplicationContext(), "查询成功！", 0).show();

			} else {
				Toast.makeText(getApplicationContext(), "查询失败！", 0).show();
			}
			pDlg.dismiss();
			// System.out.println("输出"+response);

		}

		// Establish connection and socket (data retrieval) timeouts
		private HttpParams getHttpParams() {

			HttpParams htpp = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
			HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);

			return htpp;
		}

		private HttpResponse doResponse(String url) {

			// Use our connection and data timeouts as parameters for our
			// DefaultHttpClient
			HttpClient httpclient = new DefaultHttpClient(getHttpParams());

			HttpResponse response = null;

			try {
				switch (taskType) {

				case POST_TASK:
					HttpPost httppost = new HttpPost(url);
					// Add parameters
					httppost.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					response = httpclient.execute(httppost);
					break;
				case GET_TASK:
					HttpGet httpget = new HttpGet(url);
					response = httpclient.execute(httpget);
					break;
				}
			} catch (Exception e) {

				Log.e(TAG, e.getLocalizedMessage(), e);

			}

			return response;
		}

		private String inputStreamToString(InputStream is) {

			String line = "";
			StringBuilder total = new StringBuilder();

			// Wrap a BufferedReader around the InputStream
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				// Read response until the end
				while ((line = rd.readLine()) != null) {
					total.append(line);
				}
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
			}

			// Return full string
			return total.toString();
		}

	}

	private void DisplayToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}
}
