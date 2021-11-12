package cost_MAP;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

public class costMAP extends Application {

    @Override
    public void start(Stage stage) {
        System.out.println("Initializing CostMAP.");
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        // costEnv.setDatasetDir(System.getProperty("user.dir") + "/Datasets/ASCII/");
        costEnv.setDatasetDir("/scratch/eescommon/CostMAP/100m_Datasets/ASCII_WGS84/data_small/");

        Scene scene = buildGUI(stage);
        stage.setScene(scene);
        stage.setTitle("CostMAP:Cost Surface Multi-Layer Aggregation Program");
        stage.getIcons().add(new Image("file:Outputs/Pitcure1.tif"));
        stage.show();
    }

    public Scene buildGUI(Stage stage) {

        // OS seperator
        String sep = System.getProperty("file.separator");

        //Images
        Image img1 = new Image("file:Datasets/SimCCS_logo.png");
        ImageView imgView1 = new ImageView(img1);

        //Set up
        BorderPane pane = new BorderPane();
        VBox topContainer = new VBox();  //Creates a container to hold all Menu Objects.
        MenuBar mainMenu = new MenuBar();  //Creates our main menu to hold our Sub-Menus.

        //Menus
        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        Menu help = new Menu("Help");
        Menu view = new Menu("View");

        mainMenu.getMenus().addAll(file, edit, view, help);

        //Labels
        Label defaultLabel = new Label("Default Inputs ");

        //Buttons
        Button runButton = new Button("Run");
        Button canButton = new Button("Cancel");
        Button bmpButton = new Button("Write Raster");

        //Importing info
        CheckBox chkDefaultLandcover = new CheckBox("Land Cover");
        chkDefaultLandcover.setSelected(true);
        CheckBox chkCustom = new CheckBox("Custom Rasters");
        chkCustom.setSelected(false);
        CheckBox chkDefaultSlope = new CheckBox("Slope");
        chkDefaultSlope.setSelected(true);
        CheckBox chkDefaultAspect = new CheckBox("Aspect");
        chkDefaultAspect.setSelected(true);
        CheckBox chkDefaultPop = new CheckBox("Population");
        chkDefaultPop.setSelected(true);
        CheckBox chkDefaultRivers = new CheckBox("Rivers");
        chkDefaultRivers.setSelected(true);
        CheckBox chkDefaultRoads = new CheckBox("Roads");
        chkDefaultRoads.setSelected(true);
        CheckBox chkDefaultRails = new CheckBox("Railroads");
        chkDefaultRails.setSelected(true);
        CheckBox chkDefaultPipelines = new CheckBox("Pipelines");
        chkDefaultPipelines.setSelected(true);
        CheckBox chkDefaultFedland = new CheckBox("Federal Land");
        chkDefaultFedland.setSelected(true);
        chkCustom.setDisable(true);

        ToolBar toolBar1 = new ToolBar();
        toolBar1.setOrientation(Orientation.VERTICAL);

        HBox buttonArea = new HBox();
        buttonArea.setSpacing(5);
        VBox mapBox = new VBox();

        toolBar1.getItems().addAll(
                defaultLabel,
                new Separator(),
                chkDefaultLandcover,
                new Separator(),
                chkDefaultSlope,
                new Separator(),
                chkDefaultAspect,
                new Separator(),
                chkDefaultPop,
                new Separator(),
                chkDefaultRivers,
                new Separator(),
                chkDefaultRoads,
                new Separator(),
                chkDefaultRails,
                new Separator(),
                chkDefaultPipelines,
                new Separator(),
                chkDefaultFedland,
                new Separator(),
                buttonArea,
                new Separator(),
                chkCustom,
                new Separator(),
                bmpButton
        );

        pane.getStylesheets().add("styles.css");

        //Fill Container
        topContainer.getChildren().add(mainMenu);
        topContainer.getChildren().add(toolBar1);
        buttonArea.getChildren().add(runButton);
        buttonArea.getChildren().add(canButton);
        mapBox.getChildren().add(imgView1);

        //Place Default Image
        pane.setTop(topContainer);
        buttonArea.setAlignment(Pos.BOTTOM_LEFT);
        mapBox.setAlignment(Pos.CENTER);

        pane.setLeft(toolBar1);
        pane.setCenter(mapBox);

        runButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    costSolver costs = new costSolver();
                    costSolver cells = new costSolver();

                    Dictionary costList = new Hashtable();
                    Dictionary rowList = new Hashtable();

                    boolean isSelectedPop = chkDefaultPop.isSelected();
                    boolean isSelectedAspect = chkDefaultAspect.isSelected();
                    boolean isSelectedRivers = chkDefaultRivers.isSelected();
                    boolean isSelectedRoads = chkDefaultRoads.isSelected();
                    boolean isSelectedRails = chkDefaultRails.isSelected();
                    boolean isSelectedPipelines = chkDefaultPipelines.isSelected();
                    Dictionary headerInfo = costs.getHeader(costEnv.getLandcoverPath());
                    double [][] distMult = costs.distanceMultiplier(headerInfo);

                    if (chkDefaultLandcover.isSelected()) {
                        System.out.println("Importing Landcover Data for Construction... ");
                        costList =  costs.landcoverInput(isSelectedPop, costEnv.getLandcoverPath());
                    }

                    if (chkDefaultSlope.isSelected()) {
                        System.out.println("Importing Slope Data ... ");
                        costList = costs.slopeInput(costList, isSelectedAspect, costEnv.getSlopePath());
                    }

                    if (chkDefaultRivers.isSelected()) {
                        System.out.println("Importing River Data ... ");
                        costList = costs.addRiverCrossings(costList,headerInfo, costEnv.getRiversPath());
                    }
                    if (chkDefaultRoads.isSelected()) {
                        System.out.println("Importing Roads Data ... ");
                        costList = costs.addRoadCrossings(costList,headerInfo, costEnv.getRoadsPath());
                    }
                    if (chkDefaultRails.isSelected()) {
                        System.out.println("Importing Railroad Data ... ");
                        costList = costs.addRailCrossings(costList,headerInfo, costEnv.getRailroadsPath());
                    }
                    if (chkDefaultPipelines.isSelected()) {
                        System.out.println("Importing Pipeline Data ... ");
                        costList = costs.addPipelineCorridor(costList,headerInfo, costEnv.getPipelinesPath());
                    }
                    BufferedWriter outputConstruction = new BufferedWriter(new FileWriter(costEnv.getOutputPath() + sep + "Construction Costs.txt"));

                    System.out.println("Calculating Distance ...");
                    costList = costs.solveDistance(headerInfo, distMult, costList, costEnv.getLandcoverPath());
                    System.out.println("Writing to files...");
                    costs.writeTxt(costList, headerInfo, outputConstruction);
                    System.out.println("Construction calculations are complete.");
//
                    if (chkDefaultLandcover.isSelected()) {
                        System.out.println("Importing Landcover Data for ROWS ... ");
                        rowList =  costs.landcoverInput(isSelectedPop, costEnv.getLandcoverPath());
                    }
////                    if (chkDefaultPipelines.isSelected()) {
////                        System.out.println("Importing Pipeline Data ... ");
////                        rowList = costs.addPipelineCorridor(rowList,headerInfo, "Datasets/ASCII/pipelines.asc");
////                    }
                    rowList = costs.solveDistance(headerInfo, distMult, rowList, costEnv.getLandcoverPath());
                    BufferedWriter outputROWS = new BufferedWriter(new FileWriter(costEnv.getOutputPath() + sep + "RightOfWay Costs.txt"));
                    costs.writeTxt(rowList, headerInfo, outputROWS);
//
                    System.out.println("The Rights of way calculations are complete. ");
                    System.out.println("(with yllCorner change.)");

                } catch (IOException ex) {
                    Logger.getLogger(costMAP.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(costMAP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        canButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //Just making sure stuff is working with this print
                System.out.println("Thank you for cancelling. I can rest now.");
                return;
            }
        });

        bmpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String landcoverPath = costEnv.getLandcoverPath();
                String outputPath = costEnv.getOutputPath() + "/construction.png";
                Boolean importPopulationData = true;

                costSolver tmpCostSolver = new costSolver();
                writeRaster rasterWriter = new writeRaster();

                try {
                    Dictionary headerInfo = tmpCostSolver.getHeader(landcoverPath);
                    double[][] landcoverMatrix = tmpCostSolver.getDetails(headerInfo, landcoverPath);
                    rasterWriter.writeToRaster(outputPath, landcoverMatrix);
                    //tmpCostSolver.landcoverInput(importPopulationData, "Datasets/ASCII/landcover.asc");

                    System.out.println("Successfully wrote to " + outputPath);
                } catch (IOException ex) {
                    System.err.println("Could not find path");
                }

                return;
            }
        });

        return new Scene(pane, 1050, 660);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
