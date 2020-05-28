package framework;



import java.util.List;

/***
 * 分页
 */
public class PageBean {

	private int page = 1;						// 当前页码
	private int pageSize = 12;					// 每页显示总记录数
	private Long total = 0l;					// 查询结果总数
	private List<Object> resultList = null;		// 查询结果

	private int startPage = 1;					// 显示的第一页页码
	private int endPage = 1;					// 显示的最后一页页码
	
	private int start = 0;						// 开始记录下标
	private int end = 0;						// 结束记录下标
	
	private int pageCount = 1;					// 总页数

	public PageBean(String page, String pageSize, String url) {
		if (null != page && !"".equals(page))
			this.page = Integer.parseInt(page);
		if (null != pageSize && !"".equals(pageSize))
			this.pageSize = Integer.parseInt(pageSize);
		else 
			this.pageSize = 12;
		this.start = (this.page - 1) * this.pageSize;
		this.end = this.page * this.pageSize;
	}

	public PageBean(String page, String pageSize) {
		if (null != page && !"".equals(page))
			this.page = Integer.parseInt(page);
		if (null != pageSize && !"".equals(pageSize))
			this.pageSize = Integer.parseInt(pageSize);
		else 
			this.pageSize = 12;
		
		this.start = (this.page - 1) * this.pageSize;
		this.end = this.page * this.pageSize;
	}

	public PageBean(String page, Integer pageSize) {
		if (null != page && !"".equals(page))
			this.page = Integer.parseInt(page);
		if (null != pageSize && !"".equals(pageSize))
			this.pageSize = pageSize;
		else 
			this.pageSize = 12;
		this.start = (this.page - 1) * this.pageSize;
		this.end = this.page * this.pageSize;
	}

	public void compute() {
		if (0 == total) {
			startPage = 0;
			endPage = 0;
			pageCount = 0;
			return;
		}
		int temp = 0;
		temp = (int)(total / pageSize);
		startPage = page > 2 ? page - 2 : 1;
		temp = total % pageSize > 0 ? temp + 1 : temp;
		endPage = (int) (temp > startPage + 4 ? startPage + 4 : temp);
		pageCount = temp;
		
		if (temp - page < 2)
			startPage = pageCount > 4 ? pageCount - 4 : 1;
	}

	public void compute(long total) {
		this.total = total;
		if (0 == total) {
			startPage = 0;
			endPage = 0;
			pageCount = 0;
			return;
		}
		int temp = 0;
		temp = (int)(total / pageSize);
		startPage = page > 2 ? page - 2 : 1;
		temp = total % pageSize > 0 ? temp + 1 : temp;
		endPage = (int) (temp > startPage + 4 ? startPage + 4 : temp);
		pageCount = temp;
		
		if (temp - page < 2)
			startPage = pageCount > 4 ? pageCount - 4 : 1;
	}
	
	/**
	 * 分页信息放在缓存中时调用
	 * @param maxPage 最大缓存页数
	 */
	public void compute(int maxPage, int currentTotal) {
		
		if (maxPage * this.pageSize <= currentTotal)
			pageCount = maxPage + 1;
		else 
			pageCount = currentTotal % this.pageSize > 0 ? currentTotal / this.pageSize + 1 : this.pageSize;
	}

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotal() {
		return this.total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<Object> getResultList() {
		return this.resultList;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	public int getStartPage() {
		return this.startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return this.endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public PageBean() {
	}
}