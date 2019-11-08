package com.im.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.im.controller.UserController;
import com.im.entity.ConstantBean;
import com.im.exception.CloudServerCheckedException;
import com.im.exception.CloudServerNotFoundException;
import com.im.log4j.Logger;

public class FileUtil {

	public static Logger logger = Logger.getLogger(UserController.class);

	// 理论上用户头像根目录
	private final static String filePathUrl = "/IMfile";
	private final static String CloundfilePathUrl = "http://47.97.214.92:8080/CloudServer/IMfile";

//	/usr/local/tomcat/apache-tomcat-8.5.47/webapps/IMfile/userphoto/Mr.Long/Tomson.jpg

	/**
	 * 文件上传并返回云端url,linux下的路径为“/”,window为“\”,虽然“/”路径在window文件夹打印有错,但不影响文件生成，故统一为“/”
	 * 
	 * @param request
	 * @param file
	 * @param objectName
	 * @param type
	 * @return
	 */
	public static String fileToUrl(HttpServletRequest request, MultipartFile file, String filename, String objectName,
			String type) {

		String fileRootPath = request.getServletContext().getRealPath(filePathUrl);
		File fileRoot = new File(fileRootPath);
		// 如果文件根目录不存在，则创建该目录
		if (!fileRoot.exists()) {
			fileRoot.mkdirs();
			logger.info("create dir  /IMfile");
		}

		// 获取类型目录(用户头像userphoto，群组头像groupphoto，聊天文件chatfile)
		String fileTypePath = match(fileRootPath, type);
		// 获取对象目录
		String fileObjectPath = match(fileTypePath, objectName);
		// 如果对象参数错误，类别目录路径会置空,抛出路径异常
		if (fileObjectPath == null) {
			logger.error("404, file path not found, no username matches");
			throw new CloudServerNotFoundException(ConstantBean.FILE_PATH_NOTFOUND);
		}
		System.out.println("fileObjectPath" + fileObjectPath);

		// 获取文件对象目录
		File fileObject = new File(fileObjectPath);

		// 如果类目录不存在，则创建该目录
		if (!fileObject.exists()) {
			logger.info("create category dir");
			fileObject.mkdirs();
		}

		OutputStream out;
		InputStream in;
		String urlpath = null;
		// FileInputStream in;
		// 对每一个文件

		try {
			// 1.使用输入输出流完成文件的上传
			out = new FileOutputStream(new File(fileObjectPath + "/" + filename));
			in = file.getInputStream();
			// in = new FileInputStream(file);
			// 每次写入1024字节
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
				out.flush();
			}
			out.close();
			in.close();

			// 虽然为“/”,但window下的文件也正确生成了
			File outFile = new File(fileObjectPath + "/" + filename); // 原文件已生成,根据fileName后缀确定其文件格式（jpg或png等）
			System.out.println("outFile：" + outFile);
			urlpath = CloundfilePathUrl + "/" + type + "/" + objectName + "/" + filename;
			System.out.println("urlpath：" + urlpath);
			return urlpath;
		} catch (FileNotFoundException e) {
			throw new CloudServerCheckedException("FileNotFoundException", e);
		} catch (IOException e) {
			throw new CloudServerCheckedException("IOException", e);
		}
	}

	public static List<String> selectFileList(HttpServletRequest request, String objectName, String type) {

		String fileRootPath = request.getServletContext().getRealPath(filePathUrl);
		File fileRoot = new File(fileRootPath);
		// 如果文件根目录不存在，则创建该目录
		if (!fileRoot.exists()) {
			fileRoot.mkdirs();
			logger.info("create dir  /IMfile");
		}
		// 获取原文件路径目录
		// 获取类型目录(用户头像userphoto，群组头像groupphoto，聊天文件chatfile)
		String fileTypePath = match(fileRootPath, type);
		// 获取对象目录
		String fileObjectPath = match(fileTypePath, objectName);
		// 如果对象参数错误，类别目录路径会置空,抛出路径异常
		if (fileObjectPath == null) {
			logger.error("404, file path not found, no username matches");
			throw new CloudServerNotFoundException(ConstantBean.FILE_PATH_NOTFOUND);
		}
		System.out.println("fileObjectPath" + fileObjectPath);
		File[] fileList = orderFileByTime(fileObjectPath);
		List<String> filePathList = new ArrayList<String>();
		if (fileList != null && fileList.length != 0) {
			// 获取该类目录下的所有原文件路径(文件，文件夹及里面的文件)
			for (File afile : fileList) {
				System.out.println("afile:"+afile);
				//listFilePath(afile, filePathList);
				filePathList.add("http://47.97.214.92:8080"
						+ afile.getAbsolutePath().substring(afile.getAbsolutePath().indexOf("\\IMfile\\")));     //linux系统下要改为/IMfile/,路径均以“/”分隔
			}
			return filePathList;
		} else {
			logger.error("404, file path not found");
			throw new CloudServerNotFoundException(ConstantBean.FILE_PATH_NOTFOUND);
		}
	}

	/**
	 * 匹配相应目录 虽然为“/”,但window下的文件也正确生成了
	 * 
	 * @param fileRootPath 文件根目录/IMphoto
	 * @param username     用户名
	 * @return 子目录路径
	 */
	public static String match(String fileRootPath, String type) {
		String path = null;

		path = fileRootPath + "/" + type;

		return path;
	}

	// 根据文件名后续匹配类别
	public static String filePath(String fileName) {

		String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1); // 获取文件后缀
		String path = null;

		String[] imageSuffix = { "jpg", "png", "gif" };
		String[] videoSuffix = { "avi", "mp4", "wmv", "mpeg", "mov", "mkv", "flv", "f4v", "m4v", "rmvb", "rm", "3gp",
				"dat", "ts", "mts", "vob" };

		List<String> imagelist = Arrays.asList(imageSuffix);
		List<String> videolist = Arrays.asList(videoSuffix);

		if (imagelist.contains(fileSuffix)) {
			path = "image/";
		} else if (videolist.contains(fileSuffix)) {
			path = "video/";
		}

		return path;
	}

	/**
	 * 判断是否含有特殊字符"."
	 *
	 * true为包含，false为不包含
	 */
	public static boolean withComma(String a) {
		String regEx = ".jpg";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(a);
		return m.find();
	}

	/**
	 * 按时间给文件排序
	 * 
	 * @param filePath 文件路径
	 * @return
	 */
	public static File[] orderFileByTime(String filePath) {

		File file = new File(filePath);
		File[] files = file.listFiles();

		// 判空，如果没有文件则不进行排序
		if (files == null || files.length == 0) {
			return null;
		}

		// 排序
		Arrays.sort(files, new Comparator<File>() {

			public int compare(File f1, File f2) {
				long diff = f1.lastModified() - f2.lastModified();
				if (diff > 0)
					return -1;
				else if (diff == 0)
					return 0;
				else
					return 1;
			}

			public boolean equals(Object obj) {
				return true;
			}

		});

		return files;
	}

	/**
	 * 列举文件路径
	 * 
	 * @param file      文件对象，一般为目录对象
	 * @param filePaths 待返回的文件路径列表
	 */
	public static void listFilePath(File file, List<String> filePaths) {

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File afile : files) {
				listFilePath(afile, filePaths);
			}
		}
		// 截取路径，获取从0x开头的hash至最后（在linux服务器上需要改成斜杠/）
		
//		filePaths.add(file.getAbsolutePath());
	}

}
