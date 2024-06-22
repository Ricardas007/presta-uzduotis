package lt.prestaShop.task;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class BaseTest {

    protected static WebDriver driver;

    @BeforeAll
    public static void setUp() {
        driver = new ChromeDriver();
        driver.get("http://192.168.0.27/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
    }

    @AfterAll
    static void tearsDown() {
        if (driver != null){
            driver.quit();
        }
    }
}
