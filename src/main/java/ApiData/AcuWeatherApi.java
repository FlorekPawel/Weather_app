package ApiData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Warning: mamy max 50 wykorzystań 1 klucza dziennie, wię mając 150 wykorzystań, uważajmy
// dla testow mozna pozapisywac dane jako pliki .json, lub nawet .txt, zeby nie marnować odpaleń
public class AcuWeatherApi {
    //private static final String apiKey = "KpFoV3MGxJ0yX8PZkMgYHZe89j4pkD4n"; // kiddo key
    private static final String apiKey = "AGwmg1rTWzUvFPmRt4ZHPAUM0xZDw9QM"; // florini key
    //private static final String apiKey = "GnVrkjfrIyp0aAtmUR6qJiseCY7Fzhyp"; // mata key
    private Gson gson = new Gson();
    private String getLocationKey(String location) {
        // pobranie klucza lokacji z nazwy miejscowosci

        try {
            String apiUrl = "http://dataservice.accuweather.com/locations/v1/PL/search?apikey=" + apiKey + "&q=" + location;
            String jsonResponse = sendHttpGetRequest(apiUrl);
            JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class); // jak jsony

            if (jsonArray.size() > 0) {
                JsonObject weatherData = jsonArray.get(0).getAsJsonObject();
                return weatherData.get("Key").getAsString(); // wyrzuc klucz jako String
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private List<Map<String, Object>> getDailyForecasts(String location_key){ // max 5 days
        // Dostajemy pobrane dane z naszego url i przerabiamy je na Liste ze słownikami, kazdy element listy odpowiada
        // innemu dniu, w mapie dane jak na accu

        try {
            String apiUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/" + location_key + "?apikey=" + apiKey;
            String jsonResponse = sendHttpGetRequest(apiUrl);
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
            return gson.fromJson(jsonResponse, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private List<Map<String, Object>> getHistory(String location_key){ // max 24h
        // tak samo ale 24 komorki, pogoda 24h wstecz jaka była
        try {
            String apiUrl = "http://dataservice.accuweather.com/currentconditions/v1/" + location_key + "/historical/24?apikey=" + apiKey;
            String jsonResponse = sendHttpGetRequest(apiUrl);
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
            return gson.fromJson(jsonResponse, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private List<Map<String, Object>> getHourlyForecast(String location_key){ // max 12h
        // godzinowa, miejsce na liscie - rozne godziny
        try {
            String apiUrl = "http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/" + location_key + "?apikey=" + apiKey;
            String jsonResponse = sendHttpGetRequest(apiUrl);
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
            return gson.fromJson(jsonResponse, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<Map<String, Object>> getIndices(String location_key){ // zajebiste pogody dla joggingu itd
        // rozne typy aktywnosci outdorowych i ocena czy dobra pogoda na to czy zła
        // 1 element listy to jakas pojedyncza aktywnosc w danym dniu, w srodku slowniki
        try {
            String apiUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/" + location_key + "?apikey=" + apiKey;
            String jsonResponse = sendHttpGetRequest(apiUrl);
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
            return gson.fromJson(jsonResponse, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Map<String, Object> getCurrent(String location_key){ //current weather
        // sam slownik, obecna pogoda tylko
        try {
            String apiUrl = "http://dataservice.accuweather.com/currentconditions/v1/" + location_key + "?apikey=" + apiKey;
            String jsonResponse = sendHttpGetRequest(apiUrl);
            Type listType = new TypeToken<Map<String, Object>>() {}.getType();
            return gson.fromJson(jsonResponse, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String sendHttpGetRequest(String url) throws IOException {
        // pobieranie danych z serwera i konwertowanie odpowiedzi na stringa przy dobrym status codzie
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        HttpResponse httpResponse = httpClient.execute(httpGet);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            return EntityUtils.toString(httpResponse.getEntity());
        } else {
            throw new IOException("Failed to fetch data. HTTP status code: " + statusCode);
        }
    }
    public List<Object> Executor(String location, List<Integer> choices){
        // Executor, pobiera lokalizacje i List<> z wyborami jakie komendy chcemy odpalic
        // 0 - mozliwosc dzialania programu
        // 1 - pobiera godzinową
        // 2 - pobiera obecną
        // 3 - pobiera historie
        // 4 - pobiera dzienna
        // 5 - pobiera Indieces
        // zwraca liste z utworzonymi obiektami (max size 5)
        List<Object> executed = new ArrayList<>();
        if (choices.contains(0)) {
            String location_key = getLocationKey(location);
            if (choices.contains(1)) {
                List<Map<String, Object>> hourly_data = getHourlyForecast(location_key);
                executed.add(hourly_data);
            }
            if (choices.contains(2)) {
                Map<String, Object> currentCondition = getCurrent(location_key);
                executed.add(currentCondition);
            }
            if (choices.contains(3)) {
                List<Map<String, Object>> past_24h = getHistory(location_key);
                executed.add(past_24h);
            }
            if (choices.contains(4)){
                List<Map<String, Object>> daily = getDailyForecasts(location_key);
                executed.add(daily);
            }
            if (choices.contains(5)){
                List<Map<String, Object>> indices = getIndices(location_key);
                executed.add(indices);
            }
        }
        return executed;
    }
}
//TODO:
// Przetestowanie działania tego API bo mi się już nie chciało
// GUI z mozliwoscią wyborów jakie chcemy mieć dane, można ograniczać je czasowo, ja pobieram maxymalne jakie mamy dostepne
// cleaning - zostawienie tylko potrzebnych nam informacji z listy powstałej po Execute
// optymalizacja kodu i połączenie tego w całość
// +przypominam, ze jak chcemy dużo razy odpalać kod to można write i read do/z pliku zrobić, nie na projekt, ale dla nas
// dokumentacja
// komentarze do kodu
