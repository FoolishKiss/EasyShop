package org.yearup.data.mysql;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {

        ShoppingCart cart = new ShoppingCart();

        String query = """
                SELECT sc.product_id, sc.quantity, p.*, (sc.quantity * p.price) AS line_total
                FROM shopping_cart sc
                JOIN products p ON sc.product_id = p.product_id
                WHERE sc.user_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            ResultSet results = statement.executeQuery();

            while (results.next()) {

                Product product = MySqlProductDao.mapRow(results);
                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(results.getInt("quantity"));
                cart.getItems().put(product.getProductId(), item);

            }
        }

        catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
        return cart;

    }

    @Override
    public void addOrIncrement(int userId, int productId) {
        String query = """
        INSERT INTO shopping_cart (user_id, product_id, quantity)
        VALUES (?, ?, 1)
        ON DUPLICATE KEY UPDATE quantity = quantity + 1
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void update(int userId, int productId, int quantity) {
        String query = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id = ? AND product_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);

            statement.executeUpdate();

        }
        catch (SQLException e) {
            throw  new RuntimeException(e);
        }
    }

    public void clear(int userId) {
        String query = """
                DELETE FROM shopping_cart
                WHERE user_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            statement.executeUpdate();

        }
        catch (SQLException e) {
            throw  new RuntimeException(e);
        }
    }

}
