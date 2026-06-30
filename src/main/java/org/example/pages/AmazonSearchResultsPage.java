/**
 * PAGE OBJECT: Search Results (Basic)
 * Handles baseline element extraction from the search results page.
 * Receives the WebDriver and target product name from the main execution script.
 */

package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class AmazonSearchResultsPage {
    private By targetElementLocator = By.xpath("//div[@data-asin='B0FCMPJQ1J']");
    private By priceElementLocator = By.xpath(".//span[@class='a-price-whole']");

    public int getVerifiedPrice(WebDriver driver, String productName)
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Look for price element that appears in target site
        List<WebElement> targetElement = wait.until(d -> d.findElements(targetElementLocator));
        int targetElementListCount = targetElement.size();
        String productTitle = "";

        for(int i = 0;i<targetElementListCount;i++) {
            //Double check if the product is correctly assigned or not
            productTitle = targetElement.get(i).getText().toLowerCase().trim();
            String[] clientModel = productName.toLowerCase().split(" ");
            int clientWordcount = clientModel.length;
            int foundWordCount = 0;
            for (int j = 0; j < clientWordcount; j++) {
                if (productTitle.contains(clientModel[j])) {
                    foundWordCount++;
                }
            }
            if (foundWordCount == clientWordcount) {
                System.out.println("Target verified");
                WebElement priceElement = targetElement.get(i).findElement(priceElementLocator);
                String priceValue = priceElement.getText();
                int cleanPrice = Integer.parseInt(priceValue.replace(",", ""));
                System.out.println("The current price is: Rupees " + priceValue);
                return cleanPrice;
            }
        }
        throw new RuntimeException("Scraped the wrong product! Title found: " + productTitle);
    }
}
