package simulator;

import elevator.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.concurrent.ThreadLocalRandom;

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

    ChoiceBox frustrationStyle;

    static TextField textNumberOfFloors, textRidersToAdd, textVip, textFrustration;
    static TextField fromNumberOfFloors, toNumberOfFloors, fromRidersToAdd, toRidersToAdd, fromVip, toVip, fromHomed, toHomed;
    static CheckBox numberOfFloorsCheckBox, ridersToAddCheckBox, vipCheckBox, ridersHomedCheckBox;

    static Image imageLeft, imageRight;

    final int heightFit = 20;
    final int widthFit = 20;

    final Text tooltip = new Text("Check box next to item to randomize values entered before and after \"~\"");

    final Text range = new Text(" ~ ");
    final Text rangeRiders = new Text(" ~ ");
    final Text rangeVip = new Text(" ~ ");
    final Text rangeHomed = new Text(" ~ ");

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
        rangeHomed.setWrappingWidth(36);
        rangeHomed.setTextAlignment(TextAlignment.CENTER);

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
        numberOfFloorsCheckBox = new CheckBox();

        reduceNumberOfFloors = new Button("", imageViewLeft);
        reduceNumberOfFloors.setOnAction(e -> {
            int temp = Integer.parseInt(textNumberOfFloors.getText());
            if (temp > 1) {
                textNumberOfFloors.setText(Integer.toString(temp-1));
            }
        });
        textNumberOfFloors = new TextField("5");
        textNumberOfFloors.setAlignment(Pos.CENTER);
        textNumberOfFloors.setMaxWidth(72);
        increaseNumberOfFloors = new Button("", imageViewRight);
        increaseNumberOfFloors.setOnAction(e -> {
            int temp = Integer.parseInt(textNumberOfFloors.getText());
            textNumberOfFloors.setText(Integer.toString(temp+1));
        });

        textNumberOfFloors.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textNumberOfFloors.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        fromNumberOfFloors = new TextField("3");
        toNumberOfFloors = new TextField("10");
        fromNumberOfFloors.setAlignment(Pos.CENTER);
        toNumberOfFloors.setAlignment(Pos.CENTER);
        fromNumberOfFloors.setMaxWidth(54);
        toNumberOfFloors.setMaxWidth(54);

        fromNumberOfFloors.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    fromNumberOfFloors.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        toNumberOfFloors.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    toNumberOfFloors.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });


        HBox floorsItem = new HBox();
        floorsItem.getChildren().addAll(numberOfFloorsLabel, numberOfFloorsCheckBox);
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
        ridersToAddCheckBox = new CheckBox();

        ImageView imageViewLeftRiders = new ImageView(imageLeft);
        imageViewLeftRiders.setImage(imageViewLeft.getImage());
        imageViewLeftRiders.setFitHeight(heightFit); imageViewLeftRiders.setFitWidth(widthFit);
        ImageView imageViewRightRiders = new ImageView(imageRight);
        imageViewRightRiders.setImage(imageViewRight.getImage());
        imageViewRightRiders.setFitHeight(heightFit); imageViewRightRiders.setFitWidth(widthFit);

        reduceRidersToAdd = new Button("", imageViewLeftRiders);
        reduceRidersToAdd.setOnAction(e -> {
            int temp = Integer.parseInt(textRidersToAdd.getText());
            if (temp > 1) {
                textRidersToAdd.setText(Integer.toString(temp-1));
            }
        });
        textRidersToAdd = new TextField("10");
        textRidersToAdd.setAlignment(Pos.CENTER);
        textRidersToAdd.setMaxWidth(72);
        increaseRidersToAdd = new Button("", imageViewRightRiders);
        increaseRidersToAdd.setOnAction(e -> {
            int temp = Integer.parseInt(textRidersToAdd.getText());
            textRidersToAdd.setText(Integer.toString(temp+1));
        });

        textRidersToAdd.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textRidersToAdd.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        fromRidersToAdd = new TextField("5");
        toRidersToAdd = new TextField("15");
        fromRidersToAdd.setAlignment(Pos.CENTER);
        toRidersToAdd.setAlignment(Pos.CENTER);
        fromRidersToAdd.setMaxWidth(54);
        toRidersToAdd.setMaxWidth(54);

        fromRidersToAdd.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    fromRidersToAdd.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        toRidersToAdd.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    toRidersToAdd.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

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
        vipCheckBox = new CheckBox();

        ImageView imageViewLeftVip = new ImageView(imageLeft);
        imageViewLeftVip.setImage(imageViewLeft.getImage());
        imageViewLeftVip.setFitHeight(heightFit); imageViewLeftVip.setFitWidth(widthFit);
        ImageView imageViewRightVip = new ImageView(imageRight);
        imageViewRightVip.setImage(imageViewRight.getImage());
        imageViewRightVip.setFitHeight(heightFit); imageViewRightVip.setFitWidth(widthFit);

        reduceVip = new Button("", imageViewLeftVip);
        reduceVip.setOnAction(e -> {
            int temp = Integer.parseInt(textVip.getText());
            if (temp > 0) {
                textVip.setText(Integer.toString(temp-1));
            }
        });
        textVip = new TextField("10");
        textVip.setAlignment(Pos.CENTER);
        textVip.setMaxWidth(72);
        increaseVip = new Button("", imageViewRightVip);
        increaseVip.setOnAction(e -> {
            int temp = Integer.parseInt(textVip.getText());
            if (temp < 100) {
                textVip.setText(Integer.toString(temp+1));
            }
        });

        textVip.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textVip.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        fromVip = new TextField("7");
        toVip = new TextField("13");
        fromVip.setAlignment(Pos.CENTER);
        toVip.setAlignment(Pos.CENTER);
        fromVip.setMaxWidth(54);
        toVip.setMaxWidth(54);

        fromVip.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    fromVip.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        toVip.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    toVip.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        HBox vipItem = new HBox();
        vipItem.getChildren().addAll(vipToAddLabel, vipCheckBox);
        HBox vipDisplay = new HBox();
        vipDisplay.getChildren().addAll(reduceVip, textVip, increaseVip);
        HBox vipRandomize = new HBox();
        vipRandomize.getChildren().addAll(fromVip, rangeVip, toVip);
        VBox vipBox = new VBox(vipItem, vipDisplay, vipRandomize);


        /**************************************************************
         FRUSTRATION BOX
         **************************************************************/
        Text frustrationLabel = new Text ("Frustration Type");
        frustrationLabel.setFont(Font.font("Knockout", FontWeight.BOLD, 12));
        frustrationLabel.setWrappingWidth(126);
        frustrationStyle = new ChoiceBox(FXCollections.observableArrayList("Linear",
                "Logarithmic", "Polynomial", "Exponential", "Factorial"));
        frustrationStyle.getSelectionModel().select(0);
        textFrustration = new TextField("1");
        textFrustration.setMaxWidth(36);

        HBox frustrationDisplay = new HBox(frustrationStyle, textFrustration);
        VBox frustrationBox = new VBox(frustrationLabel, frustrationDisplay);


        /**************************************************************
         RIDERS HOMED BUTTON
         **************************************************************/
        Text ridersHomedLabel = new Text ("Riders Homed");
        ridersHomedLabel.setFont(Font.font("Knockout", FontWeight.BOLD, 12));
        ridersHomedLabel.setWrappingWidth(126);
        ridersHomedCheckBox = new CheckBox();

        fromHomed = new TextField("75");
        toHomed = new TextField("125");
        fromHomed.setAlignment(Pos.CENTER);
        toHomed.setAlignment(Pos.CENTER);
        fromHomed.setMaxWidth(54);
        toHomed.setMaxWidth(54);

        HBox homedItem = new HBox();
        homedItem.getChildren().addAll(ridersHomedLabel, ridersHomedCheckBox);
        HBox homedRandomize = new HBox();
        homedRandomize.getChildren().addAll(fromHomed, rangeHomed, toHomed);
        VBox ridersHomedBox = new VBox(homedItem, homedRandomize);



        /**************************************************************
        COMBINE ALL THE BOXES
         **************************************************************/
        VBox toolBar = new VBox();
        toolBar.getChildren().addAll(toolBox, separator, tooltip, floorsBox, ridersBox, vipBox, frustrationBox);

        HBox heechansPart = new HBox();
        heechansPart.getChildren().addAll(toolBar, ridersHomedBox);

        Scene scene = new Scene(heechansPart);

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
        ridersHomed = new int[5];

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

            if (numberOfFloorsCheckBox.isSelected()) {
                int lowerFloor = Integer.parseInt(fromNumberOfFloors.getText());
                int upperFloor = Integer.parseInt(toNumberOfFloors.getText());
                numberOfFloors = ThreadLocalRandom.current().nextInt(lowerFloor, upperFloor + 1);
            } else {
                numberOfFloors = Integer.parseInt(textNumberOfFloors.getText());
            }

            ridersHomed = new int[numberOfFloors];

            if (ridersToAddCheckBox.isSelected()) {
                int lowerRiders = Integer.parseInt(fromRidersToAdd.getText());
                int upperRiders = Integer.parseInt(toRidersToAdd.getText());
                numberOfRidersToAdd = ThreadLocalRandom.current().nextInt(lowerRiders, upperRiders + 1);
            } else {
                numberOfRidersToAdd = Integer.parseInt(textRidersToAdd.getText());
            }

            if (vipCheckBox.isSelected()) {
                int lowerVip = Integer.parseInt(fromVip.getText());
                int upperVip = Integer.parseInt(toVip.getText());
                percentageVip = ThreadLocalRandom.current().nextInt(lowerVip, upperVip + 1);
            } else {
                percentageVip = Integer.parseInt(textVip.getText());
            }

            if (ridersHomedCheckBox.isSelected()) {
                int lowerHomed = Integer.parseInt(fromHomed.getText());
                int UpperHomed = Integer.parseInt(toHomed.getText());
                for (int i = 1; i < numberOfFloors; i++) {
                 ridersHomed[i] = ThreadLocalRandom.current().nextInt(lowerHomed, UpperHomed + 1);
                }
            } else {
                // NOT YET IMPLEMENTED
            }

            //LINEAR, LOGARITHMIC, POLYNOMIAL, EXPONENTIAL, FACTORIAL
            switch (frustrationStyle.getValue().toString()) {
                case "Linear":
                    fT = FrustrationTypes.LINEAR;
                    break;
                case "Logarithmic":
                    fT = FrustrationTypes.LOGARITHMIC;
                    break;
                case "Polynomial":
                    fT = FrustrationTypes.POLYNOMIAL;
                    break;
                case "Exponential":
                    fT = FrustrationTypes.EXPONENTIAL;
                    break;
                case "Factorial":
                    fT = FrustrationTypes.FACTORIAL;
                default:
                    fT = FrustrationTypes.LINEAR;
                    break;
            }
            System.out.println(ridersHomed[1] + " " + ridersHomed[2]);
            System.out.println("Double check: #Riders - " + numberOfRidersToAdd + ". #Floors - " + numberOfFloors + ". %Vip - " + percentageVip);
            elevatorDriver.Simulate(numberOfFloors, numberOfRidersToAdd, frustrationFactor, fT, percentageVip, ridersHomed);
//            System.out.println(elevatorDriver.getSimulationAM().get(2));
        }
    }

}
