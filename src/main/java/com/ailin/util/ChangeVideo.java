package com.ailin.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * java实现视频格式的转化
 * @author liuyazhuang
 *
 */
public class ChangeVideo {
	public static void main(String[] args) {
		ChangeVideo.convert("F:\\video\\aa.mp4", "F:\\video\\cc.avi");
	}
 
	/**
	 * @param inputFile:需要转换的视频
	 * @param outputFile：转换后的视频w
	 * @return
	 */
	public static boolean convert(String inputFile, String outputFile) {
		if (!checkFile(inputFile)) {
			System.out.println(inputFile + " is not file");
			return false;
		}
		if (process(inputFile, outputFile)) {
			System.out.println("ok");
			return true;
		}
		return false;
	}
 
	// 检查文件是否存在
	private static boolean checkFile(String path) {
		File file = new File(path);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}
 
	/**
	 * @param inputFile
	 * @param outputFile
	 * @return
	 * 转换视频文件
	 */
	private static boolean process(String inputFile, String outputFile) {
		int type = checkContentType(inputFile);
		boolean status = false;
		if (type == 0) {
			status = processFLV(inputFile, outputFile);// 直接将文件转为flv文件
		} else if (type == 1) {
			String avifilepath = processAVI(type, inputFile);
			if (avifilepath == null) {
				return false;// avi文件没有得到
			}
			status = processFLV(avifilepath, outputFile);// 将avi转为flv
		}
		return status;
	}
 
	private static int checkContentType(String inputFile) {
		String type = inputFile.substring(inputFile.lastIndexOf(".") + 1,
				inputFile.length()).toLowerCase();
		// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
		if ("avi".equals(type)) {
			return 0;
		} else if ("mpg".equals(type)) {
			return 0;
		} else if ("wmv".equals(type)) {
			return 0;
		} else if ("3gp".equals(type)) {
			return 0;
		} else if ("mov".equals(type)) {
			return 0;
		} else if ("mp4".equals(type)) {
			return 0;
		} else if ("asf".equals(type)) {
			return 0;
		} else if ("asx".equals(type)) {
			return 0;
		} else if ("flv".equals(type)) {
			return 0;
		}
		// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
		// 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
		else if ("wmv9".equals(type)) {
			return 1;
		} else if ("rm".equals(type)) {
			return 1;
		} else if ("rmvb".equals(type)) {
			return 1;
		}
		return 9;
	}
	// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）直接转换为目标视频
	private static boolean processFLV(String inputFile, String outputFile) {
		if (!checkFile(inputFile)) {
			System.out.println(inputFile + " is not file");
			return false;
		}
		List<String> commend = new ArrayList<>();
		
		commend.add(Constants.ffmpegPath);
		commend.add("-i");
		commend.add(inputFile);
		commend.add("-ab");
		commend.add("128");
		commend.add("-acodec");
		commend.add("libmp3lame");
		commend.add("-ac");
		commend.add("1");
		commend.add("-ar");
		commend.add("22050");
		commend.add("-r");
		commend.add("29.97");
		//高品质 
		commend.add("-qscale");
		commend.add("6");
		//低品质
//		commend.add("-b");
//		commend.add("512");
		commend.add("-y");
		
		commend.add(outputFile);
		StringBuffer test = new StringBuffer();
		for (int i = 0; i < commend.size(); i++) {
			test.append(commend.get(i)).append(" ");
		}
 
		System.out.println(test);
 
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
	// 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
	private static String processAVI(int type, String inputFile) {
		File file = new File(Constants.avifilepath);
		if (file.exists())
			file.delete();
		List<String> commend = new ArrayList<String>();
		commend.add(Constants.mencoderPath);
		commend.add(inputFile);
		commend.add("-oac");
		commend.add("mp3lame");
		commend.add("-lameopts");
		commend.add("preset=64");
		commend.add("-ovc");
		commend.add("xvid");
		commend.add("-xvidencopts");
		commend.add("bitrate=600");
		commend.add("-of");
		commend.add("avi");
		commend.add("-o");
		commend.add(Constants.avifilepath);
		StringBuffer test = new StringBuffer();
		for (int i = 0; i < commend.size(); i++) {
			test.append(commend.get(i) + " ");
		}
 
		System.out.println(test);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			Process p = builder.start();
 
			final InputStream is1 = p.getInputStream();
			final InputStream is2 = p.getErrorStream();
			new Thread(() -> {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is1));
				try {
					String lineB = null;
					while ((lineB = br.readLine()) != null) {
						if (lineB != null)
							System.out.println(lineB);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
			new Thread(() -> {
				BufferedReader br2 = new BufferedReader(
						new InputStreamReader(is2));
				try {
					String lineC = null;
					while ((lineC = br2.readLine()) != null) {
						if (lineC != null)
							System.out.println(lineC);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
 
			// 等Mencoder进程转换结束，再调用ffmepg进程
			p.waitFor();
			System.out.println("who cares");
			return Constants.avifilepath;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
}