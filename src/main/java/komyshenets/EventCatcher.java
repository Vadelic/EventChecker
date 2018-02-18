package komyshenets;

import java.time.LocalDateTime;

/**
 * Created by Komyshenets on 17.02.2018.
 */
public interface EventCatcher {
    void checkEvent(LocalDateTime time);

    long eventsCountInLastMinutes();

    long eventsCountInLastHour();

    long eventsCountInLastDay();
}
