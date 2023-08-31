import cc.robotdreams.automation.DB;
import cc.robotdreams.automation.Session;
import cc.robotdreams.automation.base.BaseTestNG;
import cc.robotdreams.automation.db.Category;
import cc.robotdreams.automation.db.Users;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBCTest extends BaseTestNG {

    int idForTest;
    @Test
    public void getCategories() throws SQLException {

        List<Category.Item> topCategories = DB.category.getTopCategories();
        for(Category.Item category : topCategories){
            System.out.println(category.id + " - " + category.name );

        }

        List<Category.Item> allCategories = DB.category.getAllCategories();
        for (Category.Item child : allCategories){
            printItem("", child);
        }
         try {
             PreparedStatement statement = Session.get().mysql( ).preparedStatement("SELECT * FROM oc_category WHERE parent_id=?;" );
             statement.setInt(1,25);
             ResultSet resultSet = statement.executeQuery();
             while(resultSet.next()){
                 System.out.println(resultSet.getInt("category_id") );
             }
         } catch (SQLException e){
             throw new RuntimeException( e );
         }

    }
    private void printItem(String prefix, Category.Item item){
        System.out.println(prefix + " " + item.name );
        for (Category.Item  child : item.childs()){
            printItem(prefix + "--",child);
        }
    }

    @Test
    public void selectUsers() throws SQLException{
        List<Users.User> users = DB.user.getAllUsers();
        for(Users.User user : users){
            System.out.println(user.id + " " + user.last_name + " " + user.first_name + " " + user.date_of_birth);
        }
    }

    @Test (groups="one")
    public void selectUserById() throws SQLException{
        Users.User searchenUser = DB.user.getUserById(1);
        Assert.assertEquals( searchenUser.first_name,"Jack");
        Assert.assertEquals( searchenUser.last_name,"Nicholson");
        Assert.assertEquals( searchenUser.login,"jnicholson");
    }

    @Test (groups="one")
    public void createNewUser() throws SQLException{
        Users.User newCreatedUser = DB.user.createNewUser("Kris","Melnik","kris111","100000","1988-06-28 00:00:0");
        Assert.assertEquals( newCreatedUser.first_name,"Kris");
        Assert.assertEquals( newCreatedUser.last_name,"Melnik");
        Assert.assertEquals( newCreatedUser.password_hash,"100000");
        idForTest = newCreatedUser.id;
    }

    @Test(groups="one", dependsOnMethods="createNewUser")
    public void updateUserData() throws SQLException{
        Users.User updatedUser = DB.user.updateUser("Kristina", "Melnik","10000000",idForTest);
        Assert.assertEquals( updatedUser.first_name,"Kristina");
        Assert.assertEquals( updatedUser.last_name,"Melnik");
        Assert.assertEquals( updatedUser.password_hash,"10000000");
    }

    @Test(groups="one", dependsOnMethods="createNewUser")
    public void deleteUser() throws SQLException{
        boolean deletingUserProcess = DB.user.deleteUser(idForTest);
        Assert.assertEquals(deletingUserProcess,true);
    }
}
