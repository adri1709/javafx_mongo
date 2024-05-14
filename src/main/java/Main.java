import com.mongodb.MongoException;
import com.mongodb.client.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;

public class MongoDBJavaFXApp extends Application {

    private final String connectionString = "mongodb+srv://lector:lector@cluster0.xr63mbi.mongodb.net/";

    private final ObservableList<Document> data = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        TableView<Document> tableView = new TableView<>();
        TableColumn<Document, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> {
            String name = cellData.getValue().getString("name");
            return name != null ? new SimpleStringProperty(name) : new SimpleStringProperty("");
        });
        tableView.getColumns().add(nameColumn);

        VBox root = new VBox(tableView);
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("MongoDB Data Viewer");

        // Connect to MongoDB Atlas and fetch data
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("pruebas");
            MongoCollection<Document> collection = database.getCollection("prueba");

            // Fetch data and add to table
            collection.find().forEach(data::add);
        } catch (MongoException e) {
            e.printStackTrace();
        }

        tableView.setItems(data);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
