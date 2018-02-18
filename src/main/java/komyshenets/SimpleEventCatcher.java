package komyshenets;

import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Komyshenets on 17.02.2018.
 */
public class SimpleEventCatcher implements EventCatcher {
    private Logger log = Logger.getLogger(SimpleEventCatcher.class);
    //    private final ConcurrentLinkedQueue<LocalDateTime> events = new ConcurrentLinkedQueue<>();
    private final List<LocalDateTime> events = new ArrayList<>();
    private volatile LocalDateTime lastClean;
    int cacheCleanPeriodMinutes = 24 * 60;//cache clean each day

    public SimpleEventCatcher() {
        this.lastClean = LocalDateTime.now();
    }


    @Override
    public void checkEvent(LocalDateTime time) {
        synchronized (events) {
            events.add(time);
        }
        cleanCache();
    }

    private void cleanCache() {
        LocalDateTime limitTime = LocalDateTime.now().minusMinutes(cacheCleanPeriodMinutes);

        log.debug("\n" + lastClean + "\tlastClean" +
                "\n" + limitTime + "\tlimit time " +
                "\nisBefore " + lastClean.isBefore(limitTime));

        if (lastClean.isBefore(limitTime)) {
            synchronized (events) {
                events.removeIf(time -> time.isBefore(limitTime));
            }
            lastClean = LocalDateTime.now();
        }
    }

    private long eventsCountInLastSeconds(int sec) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.minusSeconds(sec);
        synchronized (events) {
            return events.stream()
                    .filter(t -> t.isAfter(limit))
                    .filter(t -> t.isBefore(now) || t.isEqual(now))
                    .count();
        }
    }

    @Override
    public long eventsCountInLastMinutes() {
        return eventsCountInLastSeconds(60);
    }

    @Override
    public long eventsCountInLastHour() {
        return eventsCountInLastSeconds(60 * 60);
    }

    @Override
    public long eventsCountInLastDay() {
        return eventsCountInLastSeconds(60 * 60 * 24);
    }
}
