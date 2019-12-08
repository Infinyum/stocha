package Controller;

import VLS.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.*;

public class Controller implements Initializable {

    @FXML private Slider precision;
    @FXML private Button importer;
    @FXML private Text fichier;
    @FXML private Button demarrer;

    @FXML private TableView<Station> results;
    @FXML private TableColumn<String, String> station;
    @FXML private TableColumn<String, String> action;

    @FXML private Button exporter;
    @FXML private SplitPane split;

    public double coefNbScenario;
    ArrayList<Station> stations = new ArrayList<>();




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        demarrer.setDisable(true);
        exporter.setDisable(true);


        importer.setOnAction(event -> {
            final FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {            	
            	int dotIndex = file.getName().lastIndexOf('.');
            	String test =  (dotIndex == -1) ? "" : file.getName().substring(dotIndex + 1);
            	
            	if (test.equals("csv")) {
            		stations = parseCSV(file.getAbsolutePath());// on recupere la liste des stations => celles a utiliser par la suite
            		if (!stations.isEmpty()){
            			fichier.setText(file.getName());
            			demarrer.setDisable(false);
            		}
            	} else {
            		Alert alert = new Alert(Alert.AlertType.ERROR);
            		alert.setTitle("Error");
            		alert.setHeaderText("Mauvais Format");
            		alert.setContentText("Vous devez utiliser un fichier au format CSV !");
            		
            		alert.showAndWait();
            	}
            }
        });

        station.setCellValueFactory(new PropertyValueFactory<>("name"));
        action.setCellValueFactory(new PropertyValueFactory<>("action"));


        //recuperer valeur precision
        demarrer.setOnAction(event -> {
            coefNbScenario=precision.getValue()/10;
            //lancer ici la simulation avec en parametre les stations
            Double valeurFonctionObj = VLSSolver.solve((int)Math.round(coefNbScenario), stations);

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
            String[] columns = line.split(",");
            //skip first line ?
            if(columns.length!=3){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Mauvais Format");
                alert.setContentText("Le fichier doit avoir les trois colonnes ID, nom et capacité de la station séparer par des virgules ! \n Vérifiez le format du fichier");

                alert.showAndWait();
            }else {
                while (line != null) {
                    String[] array = line.split(",");
                    Station a = new Station(Integer.parseInt(array[0]),array[1],Integer.parseInt(array[2]));
                    listStations.add(a);

                    line = bufRead.readLine();
                }
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
        result += "Nom;Velos a ajouter\n";
        for (Station st : s){
            result += st.getName()+","+st.getId()+"\n"/*+get solution*/;
        }
        return result;
    }

}
