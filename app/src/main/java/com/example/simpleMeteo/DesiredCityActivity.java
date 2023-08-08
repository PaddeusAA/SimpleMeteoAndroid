package com.example.simpleMeteo;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DesiredCityActivity extends AppCompatActivity {

    private EditText fieldForCity;  // Текст для ввода Города
    private Button buttonForSearchWeather;  // Кнопка для запроса погоды
    private TextView resultWeather; // Поле, в котором появляется результат запроса
    private TextView resultWeatherTemp; // Поле, в котором появляется результат запроса

    private final String languageWeatherRU = "ru";  // для ссылки - вывод инфы на русском
    private final String unitsM = "metric";     // для ссылки - вывод информации в метрической системе


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desired_city);


        // Присвоение полей и кнопок к переменным
        fieldForCity = findViewById(R.id.fieldForCityTabe2);
        buttonForSearchWeather = findViewById(R.id.buttonForSearchWeatherTabe2);
        resultWeather = findViewById(R.id.resultWeatherTabe2);
        resultWeatherTemp = findViewById(R.id.resultWeatherTemp);


        //обработка нажатия на кнопку и создание нового URL запроса с просмотром погоды
        buttonForSearchWeather.setOnClickListener(view -> {
            // (получает текст, кастует к string, обрезает
            // лишние пробелы и проверяет на пустую строку)

            if(fieldForCity.getText().toString().trim().equals("")){
                Toast.makeText(DesiredCityActivity.this, R.string.notEnteredText, Toast.LENGTH_LONG).show();
            }else {
                String city = fieldForCity.getText().toString().trim(); // переменная получающая город Пользователя
                String keyFile = "openweather.properties.txt";
                String key = "";

                File file = new File(keyFile);

                try {
                    FileReader fileReader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    key = bufferedReader.readLine();

                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid="
                        + key +"&units="+ unitsM +"&lang=" + languageWeatherRU;

                new URLData().execute(url);
            }
        });
    }

    private class URLData extends AsyncTask<String, String, String> {

        //выводит текст на время получения запроса
        protected void onPreExecute(){
            super.onPreExecute();

            resultWeather.setText("Ожидание ответа...");
            resultWeatherTemp.setText("");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }

                try {
                    if(reader != null)
                        reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        // вывод результата запроса
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            // для выводы даты в приложении
            Date date = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd MMMM  E  hh:mm");

            try {
                JSONObject jsonObject = new JSONObject(result);
                int temperature = jsonObject.getJSONObject("main").getInt("temp"); // температура
                int feelslikeTemp = jsonObject.getJSONObject("main").getInt("feels_like"); // ощущаемая температура
                int speed = jsonObject.getJSONObject("wind").getInt("speed"); // скорость ветра
                int humidity = jsonObject.getJSONObject("main").getInt("humidity"); // влажность
                String weather = jsonObject.getJSONArray("weather").
                        getJSONObject(0).getString("description"); // состояние погоды


                //вывод информации о погоде
                resultWeatherTemp.setText("\t\t" + temperature + "°");
                resultWeather.setText("Ощущается на " + feelslikeTemp + "°\n" + "На улице "+ weather +
                        "\nСкорость ветра: " + speed + "м/с\nВлажность: " + humidity + "%\n\n" + formatForDateNow.format(date));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //кнопка перехода на слой "о приложении"
    public void buttSwitchToAboutD(View v) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}