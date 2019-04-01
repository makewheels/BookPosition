package crawl.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 每一本书，条码类
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "bar_code")
public class BarCode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String bookrecno;// 书的种类，父id
	private String barCode;// 条码号
	private String position;// 图书定位
	private Date createDate;// 创建时间
	private Date getPositionDate;// 获得定位时间
	private String json;// 属于这本书的json信息

	public BarCode() {
		super();
	}

	public BarCode(Integer id, String bookrecno, String barCode, String position, Date createDate, Date getPositionDate,
			String json) {
		super();
		this.id = id;
		this.bookrecno = bookrecno;
		this.barCode = barCode;
		this.position = position;
		this.createDate = createDate;
		this.getPositionDate = getPositionDate;
		this.json = json;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBookrecno() {
		return bookrecno;
	}

	public void setBookrecno(String bookrecno) {
		this.bookrecno = bookrecno;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getGetPositionDate() {
		return getPositionDate;
	}

	public void setGetPositionDate(Date getPositionDate) {
		this.getPositionDate = getPositionDate;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	@Override
	public String toString() {
		return "BarCode [id=" + id + ", bookrecno=" + bookrecno + ", barCode=" + barCode + ", position=" + position
				+ ", createDate=" + createDate + ", getPositionDate=" + getPositionDate + ", json=" + json + "]";
	}

}
