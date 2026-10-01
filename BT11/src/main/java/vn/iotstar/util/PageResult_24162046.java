package vn.iotstar.util;

import java.util.List;

/**
 * Ket qua phan trang dung chung cho cac trang danh sach.
 */
public class PageResult_24162046<T> {

    private final List<T> items;
    private final int page;
    private final int size;
    private final long totalItems;

    public PageResult_24162046(List<T> items, int page, int size, long totalItems) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
    }

    /** Chuan hoa so trang (1..totalPages) truoc khi query. */
    public static int normalizePage(int page, int size, long totalItems) {
        int totalPages = totalPages(size, totalItems);
        if (page < 1) {
            return 1;
        }
        return Math.min(page, totalPages);
    }

    private static int totalPages(int size, long totalItems) {
        return Math.max(1, (int) Math.ceil((double) totalItems / size));
    }

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages(size, totalItems);
    }

    public boolean isHasPrev() {
        return page > 1;
    }

    public boolean isHasNext() {
        return page < getTotalPages();
    }
}
