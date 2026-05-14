import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class NavigationTest2 {

        public static void main(String[] args) {
            System.out.println("Initiating Browser Hijack");

            // create an instance of WebDriver(This actually opens a firefox window)
            WebDriver driver = new FirefoxDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            try
            {
                //command browser to navigate to website
                driver.get("https://en.wikipedia.org/wiki/Spider-Man");

                //maximize the browser window
                driver.manage().window().maximize();

                //smart wait-pause until title contains our target words
                System.out.println("Waiting for page to load...");
                wait.until(d -> d.getTitle().contains("Spider-Man"));

                //Extract title of webpage to verify we landed correctly
                String pageTitle = driver.getTitle();
                System.out.println("Target acquired. Page Title: " + pageTitle);

            }
            catch (Exception e)
            {
                //if 10 seconds pass and the title never appears, it throws an error here
                System.out.println("Excecution failed or timed out: " + e.getMessage());
            }
            finally {
                //Terminate the session and close the browser completely
                driver.quit();
                System.out.println("Session terminated. Browser closed");
            }
        }
    }