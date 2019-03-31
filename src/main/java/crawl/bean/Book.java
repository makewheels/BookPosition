package crawl.bean;

/**
 * 书
 * 
 * @author Administrator
 *
 */
public class Book {
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
	private String barCode;// 条码号

	public Book() {
		super();
	}

	public Book(String no, String isbn, String coverImageUrl, String name, String bookrecno, String author,
			String publisher, String publishDate, String type, String callno, String barCode) {
		super();
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
		this.barCode = barCode;
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

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	@Override
	public String toString() {
		return "Book [no=" + no + ", isbn=" + isbn + ", coverImageUrl=" + coverImageUrl + ", name=" + name
				+ ", bookrecno=" + bookrecno + ", author=" + author + ", publisher=" + publisher + ", publishDate="
				+ publishDate + ", type=" + type + ", callno=" + callno + ", barCode=" + barCode + "]";
	}

}