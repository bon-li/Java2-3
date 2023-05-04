package java2finalproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.util.*;
import javafx.geometry.Insets;

public class IssueTracker extends Application {
    String username, password, role, currentRole="", subject, details, assignTo, 
            currentPriority, priority = "New", manager="manager",user="user",developer="developer";
    int n = 0;
    boolean userValid;
    
    userLogin userlogin;
    
    File issueFile = new File("issue.txt");
    
    Label lblError, issueList, lblRole;
    TextField txtUser, txtPwd, txtSubject, txtDetails, txtUsername, txtPassword, 
            txtVerifyPassword, txtId, txtPriority, txtAssignTo, txtNewSubject, txtNewDetails;
    
    Scene loginScreen = null;
    Scene issueListScreen = null;
    Scene newIssueScreen = null;
    Scene issueDetailsScreen = null;
    Scene addNewUserScreen = null;
    Stage stage = null;

    public void start(Stage primaryStage) {
        stage = primaryStage;
        primaryStage.setTitle("Issue Tracker");

        loginScreen = getLoginScreen();
        issueListScreen = getIssueListScreen();
        newIssueScreen = getNewIssueScreen();
        addNewUserScreen = getAddNewUserScreen();
        issueDetailsScreen = getIssueDetailsScreen();
        
        stage.setScene(loginScreen);
        stage.show();
    }

    public void switchScreens(Scene scene) {
        stage.setScene(scene);
    }

    public Scene getLoginScreen() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(5));

        Label lblUser = new Label("Username: ");
        Label lblPwd = new Label("Password: ");
        TextField txtUser = new TextField();
        TextField txtPwd = new TextField();
        Button btnLogin = new Button("Login");
        lblError = new Label();

        pane.add(lblUser, 0, 0);
        pane.add(lblPwd, 0, 1);
        pane.add(txtUser, 1, 0);
        pane.add(txtPwd, 1, 1);
        pane.add(btnLogin, 0, 2);
        pane.add(lblError, 0, 3,2,1);
        
        lblRole = new Label();
        pane.add(lblRole,2,0);
        lblRole.setVisible(false);

        btnLogin.setOnAction(e -> {
            int index = 0;
            userlogin = new userLogin();
            ArrayList<userLogin> userList = userlogin.getList();
            username = txtUser.getText();
            password = txtPwd.getText();

            while (index < userList.size()) {
                userLogin userLogin = userList.get(index);
                String usernameCheck = userLogin.getUsername();
                String passwordCheck = userLogin.getPassword();
                role = userLogin.getRole();

                if (username.equals(usernameCheck) && password.equals(passwordCheck)) {
                    userValid = true;
                    break;
                } else {
                    ++index;
                    userValid = false;
                }
            }
            if (userValid == false) {
                setError("Incorrect username or password.");
            }
            if (userValid == true) {
                switchScreens(getIssueListScreen());
                setCurrentRole(role);
                //System.out.println(getCurrentRole());
            };
        });
        loginScreen = new Scene(pane);

        return loginScreen;
    }
    
    public Scene getIssueListScreen(){
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(5));
        System.out.println(getCurrentRole());
        Label lblIssues = new Label("Issues");
        Button btnNewIssue = new Button("New Issue");
        Button btnNewUser = new Button("Add New User");
        Button btnIssueDetails = new Button("View Details");
        issueList = new Label(readSubject());
        Button next = new Button("Next");
        Button previous = new Button("Previous");
        
        pane.add(btnNewIssue, 0, 0);
        pane.add(btnNewUser, 0, 5);
        pane.add(lblIssues, 0, 1,3,1);
        pane.add(issueList, 0, 2);
        pane.add(next, 1,4);
        pane.add(previous, 0, 4);
        pane.add(btnIssueDetails, 0, 3);
        btnNewUser.setVisible(false);
        
        currentRole = getCurrentRole();
        System.out.println(currentRole);
        if(currentRole.equals(manager)){
            btnNewUser.setVisible(true);
        }
        
        next.setOnAction(e-> next());
        previous.setOnAction(e-> previous());
        btnIssueDetails.setOnAction(e -> {
            switchScreens(issueDetailsScreen);
            print();
        });
        btnNewIssue.setOnAction(e -> switchScreens(newIssueScreen));
        btnNewUser.setOnAction(e-> switchScreens(addNewUserScreen));
        issueListScreen = new Scene(pane, 250, 120);
        
        return issueListScreen;
    }
    
    public Scene getNewIssueScreen(){
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(5));
        
        Label lblSubject = new Label("Subject: ");
        Label lblDetails = new Label("Details: ");
        Label lblSaved = new Label();
        
        txtNewSubject = new TextField();
        txtNewDetails = new TextField();
        
        Button btnSubmit = new Button("Submit");
        Button btnCancel = new Button("Cancel");
        
        pane.add(lblSubject, 0, 0);
        pane.add(txtNewSubject, 1, 0);
        pane.add(lblDetails, 0,1);
        pane.add(txtNewDetails, 1, 1);
        pane.add(btnSubmit, 0,2);
        pane.add(btnCancel, 1, 2);
        pane.add(lblSaved, 0,3,2,1);
        
        btnSubmit.setOnAction(e -> {
            saveNewIssue();
            lblSaved.setText("New error successfully saved.");
            switchScreens(issueListScreen);
                });
        btnCancel.setOnAction(e -> switchScreens(issueListScreen));
        newIssueScreen = new Scene(pane);
        
        return newIssueScreen;
    }

    public Scene getAddNewUserScreen(){
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(5));

        Label lblUsername = new Label("UserName");
        Label lblPassword = new Label("Password");
        Label lblVerifyPassword = new Label("Verify Password");
        Label lblId = new Label("Id");
        txtUsername = new TextField();
        txtPassword = new TextField();
        txtVerifyPassword = new TextField();
        txtId = new TextField();

        Button btnCancel = new Button("Cancel");
        Button btnSubmit = new Button("Submit");

        pane.add(lblUsername, 0, 0);
        pane.add(txtUsername, 1, 0);
        pane.add(lblPassword, 0, 1);
        pane.add(txtPassword, 1, 1);
        pane.add(lblVerifyPassword, 0, 2);
        pane.add(txtVerifyPassword, 1, 2);
        pane.add(lblId, 0, 3);
        pane.add(txtId, 1, 3);
        pane.add(btnCancel, 0, 4);
        pane.add(btnSubmit, 1, 4);
        
        btnSubmit.setOnAction(e->{
            addNewUser();
            switchScreens(issueListScreen);       
        });
        btnCancel.setOnAction(e->switchScreens(issueListScreen));
        
        addNewUserScreen = new Scene(pane);
        return addNewUserScreen;
    }
    
    public Scene getIssueDetailsScreen() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(5));

        Label lblSubject = new Label("Subject: ");
        txtSubject = new TextField();
        Label lblDetails = new Label("Details: ");
        txtDetails = new TextField();
        Label lblPriority = new Label("Priority: ");
        ComboBox Priority = new ComboBox();
        txtPriority = new TextField();
        Priority.getItems().addAll("Assigned", "Opened", "Rejected ", "Resolved", "Validate");
        Priority.setOnAction(e -> {
            String text = Priority.getValue().toString();
            txtPriority.setText(text);
        });
        Label lblAssignTo = new Label("Assign To: ");
        ComboBox AssignTo = new ComboBox();
        txtAssignTo = new TextField();
        AssignTo.getItems().addAll("developer1", "developer 2", "developer 3");
        AssignTo.setOnAction(e->{
        String text1 = AssignTo.getValue().toString();
        txtAssignTo.setText(text1);
        });
        Button btnSave = new Button("Save Issues");
        Button btnCancel = new Button("Cancel");

        pane.add(lblSubject, 0, 2);
        pane.add(txtSubject, 1, 2);
        pane.add(lblDetails, 0, 3);
        pane.add(txtDetails, 1, 3);
        pane.add(lblPriority, 0, 4);
        pane.add(Priority, 1, 4);
        pane.add(lblAssignTo, 0, 5);
        pane.add(AssignTo, 1, 5);
        pane.add(btnSave, 0, 6);
        pane.add(btnCancel, 1, 6);

        btnSave.setOnAction(e -> {
            updateIssue();
            switchScreens(issueListScreen);
        });
        btnCancel.setOnAction(e -> switchScreens(issueListScreen));
        
        issueDetailsScreen = new Scene(pane);
        return issueDetailsScreen;
    }
    
    public void updateIssue(){
        BufferedReader updateFile=null;
        FileWriter writer = null;
        File oldFile = new File("issue.txt");
        try{
            String oldUpdate="";
            updateFile = new BufferedReader(new FileReader(oldFile)); 
            String line = updateFile.readLine();
            while(line != null){
                oldUpdate = oldUpdate + line + System.lineSeparator();
                line = updateFile.readLine();
            }
            subject = Files.readAllLines(Paths.get("issue.txt")).get(n);
            details = Files.readAllLines(Paths.get("issue.txt")).get(n+1);
            currentPriority = Files.readAllLines(Paths.get("issue.txt")).get(n+2);
            assignTo = Files.readAllLines(Paths.get("issue.txt")).get(n+3);
            String updatedIssue = oldUpdate.replace(subject, getSubject())
                                           .replace(details, getDetails())
                                           .replace(currentPriority, getPriority())
                                           .replace(assignTo, getAssignTo());

            writer = new FileWriter(oldFile);
            writer.write(updatedIssue);
            
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try{
                updateFile.close();
                writer.close();
            }catch (Exception e){
                System.out.println("Failed to close file.");
            }
        }
    }
    
    public void setError(String error) {
        lblError.setText(error);
    }
    
    public void setCurrentRole(String role){
        lblRole.setText(role);
    }
    
    public String getCurrentRole(){
        return lblRole.getText();
    }
    
    public String getNewSubject(){
        return txtNewSubject.getText();
    }
    
    public String getNewDetails(){
        return txtNewDetails.getText();
    }
    
    public String getSubject(){
        return txtSubject.getText();
    }
    
    public String getDetails(){
        return txtDetails.getText();
    }
    
    public String getPriority(){
        return txtPriority.getText();
    }
    
    public String getAssignTo(){
        return txtAssignTo.getText();
    }
    
    public String getName() {
        return txtUsername.getText();
    }
    
    public String getPassword() {
        return txtPassword.getText();
    }

    public String getId() {
        return txtId.getText();
    }
    
    public String readSubject() {
        try {
            subject = Files.readAllLines(Paths.get("issue.txt")).get(n);
        } catch (Exception e) {
            System.out.println("Unable to read.");
        }
        return subject;
    }
    
    public void next(){
        try {
            if(n<(issueFile.length())){
            n+=5;
            print();
            }
        } catch (Exception e) {
            n=0;
            System.out.println("Last issue has been reached.");
        }
    }
    
    public void previous(){
        try {
            if(n>0){
            n-=5;
            print();
            }
        } catch (Exception s) {
            System.out.println("First issue has been reached.");
        }
    }
    
    public void print(){
        try {
            subject = Files.readAllLines(Paths.get("issue.txt")).get(n);
            details = Files.readAllLines(Paths.get("issue.txt")).get(n+1);
            currentPriority = Files.readAllLines(Paths.get("issue.txt")).get(n+2);
            assignTo = Files.readAllLines(Paths.get("issue.txt")).get(n+3);
            issueList.setText(subject);
            
            txtSubject.setText(subject);
            txtDetails.setText(details);
            txtPriority.setText(currentPriority);
            txtAssignTo.setText(assignTo);
        } catch (Exception s) {
            System.out.println("Unable to view issue.");
        }
    }
    
    public void saveNewIssue(){
        PrintWriter issue = null;
        try{
        issue = new PrintWriter(new FileWriter("issue.txt", true));
        issue.println("Subject: " + getNewSubject());
        issue.println("Details: " + getNewDetails());
        issue.println("Priority: " + priority);
        issue.println("Assign To: " );
        issue.println("");
        }catch (Exception e){
            System.out.println("save new issue no work");
        }finally{
            if (issue != null) issue.close();
        }
    }

    public void addNewUser() {
        if (isValidInput()) {
            userLogin login = new userLogin(getName(), getPassword(), getId());
            //saveRecords(login);
            //clearElement();
            System.out.println("New user saved.");
        } else {
            System.out.println("Unable to save new user.");
            //showAlert("Text Field is empty");
        }
    }
    
    public boolean isValidInput() {
        String n = txtUsername.getText();
        String num = txtPassword.getText();
        String em = txtId.getText();
        return (!isEmpty(n) && !isEmpty(num) && !isEmpty(em));
    }
    
    public boolean isEmpty(String val) {
        return val.trim().equals("");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
