import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class NavigationTest1
{
    public static void main(String[] args) {
      System.out.println("Initiating Browser Hijack");

      // create an instance of WebDriver(This actually opens a firefox window)
        WebDriver driver = new FirefoxDriver();

        try
        {
            //command browser to navigate to website
            driver.get("https://en.wikipedia.org/wiki/Spider-Man");

            //maximize the browser window
            driver.manage().window().maximize();

            //Extract title of webpage to verify we landed correctly
            String pageTitle = driver.getTitle();
            System.out.println("Target acquired. Page Title: " + pageTitle);

            //pause for 5 seconds so you can visually confirm the action(Temporary crutch)
            Thread.sleep(5000);

        }
        catch (InterruptedException e)
        {
            System.out.println("Thread interruption detected");
        }
        finally {
            //Terminate the session and close the browser completely
            driver.quit();
            System.out.println("Session terminated. Browser closed");
        }
    }
}
