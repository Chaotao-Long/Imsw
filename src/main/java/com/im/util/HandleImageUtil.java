package com.im.util;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 图片处理工具类，包括：压缩、翻转、缩放、切割等
 * 
 * @author Kevin-
 *
 */

public class HandleImageUtil {

	/**
	 * 处理图片，获取更低比例的预览图
	 * 
	 * @param originFile
	 *            原始图片文件
	 * @param preViewFile
	 *            预览图文件
	 */
	public static void scalingImg(File originFile, File preViewFile) {

		try {
			Thumbnails.of(originFile).size(100, 100).toFile(preViewFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
