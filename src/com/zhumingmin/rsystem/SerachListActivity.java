package com.zhumingmin.rsystem;

import java.util.ArrayList;
import java.util.List;

import com.minxing.util.News;
import com.minxing.util.NewsAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
/*
 *这里需要完成的操作有：
 *1.获取某个关键词的相关标题
 *2.获取每个标题的阅读量，推荐数，不推荐
 */
public class SerachListActivity extends Activity {
	ListView listview;
	public static List<News> newslist = new ArrayList<News>();
	private LinearLayout ly_fanhui;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_listview);
		initNews();
		ly_fanhui = (LinearLayout) findViewById(R.id.ly_liebiao);
		ly_fanhui.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		NewsAdapter adapter = new NewsAdapter(SerachListActivity.this,
				R.layout.news_list_item, newslist);
		ListView listview = (ListView) findViewById(R.id.list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(view.getContext(),
						SerachDetailActivity.class);
				intent.putExtra("news_id", position);
				view.getContext().startActivity(intent);
			}
		});

	}

	private void initNews() {

		News news = new News(
				"香蕉种植中应该注意到的十种病虫害！",
				"香蕉种植",
				"1000",
				"2016年二十国集团民间社会（C20）会议5日在山东省青岛市开幕，中共中央总书记、国家主席习近平向会议发来贺信。"
						+ "\n"
						+ "习近平在贺信中对会议召开表示热烈祝贺，指出民间社会组织是各国民众参与公共事务、推动经济社会发展的重要力量。中国政府支持本国社会组织参与国内经济社会建设，也欢迎境外非政府组织来华开展友好交流合作，并努力创造良好法治环境，为其在华活动提供便利，保障其合法权益。");

		News news1 = new News(
				"香蕉种植中应该注意到的十种病虫害！",
				"香蕉种植",
				"1000",
				"2016年二十国集团民间社会（C20）会议5日在山东省青岛市开幕，中共中央总书记、国家主席习近平向会议发来贺信。"
						+ "\n"
						+ "习近平在贺信中对会议召开表示热烈祝贺，指出民间社会组织是各国民众参与公共事务、推动经济社会发展的重要力量。中国政府支持本国社会组织参与国内经济社会建设，也欢迎境外非政府组织来华开展友好交流合作，并努力创造良好法治环境，为其在华活动提供便利，保障其合法权益。");

		News news2 = new News(
				"香蕉种植中应该注意到的十种病虫害！",
				"香蕉种植",
				"1000",
				"2016年二十国集团民间社会（C20）会议5日在山东省青岛市开幕，中共中央总书记、国家主席习近平向会议发来贺信。"
						+ "\n"
						+ "习近平在贺信中对会议召开表示热烈祝贺，指出民间社会组织是各国民众参与公共事务、推动经济社会发展的重要力量。中国政府支持本国社会组织参与国内经济社会建设，也欢迎境外非政府组织来华开展友好交流合作，并努力创造良好法治环境，为其在华活动提供便利，保障其合法权益。");

		if (newslist.size() == 0) {
			newslist.add(news);
			newslist.add(news1);
			newslist.add(news2);
		}
	}
}
