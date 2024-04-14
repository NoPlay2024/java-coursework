package com.example.weather;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class HelloController { // здесь содержаться переменные которые ссылаются на объекты интерфейса (дизайна)
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField city;
    @FXML
    private Button getData;
    @FXML
    private Text pressure;
    @FXML
    private Text temp_feels;
    @FXML
    private Text temp_info;
    @FXML
    private Text temp_max;
    @FXML
    private Text temp_min;
    @FXML
    private Text DateText;
    @FXML
    private Text LabelTime;
    @FXML
    private ImageView changeImage;
    @FXML
    private Text humidity;
    @FXML
    private Text speed;
    @FXML
    private Text gorod;
    @FXML
    private ImageView exit;
    @FXML
    private Pane appBar;
    @FXML
    private ImageView collapse;
    @FXML
    private ImageView CityList;
    private double xOffset = 0;
    private double yOffset = 0;
    private void InitCloseButton() {
        exit.setOnMouseClicked(event -> {Exit(event);});
    }
    private void CollapseButton() {
        collapse.setOnMouseClicked(event -> {
            Collapse(event);
        });
    }
    @FXML
    void initialize() { //методы
        InitAppBar(); //возможность передвигать приложение
        DynamicTime(); // Вывод Динамического Времени
        InitCloseButton(); //кнопка(картинка) выхода
        Time(); //выыод даты
        chooseCity(); //возможность выбора города
        CollapseButton(); // возможность свернуть приложение
        getData.setDisable(true);
        getData.setOnAction(event -> {
            String getUserCity = city.getText().trim();
            gorod.setText(getUserCity);
            if(!getUserCity.equals("")) { // Получение Данных с сайта
                String output = getUrlContent("https://api.openweathermap.org/data/2.5/weather?q=" + getUserCity + "&appid=80442090e9bad59438b86babdd86afd8&units=metric");
                if (!output.isEmpty()) {
                    JSONObject obj = new JSONObject(output); // преобразование данных JSON формата
                    double receivedTemp = obj.getJSONObject("main").getDouble("temp"); // вывод необходимых данных (температура)
                    temp_info.setText("" + receivedTemp);
                    temp_feels.setText("" + obj.getJSONObject("main").getDouble("feels_like")); // Как ощущется температура
                    temp_max.setText("" + obj.getJSONObject("main").getDouble("temp_max")); // Максимальная температура
                    temp_min.setText("" + obj.getJSONObject("main").getDouble("temp_min")); // Минимальная температура
                    pressure.setText("" + obj.getJSONObject("main").getDouble("pressure")); // Давление
                    humidity.setText("" + obj.getJSONObject("main").getDouble("humidity")); // Влажность
                    speed.setText("" + obj.getJSONObject("wind").getDouble("speed")); // Скорость ветра
                    GetCurrentWeathrIcon(obj); //погодная иконка (дождь,снег,солнце,туман)
                    city.clear();
                }
            }
        });
        city.textProperty().addListener((observable, oldValue, newValue) -> {
            getData.setDisable(newValue.isEmpty());
        });
    }
    private void GetCurrentWeathrIcon(JSONObject obj) { // получение иконки с погодой
        String icon = obj.getJSONArray("weather").getJSONObject(0).getString("icon"); //пременна в которой хранится название иконки
        String iconURL = "https://openweathermap.org/img/wn/" + icon  +"@2x.png"; //пременна в которой хранится иконка, которая была выбрана исходя из названия в icon
        Image image = new Image(iconURL); //помещаем иконку в image
        changeImage.setImage(image); //помещаем иконку в changeImage созданную в SceneBuilder
    }
    private void InitAppBar() { //возможность передвигать приложение
        appBar.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        appBar.setOnMouseDragged(mouseEvent -> {
            Node node = (Node) mouseEvent.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.setX(mouseEvent.getScreenX() - xOffset);
            thisStage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }
    public void Exit (MouseEvent event) //кнопка(картинка) выхода
    {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
    public void Collapse (MouseEvent event) //сворачивание приложения
    {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.setIconified(true);
    }
    private List<String> texts = Arrays.asList("Москва", "Норильск", "Екатеринбург", "Berlin", "Paris", "Tokyo", "London", "New York", "Sydney", "Rome", "Toronto", "Melbourne", "Dubai", "Hong Kong", "Toronto", "Sao Paulo", "Cairo", "Istanbul", "Tokyo");;
    private int currentIndex = 0;
    public void chooseCity () //при нажатии на картинку появляется название города
    {
        CityList.setOnMouseClicked(event -> { currentIndex = (currentIndex + 1) % texts.size(); // Циклическое изменение индекса
            city.setText(texts.get(currentIndex));
        });;
    }
    public void Time () // Вывод даты
    {
        TextField textField = new TextField();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String formattedDate = sdf.format(currentDate);
        textField.setText(formattedDate);
        DateText.setText(formattedDate);
    }
        public void DynamicTime() {  // Вывод Динамического Времени
            updateTimeLabel();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTimeLabel()));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
        public void updateTimeLabel() {
            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LabelTime.setText(currentTime.format(formatter));
        }
    public String getUrlContent(String urlAdress){ // Обращение по URL - позволяет получить содержимое веб-страницы по заданному URL и вернуть его в виде строки
        StringBuffer content = new StringBuffer();
        try { // Обработка исключений используется для обработки все возможных исключений
            URL url = new URL(urlAdress);
            URLConnection urlConn = url.openConnection(); // Открытие соединения
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream())); // Чтение данных
            String line;
            while ((line = bufferedReader.readLine()) != null) // считывание строк
            {
                content.append(line +"\n"); // Каждая считанная строка добавляется в StringBuffer content с добавлением символа новой строки
            }
            bufferedReader.close(); // Закрытие потока
        } catch (Exception e)
        {
            gorod.setText("Такого Города Нет!"); // Конструкция для проверки на корректный ввод города
        }
        return content.toString(); // возвращается строковое представление содержимого StringBuffer content, полученного из URL
    }
}
