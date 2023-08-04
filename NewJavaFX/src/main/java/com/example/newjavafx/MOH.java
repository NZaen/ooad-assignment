package com.example.newjavafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
/**
 * A MOH class represents as a MOH account to store how many/which recipients have been distributed.
 *
 * @author Friendy
 */
public class MOH{
    private int distributeCounter = 0;   // To tell how many/which recipients have been distributed
    private int batch = 25000;           // To identify each vaccine with a unique batch number(start from batch number 25000)
    // Create MOH Account
    public static MOH accountMOH = new MOH();

    /**
     * Constructs a MOH account with distribute counter as 0.
     */
    public MOH(){}
    /**
     * Increment the distribute counter of this MOH.
     */
    public void addDistributeCounter(){
        distributeCounter++;
    }
    /**
     * Returns the distribute counter of this MOH.
     * @return distribute counter of this MOH
     */
    public int getDistributeCounter(){
        return distributeCounter;
    }

    /**
     * Increment the batch number.
     */
    public void addBatch(){
        batch++;
    }


    /**
     * Returns the batch number.
     * @return batch number
     */
    public int getBatch(){
        return batch;
    }
}
class mohMenu{
    /**
     * Displays the main menu of MOH
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */
    public static void mohMainMenu(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,40,10,40));
        grid.setVgap(10);
        grid.setHgap(10);

        // Moh Main Menu Gridpane elements
        Label title = new Label("Welcome to Ministry of Health(MOH) Menu");
        title.setWrapText(true);

        Button viewAllDataButton = new Button("View all recipient data");
        viewAllDataButton.setOnAction(e -> {
            mohMenu.viewAllData(window, recipients, VCs, accountMOH);
        });
        
        Button distributeVacButton = new Button("Distribute vaccines");
        distributeVacButton.setOnAction(e -> {
            mohMenu.distributeVac(window,recipients,VCs,accountMOH);
        });

        Button distributeRecipientButton = new Button("Distribute recipients");
        distributeRecipientButton.setOnAction(e -> {
            mohMenu.distributeRecipient(window,recipients,VCs,accountMOH);
        });

        Button viewVacStatsButton = new Button("View vaccination statistics");
        viewVacStatsButton.setOnAction(e -> {
            mohMenu.viewStats(window,recipients,VCs,accountMOH);
        });
        
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            try {
                Main.mainMenu(window,recipients,VCs,accountMOH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        //add elements into gridpane
        grid.add(title,0,0);
        grid.add(viewAllDataButton,0,2);
        grid.add(distributeVacButton,0,3);
        grid.add(distributeRecipientButton,0,4);
        grid.add(viewVacStatsButton,0,5);
        grid.add(backButton,0,6);

        //formatting
        viewAllDataButton.setMaxWidth(Double.MAX_VALUE);
        distributeVacButton.setMaxWidth(Double.MAX_VALUE);
        distributeRecipientButton.setMaxWidth(Double.MAX_VALUE);
        viewVacStatsButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        grid.getColumnConstraints().addAll(col1);

        Scene mohMenuScene = new Scene(grid, 300, 400);
        window.setTitle("MOH Menu");
        window.setScene(mohMenuScene);
        window.show();

    }
    /**
     * Displays the table of recipients for each index
     *
     * @param recipients ArrayList containing all imported recipients
     * @param index the current index of the pagination
     */
    public static TableView createTable (ArrayList<Account> recipients, int index) {
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
        int toIndex = Math.min(fromIndex + 15,recipients.size());

        //add all objects from recipients ArrayList into table
        while (fromIndex < toIndex) {
            tableView.getItems().add(recipients.get(fromIndex));
            fromIndex++;
        }
        return tableView;
    }

    /**
     * Displays the table for all recipients
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */
    public static void viewAllData(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH) {
        System.out.println(recipients.get(0).getFDoseDate());
        System.out.println(recipients.get(0).getVacDate());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0,0,0,0));
        grid.setVgap(10);
        grid.setHgap(10);

        //Pagination to divide table by 15 data instead of viewing all at once
        Pagination pagination = new Pagination((recipients.size() / 15 + 1), 0);
        pagination.setPageFactory((Integer pageIndex) -> createTable(recipients, pageIndex));

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {

            mohMenu.mohMainMenu(window, recipients, VCs, accountMOH);
        });

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
     *Distribute selected number of vaccines to selected VC
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */
    public static void distributeVac(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0,0,0,0));
        grid.setVgap(10);
        grid.setHgap(10);

        Label label = new Label("Distribute Vaccine");

        Label vcLabel = new Label("VC");
        //adding all VCs into comboBox
        ComboBox vcComboBox = new ComboBox();

        for (int i =0; i< VCs.size(); i++) {
            vcComboBox.getItems().add(VCs.get(i).getName());
        }

        Label distributeVCLabel = new Label("Number of vaccines to distribute");
        TextField distributeVCField = new TextField();

        Label message = new Label();
        message.setWrapText(true);
        message.setPadding(new Insets(10,0,10,0));

        HBox buttons = new HBox(10);
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> mohMenu.mohMainMenu(window, recipients, VCs, accountMOH));

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            if (distributeVCField.getText().isEmpty() || (vcComboBox.getValue() == null)) {
                message.setText("Error : One or more fields are empty!");
            }
            else {
                if (isInt(distributeVCField.getText())) {
                    for (int i=0; i < VCs.size(); i++) {
                        if (VCs.get(i).getName().equals(vcComboBox.getValue())) {
                            message.setText(distributeVCField.getText() + " vaccines successfully distributed to " + VCs.get(i).getName());
                            for (int j = 0; j < Integer.parseInt(distributeVCField.getText()); j++) {
                                VCs.get(i).addVaccine(1);
                            }
                        }
                    }
                }
                else {
                    message.setText("Error : Please input an integer!");
                }
            }


        });

        buttons.getChildren().addAll(backButton,submitButton);

        grid.add(label,0,0);
        grid.add(vcLabel,0,2);
        grid.add(vcComboBox,0,3);

        grid.add(distributeVCLabel,1,2);
        grid.add(distributeVCField,1,3);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(grid, message, buttons);
        vbox.setPadding(new Insets(10,40,10,40));


        Scene viewAllDataScene = new Scene(vbox,550, 200);
        window.setTitle("Distribute Vaccine Menu");
        window.setScene(viewAllDataScene);
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
     *Distribute selected number of recipients to selected VC
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */
    public static void distributeRecipient(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0,0,0,0));
        grid.setVgap(10);
        grid.setHgap(10);

        Label label = new Label("Distribute Recipient");

        Label vcLabel = new Label("VC");
        //adding all VCs into comboBox
        ComboBox vcComboBox = new ComboBox();

        for (int i =0; i< VCs.size(); i++) {
            vcComboBox.getItems().add(VCs.get(i).getName());
        }

        Label distributeVCLabel = new Label("Number of recipients to distribute");
        TextField distributeVCField = new TextField();

        Label message = new Label();
        message.setWrapText(true);
        message.setPadding(new Insets(10,0,10,0));

        HBox buttons = new HBox(10);
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> mohMenu.mohMainMenu(window, recipients, VCs, accountMOH));

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            if (distributeVCField.getText().isEmpty() || (vcComboBox.getValue() == null)) {
                message.setText("Error : One or more fields are empty!");
            }
            else {
                //Integer error handling
                if (isInt(distributeVCField.getText())) {
                    for (int i=0; i < VCs.size(); i++) {
                        if (VCs.get(i).getName().equals(vcComboBox.getValue())) {
                            //Check if MOH has enough undistributed recipients
                            if ((recipients.size() - VCs.get(i).distributedRecipients.size()) - Integer.parseInt(distributeVCField.getText()) >= 0) {
                                message.setText(distributeVCField.getText() + " recipients to " + VCs.get(i).getName());
                                for (int j = 0; j < Integer.parseInt(distributeVCField.getText()); j++) {
                                    VCs.get(i).distributedRecipients.add(recipients.get(accountMOH.getDistributeCounter()));
                                    accountMOH.addDistributeCounter();
                                }
                            } else {
                                int balance = (recipients.size() - VCs.get(i).distributedRecipients.size());
                                message.setText("MOH only has " +  balance + " recipients left!");
                            }
                        }

                    }
                } else {
                    message.setText("Error : Please input an integer!");
                }
            }
        });

        buttons.getChildren().addAll(backButton,submitButton);

        grid.add(label,0,0);
        grid.add(vcLabel,0,2);
        grid.add(vcComboBox,0,3);

        grid.add(distributeVCLabel,1,2);
        grid.add(distributeVCField,1,3);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(grid, message, buttons);
        vbox.setPadding(new Insets(10,40,10,40));


        Scene viewAllDataScene = new Scene(vbox,550, 200);

        window.setScene(viewAllDataScene);
        window.setTitle("Distribute Recipient Menu");
        window.show();
    }
    /**
     *View all stats of the MOH
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */
    public static void viewStats(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 40, 10, 40));
        grid.setVgap(10);
        grid.setHgap(10);


        Label totalVCLabel = new Label("Total Vaccination Centers");
        Label totalVaccinationLabel = new Label("Total Vaccines Distributed");
        Label totalRecipientsLabel = new Label("Total Recipients");
        Label totalYRecipientsLabel = new Label("Total Young Recipients");
        Label totalERecipientsLabel = new Label("Total Elderly Recipients");
        Label fDoseCompletedLabel = new Label("Total 1st Dose Completed");
        Label sDoseCompletedLabel = new Label("Total 2nd Dose Completed");
        Label vacCompletedLabel = new Label("Total Vaccinations Completed");

        grid.add(totalVCLabel,0,0);
        grid.add(totalRecipientsLabel,0,1);
        grid.add(totalYRecipientsLabel,0,2);
        grid.add(totalERecipientsLabel,0,3);
        grid.add(fDoseCompletedLabel,0,4);
        grid.add(sDoseCompletedLabel,0,5);
        grid.add(vacCompletedLabel,0,6);

        int Fdose = 0;      // First dose completed
        int Sdose = 0;      // Second dose completed
        int young = 0;      // Total recipient < 60
        int old = 0;        // Total recipient >= 60

        // Calculate total vaccinations, total 1st dose, total 2nd dose
        for(int j=0; j < VCs.size(); j++) {
            for(int i=0; i < VCs.get(j).distributedRecipients.size(); i++){
                if(VCs.get(j).distributedRecipients.get(i).getVacStatus().equals("1st Dose completed") ||
                        VCs.get(j).distributedRecipients.get(i).getVacStatus().equals("2nd Dose appointment")){
                    Fdose++;
                }
                else if(VCs.get(j).distributedRecipients.get(i).getVacStatus().equals("2nd Dose completed")){
                    Fdose++;
                    Sdose++;
                }
            }
        }

        for (int i = 0; i< recipients.size(); i++) {
            if(recipients.get(i).getAge() < 60)
                young++;
            else
                old++;
        }

        Label totalVC = new Label(String.valueOf(VCs.size()));
        Label totalRecipients = new Label(String.valueOf(young+old));
        Label totalYRecipients = new Label(String.valueOf(young));
        Label totalERecipients = new Label(String.valueOf(old));
        Label fDoseCompleted = new Label(String.valueOf(Fdose));
        Label sDoseCompleted = new Label(String.valueOf(Sdose));
        Label vacCompleted = new Label(String.valueOf(Fdose+Sdose));

        grid.add(totalVC,1,0);
        grid.add(totalRecipients,1,1);
        grid.add(totalYRecipients,1,2);
        grid.add(totalERecipients,1,3);
        grid.add(fDoseCompleted,1,4);
        grid.add(sDoseCompleted,1,5);
        grid.add(vacCompleted,1,6);

        HBox hbox = new HBox(10);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            mohMenu.mohMainMenu(window, recipients, VCs, accountMOH);
        });

        Button viewByDayButton = new Button("View By Day");
        viewByDayButton.setOnAction(e -> {
            mohMenu.viewByDay(window, recipients, VCs, accountMOH);
        });

        hbox.getChildren().addAll(backButton,viewByDayButton);
        grid.add(hbox,0,8);

        Scene viewMOHStatsScene = new Scene(grid,550, 270);

        window.setScene(viewMOHStatsScene);
        window.show();
    }
    /**
     *View number of vaccinations by day
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */
    public static void viewByDay(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 40, 10, 40));
        grid.setVgap(10);
        grid.setHgap(10);

        Label chooseDateLabel = new Label("Choose a Date");
        Label numVacLabel = new Label("Number of Vaccinations");
        Label numVac = new Label("");

        DatePicker datePicker = new DatePicker();

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // get the date picker value
                LocalDate localDate = datePicker.getValue();
                int counter = 0;
                for(int j=0; j < VCs.size(); j++) {
                    for (int i = 0; i< VCs.get(j).distributedRecipients.size(); i++) {
                        if (localDate.equals(VCs.get(j).distributedRecipients.get(i).getFDoseDate())) {
                            counter++;
                        }
                        if (localDate.equals(VCs.get(j).distributedRecipients.get(i).getSDoseDate())) {
                            counter++;
                        }
                    }
                }
                numVac.setText(String.valueOf(counter));
            }
        };

        datePicker.setOnAction(event);



        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            mohMenu.mohMainMenu(window, recipients, VCs, accountMOH);
        });

        grid.add(chooseDateLabel,0,0);
        grid.add(numVacLabel,1,0);

        grid.add(datePicker,0,1);
        grid.add(numVac,1,1);

        grid.add(backButton,0,2);



        Scene viewMOHStatsByDayScene = new Scene(grid,550, 150);

        window.setScene(viewMOHStatsByDayScene);
        window.show();
    }
}
