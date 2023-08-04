package com.example.newjavafx;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import java.time.LocalDate;


/**
 * An account class represents as recipient's account to store various recipient's details.
 *
 * @author Muhammad Hafidz Fawwaz
 */
public class Account{
    private String name = "Default";
    private String phoneNum = "999999999999";
    private String vacStatus = "Pending";
    private int age = 0;
    private LocalDate vacDate = null;        // Current appointment date
    private LocalDate FDoseDate = null;      // Record the date when recipient receives 1st dose
    private LocalDate SDoseDate = null;      // Record the date when recipient receives 2nd dose
    private int FBatch = 0;             // Record the first dose vaccine batch
    private int SBatch = 0;             // Record the second dose vaccine batch



    /**
     * Constructs a recipient account with name as "Default", phone number as "999999999999", vaccination status as "Pending", age as 0,
     * appointment date as 0, first and second dose date as 0, first and second batch as 0.
     */
    public Account(){}

    /**
     * Constructs a recipient account with specified name and specified phone number
     *
     * @param name the specified name
     * @param phoneNum the specified phone number
     */
    public Account(String name, String phoneNum){
        this.name = name;
        this.phoneNum = phoneNum;
    }

    /**
     * Constructs a recipient account with specified name, specified phone number, specified age
     *
     * @param name the specified name
     * @param phoneNum the specified phone number
     * @param age the specified age
     */
    public Account(String name, String phoneNum, int age){
        this.name = name;
        this.phoneNum = phoneNum;
        this.age = age;
    }

    /**
     * Constructs a recipient account with specified name, specified phone number, specified status, specified appointment date,
     * specified first and second dose date, specified first and second vaccine's batches, specified age
     *
     * @param name the specified name
     * @param phoneNum the specified phone number
     * @param vacStatus the specified status
     * @param vacDate the specified appointment date
     * @param FDoseDate the specified first dose date
     * @param SDoseDate the specified second dose date
     * @param FBatch the specified first vaccine's batch
     * @param SBatch the specified second vaccine's batch
     * @param age the specified age
     */
    public Account(String name, String phoneNum, String vacStatus, LocalDate vacDate, LocalDate FDoseDate, LocalDate SDoseDate, int FBatch, int SBatch, int age){
        this.name = name;
        this.phoneNum = phoneNum;
        this.vacStatus = vacStatus;
        this.vacDate = vacDate;
        this.FDoseDate = FDoseDate;
        this.SDoseDate = SDoseDate;
        this.FBatch = FBatch;
        this.SBatch = SBatch;
        this.age = age;
    }

    /**
     * Set recipient's name.
     *
     * @param name the new name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Set recipient's phone number.
     *
     * @param phoneNum the new phone number
     */
    public void setPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
    }

    /**
     * Set recipient's vaccination status.
     *
     * @param vacStatus the new vaccination status
     */
    public void setVacStatus(String vacStatus){
        this.vacStatus = vacStatus;
    }

    /**
     * Set recipient's age.
     *
     * @param age the new age
     */
    public void setAge(int age){
        this.age = age;
    }

    /**
     * Set recipient's appointment date.
     *
     * @param vacDate the new appointment date
     */
    public void setVacDate(LocalDate vacDate){
        this.vacDate = vacDate;
    }

    /**
     * Record the date when recipient receives first dose.
     *
     * @param FDoseDate the date when recipient receives first dose
     */
    public void setFDoseDate(LocalDate FDoseDate){
        this.FDoseDate = FDoseDate;
    }

    /**
     * Record the date when recipient receives second dose.
     *
     * @param SDoseDate the date when recipient receives second dose
     */
    public void setSDoseDate(LocalDate SDoseDate){
        this.SDoseDate = SDoseDate;
    }

    /**
     * Record the first vaccine's batch.
     *
     * @param batch the batch of first vaccine
     */
    public void setFBatch(int batch){
        FBatch = batch;
    }

    /**
     * Record the second vaccine's batch.
     *
     * @param batch the batch of second vaccine
     */
    public void setSBatch(int batch){
        SBatch = batch;
    }

    /**
     *  Returns the recipient's name.
     *  @return recipient's name
     */
    public String getName(){
        return name;
    }

    /**
     *  Returns the recipient's phone number.
     *  @return recipient's phone number
     */
    public String getPhoneNum(){
        return phoneNum;
    }

    /**
     *  Returns the recipient's vaccination status.
     *  @return recipient's vaccination status
     */
    public String getVacStatus(){
        return vacStatus;
    }

    /**
     *  Returns the recipient's age.
     *  @return recipient's age
     */
    public int getAge(){
        return age;
    }

    /**
     *  Returns the recipient's appointment date.
     *  @return recipient's appointment date
     */
    public LocalDate getVacDate(){
        return vacDate;
    }

    /**
     *  Returns the date when recipient receives first dose.
     *  @return the date when recipient receives first dose
     */
    public LocalDate getFDoseDate(){
        return FDoseDate;
    }

    /**
     *  Returns the date when recipient receives second dose.
     *  @return the date when recipient receives second dose
     */
    public LocalDate getSDoseDate(){
        return SDoseDate;
    }

    /**
     *  Returns the first vaccine's batch.
     *  @return the first vaccine's batch
     */
    public int getFBatch(){
        return FBatch;
    }

    /**
     *  Returns the second vaccine's batch.
     *  @return the second vaccine's batch
     */
    public int getSBatch(){
        return SBatch;
    }

    /**
     * Returns a string representation of the name, phone number, and age of this recipient account.
     * @return a string representation of the name, phone number, and age of this recipient account
     */
    public String toCSVString(){
        return name + "," + phoneNum + "," + age;
    }




    /**
     * Reads Accounts.csv and converts it into an Account ArrayList
     */
    public static ArrayList<Account> readAccountFromFile() throws IOException{
        ArrayList<Account> temp =  new ArrayList<Account>();

        // read accounts.csv into a list of lines.
        List<String> lines = Files.readAllLines(Paths.get("Accounts.csv"));

        for (int i = 0; i < lines.size(); i++){
            // split a line by comma
            String[] items = lines.get(i).split(",");

            // items[0] name, items[1] phoneNum,  items[2] age
            temp.add(new Account(items[0], items[1], Integer.parseInt(items[2])));
        }
        return temp;
    }

    /**
     * save accounts ArrayList to Accounts.csv
     *
     * @param accounts ArrayList containing recipient accounts
     */
    public static void saveAccountToFile(ArrayList<Account> accounts) throws IOException{
        // read accounts.csv into a list of lines.
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < accounts.size(); i++)
            sb.append(accounts.get(i).toCSVString() + "\n");

        Files.write(Paths.get("Accounts.csv"), sb.toString().getBytes());
    }
}

class Recipient {
    /**
     * Displays Sign Up Menu for recipient
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */
    public static void recipientSignUpMenu(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH) throws IOException {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,40,10,40));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setMinWidth(500);



        Label title = new Label("Welcome to Recipient Menu");
        title.setWrapText(true);
        grid.add(title,0,0);

        Label nameLabel = new Label("Name : ");
        grid.add(nameLabel,0,2);

        TextField nameInput = new TextField();
        grid.add(nameInput,0,3);
        nameInput.setPromptText("Has to be a string");

        Label numLabel = new Label("Phone Number : ");
        grid.add(numLabel,0,4);

        TextField numInput = new TextField();
        grid.add(numInput,0,5);
        numInput.setPromptText("Has to be integer");

        Label ageLabel = new Label("Age : ");
        grid.add(ageLabel,0,6);

        TextField ageInput = new TextField();
        grid.add(ageInput,0,7);
        ageInput.setPromptText("Has to be integer");

        Label message= new Label("");
        message.setWrapText(true);

        grid.add(message,0,8);


        Button signUpButton = new Button("Sign Up");

        signUpButton.setOnAction(e -> {
            if (nameInput.getText().isEmpty() || ageInput.getText().isEmpty()|| numInput.getText().isEmpty())  {
                message.setText("Error : Please fill in all of the fields!");
            }
            else {
                if (isInt(nameInput.getText())) {
                    message.setText("Error : Name cannot be an integer!");
                }
                else if ((isInt(ageInput.getText())== false) ||  isInt(numInput.getText())== false) {
                    message.setText("Error : Age and Phone Number has to be an integer!");
                }
                else {
                    Account placeholder = new Account(nameInput.getText(), numInput.getText(), Integer.parseInt(ageInput.getText()));
                    Boolean isFound = false;
                    for(Account i : recipients){
                        if(i.getName().equals(placeholder.getName()) && i.getPhoneNum().equals(placeholder.getPhoneNum())){
                            message.setText("Error : Account already exists!");
                            isFound = true;
                        }
                    }
                    if (isFound == false) {
                        message.setText("Sign Up successful!");
                        recipients.add(new Account(nameInput.getText(), numInput.getText(), Integer.parseInt(ageInput.getText())));
                        try {
                            Account.saveAccountToFile(recipients);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });



        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            recipientMainMenu(window,recipients, VCs, accountMOH);
        });

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        grid.getColumnConstraints().addAll(col1);


        signUpButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setMaxWidth(Double.MAX_VALUE);

        grid.add(signUpButton,0,10);
        grid.add(backButton,0,11);

        Scene mainMenuScene = new Scene(grid, 300, 400);
        window.setTitle("Recipient Menu");
        window.setScene(mainMenuScene);
        window.show();
    }
    /**
     * Displays Sign In Menu for recipient
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */
    public static void recipientSignInMenu(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH) throws IOException {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,40,10,40));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setMinWidth(500);



        Label title = new Label("Welcome to Sign In Menu");
        title.setWrapText(true);
        grid.add(title,0,0);

        Label nameLabel = new Label("Name : ");
        grid.add(nameLabel,0,2);

        TextField nameInput = new TextField();
        grid.add(nameInput,0,3);
        nameInput.setPromptText("Has to be a string");

        Label numLabel = new Label("Phone Number : ");
        grid.add(numLabel,0,4);

        TextField numInput = new TextField();
        grid.add(numInput,0,5);
        numInput.setPromptText("Has to be integer");


        Label message= new Label("");

        grid.add(message,0,6);


        Button signInButton = new Button("Sign In");

        signInButton.setOnAction(e -> {
            if (nameInput.getText().isEmpty() || numInput.getText().isEmpty())  {
                message.setText("Error : Please fill in all of the fields!");

            }
            else {
                if (isInt(nameInput.getText())) {
                    message.setText("Error : Name cannot be an integer!");
                } else if (isInt(numInput.getText()) == false) {
                    message.setText("Error : Phone Number has to be an integer!");
                } else {
                    Account placeholder = new Account(nameInput.getText(), numInput.getText());
                    Boolean isFound = false;
                    for (Account i : recipients) {
                        if (i.getName().equals(placeholder.getName()) && i.getPhoneNum().equals(placeholder.getPhoneNum())) {
                            message.setText("Login Successful!");
                            isFound = true;
                            try {
                                recipientProfile(window, i,recipients,VCs, accountMOH);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    if (isFound == false) {
                        message.setText("Error : Account doesn't exist!");
                    }
                }
            }

        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {

                recipientMainMenu(window,recipients, VCs, accountMOH);

        });

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        grid.getColumnConstraints().addAll(col1);

        signInButton.setMaxWidth(Double.MAX_VALUE);

        backButton.setMaxWidth(Double.MAX_VALUE);
        grid.add(signInButton,0,8);

        grid.add(backButton,0,9);

        Scene mainMenuScene = new Scene(grid, 300, 400);
        window.setTitle("Sign In Menu");
        window.setScene(mainMenuScene);
        window.show();
    }

    public static boolean isInt(String input) {
       boolean flag = true;
            for ( int i=0 ; i < input.length(); i++)
            {
                flag = Character.isDigit(input.charAt(i));
                if (flag == false) {

                    break;
                }
            }
            return flag;

    }
    /**
     * Displays main menu for recipient
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */
    public static void recipientMainMenu(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,40,10,40));
        grid.setVgap(10);
        grid.setHgap(10);

        FlowPane flow = new FlowPane();
        Label title = new Label("Welcome to Recipient Main Menu");
        title.setWrapText(true);
        Button signInButton = new Button("Sign In");

        signInButton.setOnAction(e -> {
            try {
                Recipient.recipientSignInMenu(window,recipients,VCs,accountMOH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(e -> {
            try {
                Recipient.recipientSignUpMenu(window,recipients,VCs,accountMOH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            try {
                Main.mainMenu(window, recipients, VCs, accountMOH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        grid.add(title,0,0);
        grid.add(signInButton,0,2);
        grid.add(signUpButton,0,3);
        grid.add(backButton,0,4);

        signInButton.setMaxWidth(Double.MAX_VALUE);
        signUpButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        grid.getColumnConstraints().addAll(col1);

        Scene mainMenuScene = new Scene(grid, 300, 400);
        window.setTitle("Recipient Menu");
        window.setScene(mainMenuScene);
        window.show();
    }

    /**
     * Displays the profile of recipient
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */
    public static void recipientProfile(Stage window, Account i, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH) throws IOException {
        TableView table = new TableView();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,40,10,40));
        grid.setVgap(10);
        grid.setHgap(10);

        Label title = new Label("Profile");
        grid.add(title, 0, 0);

        Label name = new Label("Name : ");
        grid.add(name, 0, 2);

        Label Accname = new Label(i.getName());
        grid.add(Accname, 1, 2);

        Label age = new Label("Age : ");
        grid.add(age, 0, 3);

        Label Accage = new Label(String.valueOf(i.getAge()));
        grid.add(Accage, 1, 3);

        Label phoneNum = new Label("Phone Number : ");
        grid.add(phoneNum, 0, 4);

        Label Accphonenum = new Label(i.getPhoneNum());
        grid.add(Accphonenum, 1, 4);

        Label status = new Label("Status : ");
        grid.add(status, 0, 5);

        Label Accstatus = new Label(i.getVacStatus());
        grid.add(Accstatus, 1, 5);

        if (i.getVacStatus().equals("1st Dose appointment")) {
            Label appDateLabel = new Label(" 1st Dose Appointment Date : ");
            grid.add(appDateLabel,0,6);
            Label appDate = new Label(i.getVacDate().toString());
            grid.add(appDate,1,6);
        } else if (i.getVacStatus().equals("1st Dose completed")) {
            Label fDoseDateLabel = new Label(" 1st Dose Date : ");
            grid.add(fDoseDateLabel,0,6);
            Label fDoseDate = new Label(i.getFDoseDate().toString()+ ", Batch : " + i.getFBatch());
            grid.add(fDoseDate,1,6);
        } else if (i.getVacStatus().equals("2nd Dose appointment")) {
            Label appDateLabel = new Label(" 2nd Dose Appointment Date : ");
            grid.add(appDateLabel,0,6);
            Label appDate = new Label(i.getVacDate().toString());
            grid.add(appDate,1,6);
            Label fDoseDateLabel = new Label(" 1st Dose Date : ");
            grid.add(fDoseDateLabel,0,7);
            Label fDoseDate = new Label(i.getFDoseDate().toString()+ ", Batch : " + i.getFBatch());
            grid.add(fDoseDate,1,7);
        } else if (i.getVacStatus().equals("2nd Dose completed")) {
            Label fDoseDateLabel = new Label(" 1st Dose Date : ");
            grid.add(fDoseDateLabel,0,6);
            Label fDoseDate = new Label(i.getFDoseDate().toString() + ", Batch : " + i.getFBatch());
            grid.add(fDoseDate,1,6);
            Label sDoseDateLabel = new Label(" 2nd Dose Date : ");
            grid.add(sDoseDateLabel,0,7);
            Label sDoseDate = new Label(i.getSDoseDate().toString()+ ", Batch : " + i.getSBatch());
            grid.add(sDoseDate,1,7);

    }

            Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            try {
                Recipient.recipientSignInMenu(window,recipients, VCs, accountMOH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        grid.add(backButton,0,8);


        Scene recipientProfileScene= new Scene(grid, 400, 400);
        window.setTitle("Recipient Profile");
        window.setScene(recipientProfileScene);
        window.show();


    }




}