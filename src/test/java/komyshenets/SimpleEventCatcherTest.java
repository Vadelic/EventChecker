package komyshenets;

import org.junit.Assert;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Komyshenets on 17.02.2018.
 */
class SimpleEventCatcherTest {

    @org.junit.jupiter.api.Test
    void checkEvent() throws NoSuchFieldException, IllegalAccessException {

        Logger.getLogger(SimpleEventCatcher.class.getName()).setLevel(Level.ALL);
        Field lastCleanField = SimpleEventCatcher.class.getDeclaredField("lastClean");
        lastCleanField.setAccessible(true);

        Field eventsArray = SimpleEventCatcher.class.getDeclaredField("events");
        eventsArray.setAccessible(true);


        LocalDateTime now = LocalDateTime.now();
        System.out.println(now + "\tnow");
        SimpleEventCatcher checker = new SimpleEventCatcher();
        System.out.println(now.minusHours(checker.cacheCleanPeriodMinutes) + "\tcheckPeriod");

        lastCleanField.set(checker, now);
        checker.checkEvent(now);
        checker.checkEvent(now.minusHours(25));
        checker.checkEvent(now.plusHours(25));
        Assert.assertEquals(((List) eventsArray.get(checker)).size(), 3);

        lastCleanField.set(checker, now.minusHours(48));
        checker.checkEvent(now);
        Assert.assertEquals(((List) eventsArray.get(checker)).size(), 3);

        lastCleanField.set(checker, now);
        checker.checkEvent(now);
        Assert.assertEquals(((List) eventsArray.get(checker)).size(), 4);

        lastCleanField.set(checker, now.minusHours(48));
        checker.checkEvent(now);

        Assert.assertEquals(((List) eventsArray.get(checker)).size(), 5);
        checker.checkEvent(now);
        Assert.assertEquals(((List) eventsArray.get(checker)).size(), 6);

        checker.checkEvent(now.plusHours(25));
        Assert.assertEquals(((List) eventsArray.get(checker)).size(), 7);

        checker.checkEvent(now.minusHours(25));
        Assert.assertEquals(((List) eventsArray.get(checker)).size(), 8);

        lastCleanField.set(checker, now.minusHours(48));
        checker.checkEvent(now.minusHours(25));
        Assert.assertEquals(((List) eventsArray.get(checker)).size(), 7);

        Assert.assertEquals(checker.eventsCountInLastMinutes(), 5);
        Assert.assertEquals(checker.eventsCountInLastHour(), 5);
        Assert.assertEquals(checker.eventsCountInLastDay(), 5);

        checker.checkEvent(now.minusMinutes(2));
        checker.checkEvent(now.minusMinutes(3));
        checker.checkEvent(now.minusMinutes(4));
        Assert.assertEquals(checker.eventsCountInLastMinutes(), 5);
        Assert.assertEquals(checker.eventsCountInLastHour(), 8);
        Assert.assertEquals(checker.eventsCountInLastDay(), 8);

        checker.checkEvent(now.minusHours(2));
        checker.checkEvent(now.minusHours(3));
        checker.checkEvent(now.minusHours(4));
        Assert.assertEquals(checker.eventsCountInLastMinutes(), 5);
        Assert.assertEquals(checker.eventsCountInLastHour(), 8);
        Assert.assertEquals(checker.eventsCountInLastDay(), 11);


    }

}