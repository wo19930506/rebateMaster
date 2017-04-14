package com.edm.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import com.edm.modules.orm.MapBean;
import com.edm.utils.consts.Const;
import com.edm.utils.except.Errors;

public class Files {

	public static void valid(String path) {
		File file = new File(path);
		if (!file.isDirectory()) {
			throw new Errors("文件路径不存在");
		}
		if (!file.canWrite()) {
			throw new Errors("文件路径没有写权限");
		}
	}
	
	public static void valid(MultipartFile file, MapBean suffMap) {
		if (file == null) {
			throw new Errors("文件不存在");
		}
		if (file.isEmpty()) {
			throw new Errors("文件内容为空");
		}
		if (!suffMap(file.getOriginalFilename(), suffMap)) {
			throw new Errors("文件后缀有误");
		}
	}
	
	public static boolean exists(String path) {
		if (new File(path).exists()) {
			return true;
		}
		return false;
	}

	private static boolean suffMap(String path, MapBean suffMap) {
		String suff = StringUtils.lowerCase(StringUtils.substringAfterLast(path, "."));
		if (!Arrays.<String> asList(suffMap.getString("file").split(",")).contains(suff)) {
			return false;
		}
		return true;
	}

	public static String get(String webRoot, String path) {
		try {
			File file = new File(webRoot + path);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				String content = IOUtils.toString(fis, "utf-8");
				fis.close();
				return content;
			}
		} catch (Exception e) {
			throw new Errors("文件不存在", e);
		}
		return null;
	}

	public static void delete(String webRoot, String filePath, String root) {
		if (StringUtils.isNotBlank(filePath) && StringUtils.startsWith(filePath, root)) {
			delete(webRoot + filePath);
		}
	}
	
	public static void delete(String path) {
		try {
			if (StringUtils.isNotBlank(path)) {
				File file = new File(path);
				if (file.exists()) {
					FileUtils.forceDelete(file);
				}
			}
		} catch (Exception e) {
			throw new Errors("文件删除失败", e);
		}
	}

	public static void make(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static String dir(int corpId, DateTime date) {
		return corpId + "/" + date.toString("yyyyMM") + "/";
	}
	
	public static String name(DateTime date, String suffix) {
		return date.toString("yyyyMMddHHmmss") + "_" + new Random().nextInt(1000) + suffix;
	}
	
	public static String prefix(String path) {
		if (StringUtils.isNotBlank(path)) {
			return StringUtils.substringBeforeLast(path, ".");
		}
		return null;
	}
	
	public static String suffix(String path) {
		if (StringUtils.isNotBlank(path)) {
			return StringUtils.lowerCase(StringUtils.substringAfterLast(path, "."));
		}
		return null;
	}

	public static String unit(double value) {
		double size = 0;
		String unit = null;

		double b = bd(value, Const.B);
		if ((b >= 0) && (b < Const.KB)) {
			size = b;
			unit = " 字节";
		} else if ((b >= Const.KB) && (b < Const.MB)) {
			size = bd(value, Const.KB);
			unit = " Kb";
		} else if ((b >= Const.MB) && (b < Const.GB)) {
			size = bd(value, Const.MB);
			unit = " Mb";
		} else if ((b >= Const.GB) && (b < Const.TB)) {
			size = bd(value, Const.GB);
			unit = " Gb";
		}
		
		return size + unit;
	}
	
	private static double bd(double value, int unit) {
		BigDecimal bd = new BigDecimal(value / unit);
		return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
    public static FileItem create(String filePath, String fileName) {
        FileItem item = new DiskFileItemFactory().createItem(null, null, false, fileName);
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(new File(filePath));
            output = item.getOutputStream();
            IOUtils.copy(input, output);
        } catch (IOException e) {
        } finally {
            try {
                if (output != null) output.close();
                if (input != null) input.close();
            } catch (IOException e) { }
        }
        return item;
    }
    
    /**
     * 获取最近非"~"开头的文件.
     */
    public static File recent(String path) {
        File root = new File(path);
        File file = null;
        if (root.exists()) {
            File[] files = root.listFiles();
            if (files != null && files.length > 0) {
                Arrays.sort(files, new RecentComparator());
                for (int i = 0, len = files.length; i < len; i++) {
                    File f = files[i];
                    String fileName = f.getName();
                    if (!StringUtils.startsWith(fileName, "~")) {
                        file = f;
                        break;
                    }
                }
            }

        }
        return file;
    }
    
    /**
	 * 文件按时间倒序.
	 */
    private static class RecentComparator implements Comparator<File> {
        public int compare(File f1, File f2) {
            long result = f1.lastModified() - f2.lastModified();
            if (result > 0)
                return -1;
            else if (result == 0)
                return 0;
            else
                return 1;
        }
    }
    
    /**
     * 创建文件
     * @param path
     * @return
     */
    public static boolean createFile(String path) {
    	File file = new File(path);
        boolean b = false;
        try {
        	b = file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return b;
    }

    
}
