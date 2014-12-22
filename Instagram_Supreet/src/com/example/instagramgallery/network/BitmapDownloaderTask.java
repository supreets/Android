package com.example.instagramgallery.network;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {	
	private static HashMap<String, Bitmap> cache;

	private final WeakReference<ImageView> imageViewReference;

	private String url;

	
	public BitmapDownloaderTask(ImageView imageView) {
		imageViewReference = new WeakReference<ImageView>(imageView);

				if (cache == null) {
			cache = new HashMap<String, Bitmap>();
		}
	}

	
	protected Bitmap doInBackground(String... params) {
		url = params[0];

		
		if (cache.containsKey(url)) {
			return cache.get(url);
		}

		
		return WebInterface.downloadBitmap(url);
	}

	@Override
	
	protected void onPostExecute(Bitmap bitmap) { 
		
		cache.put(url, bitmap); 
		
		if (isCancelled()) {
			return;
		}
		
		
		
		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {          	

				
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	public boolean searchCache(String url) {
		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {          	

								if (cache.containsKey(url)) {

					
					imageView.setImageBitmap(cache.get(url));

					return true;
				}
			}
		}
		
		return false;
	}
	
}