package com.edm.modules.orm;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

/**
 * 涓庡叿浣揙RM瀹炵幇鏃犲叧鐨勫垎椤靛弬鏁板強鏌ヨ缁撴灉灏佽. 
 */
public class Page<T> implements Iterable<T> {
	
	// ** 鍏叡鍙橀噺 **//
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	// ** 鍒嗛〉鏌ヨ鍙傛暟 **//
	protected int pageNo = 1;
	protected int pageSize = -1;
	protected boolean autoCount = true;
	protected boolean search = false;
	protected String orderBy = null;
	protected String order = null;
	
	// ** 杩斿洖缁撴灉 **//
	protected List<T> result = Lists.newArrayList();
	protected long totalItems = -1;

	// ** 鏋勯�犲嚱鏁� **//
	public Page() {
	}

	public Page(int pageSize) {
		setPageSize(pageSize);
	}

	public Page(int pageNo, int pageSize) {
		setPageNo(pageNo);
		setPageSize(pageSize);
	}

	// ** 鍒嗛〉鍙傛暟璁块棶鍑芥暟 **//
	/**
	 * 鑾峰緱褰撳墠椤电殑椤靛彿, 搴忓彿浠�1寮�濮�, 榛樿涓�1.
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 璁剧疆褰撳墠椤电殑椤靛彿, 搴忓彿浠�1寮�濮�, 浣庝簬1鏃惰嚜鍔ㄨ皟鏁翠负1.
	 */
	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	/**
	 * 鑾峰緱姣忛〉鐨勮褰曟暟閲�, 榛樿涓�-1.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 璁剧疆姣忛〉鐨勮褰曟暟閲�.
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 鑾峰緱鎺掑簭瀛楁, 鏃犻粯璁ゅ��. 澶氫釜鎺掑簭瀛楁鏃剁敤','鍒嗛殧.
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 璁剧疆鎺掑簭瀛楁, 澶氫釜鎺掑簭瀛楁鏃剁敤','鍒嗛殧.
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 鑾峰緱鏄惁鑷姩鏌ヨ鎬昏褰曟暟.
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * 璁剧疆鏄惁鑷姩鏌ヨ鎬昏褰�.
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	/**
	 * 鑾峰緱鏄惁杈撳叆鎼滅储鏉′欢.
	 */
	public boolean isSearch() {
		return search;
	}

	/**
	 * 璁剧疆鏄惁杈撳叆鎼滅储鏉′欢.
	 */
	public void setSearch(final boolean search) {
		this.search = search;
	}

	/**
	 * 鑾峰緱鎺掑簭鏂瑰悜, 鏃犻粯璁ゅ��.
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 璁剧疆鎺掑簭鏂瑰紡鍚�.
	 * 
	 * @param order 鍙�夊�间负desc鎴朼sc, 澶氫釜鎺掑簭瀛楁鏃剁敤','鍒嗛殧.
	 */
	public void setOrder(final String order) {
		String lowcaseOrder = StringUtils.lowerCase(order);

		// 妫�鏌rder瀛楃涓茬殑鍚堟硶鍊�
		String[] orders = StringUtils.split(lowcaseOrder, ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
				throw new IllegalArgumentException("鎺掑簭鏂瑰悜" + orderStr + "涓嶆槸鍚堟硶鍊�");
			}
		}

		this.order = lowcaseOrder;
	}

	/**
	 * 鏄惁宸茶缃帓搴忓瓧娈�, 鏃犻粯璁ゅ��.
	 */
	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}

	/**
	 * 鏍规嵁pageNo鍜宲ageSize璁＄畻褰撳墠椤电涓�鏉¤褰曞湪鎬荤粨鏋滈泦涓殑浣嶇疆, 搴忓彿浠�0寮�濮�. 
	 * 鐢ㄤ簬Mysql, Hibernate.
	 */
	public int getOffset() {
		return ((pageNo - 1) * pageSize);
	}

	/**
	 * 鏍规嵁pageNo鍜宲ageSize璁＄畻褰撳墠椤电涓�鏉¤褰曞湪鎬荤粨鏋滈泦涓殑浣嶇疆, 搴忓彿浠�1寮�濮�. 
	 * 鐢ㄤ簬Oracle.
	 */
	public int getStartRow() {
		return getOffset() + 1;
	}

	/**
	 * 鏍规嵁pageNo鍜宲ageSize璁＄畻褰撳墠椤垫渶鍚庝竴鏉¤褰曞湪鎬荤粨鏋滈泦涓殑浣嶇疆, 搴忓彿浠�1寮�濮�.
	 * 鐢ㄤ簬Oracle.
	 */
	public int getEndRow() {
		return pageSize * pageNo;
	}

	// ** 璁块棶鏌ヨ缁撴灉鍑芥暟 **//

	/**
	 * 鑾峰緱椤靛唴鐨勮褰曞垪琛�.
	 */
	public List<T> getResult() {
		return result;
	}

	/**
	 * 璁剧疆椤靛唴鐨勮褰曞垪琛�.
	 */
	public void setResult(final List<T> result) {
		this.result = result;
	}

	/**
	 * 瀹炵幇Iterable鎺ュ彛, 鍙互for(Object item : page)閬嶅巻浣跨敤
	 */
	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		return result == null ? IteratorUtils.EMPTY_ITERATOR : result.iterator();
	}

	/**
	 * 鑾峰緱鎬昏褰曟暟, 榛樿鍊间负-1.
	 */
	public long getTotalItems() {
		return totalItems;
	}

	/**
	 * 璁剧疆鎬昏褰曟暟.
	 */
	public void setTotalItems(final long totalItems) {
		this.totalItems = totalItems;
	}

	/**
	 * 鏄惁鏈�鍚庝竴椤�.
	 */
	public boolean isLastPage() {
		return pageNo == getTotalPages();
	}

	/**
	 * 鏄惁杩樻湁涓嬩竴椤�.
	 */
	public boolean isHasNextPage() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * 鍙栧緱涓嬮〉鐨勯〉鍙�, 搴忓彿浠�1寮�濮�. 
	 * 褰撳墠椤典负灏鹃〉鏃朵粛杩斿洖灏鹃〉搴忓彿.
	 */
	public int getNextPage() {
		if (isHasNextPage()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	/**
	 * 鏄惁绗竴椤�.
	 */
	public boolean isFirstPage() {
		return pageNo == 1;
	}

	/**
	 * 鏄惁杩樻湁涓婁竴椤�.
	 */
	public boolean isHasPrePage() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 鍙栧緱涓婇〉鐨勯〉鍙�, 搴忓彿浠�1寮�濮�. 
	 * 褰撳墠椤典负棣栭〉鏃惰繑鍥為椤靛簭鍙�.
	 */
	public int getPrePage() {
		if (isHasPrePage()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}

	/**
	 * 鏍规嵁pageSize涓巘otalItems璁＄畻鎬婚〉鏁�, 榛樿鍊间负-1.
	 */
	public long getTotalPages() {
		if (totalItems < 0) {
			return -1;
		}

		long count = totalItems / pageSize;
		if (totalItems % pageSize > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 璁＄畻浠ュ綋鍓嶉〉涓轰腑蹇冪殑椤甸潰鍒楄〃, 濡�"棣栭〉,23,24,25,26,27,鏈〉"
	 * 
	 * @param count 闇�瑕佽绠楃殑鍒楄〃澶у皬
	 * @return pageNo鍒楄〃
	 */
	public List<Long> getSlider(int count) {
		int halfSize = count / 2;
		long totalPage = getTotalPages();

		long startPageNumber = Math.max(pageNo - halfSize, 1);
		long endPageNumber = Math.min(startPageNumber + count - 1, totalPage);

		if (endPageNumber - startPageNumber < count) {
			startPageNumber = Math.max(endPageNumber - count, 1);
		}

		List<Long> result = Lists.newArrayList();
		for (long i = startPageNumber; i <= endPageNumber; i++) {
			result.add(new Long(i));
		}
		return result;
	}
}
