package forecast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherForecast {

    private static final String API_KEY = "b6907d289e10d714a6e88b30761fae22";
    private static final String BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=";
    
    public static JSONObject getWeatherData(String city) throws IOException, JSONException {
        URL url = new URL(BASE_URL + city + "&appid=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return new JSONObject(response.toString());
    }

    public static double getTemperature(JSONObject data, String date) throws JSONException {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject forecast = list.getJSONObject(i);
            if (forecast.getString("dt_txt").startsWith(date)) {
                return forecast.getJSONObject("main").getDouble("temp");
            }
        }
        return Double.NaN;
    }

    public static double getWindSpeed(JSONObject data, String date) throws JSONException {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject forecast = list.getJSONObject(i);
            if (forecast.getString("dt_txt").startsWith(date)) {
                return forecast.getJSONObject("wind").getDouble("speed");
            }
        }
        return Double.NaN;
    }

    public static double getPressure(JSONObject data, String date) throws JSONException {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject forecast = list.getJSONObject(i);
            if (forecast.getString("dt_txt").startsWith(date)) {
                return forecast.getJSONObject("main").getDouble("pressure");
            }
        }
        return Double.NaN;
    }

    public static void main(String[] args) throws IOException, JSONException {
        String city = "London, US ";
        JSONObject data = getWeatherData(city);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Get weather");
            System.out.println("2. Get Wind Speed");
            System.out.println("3. Get Pressure");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            String choice = br.readLine();

            if ("1".equals(choice)) {
                System.out.print("Enter the date (YYYY-MM-DD): ");
                String date = br.readLine();
                double temperature = getTemperature(data, date);
                if (!Double.isNaN(temperature)) {
                    System.out.println("Temperature on " + date + ": " + temperature + " K");
                } else {
                    System.out.println("Data not found for the given date.");
                }
            } else if ("2".equals(choice)) {
                System.out.print("Enter the date (YYYY-MM-DD): ");
                String date = br.readLine();
                double windSpeed = getWindSpeed(data, date);
                if (!Double.isNaN(windSpeed)) {
                    System.out.println("Wind Speed on " + date + ": " + windSpeed + " m/s");
                } else {
                    System.out.println("Data not found for the given date.");
                }
            } else if ("3".equals(choice)) {
                System.out.print("Enter the date (YYYY-MM-DD): ");
                String date = br.readLine();
                double pressure = getPressure(data, date);
                if (!Double.isNaN(pressure)) {
                    System.out.println("Pressure on " + date + ": " + pressure + " hPa");
                } else {
                    System.out.println("Data not found for the given date.");
                }
            } else if ("0".equals(choice)) {
                System.out.println("Exiting the program.");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        br.close();
    }
}

