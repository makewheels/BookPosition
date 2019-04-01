package crawl.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 一种书
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String no;// 在二级分类下的序号
	private String isbn;// isbn
	private String coverImageUrl;// 封面图片url
	private String name;// 书名
	private String bookrecno;// 书的id
	private String author;// 作者
	private String publisher;// 出版社
	private String publishDate;// 出版日期
	private String type;// 文献类型
	private String callno;// 索书号
	private String holdingJson;// 发请求查到的holdingJson
	private Date createDate;
	private String fromUrl;

	public Book() {
		super();
	}

	public Book(Integer id, String no, String isbn, String coverImageUrl, String name, String bookrecno, String author,
			String publisher, String publishDate, String type, String callno, String holdingJson, Date createDate,
			String fromUrl) {
		super();
		this.id = id;
		this.no = no;
		this.isbn = isbn;
		this.coverImageUrl = coverImageUrl;
		this.name = name;
		this.bookrecno = bookrecno;
		this.author = author;
		this.publisher = publisher;
		this.publishDate = publishDate;
		this.type = type;
		this.callno = callno;
		this.holdingJson = holdingJson;
		this.createDate = createDate;
		this.fromUrl = fromUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getCoverImageUrl() {
		return coverImageUrl;
	}

	public void setCoverImageUrl(String coverImageUrl) {
		this.coverImageUrl = coverImageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBookrecno() {
		return bookrecno;
	}

	public void setBookrecno(String bookrecno) {
		this.bookrecno = bookrecno;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCallno() {
		return callno;
	}

	public void setCallno(String callno) {
		this.callno = callno;
	}

	public String getHoldingJson() {
		return holdingJson;
	}

	public void setHoldingJson(String holdingJson) {
		this.holdingJson = holdingJson;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getFromUrl() {
		return fromUrl;
	}

	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", no=" + no + ", isbn=" + isbn + ", coverImageUrl=" + coverImageUrl + ", name="
				+ name + ", bookrecno=" + bookrecno + ", author=" + author + ", publisher=" + publisher
				+ ", publishDate=" + publishDate + ", type=" + type + ", callno=" + callno + ", holdingJson="
				+ holdingJson + ", createDate=" + createDate + ", fromUrl=" + fromUrl + "]";
	}

}