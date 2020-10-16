package fr.redfroggy;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

/**
 * Test for the app.
 */
public class AppTest {
    private static final Logger log = LoggerFactory.getLogger(AppTest.class);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    /**
     * setup a stream to capture the output from the program
     */
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Make sure that "Hello My World!" string can be printed.
     *
     * @throws Exception
     */
    @Test
    public void canPrintHelp() throws Exception {
        String[] args = {"-help"};
        App.main(args);
        assertThat(outContent.toString()).contains("Hello My World!");
    }

    @Test
    public void cacheTest() throws InterruptedException {
        LoadingCache<String, String> tokenCache = Caffeine.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build(k -> getNewToken());

        System.out.println(tokenCache.get("token"));
        System.out.println(tokenCache.get("token"));
        Thread.sleep(3000);
        System.out.println(tokenCache.get("token"));
    }

    private String getNewToken() {
        String now = Instant.now().toString();
        System.out.println("new token :" + now);
        return now;
    }
}
