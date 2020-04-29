package com.git.reny.lib_base.base.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.git.reny.lib_base.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

public class FileUtils {

	private static final String SEPARATOR = File.separator;//路径分隔符


	/**
	 * 检查设备是否存在SDCard的工具方法
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
        // 有存储的SDCard
        return state.equals(Environment.MEDIA_MOUNTED);
	}
	
	/***
	 * 根据路径 删除文件(夹)
	 * @param file
	 */
	public static boolean deleteFile(File file) {
		try {
			if (!file.exists()) {
				return false;
			} else {
				if (file.isFile()) {
					file.delete();
					return true;
				}
				if (file.isDirectory()) {
					File[] childFile = file.listFiles();
					if (childFile == null || childFile.length == 0) {
						file.delete();
						return true;
					}
					for (File f : childFile) {
						deleteFile(f);
					}
					file.delete();
				}
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//刷新图库等文件系统  用来保存图片后调用
	public static void notifyFileSystemChanged(Context context, String path) {
		if (TextUtils.isEmpty(path))
			return;
		try {
			final File f = new File(path);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //添加此判断，判断SDK版本是不是4.4或者高于4.4
				String[] paths = new String[]{path};
				MediaScannerConnection.scanFile(context, paths, null, null);
			} else {
				final Intent intent;
				if (f.isDirectory()) {
					intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
					intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
					intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
					//Log.v(LOG_TAG, "directory changed, send broadcast:" + intent.toString());
				} else {
					intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					intent.setData(Uri.fromFile(f));
					//Log.v(LOG_TAG, "file changed, send broadcast:" + intent.toString());
				}
				context.sendBroadcast(intent);
			}
		}catch (Exception e){}
	}


    public static String getCachePath(String dirName) {
		if(TextUtils.isEmpty(dirName)){
			dirName = "cache";
		}
		String cachePath = null;
		if (hasSdcard()) {
			cachePath = Environment.getExternalStorageDirectory().getPath() + SEPARATOR + BuildConfig.packageName + SEPARATOR + dirName;
		} else {
			cachePath = Environment.getDataDirectory().getPath() + SEPARATOR + BuildConfig.packageName + SEPARATOR + dirName;
		}
		File file = new File(cachePath);
		if (!file.exists())
			file.mkdirs();// 创建文件夹
		return cachePath;
    }


	/***
	 * 获取文件夹大小
	 * @param file 文件
	 * @return long
	 */
	public static long getFolderSize(File file){
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++){
				if (fileList[i].isDirectory()){
					size = size + getFolderSize(fileList[i]);
				}else{
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size/1024;
		if(kiloByte < 1) {
			return size + "Byte(s)";
		}

		double megaByte = kiloByte/1024;
		if(megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
		}

		double gigaByte = megaByte/1024;
		if(gigaByte < 1) {
			BigDecimal result2  = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
		}

		double teraBytes = gigaByte/1024;
		if(teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
	}



	public static String getFileName(String path){
		int start = path.lastIndexOf("/");
		int end = path.lastIndexOf(".");
		if(start != -1 && end != -1){
			return path.substring(start + 1, end);
		}else{
			return null;
		}
	}

	/**
	 * 复制res/raw中的文件到指定目录
	 * @param context 上下文
	 * @param rawId 资源ID
	 * @param fileName 文件名
	 * @param dirPath 目标文件夹的路径
	 */
	public static void copyFilesFromRaw(Context context, int rawId, String dirPath, String fileName){
		InputStream inputStream = context.getResources().openRawResource(rawId);
		File file = new File(dirPath);
		if (!file.exists()) {//如果文件夹不存在，则创建新的文件夹
			file.mkdirs();
		}
		readInputStream(dirPath, fileName, inputStream);
	}

	/**
	 * 读取输入流中的数据写入输出流
	 *
	 * @param dirPath 目标文件路径
	 * @param inputStream 输入流
	 */
	private static void readInputStream(String dirPath, String fileName, InputStream inputStream) {
		File file = new File(dirPath, fileName);
		try {
			if (!file.exists()) {
				// 1.建立通道对象
				FileOutputStream fos = new FileOutputStream(file);
				// 2.定义存储空间
				byte[] buffer = new byte[inputStream.available()];
				// 3.开始读文件
				int lenght = 0;
				while ((lenght = inputStream.read(buffer)) != -1) {// 循环从输入流读取buffer字节
					// 将Buffer中的数据写到outputStream对象中
					fos.write(buffer, 0, lenght);
				}
				fos.flush();// 刷新缓冲区
				// 4.关闭流
				fos.close();
				inputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
