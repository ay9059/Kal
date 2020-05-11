package calendar.view_controller.fx;

import calendar.model.Appointment;
import calendar.model.Calendar;
import edu.rit.cs.Observer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.security.auth.Subject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Bread and Butter for Calendar object which uses JavaFX for GUI representation.
 *
 * The GUI uses "!" to represent if appointments exist on that particular date.
 *
 * @author Abhishek Yadav
 */

public class KalGUI extends Application implements Observer<Subject> {
    private Calendar model;
    /**
     * A Vertical box appointvBox is created to stack appointments one on top of the other.
     */
    private VBox appointmentvBox;
    private VBox appointmentbox;
    /**
     * This boxes are used for setting the location of add appointment button and
     * remove appointment button.
     */
    private HBox hbox1;
    private VBox vbox3;
    /**
     * buttonPane is used to center the Save Button
     */
    private BorderPane buttonPane;
    /**
     * borderPane is used to set the location of the day grid and the save button.
     * The day grid is placed on Top of the borderPane whereas the save button is placed at the bottom.
     */
    private BorderPane borderPane;

    /**
     * the BorderPane on which the appointmentvBox is added
     */
    private BorderPane borderPane1;

    /**
     * The update is called when the observer notifies it. The notifying is done in the model.
     *
     * @param subject object that informs this object about something.
     * @param something object that represents new information about subject
     *
     */
    @Override
    public void update(Subject subject, Object something) {
        HashMap<Button, Appointment> map = (HashMap<Button, Appointment>) something;

        for (Button btn : map.keySet()) {

            if (!map.get(btn).remove()) {
                for (Appointment appointment1 : model.appointmentsOn(Integer.parseInt(btn.getText()
                        .split(" ")[0]))) {
                    appointmentvBox.getChildren().add(new Label(appointment1.toString()));
                    btn.setText(btn.getText().split(" ")[0] + " !");
                }

            } else {
                /**
                 * The if and else if logics are used to figure out whether or not the button would have a "!"
                 * attached to them
                 */
                ArrayList<Appointment> appointments = model.appointmentsOn(Integer.parseInt(btn.getText()
                        .split(" ")[0]));

                if (model.appointmentsOn(Integer.parseInt(btn.getText().split(" ")[0])).size() == 0) {
                    btn.setText(btn.getText().split(" ")[0]);

                } else if (model.appointmentsOn(Integer.parseInt(btn.getText().split(" ")[0])).size() > 0) {
                    btn.setText(btn.getText().split(" ")[0] + " !");
                }
                this.appointmentvBox.getChildren().clear();
                for (Appointment appointment : appointments) {
                    appointmentvBox.getChildren().add(new Label(appointment.toString()));
                }
            }
        }
    }

    /**
     * The main method which is called by JavaFx when initiating the GUI aspect of the program. This method
     * creates and sets various nodes that are used to represent a Calendar. This has the same functionality
     * as its PTUI counterpart.
     *
     * @param stage the main node on which the scene is set.
     */
    @Override
    public void start(Stage stage) {

        stage.setTitle("Kal");
        borderPane = new BorderPane();
        buttonPane = new BorderPane();

        Button Save = new Button("Save Kal");
        buttonPane.setCenter(Save);


        Save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    model.toFile();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("File Saved");
                    alert.setContentText("Appointments have been saved to the original file.");
                    alert.setHeaderText("Attention");
                    alert.show();

                } catch (IOException io) {

                    Alert warning = new Alert(Alert.AlertType.WARNING);
                    warning.setTitle("File Not Read");
                    warning.setContentText("The appointments were not read from a file so it cannot be saved to one.");
                    warning.show();

                }
            }
        });

        GridPane gridPane = new GridPane();
        borderPane.setCenter(gridPane);
        borderPane.setBottom(buttonPane);


        //get the monthszie from init or main
        int monthsize = model.getMonthsize();


        //Round up the row number since the no. of rows can't be a double.
        Integer rows =(int) Math.round(Math.ceil(monthsize / 7));
        int day = 1;

        //Use a double for loop to put the buttons that represent the days in the month
        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j < 7; j++) {

                Button btn = new Button("" + day);
                if (model.appointmentsOn(day).size() != 0) {
                    btn.setText(btn.getText() + " !");

                }
                /**
                 * The buttons that represent the Days
                 */
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        final Stage dialog = new Stage();
                        dialog.setTitle("Appointments for date "+btn.getText().split(" ")[0]);
                        Button addApp = new Button("Add Appointment");
                        Button removeApp = new Button("Remove appointment");


                        VBox appoints = new VBox();

                        borderPane1 = new BorderPane();
                        appointmentbox = new VBox(20);
                        hbox1 = new HBox(20);
                        borderPane1.setBottom(hbox1);

                        /**
                         * Remove Appointment Buttons
                         */
                        removeApp.setOnAction(new EventHandler<ActionEvent>() {


                            @Override
                            public void handle(ActionEvent actionEvent) {

                                Label timelabel = new Label("Time");
                                HBox timelabel_time = new HBox();
                                timelabel_time.setSpacing(3);
                                TextField time = new TextField();
                                timelabel_time.getChildren().addAll(timelabel,time);
                                final Stage removeap = new Stage();
                                removeap.setTitle("Remove Appointment");
                                vbox3 = new VBox();
                                removeap.setScene(new Scene(vbox3, 250, 100));
                                removeap.show();
                                Button confirm_delete = new Button("Confirm");
                                vbox3.getChildren().addAll(timelabel_time, confirm_delete);
                                confirm_delete.setOnAction(new EventHandler<ActionEvent>() {

                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        appointmentvBox = new VBox();
                                        Appointment Appointment_to_remove = Appointment.fromString(
                                                btn.getText().split(" ")[0] + "," +
                                                time.getText() + "," + "what");
                                        model.remove(Appointment_to_remove);
                                        Appointment_to_remove.setRemoved(true);
                                        HashMap<Button, Appointment> object = new HashMap<>();
                                        object.put(btn, Appointment_to_remove);
                                        update(new Subject(), object);
                                        borderPane1.setTop(appointmentvBox);


                                    }
                                });

                            }
                        });

                        /**
                         * Add Appointment button
                         */

                        addApp.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {

                                final Stage query = new Stage();
                                VBox vbox2 = new VBox();
                                HBox time_textfield = new HBox();
                                Label time_label = new Label("Time");
                                Label what_label = new Label("What");
                                TextField time = new TextField();
                                TextField what = new TextField();
                                Button confirm = new Button("Confirm");
                                time_textfield.getChildren().addAll(time_label,time);
                                HBox what_textfield = new HBox();
                                what_textfield.getChildren().addAll(what_label,what);
                                vbox2.getChildren().add(what_textfield);
                                vbox2.getChildren().addAll(time_textfield);
                                vbox2.getChildren().add(confirm);
                                vbox2.setSpacing(2);
                                query.setTitle("Add Appointment");
                                query.setScene(new Scene(vbox2, 250, 120));
                                query.show();


                                /**
                                 * CONFIRM appointment button
                                 */
                                confirm.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {

                                        appointmentvBox = new VBox();
                                        Appointment appointment =
                                                Appointment.fromString(btn.getText().split(" ")[0] + "," +
                                                        time.getText() + "," + what.getText());
                                        model.add(appointment);
                                        HashMap<Button, Appointment> object = new HashMap<>();
                                        object.put(btn, appointment);
                                        update(new Subject(), object);
                                        borderPane1.setTop(appointmentvBox);
                                    }
                                });

                            }
                        });
                        ArrayList<Appointment> appointments = new ArrayList<>();

                        try {
                            appointments = model.appointmentsOn(Integer.parseInt(btn.getText().split(" ")[0]));
                        } catch (NullPointerException n) {

                        }

                        for (Appointment appointment : appointments) {
                            Label app = new Label(appointment.toString());
                            appointmentbox.getChildren().add(app);
                        }
                        hbox1.getChildren().add(addApp);
                        hbox1.getChildren().add(removeApp);
                        borderPane1.setBottom(hbox1);
                        borderPane1.setTop(appointmentbox);

                        dialog.setScene(new Scene(borderPane1, 400, 600));
                        dialog.show();

                    }
                });
                day++;
                btn.setPrefSize(200, 100);
                gridPane.add(btn, j, i);
                if (day >= model.getMonthsize() + 1) {
                    break;
                }
            }
        }

        stage.setScene(new Scene(borderPane, 500, 500));
        stage.show();
    }

    /**
     * the main methods that initiates the GUI
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initalizes the GUI by reading the parameters and setting the model's month size and the appointments if they
     * exist.
     *
     * NOTE: If no argument is provided, my program uses 29 days as default since there are 29 days in Feb
     * 2020.
     */
    @Override
    public void init() {
        try {
            List<String> cmdLineArgs = getParameters().getRaw();
            if (cmdLineArgs.isEmpty()) {
                this.model = Calendar.fromFile("NONE");
            } else {
                this.model = Calendar.fromFile(cmdLineArgs.get(0));
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());

        }
    }


}