package com.chang.news.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache {

	// 缓存类
	private static LruCache<String, Bitmap> mLruCache;
	private static DiskLruCache mDiskLruCache;

	String DISK_CACHE_DIR = "volley_image";
	final int RAM_CACHE_SIZE = 10 * 1024 * 1024;
	final long DISK_MAX_SIZE = 20 * 1024 * 1024;

	public BitmapCache() {
		mLruCache = new LruCache<String, Bitmap>(RAM_CACHE_SIZE) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
		File cacheDir = new File(Environment.getExternalStorageDirectory(),
				DISK_CACHE_DIR);
		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}
		try {
			mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1, DISK_MAX_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Bitmap getBitmap(String url) {
		String key = MD5Utils.hashKeyForDisk(url);
		Bitmap bitmap = mLruCache.get(url);
		if (bitmap == null) {
			bitmap = getBitmapFromDiskLruCache(key);
			// 从磁盘读出后，放入内存
			if (bitmap != null) {
				mLruCache.put(key, bitmap);
			}
		}
		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		String key = MD5Utils.hashKeyForDisk(url);
		mLruCache.put(url, bitmap);
		putBitmapToDiskLruCache(key, bitmap);
	}

	private void putBitmapToDiskLruCache(String key, Bitmap bitmap) {
		try {
			DiskLruCache.Editor editor = mDiskLruCache.edit(key);
			if (editor != null) {
				OutputStream outputStream = editor.newOutputStream(0);
				bitmap.compress(CompressFormat.JPEG, 30, outputStream);
				editor.commit();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Bitmap getBitmapFromDiskLruCache(String key) {
		try {
			DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
			if (snapshot != null) {
				InputStream inputStream = snapshot.getInputStream(0);
				if (inputStream != null) {
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					inputStream.close();
					return bitmap;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
