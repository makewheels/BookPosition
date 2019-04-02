package crawl.position.bean;

public class Position {
	private String code;
	private String belong;
	private String detail;

	public Position() {
		super();
	}

	public Position(String code, String belong, String detail) {
		super();
		this.code = code;
		this.belong = belong;
		this.detail = detail;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "Position [code=" + code + ", belong=" + belong + ", detail=" + detail + "]";
	}

}
