package simulator;

import elevator.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler<ActionEvent> {

    static int frustrationFactor, percentageVip, numberOfFloors, numberOfRidersToAdd;
    static FrustrationTypes fT;
    static int[] ridersHomed;
    static ElevatorDriver elevatorDriver;

    Separator separator = new Separator();

    Button slowButton, pauseButton, startButton, fastButton;

    Button reduceNumberOfFloors, increaseNumberOfFloors;
    Button reduceRidersToAdd, increaseRidersToAdd;
    Button reduceVip, increaseVip;

    static Image imageLeft, imageRight;

    final int heightFit = 20;
    final int widthFit = 20;

    final Text tooltip = new Text("Check box next to item to randomize values entered before and after \"~\"");

    final Text range = new Text(" ~ ");
    final Text rangeRiders = new Text(" ~ ");
    final Text rangeVip = new Text(" ~ ");

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Elevator Simulator");

        range.setWrappingWidth(36);
        range.setTextAlignment(TextAlignment.CENTER);
        rangeRiders.setWrappingWidth(36);
        rangeRiders.setTextAlignment(TextAlignment.CENTER);
        rangeVip.setWrappingWidth(36);
        rangeVip.setTextAlignment(TextAlignment.CENTER);

        tooltip.setFont(Font.font("Knockout", 8));
        tooltip.setWrappingWidth(144);


        /**************************************************************
        TOP TOOL BOX THAT CONTAINS FIRST, PAUSE, PLAY, LAST BUTTON
         **************************************************************/
        Image imageSlow = new Image(getClass().getResourceAsStream("ic_first.png"));
        ImageView imageViewSlow = new ImageView(imageSlow);
        imageViewSlow.setFitHeight(heightFit); imageViewSlow.setFitWidth(widthFit);
        slowButton= new Button("", imageViewSlow);
        slowButton.setOnAction(this);

        Image imagePause = new Image(getClass().getResourceAsStream("ic_pause.png"));
        ImageView imageViewPause = new ImageView(imagePause);
        imageViewPause.setFitHeight(heightFit); imageViewPause.setFitWidth(widthFit);
        pauseButton= new Button("", imageViewPause);
        pauseButton.setOnAction(this);

        Image imagePlay = new Image(getClass().getResourceAsStream("ic_play.png"));
        ImageView imageViewPlay = new ImageView(imagePlay);
        imageViewPlay.setFitHeight(heightFit); imageViewPlay.setFitWidth(widthFit);
        startButton= new Button("", imageViewPlay);
        startButton.setOnAction(this);

        Image imageFast = new Image(getClass().getResourceAsStream("ic_last.png"));
        ImageView imageViewFast = new ImageView(imageFast);
        imageViewFast.setFitHeight(heightFit); imageViewFast.setFitWidth(widthFit);
        fastButton= new Button("", imageViewFast);
        fastButton.setOnAction(this);

        HBox toolBox = new HBox();
        toolBox.getChildren().addAll(slowButton, pauseButton, startButton, fastButton);


        /**************************************************************
        LEFT AND RIGHT IMAGES THAT ARE USED THROUGHOUT (THEY NEED TO BE DUPLICATED MANUALLY)
         **************************************************************/
        imageLeft = new Image(getClass().getResourceAsStream("ic_left.png"));
        ImageView imageViewLeft = new ImageView(imageLeft);
        imageViewLeft.setFitHeight(heightFit); imageViewLeft.setFitWidth(widthFit);

        imageRight = new Image(getClass().getResourceAsStream("ic_right.png"));
        ImageView imageViewRight = new ImageView(imageRight);
        imageViewRight.setFitHeight(heightFit); imageViewRight.setFitWidth(widthFit);


        /**************************************************************
        NUMBER OF FLOORS BOX
         **************************************************************/
        Text numberOfFloorsLabel = new Text ("Number of Floors");
        numberOfFloorsLabel.setFont(Font.font("Knockout", FontWeight.BOLD, 12));
        numberOfFloorsLabel.setWrappingWidth(126);
        CheckBox numberOfFloorsRandomize = new CheckBox();

        reduceNumberOfFloors = new Button("", imageViewLeft);
        TextField textNumberOfFloors = new TextField();
        textNumberOfFloors.setAlignment(Pos.CENTER);
        textNumberOfFloors.setMaxWidth(72);
        increaseNumberOfFloors = new Button("", imageViewRight);

        TextField fromNumberOfFloors = new TextField();
        TextField toNumberOfFloors = new TextField();
        fromNumberOfFloors.setMaxWidth(54);
        toNumberOfFloors.setMaxWidth(54);

        HBox floorsItem = new HBox();
        floorsItem.getChildren().addAll(numberOfFloorsLabel, numberOfFloorsRandomize);
        HBox floorsDisplay = new HBox();
        floorsDisplay.getChildren().addAll(reduceNumberOfFloors, textNumberOfFloors, increaseNumberOfFloors);
        HBox floorsRandomize = new HBox();
        floorsRandomize.getChildren().addAll(fromNumberOfFloors, range, toNumberOfFloors);
        VBox floorsBox = new VBox(floorsItem, floorsDisplay, floorsRandomize);


        /**************************************************************
        RIDERS TO ADD BOX
         **************************************************************/
        Text ridersToAddLabel = new Text("Riders to Add");
        ridersToAddLabel.setFont(Font.font("Knockout", FontWeight.BOLD, 12));
        ridersToAddLabel.setWrappingWidth(126);
        CheckBox ridersToAddCheckBox = new CheckBox();

        ImageView imageViewLeftRiders = new ImageView(imageLeft);
        imageViewLeftRiders.setImage(imageViewLeft.getImage());
        imageViewLeftRiders.setFitHeight(heightFit); imageViewLeftRiders.setFitWidth(widthFit);
        ImageView imageViewRightRiders = new ImageView(imageRight);
        imageViewRightRiders.setImage(imageViewRight.getImage());
        imageViewRightRiders.setFitHeight(heightFit); imageViewRightRiders.setFitWidth(widthFit);

        reduceRidersToAdd = new Button("", imageViewLeftRiders);
        TextField textRidersToAdd = new TextField();
        textRidersToAdd.setAlignment(Pos.CENTER);
        textRidersToAdd.setMaxWidth(72);
        increaseRidersToAdd = new Button("", imageViewRightRiders);

        TextField fromRidersToAdd = new TextField();
        TextField toRidersToAdd = new TextField();
        fromRidersToAdd.setMaxWidth(54);
        toRidersToAdd.setMaxWidth(54);

        HBox ridersItem = new HBox();
        ridersItem.getChildren().addAll(ridersToAddLabel, ridersToAddCheckBox);
        HBox ridersDisplay = new HBox();
        ridersDisplay.getChildren().addAll(reduceRidersToAdd, textRidersToAdd, increaseRidersToAdd);
        HBox ridersRandomize = new HBox();
        ridersRandomize.getChildren().addAll(fromRidersToAdd, rangeRiders, toRidersToAdd);
        VBox ridersBox = new VBox(ridersItem, ridersDisplay, ridersRandomize);


        /****************************************************************
        VIP PERCENTAGE BOX
         **************************************************************/
        Text vipToAddLabel = new Text("VIP Percentage");
        vipToAddLabel.setFont(Font.font("Knockout", FontWeight.BOLD, 12));
        vipToAddLabel.setWrappingWidth(126);
        CheckBox vipCheckBox = new CheckBox();

        ImageView imageViewLeftVip = new ImageView(imageLeft);
        imageViewLeftVip.setImage(imageViewLeft.getImage());
        imageViewLeftVip.setFitHeight(heightFit); imageViewLeftVip.setFitWidth(widthFit);
        ImageView imageViewRightVip = new ImageView(imageRight);
        imageViewRightVip.setImage(imageViewRight.getImage());
        imageViewRightVip.setFitHeight(heightFit); imageViewRightVip.setFitWidth(widthFit);

        reduceVip = new Button("", imageViewLeftVip);
        TextField textVip = new TextField();
        textVip.setAlignment(Pos.CENTER);
        textVip.setMaxWidth(72);
        increaseVip = new Button("", imageViewRightVip);

        TextField fromVip = new TextField();
        TextField toVip = new TextField();
        fromVip.setMaxWidth(54);
        toVip.setMaxWidth(54);

        HBox vipItem = new HBox();
        vipItem.getChildren().addAll(vipToAddLabel, vipCheckBox);
        HBox vipDisplay = new HBox();
        vipDisplay.getChildren().addAll(reduceVip, textVip, increaseVip);
        HBox vipRandomize = new HBox();
        vipRandomize.getChildren().addAll(fromVip, rangeVip, toVip);
        VBox vipBox = new VBox(vipItem, vipDisplay, vipRandomize);


        /**************************************************************
        COMBINE ALL THE BOXES
         **************************************************************/
        VBox toolBar = new VBox();
        toolBar.getChildren().addAll(toolBox, separator, tooltip, floorsBox, ridersBox, vipBox);

        Scene scene = new Scene(toolBar);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        elevatorDriver = new ElevatorDriver();

        // DEFAULT SETTINGS;
        frustrationFactor = 1;
        fT = FrustrationTypes.LINEAR;
        percentageVip = 10; // n%
        numberOfFloors = 5;
        numberOfRidersToAdd = 10;
        ridersHomed = new int[numberOfFloors];

        ridersHomed[0] = 0;
        ridersHomed[1] = 100;
        ridersHomed[2] = 65;
        ridersHomed[3] = 75;
        ridersHomed[4] = 90;

        launch(args);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == startButton) {
            elevatorDriver.Simulate(numberOfFloors, numberOfRidersToAdd, frustrationFactor, fT, percentageVip, ridersHomed);
            System.out.println(elevatorDriver.getSimulationAM().get(2));
        }
    }
}
