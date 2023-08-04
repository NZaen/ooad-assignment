package com.example.newjavafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.scene.control.DatePicker;
/**
 * A VC class represents as a VC account to store various VC's details.
 *
 * @author Nik Muhammad Zaen bin Nik Ram Zaedi
 */
public class VC {
    private String name = "Default";
    private int capDay = 0;                                                         // Capacity per day
    public ArrayList<Account> distributedRecipients = new ArrayList<Account>();    // Recipients for the current VC
    public ArrayList<LocalDate> dateAppointments = new ArrayList<>();               //Stores the dates of all apointments in VC

    private int vaccine = 0;
                 // Record the appointment dates


    /**
     * Constructs a VC account with name as "Default", capacity per day as 0, vaccine as 0.
     */
    public VC() {
    }

    /**
     * Constructs a VC account with specified name, capacity per day as 0, vaccine as 0.
     *
     * @param name the specified name
     */
    public VC(String name) {
        this.name = name;
    }

    /**
     * Constructs a VC account with specified name, specified capacity per day, vaccine as 0.
     *
     * @param name   the specified name
     * @param capDay the specified capacity per day
     */
    public VC(String name, int capDay) {
        this.name = name;
        this.capDay = capDay;


    }

    /**
     * Set VC's name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set VC's capacity per day.
     *
     * @param capDay the new capacity per day
     */
    public void setCapDay(int capDay) {
        this.capDay = capDay;
    }

    /**
     * Add an ammount of vaccines to this VC.
     *
     * @param vaccine the amount of vaccines that will be added
     */
    public void addVaccine(int vaccine) {
        this.vaccine += vaccine;
    }

    /**
     * Sets amount of vaccines to this VC
     *
     * @param vaccine the amount of vaccines that will be added
     */
    public void setVaccine(int vaccine) {
        this.vaccine = vaccine;
    }



    /**
     * Returns the VC's name.
     *
     * @return VC's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the VC's capacity per day.
     *
     * @return VC's capacity per day
     */
    public int getCapDay() {
        return capDay;
    }



    /**
     * Returns the VC's ammount of vaccines.
     *
     * @return VC's ammount of vaccines
     */
    public int getVaccine() {
        return vaccine;
    }

    /**
     * Save the updated details of distributed recipients to the main recipient accounts.
     * @param VCs ArrayList containing all Vaccination Centers
     * @param recipients the main recipient accounts
     */

    public void saveToSystem(ArrayList<VC> VCs, ArrayList<Account> recipients){
        for (int i=0; i < VCs.size(); i++) {
            for (int j =0; j < recipients.size(); j++) {
                for (int k =0; k < VCs.get(i).distributedRecipients.size(); k++)
                    if (VCs.get(i).distributedRecipients.get(k).getName().equals(recipients.get(j).getName()) || VCs.get(i).distributedRecipients.get(k).getPhoneNum() == recipients.get(j).getPhoneNum()) {
                        recipients.get(j).setSBatch(VCs.get(i).distributedRecipients.get(k).getSBatch());
                        recipients.get(j).setFBatch(VCs.get(i).distributedRecipients.get(k).getFBatch());
                        recipients.get(j).setVacDate(VCs.get(i).distributedRecipients.get(k).getVacDate());
                        recipients.get(j).setFDoseDate(VCs.get(i).distributedRecipients.get(k).getFDoseDate());
                        recipients.get(j).setSDoseDate(VCs.get(i).distributedRecipients.get(k).getSDoseDate());
                    }
            }
        }
    }
}



class vcMenu {
    /**
     * Choose which VC to view for vcMenu
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */
    public static void chooseVCMenu (Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH){
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 40, 10, 40));
        grid.setVgap(10);
        grid.setHgap(10);

        Label label = new Label("Which VC would you like to access?");


        //adding all VCs into comboBox
        ComboBox vcComboBox = new ComboBox();

        for (int i =0; i< VCs.size(); i++) {
            vcComboBox.getItems().add(VCs.get(i).getName());
        }

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            for (int i=0; i < VCs.size(); i++) {
                if (VCs.get(i).getName().equals(vcComboBox.getValue())) {
                    vcMenu.vcMainMenu(window, recipients, VCs, accountMOH, i);
                }
            }
        });

        HBox buttons = new HBox(10);
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            try {
                Main.mainMenu(window, recipients, VCs, accountMOH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        buttons.getChildren().addAll(backButton,submitButton);

        grid.add(label,0,0);
        grid.add(vcComboBox,0,1);
        grid.add(buttons,0,2);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        grid.getColumnConstraints().addAll(col1);

        vcComboBox.setMaxWidth(Double.MAX_VALUE);

        Scene viewAllDataScene = new Scene(grid,550, 200);

        window.setScene(viewAllDataScene);
        window.show();
    }

    /**
     * Displays main menu of VC
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     * @param activeVC the selected VC
     */
    public static void vcMainMenu(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH, int activeVC) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 40, 10, 40));
        grid.setVgap(10);
        grid.setHgap(10);

        //save distributed data to recipients
        VCs.get(activeVC).saveToSystem(VCs,recipients);

        Label title = new Label("Welcome to "+ VCs.get(activeVC).getName() + " Vaccination Center (VC) Menu");
        title.setWrapText(true);

        Button viewRecipientButton = new Button("View recipient");
        viewRecipientButton.setOnAction(e -> {
                vcMenu.viewRecipient(window, recipients, VCs, accountMOH, activeVC);
        });

        Button setAppointmentDateButton = new Button("Set Appointment Date");
        setAppointmentDateButton.setOnAction(e -> {
            vcMenu.setAppDate(window, recipients, VCs, accountMOH, activeVC);
                });

        Button updateRecipientStatButton = new Button("Update Recipient's Status");
        updateRecipientStatButton.setOnAction(e -> {
            vcMenu.updateStatus(window, recipients, VCs, accountMOH, activeVC);
        });

        Button viewVCStatsButton = new Button("View VC Statistics");
        viewVCStatsButton.setOnAction(e -> {
            vcMenu.viewVCStats(window, recipients, VCs, accountMOH, activeVC);
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            try {
                Main.mainMenu(window, recipients, VCs, accountMOH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        grid.add(title, 0, 0);
        grid.add(viewRecipientButton, 0, 2);
        grid.add(setAppointmentDateButton, 0, 3);
        grid.add(updateRecipientStatButton, 0, 4);
        grid.add(viewVCStatsButton, 0, 5);
        grid.add(backButton, 0, 6);

        viewRecipientButton.setMaxWidth(Double.MAX_VALUE);
        setAppointmentDateButton.setMaxWidth(Double.MAX_VALUE);
        updateRecipientStatButton.setMaxWidth(Double.MAX_VALUE);
        viewVCStatsButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        grid.getColumnConstraints().addAll(col1);

        Scene vcMenuScene = new Scene(grid, 300, 400);
        window.setTitle("VC Menu");
        window.setScene(vcMenuScene);
        window.show();

    }
    /**
     * Displays the table of recipients for each index
     *
     * @param VCs ArrayList containing all Vaccination Centers
     * @param index the current index of the pagination
     * @param activeVC the selected VC
     */
    public static TableView createTable (ArrayList<VC> VCs, int index, int activeVC) {
        //Tableview stores data from recipients in tabular form
        TableView tableView = new TableView();

        TableColumn<Account, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Account, String> phoneNumColumn = new TableColumn<>("Phone Number");
        phoneNumColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));

        TableColumn<Account, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Account, String>vacStatusColumn = new TableColumn<>("Vaccination Status");
        vacStatusColumn.setCellValueFactory(new PropertyValueFactory<>("vacStatus"));

        TableColumn<Account, LocalDate>vacDateColumn = new TableColumn<>("Appointment Date");
        vacDateColumn.setCellValueFactory(new PropertyValueFactory<>("vacDate"));

        TableColumn<Account, LocalDate>FDoseColumn = new TableColumn<>("First Dose Date");
        FDoseColumn.setCellValueFactory(new PropertyValueFactory<>("FDoseDate"));

        TableColumn<Account, LocalDate>SDoseColumn = new TableColumn<>("Second Dose Date");
        SDoseColumn.setCellValueFactory(new PropertyValueFactory<>("SDoseDate"));


        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(phoneNumColumn);
        tableView.getColumns().add(ageColumn);
        tableView.getColumns().add(vacStatusColumn);
        tableView.getColumns().add(vacDateColumn);
        tableView.getColumns().add(FDoseColumn);
        tableView.getColumns().add(SDoseColumn);


        int fromIndex = index * 15;
        int toIndex = Math.min(fromIndex + 15,VCs.get(activeVC).distributedRecipients.size());

        //add all objects from recipients ArrayList into table
        while (fromIndex < toIndex) {
            tableView.getItems().add(VCs.get(activeVC).distributedRecipients.get(fromIndex));
            fromIndex++;
        }
        return tableView;
    }

    /**
     * Displays the table for all recipients in this VC
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     * @param activeVC the selected VC
     */
    public static void viewRecipient(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH, int activeVC) {
        System.out.println(recipients.get(0).getFDoseDate());
        System.out.println(recipients.get(0).getVacDate());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0,0,0,0));
        grid.setVgap(10);
        grid.setHgap(10);



        Pagination pagination = new Pagination(VCs.get(activeVC).distributedRecipients.size() / 15 + 1, 0);
        pagination.setPageFactory((Integer pageIndex) -> createTable(VCs, pageIndex, activeVC));

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> vcMenu.vcMainMenu(window, recipients, VCs, accountMOH,activeVC));


        grid.add(pagination,0,0);
        grid.add(backButton,0,1);

        pagination.setMaxWidth(Double.MAX_VALUE);
        backButton.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100);
        grid.getColumnConstraints().addAll(col1);

        Scene viewAllDataScene = new Scene(grid,675, 475);

        window.setScene(viewAllDataScene);
        window.show();
    }
    /**
     * Set the appointment date for selected number of recipients
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     * @param activeVC the selected VC
     */
    public static void setAppDate(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH, int activeVC) {
        int test =0;
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0,0,0,0));
        grid.setVgap(10);
        grid.setHgap(10);

        Label label = new Label("Set appointment Date");

        Label numRecipientsLabel = new Label("Number of recipients");
        Label recipientDate = new Label("Selected Date : None");
        TextField numRecipients = new TextField();

        DatePicker datePicker = new DatePicker();

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // get the date picker value
                LocalDate i = datePicker.getValue();

                // get the selected date
                recipientDate.setText(i.toString());
            }
        };

        datePicker.setOnAction(event);

        HBox hbox = new HBox(datePicker);

        Label message = new Label();
        message.setWrapText(true);
        message.setPadding(new Insets(10,0,10,0));

        HBox buttons = new HBox(10);
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> vcMenu.vcMainMenu(window, recipients, VCs, accountMOH,activeVC));

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            if (numRecipients.getText().isEmpty() || recipientDate.getText().equals("Selected Date : None")) {
                message.setText("Error : One or more fields are empty!");
            }
            else{
                if (isInt(numRecipients.getText())) {
                    int counter = 0;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(recipientDate.getText(),formatter);

                    //count how many appointments are on that date
                    int count = Integer.parseInt(numRecipients.getText());
                    for (int i =0; i< VCs.get(activeVC).dateAppointments.size(); i++) {
                        if (localDate.equals(VCs.get(activeVC).dateAppointments.get(i))) {
                            count++;
                        }
                    }
                    //loop to count how many recipients available for appointment
                    int availableCounter = 0;
                    for (int i = 0; i < VCs.get(activeVC).distributedRecipients.size(); i++) {
                        if (VCs.get(activeVC).distributedRecipients.get(i).getVacStatus().equals("Pending") || (VCs.get(activeVC).distributedRecipients.get(i).getVacStatus().equals("1st Dose completed"))) {
                            availableCounter++;
                        }
                    }

                    //check if input recipient is less than available recipient
                    if (Integer.parseInt(numRecipients.getText()) <= availableCounter) {
                        System.out.println(availableCounter);
                        //if recipient amount exceeds the daily cap
                        if (count <= VCs.get(activeVC).getCapDay()) {
                            System.out.println(VCs.get(activeVC).getCapDay());
                            System.out.println(count);
                            for (int i = 0; i < VCs.get(activeVC).distributedRecipients.size(); i++) {
                                if (counter == Integer.parseInt(numRecipients.getText())) {
                                    break;
                                } else if (VCs.get(activeVC).distributedRecipients.get(i).getVacStatus().equals("Pending")) {         // Set for 1st appointment
                                    message.setText("Appointment successfully registered.");
                                    VCs.get(activeVC).distributedRecipients.get(i).setVacStatus("1st Dose appointment");
                                    VCs.get(activeVC).distributedRecipients.get(i).setVacDate(localDate);
                                    VCs.get(activeVC).dateAppointments.add(localDate);
                                    VCs.get(activeVC).saveToSystem(VCs,recipients);
                                    counter++;
                                } else if (VCs.get(activeVC).distributedRecipients.get(i).getVacStatus().equals("1st Dose completed")) { // Set for 2nd appointment
                                    message.setText("Appointment successfully registered.");
                                    VCs.get(activeVC).distributedRecipients.get(i).setVacStatus("2nd Dose appointment");
                                    VCs.get(activeVC).distributedRecipients.get(i).setVacDate(localDate);
                                    VCs.get(activeVC).dateAppointments.add(localDate);
                                    VCs.get(activeVC).saveToSystem(VCs,recipients);
                                    counter++;
                                } else {
                                    continue;
                                }
                            }
                        } else {
                            System.out.println(count);
                            int count2 = 0;
                            for (int i = 0; i < VCs.get(activeVC).dateAppointments.size(); i++) {
                                if (localDate.equals(VCs.get(activeVC).dateAppointments.get(i))) {
                                    count2++;
                                }
                            }
                            message.setText("Error : VC is full , it can only support " + (VCs.get(activeVC).getCapDay() - count2) + " more recipients");
                        }
                    }else {
                        System.out.println(availableCounter);
                        message.setText("Error : There are only " + (availableCounter) + " recipients available");
                    }
                }
                else {
                    message.setText("Error : Please input an integer!");
                }
            }
        });

        buttons.getChildren().addAll(backButton,submitButton);

        grid.add(label,0,0);
        grid.add(numRecipientsLabel,0,2);
        grid.add(numRecipients,0,3);

        grid.add(recipientDate,1,2);
        grid.add(hbox,1,3);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(grid, message, buttons);
        vbox.setPadding(new Insets(10,40,10,40));


        Scene viewAllDataScene = new Scene(vbox,550, 200);

        window.setScene(viewAllDataScene);
        window.show();
    }

    /**
     * Determines if input is an integer
     *
     * @param input Can be any String

     */
    public static boolean isInt(String input) {
        boolean flag = true;
        for ( int i=0 ; i < input.length(); i++)
        {
            flag = Character.isDigit(input.charAt(i));
            if (flag == false) {
                flag = false;
                break;
            }
        }
        return flag;

    }

    /**
     * Updates status for selected recipient
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     * @param activeVC the selected VC
     */
    public static void updateStatus(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH, int activeVC) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 40, 10, 40));
        grid.setVgap(10);
        grid.setHgap(10);

        Label label = new Label("Choose a recipient to update");

        Label recipientLabel = new Label("Recipient");
        //adding all VCs into comboBox
        ComboBox recipientComboBox = new ComboBox();

        for (int i =0; i< VCs.get(activeVC).distributedRecipients.size(); i++) {
            recipientComboBox.getItems().add(VCs.get(activeVC).distributedRecipients.get(i).getName());
        }

        Label message = new Label();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {

            if (recipientComboBox.getValue() == null) {
                message.setText("Error : Please select a recipient");
            }
            else {
                for (int i =0; i< VCs.get(activeVC).distributedRecipients.size(); i++) {
                    if(recipientComboBox.getValue().equals(VCs.get(activeVC).distributedRecipients.get(i).getName())) {
                        if ((VCs.get(activeVC).distributedRecipients.get(i).getVacStatus().equals("1st Dose appointment"))) {
                            if (VCs.get(activeVC).getVaccine() > 0) {
                                VCs.get(activeVC).distributedRecipients.get(i).setVacStatus("1st Dose completed");
                                VCs.get(activeVC).distributedRecipients.get(i).setFDoseDate(VCs.get(activeVC).distributedRecipients.get(i).getVacDate());
                                VCs.get(activeVC).distributedRecipients.get(i).setVacDate(null);
                                VCs.get(activeVC).distributedRecipients.get(i).setFBatch(accountMOH.getBatch());
                                accountMOH.addBatch();
                                VCs.get(activeVC).setVaccine(VCs.get(activeVC).getVaccine() - 1);
                                message.setText(VCs.get(activeVC).distributedRecipients.get(i).getName() + "'s 1st dose completed.");

                            } else {
                                message.setText(VCs.get(activeVC).distributedRecipients.get(i).getName() + " cannot receive dose because the vaccine has run out of stock.");
                            }
                        } else if (VCs.get(activeVC).distributedRecipients.get(i).getVacStatus().equals("2nd Dose appointment")) {
                            if (VCs.get(activeVC).getVaccine() > 0) {
                                VCs.get(activeVC).distributedRecipients.get(i).setVacStatus("2nd Dose completed");
                                VCs.get(activeVC).distributedRecipients.get(i).setSDoseDate(VCs.get(activeVC).distributedRecipients.get(i).getVacDate());
                                VCs.get(activeVC).distributedRecipients.get(i).setVacDate(null);
                                VCs.get(activeVC).distributedRecipients.get(i).setSBatch(accountMOH.getBatch());
                                accountMOH.addBatch();
                                VCs.get(activeVC).setVaccine(VCs.get(activeVC).getVaccine() - 1);
                                message.setText(VCs.get(activeVC).distributedRecipients.get(i).getName() + "'s 2nd dose completed.");

                            } else {
                                message.setText(VCs.get(activeVC).distributedRecipients.get(i).getName() + " cannot receive dose because the vaccine has run out of stock.");
                            }
                        } else {
                            message.setText(VCs.get(activeVC).distributedRecipients.get(i).getName() + "'s status cannot be updated.");
                        }
                    }

                }
            }



        });

        HBox buttons = new HBox(10);
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
                vcMenu.vcMainMenu(window, recipients, VCs, accountMOH,activeVC);
        });


        buttons.getChildren().addAll(backButton,submitButton);

        grid.add(label,0,0);
        grid.add(recipientComboBox,0,1);
        grid.add(message,0,2);
        grid.add(buttons,0,3);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        grid.getColumnConstraints().addAll(col1);

        recipientComboBox.setMaxWidth(Double.MAX_VALUE);


        Scene updateStatusScene = new Scene(grid,550, 200);
        window.setScene(updateStatusScene);
        window.show();

    }



    /**
     * View Stats of the VC
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     * @param activeVC the selected VC
     */
    public static void viewVCStats(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH, int activeVC) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 40, 10, 40));
        grid.setVgap(10);
        grid.setHgap(10);

        Label vcNameLabel = new Label("VC Name");
        Label totalVaccinationLabel = new Label("Total Vaccine available");
        Label capacityDayLabel = new Label("Capacity per day");
        Label totalRecipientsLabel = new Label("Total Recipients");
        Label totalYRecipientsLabel = new Label("Total Young Recipients");
        Label totalERecipientsLabel = new Label("Total Elderly Recipients");
        Label fDoseCompletedLabel = new Label("1st Dose Completed");
        Label sDoseCompletedLabel = new Label("2nd Dose Completed");
        Label vacCompletedLabel = new Label("Vaccinations completed");

        grid.add(vcNameLabel,0,0);
        grid.add(totalVaccinationLabel,0,1);
        grid.add(capacityDayLabel,0,2);
        grid.add(totalRecipientsLabel,0,3);
        grid.add(totalYRecipientsLabel,0,4);
        grid.add(totalERecipientsLabel,0,5);
        grid.add(fDoseCompletedLabel,0,6);
        grid.add(sDoseCompletedLabel,0,7);
        grid.add(vacCompletedLabel,0,8);

        Label vcName = new Label(VCs.get(activeVC).getName());
        Label totalVaccination = new Label((String.valueOf(VCs.get(activeVC).getVaccine())));

        Label capacityDay = new Label(String.valueOf(VCs.get(activeVC).getCapDay()));
        Label totalRecipients = new Label(String.valueOf(VCs.get(activeVC).distributedRecipients.size()));

        int vacCom = 0;     // Total vaccinations
        int Fdose = 0;      // First dose completed
        int Sdose = 0;      // Second dose completed
        int young = 0;      // Total recipient < 60
        int old = 0;        // Total recipient >= 60

        // Calculate total vaccinations, total 1st dose, total 2nd dose
        for(int i=0; i < VCs.get(activeVC).distributedRecipients.size(); i++){
            if(VCs.get(activeVC).distributedRecipients.get(i).getVacStatus().equals("1st Dose completed") ||
                    VCs.get(activeVC).distributedRecipients.get(i).getVacStatus().equals("2nd Dose appointment")){
                vacCom++;
                Fdose++;
            }
            else if(VCs.get(activeVC).distributedRecipients.get(i).getVacStatus().equals("2nd Dose completed")){
                vacCom += 2;
                Fdose++;
                Sdose++;
            }

            if(VCs.get(activeVC).distributedRecipients.get(i).getAge() < 60)
                young++;
            else
                old++;
        }

        Label totalYRecipients = new Label(String.valueOf(young));
        Label totalERecipients = new Label(String.valueOf(old));
        Label fDoseCompleted = new Label(String.valueOf(Fdose));
        Label sDoseCompleted = new Label(String.valueOf(Sdose));
        Label vacCompleted = new Label(String.valueOf(vacCom));

        grid.add(vcName,1,0);
        grid.add(totalVaccination,1,1);
        grid.add(capacityDay,1,2);
        grid.add(totalRecipients,1,3);
        grid.add(totalYRecipients,1,4);
        grid.add(totalERecipients,1,5);
        grid.add(fDoseCompleted,1,6);
        grid.add(sDoseCompleted,1,7);
        grid.add(vacCompleted,1,8);

        HBox hbox = new HBox(10);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
                vcMenu.vcMainMenu(window, recipients, VCs, accountMOH,activeVC);
        });

        Button viewByDayButton = new Button("View By Day");
        viewByDayButton.setOnAction(e -> {
            vcMenu.viewByDay(window, recipients, VCs, accountMOH,activeVC);
        });

        hbox.getChildren().addAll(backButton,viewByDayButton);
        grid.add(hbox,0,9);

        Scene viewVCStatsScene = new Scene(grid,550, 300);

        window.setScene(viewVCStatsScene);
        window.show();
    }

    /**
     * View number of vaccinations in the vc by day
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     * @param activeVC the selected VC
     */
    public static void viewByDay(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH, int activeVC) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 40, 10, 40));
        grid.setVgap(10);
        grid.setHgap(10);

        Label chooseDateLabel = new Label("Selected Date : None");
        Label numVacLabel = new Label("Number of Vaccinations");
        Label numVac = new Label("");

        DatePicker datePicker = new DatePicker();

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // get the date picker value
                LocalDate localDate = datePicker.getValue();
                int counter = 0;

                for (int i = 0; i< VCs.get(activeVC).distributedRecipients.size(); i++) {
                    if (localDate.equals(VCs.get(activeVC).distributedRecipients.get(i).getFDoseDate())) {
                        counter++;
                    }
                    if (localDate.equals(VCs.get(activeVC).distributedRecipients.get(i).getSDoseDate())) {
                        counter++;
                    }
                }


                numVac.setText(String.valueOf(counter));
            }
        };

        datePicker.setOnAction(event);



        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            vcMenu.vcMainMenu(window, recipients, VCs, accountMOH,activeVC);
        });

        grid.add(chooseDateLabel,0,0);
        grid.add(numVacLabel,1,0);

        grid.add(datePicker,0,1);
        grid.add(numVac,1,1);

        grid.add(backButton,0,2);



        Scene viewVCStatsByDayScene = new Scene(grid,550, 150);

        window.setScene(viewVCStatsByDayScene);
        window.show();
    }
}


