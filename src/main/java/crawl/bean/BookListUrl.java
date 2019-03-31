package crawl.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 书的列表页
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "book_list_url")
public class BookListUrl {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String url;// url
	private Boolean isCrawled;// 是否爬过了
	private Boolean isSucceed;// 是否成功
	private Date createDate;// 创建时间
	private Date crawlDate;// 爬取时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getIsCrawled() {
		return isCrawled;
	}

	public void setIsCrawled(Boolean isCrawled) {
		this.isCrawled = isCrawled;
	}

	public Boolean getIsSucceed() {
		return isSucceed;
	}

	public void setIsSucceed(Boolean isSucceed) {
		this.isSucceed = isSucceed;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCrawlDate() {
		return crawlDate;
	}

	public void setCrawlDate(Date crawlDate) {
		this.crawlDate = crawlDate;
	}

}