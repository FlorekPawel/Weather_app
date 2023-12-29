package ApiData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CleaningData {
    //TODO
    // napisac funkcje czyszczące dane do potrzebnych wartosci
    // wielowatkowosc i executor powinien dzialac

    private List<Map<String, Object>> cleanHourly(List<Map<String, Object>> data){
        return null;
    }
    private List<Map<String, Object>> cleanDaily(List<Map<String, Object>> data){
        return null;
    }
    private List<Map<String, Object>> cleanCurrent(List<Map<String, Object>> data){
        return null;
    }
    private List<Map<String, Object>> cleanHistory(List<Map<String, Object>> data){
        return null;
    }
    private List<Map<String, Object>> cleanIndices(List<Map<String, Object>> data){
        return null;
    }
    public List<List<Map<String, Object>>> CleaningExecutor(List<List<Map<String, Object>>> main_data){
        List<List<Map<String,Object>>> executed = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();
        Thread hourlyThread = new Thread(() -> {
            List<Map<String, Object>> hourlyData = cleanHourly(main_data.get(0));
            synchronized (executed) {
                executed.add(hourlyData);
            }
        });
        threads.add(hourlyThread);

        Thread currentConditionThread = new Thread(() -> {
            List<Map<String, Object>> currentCondition = cleanCurrent(main_data.get(1));
            synchronized (executed) {
                executed.add(currentCondition);
            }
        });
        threads.add(currentConditionThread);

        Thread past24hThread = new Thread(() -> {
            List<Map<String, Object>> past24h = cleanHistory(main_data.get(2));
            synchronized (executed) {
                executed.add(past24h);
            }
        });
        threads.add(past24hThread);

        Thread dailyThread = new Thread(() -> {
            List<Map<String, Object>> daily = cleanDaily(main_data.get(3));
            synchronized (executed) {
                executed.add(daily);
            }
        });
        threads.add(dailyThread);

        Thread indicesThread = new Thread(() -> {
            List<Map<String, Object>> indices = cleanIndices(main_data.get(4));
            synchronized (executed) {
                executed.add(indices);
            }
        });
        threads.add(indicesThread);
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return executed;
    }

}
