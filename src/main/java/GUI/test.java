package GUI;

import ApiData.AcuWeatherApi;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        AcuWeatherApi acu = new AcuWeatherApi();
        acu.Executor("Warsaw");
    }
}
