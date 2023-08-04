package com.example.newjavafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
/**
 * A Main class to initialise program and display main menu
 *
 * @author Nik Muhammad Zaen bin Nik Ram Zaedi
 */
public class Main extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Read Recipient accounts from file
        ArrayList<Account> recipients = Account.readAccountFromFile();

        // Create Vaccination Center Accounts
        ArrayList<VC> VCs =  new ArrayList<VC>();
        VCs.add(new VC("Sejahtera", 10));
        VCs.add(new VC("Mulia", 20));

        // Create MOH Account
        MOH accountMOH = new MOH();
        Stage window = primaryStage;
        mainMenu(window, recipients, VCs, accountMOH);


    }
    /**
     * Displays the main menu of the program
     *
     * @param window The stage of the program
     * @param recipients ArrayList containing all imported recipients
     * @param VCs ArrayList containing all Vaccination Centers
     * @param accountMOH main account for MOH
     */

    public static void mainMenu(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH) throws IOException {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,40,10,40));
        grid.setVgap(10);
        grid.setHgap(10);

        //Main Menu gridpane elements
        Label title = new Label("Welcome to Covid-19 Vaccination Program");
        title.setWrapText(true);
        Button recipientButton = new Button("Recipient");

        recipientButton.setOnAction(e -> {
            Recipient.recipientMainMenu(window,recipients,VCs,accountMOH);
        });
        Button mohButton = new Button("MOH");
        mohButton.setOnAction(e -> {
            mohMenu.mohMainMenu(window,recipients,VCs,accountMOH);
        });
        Button vcButton = new Button("VC");
        vcButton.setOnAction(e -> {
            vcMenu.chooseVCMenu(window,recipients,VCs,accountMOH);
        });
        Button vcHallButton = new Button("VC Hall Simulator");
        vcHallButton.setOnAction(e -> {
            VCHall.chooseVCMenu(window,recipients,VCs,accountMOH);
        });

        //insert elements into gridpane
        grid.add(title,0,0);
        grid.add(recipientButton,0,2);
        grid.add(mohButton,0,3);
        grid.add(vcButton,0,4);
        grid.add(vcHallButton,0,5);

        //formatting
        recipientButton.setMaxWidth(Double.MAX_VALUE);
        mohButton.setMaxWidth(Double.MAX_VALUE);
        vcButton.setMaxWidth(Double.MAX_VALUE);
        vcHallButton.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        grid.getColumnConstraints().addAll(col1);

        Scene mainMenuScene = new Scene(grid, 300, 400);
        window.setTitle("Main Menu");
        window.setScene(mainMenuScene);
        window.show();
    }








}