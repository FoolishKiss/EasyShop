package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        // Creates a list to store each category from the database
        List<Category> categories = new ArrayList<>();

        // Query to get all rows from the database
        String query = """
                SELECT *
                FROM categories
                """;

        // try with resources to auto close all database resources
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet results = statement.executeQuery())
        {
            // Loops through the results and turns them into a category object and adds to list
            while (results.next())
            {
                categories.add(mapRow(results));
            }
        }
        // Catches any database errors and rethrows them
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        // Returns the list of all categories
        return categories;

    }

    @Override
    public Category getById(int categoryId)
    {

        // Query to get category by id
        String query = """
                SELECT *
                FROM categories
                WHERE category_id = ?
                """;

        // Try with resources to auto close Connection, and PreparedStatement database resources
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            // Binds the categoryId to ?
            statement.setInt(1, categoryId);

            // Execute the query
            ResultSet results = statement.executeQuery();

            // Checks if a category with id exists
            if (results.next())
            {
                // Convert the result into a category
                return mapRow(results);
            }
        }
        // Catches any database errors and rethrows them
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        // If no match return null
        return null;
    }

    @Override
    public Category create(Category category)
    {
        // Query to insert/create (POST) a new category
        String query = """
                INSERT INTO categories (name, description)
                VALUES (?, ?)
                """;

        // Try with resources to auto close Connection, and PreparedStatement resources
        try (Connection connection = getConnection();
             // Prepared statement with second arg to return the generated keys after Insert
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Bind category name, and description to ? placeholders
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            // Execute the INSERT query
            statement.executeUpdate();

            // Gets auto generated keys
            ResultSet keys = statement.getGeneratedKeys();

            // Check if a key is returned
            if (keys.next())
            {
                // Reads the id of the new category
                int id = keys.getInt(1);

                // Gets the category from the database with new id
                return getById(id);
            }
        }
        // Catches any database errors and rethrows them
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        // Return null if no keys
        return null;

    }

    @Override
    public void update(int categoryId, Category category)
    {
        // Query to update (PUT) category
        String query = """
                UPDATE categories
                SET name = ?, description = ?
                WHERE category_id = ?
                """;

        // try with resources to auto close Connection, and PreparedStatement resources
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Binds  Bind category name, description, and categoryId to ? placeholders
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            // Execute the query
            statement.executeUpdate();

        }
        // Catches any database errors and rethrows them
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // Query to delete category
        String query = """
                DELETE FROM categories
                WHERE category_id = ?
                """;

        // try with resources to auto close Connection, and PreparedStatement resources
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Binds  Bind category name, description, and categoryId to ? placeholders
            statement.setInt(1, categoryId);

            // Execute the query
            statement.executeUpdate();

        }
        // Catches any database errors and rethrows them
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
