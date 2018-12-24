/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.core.boot.file;

import org.springblade.core.tool.constant.SystemConstant;
import org.springblade.core.tool.date.DateUtil;
import org.springblade.core.tool.utils.ImageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

public class BladeFileProxyFactory implements IFileProxy {

	@Override
	public File rename(File f, String path) {
		File dest = new File(path);
		f.renameTo(dest);
		return dest;
	}

	@Override
	public String [] path(File f, String dir) {
		//避免网络延迟导致时间不同步
		long time = System.nanoTime();
		
		StringBuilder uploadPath = new StringBuilder()
		.append(getFileDir(dir, SystemConstant.me().getUploadRealPath()))
		.append(time)
		.append(getFileExt(f.getName()));
		
		StringBuilder virtualPath = new StringBuilder()
		.append(getFileDir(dir, SystemConstant.me().getUploadCtxPath()))
		.append(time)
		.append(getFileExt(f.getName()));
		
		return new String [] {BladeFileUtil.formatUrl(uploadPath.toString()), BladeFileUtil.formatUrl(virtualPath.toString())};
	}
	
	/**
	 * 获取文件后缀
	 */
	public static String getFileExt(String fileName) {
		if (fileName.indexOf(".") == -1)
			return ".jpg";
		else 
			return fileName.substring(fileName.lastIndexOf('.'), fileName.length());
	}

	/**
	 * 获取文件保存地址
	 * @param saveDir
	 * @return
	 */
	public static String getFileDir(String dir, String saveDir) {
		StringBuilder newFileDir = new StringBuilder();
		newFileDir.append(saveDir)
				.append(File.separator).append(dir).append(File.separator).append(DateUtil.format(new Date(), "yyyyMMdd"))
				.append(File.separator);
		return newFileDir.toString();
	}


	/**
	 * 图片压缩
	 * @param path 文件地址
	 * @return
	 */
	public void compress(String path) {
		try {
            ImageUtil.zoomScale(ImageUtil.readImage(path), new FileOutputStream(new File(path)), null, SystemConstant.me().getCompressScale(), SystemConstant.me().isCompressFlag());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
