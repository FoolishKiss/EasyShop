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

    // Constructor that receives DataSource and passes it to the base class
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    // Gets the full shopping cart for a user based on their userId
    @Override
    public ShoppingCart getByUserId(int userId) {

        // Creates an empty shopping cart
        ShoppingCart cart = new ShoppingCart();

        // Query joins shopping_cart and products tables and calculates the line_total for each item
        String query = """
                SELECT sc.product_id, sc.quantity, p.*, (sc.quantity * p.price) AS line_total
                FROM shopping_cart sc
                JOIN products p ON sc.product_id = p.product_id
                WHERE sc.user_id = ?
                """;

        // Try-with-resources to open database connection and prepares the query
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Binds the userId to ? placeholder
            statement.setInt(1, userId);

            // Executes the query
            ResultSet results = statement.executeQuery();

            // Loops through each result maps it to a Product, builds a ShoppingCartItem, and adds it to the cart
            while (results.next()) {

                Product product = MySqlProductDao.mapRow(results);
                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(results.getInt("quantity"));
                cart.getItems().put(product.getProductId(), item);

            }
        }

        // Handles database errors
        catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
        // Returns the full shopping cart
        return cart;

    }

    @Override
    public void addOrIncrement(int userId, int productId) {

        // Query to insert a new product with quantity 1, or increase it if it exists
        String query = """
        INSERT INTO shopping_cart (user_id, product_id, quantity)
        VALUES (?, ?, 1)
        ON DUPLICATE KEY UPDATE quantity = quantity + 1
        """;

        // Try-with-resources to open database connection and prepares the query
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Binds the userId to first ? placeholder and productId second ? placeholder
            statement.setInt(1, userId);
            statement.setInt(2, productId);

            // Executes the query
            statement.executeUpdate();

            // Handles database errors
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void update(int userId, int productId, int quantity) {

        // Query to update the quantity
        String query = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id = ? AND product_id = ?
                """;

        // Try-with-resources to open database connection and prepares the query
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Binds the quantity to first ? placeholder, userId second ? placeholder, and productId to third ? placeholder
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);

            // Executes the query
            statement.executeUpdate();

        }
        // Handles database errors
        catch (SQLException e) {
            throw  new RuntimeException(e);
        }
    }

    public void clear(int userId) {

        // Query to clear the cart
        String query = """
                DELETE FROM shopping_cart
                WHERE user_id = ?
                """;

        // Try-with-resources to open database connection and prepares the query
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Binds the userId to ? placeholder
            statement.setInt(1, userId);

            // Executes the query
            statement.executeUpdate();

        }
        // Handles database errors
        catch (SQLException e) {
            throw  new RuntimeException(e);
        }
    }

}
