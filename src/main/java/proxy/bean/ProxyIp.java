package proxy.bean;

/**
 * 代理IP
 * 
 * @author Administrator
 *
 */
public class ProxyIp {
	private String ip;// ip
	private Integer port;// 端口
	private String address;// 地址
	private Boolean isAnonymous;// 是否匿名
	private String type;// 类型：http或https
	private Long speed;// 速度
	private Long connectTime;// 连接时间
	private Long aliveTime;// 存活时间
	private Long validateTime;// 验证时间

	public ProxyIp() {
		super();
	}

	public ProxyIp(String ip, Integer port, String address, Boolean isAnonymous, String type, Long speed,
			Long connectTime, Long aliveTime, Long validateTime) {
		super();
		this.ip = ip;
		this.port = port;
		this.address = address;
		this.isAnonymous = isAnonymous;
		this.type = type;
		this.speed = speed;
		this.connectTime = connectTime;
		this.aliveTime = aliveTime;
		this.validateTime = validateTime;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getIsAnonymous() {
		return isAnonymous;
	}

	public void setIsAnonymous(Boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSpeed() {
		return speed;
	}

	public void setSpeed(Long speed) {
		this.speed = speed;
	}

	public Long getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(Long connectTime) {
		this.connectTime = connectTime;
	}

	public Long getAliveTime() {
		return aliveTime;
	}

	public void setAliveTime(Long aliveTime) {
		this.aliveTime = aliveTime;
	}

	public Long getValidateTime() {
		return validateTime;
	}

	public void setValidateTime(Long validateTime) {
		this.validateTime = validateTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "ProxyIp [ip=" + ip + ", port=" + port + ", address=" + address + ", isAnonymous=" + isAnonymous
				+ ", type=" + type + ", speed=" + speed + ", connectTime=" + connectTime + ", aliveTime=" + aliveTime
				+ ", validateTime=" + validateTime + "]";
	}

}
