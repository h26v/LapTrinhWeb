package vn.iotstar.service.impl;

import vn.iotstar.entity.Book_24162046;
import vn.iotstar.entity.CartItem_24162046;
import vn.iotstar.repository.IBookRepository_24162046;
import vn.iotstar.repository.ICartRepository_24162046;
import vn.iotstar.repository.impl.BookRepository_24162046;
import vn.iotstar.repository.impl.CartRepository_24162046;
import vn.iotstar.service.ICartService_24162046;
import vn.iotstar.util.Constant_24162046;

import java.math.BigDecimal;
import java.util.List;

public class CartService_24162046 implements ICartService_24162046 {

    private final ICartRepository_24162046 cartRepository = new CartRepository_24162046();
    private final IBookRepository_24162046 bookRepository = new BookRepository_24162046();

    @Override
    public List<CartItem_24162046> getItems(int userid) {
        return cartRepository.findByUser(userid);
    }

    @Override
    public long countItems(int userid) {
        return cartRepository.countByUser(userid);
    }

    @Override
    public BigDecimal getTotal(List<CartItem_24162046> items) {
        return items.stream()
                .map(CartItem_24162046::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void addToCart(int userid, int bookid, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        Book_24162046 book = bookRepository.findById(bookid);
        if (book == null) {
            throw new IllegalArgumentException("Không tìm thấy sách");
        }
        if (book.getPrice() == null) {
            throw new IllegalArgumentException("Sách này chưa có giá bán");
        }
        if (!book.isInStock()) {
            throw new IllegalArgumentException("Sách \"" + book.getTitle() + "\" đã hết hàng");
        }

        CartItem_24162046 existing = cartRepository.findByUserAndBook(userid, bookid);
        int current = existing == null ? 0 : existing.getQuantity();
        int max = book.getMaxOrderQuantity();
        if (current + quantity > max) {
            int canAdd = max - current;
            String message;
            if (current == 0) {
                message = "Chỉ mua được tối đa " + max + " cuốn";
            } else if (canAdd > 0) {
                message = "Giỏ đã có " + current + " cuốn, chỉ thêm được tối đa " + canAdd + " cuốn nữa";
            } else {
                message = "Giỏ đã có " + current + " cuốn, đạt giới hạn rồi";
            }
            throw new IllegalArgumentException(message + " (" + limitText(book) + ")");
        }

        if (existing == null) {
            cartRepository.insert(userid, bookid, quantity);
        } else {
            cartRepository.updateQuantity(existing.getId(), current + quantity);
        }
    }

    @Override
    public void updateQuantity(int userid, int itemId, int quantity) {
        CartItem_24162046 item = cartRepository.findByIdAndUser(itemId, userid);
        if (item == null) {
            throw new IllegalArgumentException("Sách này không có trong giỏ hàng");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("Số lượng ít nhất là 1, muốn bỏ sách thì bấm nút xóa");
        }
        Book_24162046 book = item.getBook();
        int max = book.getMaxOrderQuantity();
        if (max == 0) {
            throw new IllegalArgumentException("Sách \"" + book.getTitle() + "\" đã hết hàng, bạn xóa khỏi giỏ giúp nhé");
        }
        if (quantity > max) {
            throw new IllegalArgumentException("Tối đa " + max + " cuốn thôi (" + limitText(book) + ")");
        }
        cartRepository.updateQuantity(itemId, quantity);
    }

    @Override
    public void remove(int userid, int itemId) {
        cartRepository.delete(itemId, userid);
    }

    @Override
    public void clear(int userid) {
        cartRepository.deleteByUser(userid);
    }

    /** Ly do gioi han: het kho hay gioi han moi lan mua. */
    private String limitText(Book_24162046 book) {
        return book.getQuantity() < Constant_24162046.CART_MAX_PER_ITEM
                ? "kho chỉ còn " + book.getQuantity() + " cuốn"
                : "mỗi sách mua tối đa " + Constant_24162046.CART_MAX_PER_ITEM + " cuốn";
    }
}
