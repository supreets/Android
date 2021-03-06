package com.example.instagram;

import com.example.instagramgallery.network.BitmapDownloaderTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageActivity extends Activity {

	ZoomImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image);

		
		Intent i = getIntent();
		String url = i.getExtras().getString("url");

		if (url.length() > 0) {

			imageView = (ZoomImageView) findViewById(R.id.full_image_view);

			
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
			if (!task.searchCache(url))
				task.execute(url);

		} else {

			

		}
	}
}
