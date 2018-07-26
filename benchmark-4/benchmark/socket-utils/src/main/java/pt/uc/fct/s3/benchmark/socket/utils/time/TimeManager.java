package pt.uc.fct.s3.benchmark.socket.utils.time;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimeManager {
    private Map<UUID, Long> timeMap;
    private int requestNumber;
    private long totalTime;

    private static TimeManager ourInstance = new TimeManager();

    public static TimeManager getInstance() {
        return ourInstance;
    }

    private TimeManager() {
        requestNumber = 0;
        totalTime = 0;
        timeMap = new HashMap<>();
    }

    public void start(UUID uuid){
        timeMap.put(uuid, System.nanoTime());
    }

    public long rtt(UUID uuid){
        long start = timeMap.get(uuid);
        timeMap.remove(uuid);
        requestNumber++;
        final long rtt = System.nanoTime() - start;
        totalTime+=rtt;
        return rtt;
    }

    public double throughput(){
        return (double)requestNumber/(double)totalTime;
    }
}
