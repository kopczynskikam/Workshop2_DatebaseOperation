package pl.coderslab.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserDao {

    private static final String CREATE_USER_QUERTY = "INSERT INTO users(email, userName, password) VALUES (?,?,?);";
    private static final String READ_USER_QUERTY = "SELECT * FROM users WHERE ID=?;";
    private static final String UPDATE_USER_QUERTY = "UPDATE users SET email=?, username=?, password=? WHERE id=?:";
    private static final String DELETE_USER_QUERTY = "DELETE FROM users WHERE id=?;";
    private static final String FIND_ALL_USERS_QUERTY="SELECT * FROM users;";

    public User create(User user) {
        try(Connection connection=DbUtil.getConnection()){
            PreparedStatement statement= connection.prepareStatement(CREATE_USER_QUERTY,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()){
               // user.setId(resultSet.getInt(1));
                long id= resultSet.getLong(1);
                System.out.println("Id dodanego użytkownika to: "+ id);
                return user;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;

    }

    private String hashPassword(String password){
        return org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
    }

    public User read(int UserId){
        try (Connection connection=DbUtil.getConnection()){
            PreparedStatement statement= connection.prepareStatement(READ_USER_QUERTY);
            statement.setInt(1,UserId);

            ResultSet resultSet= statement.executeQuery();
            if (resultSet.next()){
                User user=new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void update(User user){
        try (Connection connection=DbUtil.getConnection()){
            PreparedStatement statement=connection.prepareStatement(UPDATE_USER_QUERTY);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getPassword());
            statement.setInt(4,user.getId());
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(int userId){
        try (Connection connection=DbUtil.getConnection()){
            PreparedStatement statement=connection.prepareStatement(DELETE_USER_QUERTY);
            statement.setInt(1,userId);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private User[] addToArray(User[] arr, User user){
        User[] newArr= Arrays.copyOf(arr,arr.length+1);
        newArr[newArr.length-1]=user;
        return newArr;
    }

    public User[] findAll(){
        User[] users=new User[0];
        try(Connection connection=DbUtil.getConnection()) {
        PreparedStatement statement=connection.prepareStatement(FIND_ALL_USERS_QUERTY);
        ResultSet resultSet=statement.executeQuery();

        while(resultSet.next()){
            User user=new User();
            user.setId(resultSet.getInt("id"));
            user.setEmail(resultSet.getString("email"));
            user.setUserName(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            users=addToArray(users,user);
        }
        return users;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
