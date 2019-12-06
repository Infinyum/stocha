package Controller;

import VLS.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.*;

public class Controller implements Initializable {

    @FXML private Slider precision;
    @FXML private Button importer;
    @FXML private Button demarrer;

    @FXML private TableView<Station> results;
    @FXML private TableColumn<String, String> station;
    @FXML private TableColumn<String, String> action;

    @FXML private CheckBox check1;
    @FXML private CheckBox check2;
    @FXML private CheckBox check3;
    @FXML private Button afficher;
    @FXML private Button exporter;
    @FXML private SplitPane split;

    public double coefNbScenario;
    ArrayList<Station> stations = new ArrayList<>();




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        demarrer.setDisable(true);
        afficher.setDisable(true);
        exporter.setDisable(true);


        importer.setOnAction(event -> {
            final FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);

             stations = parseCSV(file.getAbsolutePath());
             System.out.println(stations);
//            if(!file.equals(null)) {
            demarrer.setDisable(false);
//            }
        });

        // TODO retour de stations a afficher dans le tableau
        station.setCellValueFactory(new PropertyValueFactory<>("name"));
        action.setCellValueFactory(new PropertyValueFactory<>("id"));





        //recuperer valeur precision
        demarrer.setOnAction(event -> {
            coefNbScenario=precision.getValue()/100;
            //lancer la simulation
            //besoin d'une liste de station et solution =>stations
            afficher.setDisable(false);
            exporter.setDisable(false);
            ObservableList<Station> list = FXCollections.observableArrayList(stations);
            results.setItems(list);
        });

        //exporter les stations
        exporter.setOnAction(event -> {
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            File dir = directoryChooser.showDialog(null);
            File filecsv = new File(dir.getAbsolutePath()+"\\results.csv");
            System.out.println(filecsv.getAbsolutePath());

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filecsv);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                filecsv.createNewFile();
                String toCSV= listToCSV(stations);
                fos.write(toCSV.getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

    }

    public  ArrayList<Station> parseCSV(String fichier) {

        ArrayList<Station> listStations = new ArrayList<Station>();
        try {
            FileReader file = new FileReader(fichier);
            BufferedReader bufRead = new BufferedReader(file);

            String line = bufRead.readLine();
            while (line != null) {
                String[] array = line.split(",");
                Station a = new Station(Integer.parseInt(array[0]),array[1],Integer.parseInt(array[2]));
                listStations.add(a);

                line = bufRead.readLine();
            }

            bufRead.close();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return listStations;
    }

    public String listToCSV (List<Station> s){
        String result="";
        for (Station st : s){
            result += st.getName()+","+st.getId()/*+get solution*/;
        }
        return result;
    }



}
