package java2finalproject;

import java.util.ArrayList;

public class userLogin {
    String username;
    String password;
    String role;
    ArrayList<userLogin> userList;
    
    /*public userLogin(){
        username = "";
        password = "";
        id = "";
    
    }*/
    
    public userLogin(){
        userList = new ArrayList<userLogin>();
        userList.add(new userLogin("user1", "abc123", "user"));
        userList.add(new userLogin("manager1", "123abc", "manager"));
        userList.add(new userLogin("developer1", "a1b2c3", "developer"));
    }

    public userLogin(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String getRole() {
        return role;
    }
    
    public ArrayList<userLogin> getList(){
        return userList;
    }
    
   
    
     /*public static void main(String[]args){
        
        //for(userLogin u : userList){
            System.out.println(u);
        }
        
    }

    @Override
    public String toString() {
        return "userLogin{" + "username=" + username + ", password=" + password + ", id=" + id + '}';
    } */
    
    
}
