/**
 * PAGE OBJECT: Search Results (Advanced Heuristic Filter)
 * An upgraded POM designed to combat false positives in search results.
 * Accepts a dynamic array of blacklisted strings from the main script to
 * programmatically skip unauthorized accessories, cases, and incorrect models.
 */

package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class AmazonSearchResultsPage2 {
    private By targetElementLocator = By.xpath("//div[@data-component-type='s-search-result']");
    private By priceElementLocator = By.xpath(".//span[@class='a-price-whole']");

    public int getVerifiedPrice(WebDriver driver, String productName, String[] blacklist) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Look for price element that appears in target site
        List<WebElement> targetElement = wait.until(d -> d.findElements(targetElementLocator));
        int targetElementListCount = targetElement.size();
        String productTitle = "";

        for (int i = 0; i < targetElementListCount; i++) {
            //Double check if the product is correctly assigned or not
            productTitle = targetElement.get(i).getText().toLowerCase().trim();
            String[] clientModel = productName.toLowerCase().split(" ");
            int clientWordcount = clientModel.length;
            int foundWordCount = 0;
            boolean containsBadWord = false;

            for (String badWord : blacklist) {//check for blacklisted words
                if (productTitle.contains(badWord) && !productName.toLowerCase().contains(badWord)) { //if product found on page has a blacklisted word and the requested product does not have the blacklisted word in its name
                    containsBadWord = true;
                    break;
                }
            }

            if (containsBadWord)
                continue; //skips the current loop and moves on to next product of the page

            //check if the scraped titles have the words matching the name given by client
            String[] scrapedWords = productTitle.split(" ");
            for (String modelWord : clientModel) {
                boolean found = false;
                for (String scrapedWord : scrapedWords) {
                    if (scrapedWord.equalsIgnoreCase(modelWord)) {
                        found = true;
                        break;
                    }
                }
                if (found)
                    foundWordCount++;
            }

            if (foundWordCount == clientWordcount) {
               System.out.println("Target verified! Found: " + productTitle);
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
