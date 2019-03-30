package util.proxy;

/**
 * 代理IP
 * 
 * @author Administrator
 *
 */
public class ProxyIp {
	private String ip;
	private Integer port;

	public ProxyIp() {
		super();
	}

	public ProxyIp(String ip, Integer port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
