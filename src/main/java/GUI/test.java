package GUI;

import ApiData.AcuWeatherApi;
import ApiData.CleaningData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        AcuWeatherApi acu = new AcuWeatherApi();
        CleaningData cleaningData = new CleaningData();
        List<List<Map<String,Object>>> downloaded_data = acu.Executor("Warsaw");
        List<List<Map<String,Object>>> cleaned_data = cleaningData.CleaningExecutor(downloaded_data);

    }
}
