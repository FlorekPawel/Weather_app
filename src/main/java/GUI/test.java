package GUI;

import ApiData.AcuWeatherApi;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        AcuWeatherApi acu = new AcuWeatherApi();
        List<Integer> choices = new ArrayList<>();
        choices.add(0);
        choices.add(1);
        choices.add(2);
        choices.add(3);
        choices.add(4);
        choices.add(5);
        acu.Executor("Warsaw", choices);
    }
}
