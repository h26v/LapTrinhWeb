package vn.iotstar.config;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            if (categoryRepository.count() > 0) {
                return;
            }

            Category electronics = categoryRepository.save(
                    new Category("Electronics", "https://images.unsplash.com/photo-1498049794561-7780e7231661?w=120&q=80"));
            Category lifestyle = categoryRepository.save(
                    new Category("Lifestyle", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=120&q=80"));
            Category home = categoryRepository.save(
                    new Category("Home & Living", "https://images.unsplash.com/photo-1556228453-efd6c1ff04f6?w=120&q=80"));

            productRepository.save(product("Noise-canceling Headphones", electronics, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=160&q=80", "249.00", "12.00", 24));
            productRepository.save(product("Smart Watch Series 5", electronics, "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=160&q=80", "199.00", "8.00", 18));
            productRepository.save(product("Minimal Desk Lamp", home, "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=160&q=80", "79.00", "5.00", 35));
            productRepository.save(product("Everyday Canvas Backpack", lifestyle, "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=160&q=80", "68.00", "0.00", 42));
            productRepository.save(product("Ceramic Pour-over Set", home, "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=160&q=80", "45.00", "10.00", 12));
            productRepository.save(product("Portable Bluetooth Speaker", electronics, "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=160&q=80", "129.00", "15.00", 9));
        };
    }

    private Product product(String name, Category category, String image, String price, String discount, int quantity) {
        Product product = new Product();
        product.setProductName(name);
        product.setCategory(category);
        product.setImages(image);
        product.setUnitPrice(new BigDecimal(price));
        product.setDiscount(new BigDecimal(discount));
        product.setDescription("Sản phẩm mẫu cho chức năng quản lý Product bằng RESTful API và AJAX.");
        product.setQuantity(quantity);
        product.setStatus((short) 1);
        return product;
    }
}
