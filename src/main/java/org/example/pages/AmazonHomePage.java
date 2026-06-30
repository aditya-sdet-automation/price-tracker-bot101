/**
 * PAGE OBJECT: Amazon Home Page
 * Handles the baseline UI locators and search bar interactions for the landing page.
 */


package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
//pom does not have the driver.get.... part,its better to keep it in main test script.
public class AmazonHomePage {
        private By searchBarLocator = By.xpath("//input[@placeholder='Search Amazon.in']");
        private By searchGlassLocator = By.xpath("//input[@value='Go']");

        public void enterSearchTerm(WebDriver driver, String productName)
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement searchElement = wait.until(d -> d.findElement(searchBarLocator));
            searchElement.sendKeys(productName);
            WebElement searchGlass = wait.until(d -> d.findElement(searchGlassLocator));
            searchGlass.click();
        }
}
