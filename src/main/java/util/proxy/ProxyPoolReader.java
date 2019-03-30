package util.proxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import util.Constants;

/**
 * 从IP代理池读IP
 * 
 * @author Administrator
 *
 */
public class ProxyPoolReader {
	private static List<ProxyIp> proxyIpList;

	/**
	 * 返回代理池所有ip
	 * 
	 * @return
	 */
	public static List<ProxyIp> getProxyIpList() {
		if (proxyIpList != null) {
			return proxyIpList;
		}
		proxyIpList = new ArrayList<>();
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(new File(Constants.RESOURCES_BASE_PATH, "/proxyIpPool"), Constants.CHARSET);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String line : lines) {
			String[] split = line.split(":");
			String ip = split[0];
			int port = Integer.parseInt(split[1]);
			proxyIpList.add(new ProxyIp(ip, port));
		}
		return proxyIpList;
	}

}
