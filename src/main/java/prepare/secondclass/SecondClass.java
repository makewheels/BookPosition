package prepare.secondclass;

/**
 * 二级分类对象
 * 
 * @author Administrator
 *
 */
public class SecondClass {
	private String id;// 2000
	private String parentId;// 100
	private String name;// A1
	private String categoryDesc;// A1马克思、恩格斯著作
	private Integer count;// 该二级分类下的，图书总数

	public SecondClass() {
		super();
	}

	public SecondClass(String id, String parentId, String name, String categoryDesc, Integer count) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.categoryDesc = categoryDesc;
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "SecondClass [id=" + id + ", parentId=" + parentId + ", name=" + name + ", categoryDesc=" + categoryDesc
				+ ", count=" + count + "]";
	}

}