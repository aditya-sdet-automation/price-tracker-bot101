import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import java.time.Duration;

public class AmazonNavigationTest1 {

    public static void main(String[] args) {
        System.out.println("Initiating Browser Hijack");

        // create an instance of WebDriver(This actually opens a firefox window)
        WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try
        {
            //command browser to navigate to website
            driver.get("https://www.amazon.in/");

            //maximize the browser window
            driver.manage().window().maximize();

            //use Locator(id is regularly changed by amazon)
            WebElement searchBox = wait.until(d -> d.findElement(By.id("twotabsearchtextbox")));

            //Interaction: Type, Enter
            System.out.println("Searchbox located!");
            System.out.println("Searching for target item...");
            searchBox.sendKeys("Stethoscope" + Keys.ENTER);

            //Verify if new page has loaded by checking the header
            wait.until(d -> d.getTitle().contains("Stethoscope"));
            System.out.println("Successfully navigated to: " + driver.getTitle());

            //Look for first price element that appears in search results
            WebElement firstPrice = wait.until(d -> d.findElement(By.className("a-price-whole")));

            String priceValue = firstPrice.getText();
            System.out.println("The current price is: Rupees " + priceValue);

            //Freeze the browser for 5 seconds so human eyes can see it
            Thread.sleep(5000);

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