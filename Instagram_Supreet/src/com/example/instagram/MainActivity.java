package com.example.instagram;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.instagramgallery.network.WebInterface;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity {

	private JSONObject imageData;
	private GridView gridView;
	private static int TILE_WIDTH = 220;
	int number = 0;
	RequestImagesTask request;
	Context contexto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.image_grid_view);

		request = new RequestImagesTask(
				"https://api.instagram.com/v1/tags/selfie/media/recent?access_token=45793650.f59def8.22bb0e16b35946ab91918843ac320b91",
				this);
		request.execute();

		contexto = this;

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		gridView.setNumColumns(metrics.widthPixels / TILE_WIDTH);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				Intent i = new Intent(MainActivity.this, ImageActivity.class);

				try {

					String url = imageData.getJSONArray("data")
							.getJSONObject(position).getJSONObject("images")
							.getJSONObject("standard_resolution")
							.getString("url");
					i.putExtra("url", url);
				} catch (JSONException e) {
					i.putExtra("url", "");
				}

				startActivity(i);
			}
		});
		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

	}

	private class RequestImagesTask extends AsyncTask<Void, Void, Void> {
		private String url;
		private Context c;

		public RequestImagesTask(String url, Context c) {
			super();
			this.url = url;
			this.c = c;
		}

		@Override
		protected Void doInBackground(Void... params) {
			imageData = WebInterface.requestWebService(url);
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			gridView.setAdapter(new ImageStreamAdapter(c, imageData, number));
		}

	}

}