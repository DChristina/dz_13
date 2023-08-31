package cc.robotdreams.automation.db;

import cc.robotdreams.automation.Session;
import org.apache.commons.text.StringEscapeUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public  class Users {
    static public class User {
        final public int id;
        final public String first_name;
        final public String last_name;
        final public String login;
        final public String password_hash;
        final public String date_of_birth;


        public String getFirtsName() {
            return this.first_name;
        }

        final private List<User> clients = new LinkedList<>( ) {
        };

        public User(int id, String first_name, String last_name, String login,  String password_hash,String date_of_birth) {
            this.id = id;
            this.first_name = first_name;
            this.last_name = last_name;
            this.login = login;
            this.password_hash = password_hash;
            this.date_of_birth = date_of_birth;

        }

        final public void addUser(User newUser) {
            if (newUser == null) {
                throw new RuntimeException("Child can not be null");
            }
            this.clients.add(newUser);
        }

        final public List<User> clients() {
            return new LinkedList<>(this.clients);
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new LinkedList<>( );
        ResultSet resultSet = Session.get( ).mysql( ).executeQuery(
                "SELECT " +
                        "homework_user_data.id, " +
                        "homework_user_data.first_name, " +
                        "homework_user_data.last_name, " +
                        "homework_user_data.login, " +
                        "homework_user_data.password_hash, " +
                        "homework_user_data.date_of_birth " +
                        "FROM homework_user_data " +
                        "ORDER BY homework_user_data.id ASC"
        );
        try {
            while (resultSet.next( )) {
                int userId = resultSet.getInt("homework_user_data.id");
                String firstName = StringEscapeUtils.unescapeHtml4(resultSet.getString("homework_user_data.first_name"));
                String lastName = StringEscapeUtils.unescapeHtml4(resultSet.getString("homework_user_data.last_name"));
                String login = StringEscapeUtils.unescapeHtml4(resultSet.getString("homework_user_data.login"));
                String dateOfBirth = StringEscapeUtils.unescapeHtml4(resultSet.getString("homework_user_data.date_of_birth"));
                String passwordHash = StringEscapeUtils.unescapeHtml4(resultSet.getString("homework_user_data.password_hash"));
                result.add(new User(userId, firstName, lastName, login, dateOfBirth, passwordHash));
            }
        } catch (SQLException e) {

        }
        return result;
    }

    public Users.User getUserById(int id) {
        User searchenUser = new User(1, "test_name", "test_lastName", "test_login", "test_date", "test_passwordHash");
        try {
            PreparedStatement statement = Session.get( ).mysql( ).preparedStatement("SELECT * FROM homework_user_data WHERE id=?;");
            statement.setInt(1, 1);
            ResultSet resultSet = statement.executeQuery( );
            if (resultSet.next( )) {
                User resultUser = new User(resultSet.getInt(id), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("login"), resultSet.getString("password_hash"), resultSet.getString("date_of_birth"));
                searchenUser = resultUser;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return searchenUser;
    }

    public Users.User createNewUser(String name, String lastName, String login, String password, String dateOfBirth) {
        User newUserCreationTemp = new User(1, "test_name", "test_lastName", "test_login", "test_date", "test_passwordHash");
        try {
            PreparedStatement statement = Session.get( ).mysql( ).preparedStatement("INSERT INTO homework_user_data ( homework_user_data.first_name, homework_user_data.last_name, homework_user_data.login, homework_user_data.password_hash, homework_user_data.date_of_birth) VALUES (?,?,?,?,?);");
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setString(3, login);
            statement.setString(4, password);
            statement.setString(5, dateOfBirth);
            int row = statement.executeUpdate( );

            PreparedStatement statement2 = Session.get( ).mysql( ).preparedStatement("SELECT * FROM homework_user_data WHERE homework_user_data.login=? AND homework_user_data.password_hash=?");
            statement2.setString(1, login);
            statement2.setString(2, password);
            ResultSet newUser = statement2.executeQuery( );
            if (newUser.next( )) {
                User resultUser = new User(newUser.getInt("id"), newUser.getString("first_name"), newUser.getString("last_name"), newUser.getString("login"), newUser.getString("password_hash"), newUser.getString("date_of_birth"));
                newUserCreationTemp = resultUser;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newUserCreationTemp;
    }

    public Users.User updateUser(String name, String lastName, String password, int id) {
        User updatedUserTemp = new User(1, "test_name", "test_lastName", "test_login", "test_date", "test_passwordHash");
        try {
            PreparedStatement statement = Session.get( ).mysql( ).preparedStatement("UPDATE homework_user_data SET homework_user_data.first_name=?, homework_user_data.last_name=?, homework_user_data.password_hash=? WHERE homework_user_data.id=? ;");
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setString(3, password);
            statement.setInt(4, id);
            int row = statement.executeUpdate( );
            PreparedStatement statement2 = Session.get( ).mysql( ).preparedStatement("SELECT * FROM homework_user_data WHERE homework_user_data.id=?;");
            statement2.setInt(1, id);
            ResultSet updatesUser = statement2.executeQuery( );
            if (updatesUser.next( )) {
                User resultUser = new User(updatesUser.getInt("id"), updatesUser.getString("first_name"), updatesUser.getString("last_name"), updatesUser.getString("login"), updatesUser.getString("password_hash"), updatesUser.getString("date_of_birth"));
                updatedUserTemp = resultUser;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return updatedUserTemp;
    }
    public Boolean deleteUser(int id) {
        Boolean result = false;
        //User updatedUserTemp = new User(1, "test_name", "test_lastName", "test_login", "test_date", "test_passwordHash");
        try {
            PreparedStatement statement = Session.get( ).mysql( ).preparedStatement("DELETE FROM homework_user_data WHERE homework_user_data.id=?;");
            statement.setInt(1, id);
            int row = statement.executeUpdate( );
            PreparedStatement statement2 = Session.get( ).mysql( ).preparedStatement("SELECT * FROM homework_user_data WHERE homework_user_data.id=?;");
            statement2.setInt(1, id);
            boolean updatesUser = statement2.execute();
            result = updatesUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

