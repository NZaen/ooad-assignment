package com.example.newjavafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * A VCHall class simulates a VC Hall Simulator.
 *
 * @author Manzo Clarence
 */
public class VCHall {

    /**
     * Select VC and select date.
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
        Label vclabel = new Label("VC");
        Label dateLabel = new Label("No Date Selected");

        //adding all VCs into comboBox
        ComboBox vcComboBox = new ComboBox();

        for (int i =0; i< VCs.size(); i++) {
            vcComboBox.getItems().add(VCs.get(i).getName());
        }


        Label message =  new Label();
        Button submitButton = new Button("Submit");


        HBox buttons = new HBox(10);
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            try {
                Main.mainMenu(window, recipients, VCs, accountMOH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        DatePicker datePicker = new DatePicker();

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // get the date picker value
                LocalDate i = datePicker.getValue();

                // get the selected date
                dateLabel.setText(i.toString());
            }
        };

        datePicker.setOnAction(event);

        submitButton.setOnAction(e -> {
            if (vcComboBox.getValue() == null || dateLabel.getText().equals("No Date Selected")) {
                message.setText("Error : One or more fields are empty!");
            }
            else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(dateLabel.getText(),formatter);
                for (int i=0; i < VCs.size(); i++) {
                    if (VCs.get(i).getName().equals(vcComboBox.getValue())) {
                        try {
                            VCHall.beginSimulator(window, recipients, VCs, accountMOH, i, localDate);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
                });

        buttons.getChildren().addAll(backButton,submitButton);

        grid.add(label,0,0);

        grid.add(vclabel,0,1);
        grid.add(dateLabel,1,1);

        grid.add(vcComboBox,0,2);
        grid.add(datePicker,1,2);
        grid.add(message,0,3);
        grid.add(buttons,0,4);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1);
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col2);

        vcComboBox.setMaxWidth(Double.MAX_VALUE);

        Scene chooseVCScene = new Scene(grid,550, 200);

        window.setTitle("VC Hall Menu");
        window.setScene(chooseVCScene);
        window.show();
    }
    /**
     * Add recipients of the selected VC with the selected date to the queue and add vaccines with unique batches to the stack.
     *
     * @param window The stage of the program
     * @param recipients All recipients
     * @param VCs Arraylist of VCs
     * @param accountMOH The MOH Account
     * @param activeVC the selected VC
     * @param localDate the selected date
     */
    public static void beginSimulator(Stage window, ArrayList<Account> recipients, ArrayList<VC> VCs, MOH accountMOH, int activeVC, LocalDate localDate) throws IOException {
        Queue<Account> queue = new LinkedList<>();
        Stack<Integer> vaccineStack = new Stack<>();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 40, 10, 40));
        grid.setVgap(10);
        grid.setHgap(10);


        // Add recipients of the current VC with the selected appointment date to the queue
        for(int i=0; i < VCs.get(activeVC).distributedRecipients.size(); i++){
            if(localDate.equals(VCs.get(activeVC).distributedRecipients.get(i).getVacDate()))
                queue.offer(VCs.get(activeVC).distributedRecipients.get(i));
        }

        // Add vaccines with unique batches to the stack
        for(int i=0; i < queue.size(); i++){
            vaccineStack.push(accountMOH.getBatch());
            accountMOH.addBatch();
        }

        Label label1 = new Label("Selected date : " + localDate.toString());

        Label label2 = new Label("Assigned recipients and their age");
        Label queueLabel = new Label();
        queueLabel.setWrapText(true);

        String placeholder = "";
        if(queue.size() != 0){
            for(Account recipient : queue){
                placeholder += ("[" + recipient.getName() + "," + recipient.getAge() + "] ");
            }
            queueLabel.setText(placeholder);
        }else {
            queueLabel.setText("Empty");
        }

        Label label3 = new Label("Stack of Vaccines");
        Label vacStack = new Label();
        vacStack.setWrapText(true);

        String placeholder2 = "";
        if(vaccineStack.size() != 0){
            for(int batch : vaccineStack){
                placeholder2+= ("[" + batch + "] ");
            }
            vacStack.setText(placeholder2);
        }else {
            vacStack.setText("Empty");
        }
        HBox buttons = new HBox(10);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
                VCHall.chooseVCMenu(window, recipients, VCs, accountMOH);
        });

        Button submitButton = new Button("Next");
        submitButton.setOnAction(e -> {

                    try {
                        VCHall.groupByAge(window, recipients, VCs.get(activeVC), queue, vaccineStack, VCs, accountMOH, activeVC);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

        buttons.getChildren().add(backButton);
        if(!queueLabel.getText().equals("Empty")){
            buttons.getChildren().add(submitButton);
        }

        grid.add(label1,0,0);
        grid.add(label2,0,2);
        grid.add(queueLabel,0,3);
        grid.add(label3,0,4);
        grid.add(vacStack,0,5);
        grid.add(buttons,0,6);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        grid.getColumnConstraints().addAll(col1);

        Scene beginSimScene = new Scene(grid,700, 300);
        window.setScene(beginSimScene);
        window.show();

    }
    /**
     * Group the queue of recipients by age. There are senior queue and normal queue.
     *
     * @param window The stage of the program
     * @param recipients All recipients
     * @param vc The selected VC
     * @param queue The queue of recipients
     * @param vaccineStack The stack of vaccines
     * @param VCs Arraylist of VCs
     * @param accountMOH The MOH Account
     */
    public static void groupByAge(Stage window, ArrayList<Account> recipients, VC vc, Queue<Account> queue, Stack<Integer> vaccineStack, ArrayList<VC> VCs, MOH accountMOH, int activeVC) throws IOException{
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 40, 10, 40));
        grid.setVgap(10);
        grid.setHgap(10);

        Queue<Account> normalQueue = new LinkedList<>();
        Queue<Account> seniorQueue = new LinkedList<>();
        Queue<Account> vaccinatedQueue = new LinkedList<>();

        for(Account recipient : queue){
            if(recipient.getAge() >= 60)
                seniorQueue.offer(recipient);
            else
                normalQueue.offer(recipient);
        }

        Label label1 = new Label("Senior queue");
        Label label2 = new Label();
        label2.setWrapText(true);

        String a = "";
        if(seniorQueue.size() != 0){
            for(Account recipient : seniorQueue){
                a+=("[" + recipient.getName() + ", " + recipient.getAge() + "] ");
            }
            label2.setText(a);
        }

        else label2.setText("Empty");

        Label label3 =  new Label("Normal Queue");
        Label label4 = new Label();
        label4.setWrapText(true);

        String b ="";
        if(normalQueue.size() != 0){
            for(Account recipient : normalQueue){
                b+=("[" + recipient.getName() + ", " + recipient.getAge() + "] ");
            }
            label4.setText(b);
        }

        else label4.setText("Empty");

        Label label5 = new Label("Stack of Vaccines");
        Label label6 =  new Label();
        label6.setWrapText(true);

        String c = "";
        if(vaccineStack.size() != 0){
            for(int batch : vaccineStack){
                c+= ("[" + batch + "] ");
            }
            label6.setText(c);
        }
        else label6.setText("Empty");

        Label label7 = new Label("Vaccinated");
        Label label8 =  new Label();
        label8.setWrapText(true);

        String d = "";

        if(vaccinatedQueue.size() != 0){
            for(Account recipient : vaccinatedQueue){
                if(recipient.getVacStatus().equals("1st Dose completed"))
                    d+=("[" + recipient.getFBatch() + ", " + recipient.getName() + ", " + recipient.getAge() + "] ");
                else if(recipient.getVacStatus().equals("2nd Dose completed"))
                    d+=("[" + recipient.getSBatch() + ", " + recipient.getName() + ", " + recipient.getAge() + "] ");
            }
            label8.setText(d);
        }
        else System.out.print("Empty");

        Button submitButton = new Button("Next");
        submitButton.setOnAction(e -> {
            if(vaccineStack.size() != 0) {
                try {
                    VCHall.updateQueue(window,recipients, vc, queue,normalQueue, seniorQueue, vaccinatedQueue, vaccineStack, VCs, accountMOH, activeVC);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });



        grid.add(label1,0,0);
        grid.add(label2,0,1);

        grid.add(label3,0,3);
        grid.add(label4,0,4);

        grid.add(label5,0,6);
        grid.add(label6,0,7);

        grid.add(label7,0,8);
        grid.add(label8,0,9);

        grid.add(submitButton,0,10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        grid.getColumnConstraints().addAll(col1);


        Scene groupByAgeScene = new Scene(grid,700, 400);
        window.setScene(groupByAgeScene);
        window.show();

    }
    /**
     * Remove the first recipient from 2 queues, assign them to the vaccines from top of the stack, and put them in the vaccinated queue.
     * Also save the updated data to main recipients.
     *
     * @param window The stage of the program
     * @param recipients All recipients
     * @param vc The selected VC
     * @param normalQueue The queue of recipients below 60
     * @param seniorQueue The queue of recipients 60 and older
     * @param vaccinatedQueue The queue of vaccinated recipients
     * @param vaccineStack The stack of vaccines
     */
    public static void updateQueue(Stage window,ArrayList<Account> recipients, VC vc,  Queue<Account> queue,Queue<Account> normalQueue, Queue<Account> seniorQueue, Queue<Account> vaccinatedQueue, Stack<Integer> vaccineStack, ArrayList<VC> VCs, MOH accountMOH, int activeVC) throws IOException{
        if(seniorQueue.size() != 0){
            // Update the first recipient of senior queue
            if(seniorQueue.peek().getVacStatus().equals("1st Dose appointment")){
                seniorQueue.peek().setVacStatus("1st Dose completed");
                seniorQueue.peek().setFDoseDate(seniorQueue.peek().getVacDate());
                seniorQueue.peek().setVacDate(null);
                seniorQueue.peek().setFBatch(vaccineStack.pop());
                vc.addVaccine(-1);
            }
            else{
                seniorQueue.peek().setVacStatus("2nd Dose completed");
                seniorQueue.peek().setSDoseDate(seniorQueue.peek().getVacDate());
                seniorQueue.peek().setVacDate(null);
                seniorQueue.peek().setSBatch(vaccineStack.pop());
                vc.addVaccine(-1);
            }
            // Save the updated data to main recipients
            for(int i=0; i < recipients.size(); i++){
                if(recipients.get(i).getName().equals(seniorQueue.peek().getName()) && recipients.get(i).getPhoneNum().equals(seniorQueue.peek().getPhoneNum())){
                    recipients.get(i).setVacStatus(seniorQueue.peek().getVacStatus());
                    recipients.get(i).setVacDate(seniorQueue.peek().getVacDate());
                    recipients.get(i).setFDoseDate(seniorQueue.peek().getFDoseDate());
                    recipients.get(i).setSDoseDate(seniorQueue.peek().getSDoseDate());
                    recipients.get(i).setFBatch(seniorQueue.peek().getFBatch());
                    recipients.get(i).setSBatch(seniorQueue.peek().getSBatch());
                }
            }
            // Remove the first recipient of senior queue and add to vaccinated queue
            vaccinatedQueue.offer(seniorQueue.poll());
        }

        if(normalQueue.size() != 0){
            // Update the first recipient of normal queue
            if(normalQueue.peek().getVacStatus().equals("1st Dose appointment")){
                normalQueue.peek().setVacStatus("1st Dose completed");
                normalQueue.peek().setFDoseDate(normalQueue.peek().getVacDate());
                normalQueue.peek().setVacDate(null);
                normalQueue.peek().setFBatch(vaccineStack.pop());
                vc.addVaccine(-1);
            }
            else{
                normalQueue.peek().setVacStatus("2nd Dose completed");
                normalQueue.peek().setSDoseDate(normalQueue.peek().getVacDate());
                normalQueue.peek().setVacDate(null);
                normalQueue.peek().setSBatch(vaccineStack.pop());
                vc.addVaccine(-1);
            }
            // Save the updated data to main recipients
            for(int i=0; i < recipients.size(); i++){
                if(recipients.get(i).getName().equals(normalQueue.peek().getName()) && recipients.get(i).getPhoneNum().equals(normalQueue.peek().getPhoneNum())){
                    recipients.get(i).setVacStatus(normalQueue.peek().getVacStatus());
                    recipients.get(i).setVacDate(normalQueue.peek().getVacDate());
                    recipients.get(i).setFDoseDate(normalQueue.peek().getFDoseDate());
                    recipients.get(i).setSDoseDate(normalQueue.peek().getSDoseDate());
                    recipients.get(i).setFBatch(normalQueue.peek().getFBatch());
                    recipients.get(i).setSBatch(normalQueue.peek().getSBatch());
                }
            }
            // Remove the first recipient of normal queue and add to vaccinated queue
            vaccinatedQueue.offer(normalQueue.poll());
        }

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 40, 10, 40));
        grid.setVgap(10);
        grid.setHgap(10);

        Label label1 = new Label("Senior queue");
        Label label2 = new Label();
        label2.setWrapText(true);

        String a = "";
        if(seniorQueue.size() != 0){
            for(Account recipient : seniorQueue){
                a+=("[" + recipient.getName() + ", " + recipient.getAge() + "] ");
            }
            label2.setText(a);
        }

        else label2.setText("Empty");

        Label label3 =  new Label("Normal Queue");
        Label label4 = new Label();

        String b ="";
        if(normalQueue.size() != 0){
            for(Account recipient : normalQueue){
                b+=("[" + recipient.getName() + ", " + recipient.getAge() + "] ");
            }
            label4.setText(b);
        }

        else label4.setText("Empty");

        Label label5 = new Label("Stack of Vaccines");
        Label label6 =  new Label();
        label6.setWrapText(true);

        String c = "";
        if(vaccineStack.size() != 0){
            for(int batch : vaccineStack){
                c+= ("[" + batch + "] ");
            }
            label6.setText(c);
        }
        else label6.setText("Empty");

        Label label7 = new Label("Vaccinated");
        Label label8 =  new Label();
        label8.setWrapText(true);

        String d = "";
        if(vaccinatedQueue.size() != 0){
            for(Account recipient : vaccinatedQueue){
                if(recipient.getVacStatus().equals("1st Dose completed"))
                    d+=("[" + recipient.getFBatch() + ", " + recipient.getName() + ", " + recipient.getAge() + "] ");
                else if(recipient.getVacStatus().equals("2nd Dose completed"))
                    d+=("[" + recipient.getSBatch() + ", " + recipient.getName() + ", " + recipient.getAge() + "] ");
            }
            label8.setText(d);
        }
        else System.out.print("Empty");

        Button submitButton = new Button("Next");
        submitButton.setOnAction(e -> {
            if(vaccineStack.size() != 0) {
                try {
                    VCHall.updateQueue(window,recipients, vc, queue,normalQueue, seniorQueue, vaccinatedQueue, vaccineStack, VCs, accountMOH, activeVC);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            else {

                    try {
                        Main.mainMenu(window, recipients, VCs, accountMOH);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

            }

        });



        grid.add(label1,0,0);
        grid.add(label2,0,1);

        grid.add(label3,0,3);
        grid.add(label4,0,4);

        grid.add(label5,0,6);
        grid.add(label6,0,7);

        grid.add(label7,0,8);
        grid.add(label8,0,9);

        grid.add(submitButton,0,12);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        grid.getColumnConstraints().addAll(col1);


        Scene groupByAgeScene = new Scene(grid,700, 400);
        window.setScene(groupByAgeScene);
        window.show();

    }


}
