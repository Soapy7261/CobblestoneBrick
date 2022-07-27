import dev.JustRed23.stonebrick.app.Application;

import java.util.concurrent.TimeUnit;

public class ApplicationTest$TestApp extends Application {


    protected void init() throws Exception {
        System.out.println("App init");
    }

    protected void start() throws Exception {
        System.out.println("App start");
        Application.runLater(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            exit();
        });
    }

    protected void stop() throws Exception {
        System.out.println("App stop");
    }
}
