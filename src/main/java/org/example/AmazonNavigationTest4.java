/**
 * VERSION 4: Proof of Concept - Hardcoded Target Search
 * * This script serves as the baseline automation test. It successfully navigates
 * to the Amazon home page, inputs a product name, and filters the search results
 * using a specific ASIN.
 * Note: Deprecated due to the ASIN being hardcoded into the main script,
 * limiting scalability to a single product.
 */
package org.example;
import com.sun.source.util.SourcePositions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.time.Duration;
import java.io.FileWriter;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AmazonNavigationTest4
{

    public static void main(String[] args)
    {
        System.out.println("Initiating Browser Hijack");
        //BROWSER STEALTH & HEADLESS GHOST MODE
        org.openqa.selenium.firefox.FirefoxOptions options = new org.openqa.selenium.firefox.FirefoxOptions();

        //Make it invisible(Headless)
        options.addArguments("-headless");

        //Anti bot evasion(Remove bot tags)
        options.addPreference("dom.webdriver.enabled", false);
        options.addPreference("useAutomationExtension", false);

        // create an instance of WebDriver(This actually opens a firefox window)
        WebDriver driver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        int oldPrice=0;
        int cleanPrice=0;

        while (true) {
            try {
                //Sticky note to remember last price for comparison
                File stickyNote = new File("last_known_price.txt");
                Scanner sc = new Scanner(stickyNote);
                oldPrice = Integer.parseInt(sc.nextLine().replace(",", ""));
                sc.close();

                //command browser to navigate to website
                System.out.println("Opening Amazon....");
                driver.get("https://www.amazon.in/");

                //Look for  search element that appears in target site
                System.out.println("Page loaded, hunting for search element");
                WebElement searchElement = wait.until(d -> d.findElement(By.xpath("//input[@placeholder='Search Amazon.in']")));
                searchElement.sendKeys("One Plus Buds 4");
                WebElement searchGlass = wait.until(d -> d.findElement(By.xpath("//input[@value='Go']")));
                searchGlass.click();

                //Look for price element that appears in target site
                WebElement targetElement = wait.until(d -> d.findElement(By.xpath("//div[@data-asin='B0FCMPJQ1J']")));
                WebElement priceElement = targetElement.findElement(By.xpath(".//span[@class='a-price-whole']"));;

                //Double check if the product is correctly assigned or not
                String productTitle = targetElement.getText().toLowerCase().trim();
                String brand = "OnePlus".toLowerCase().trim();
                String model = "Buds 4".toLowerCase().trim();
                if(productTitle.contains(brand) && productTitle.contains(model))
                    System.out.println("Target verified");
                else{
                    System.out.println("Scraped the wrong product !");
                    System.out.println(productTitle);
                    System.exit(0);
                }

                String priceValue = priceElement.getText();
                cleanPrice = Integer.parseInt(priceValue.replace(",", ""));
                System.out.println("The current price is: Rupees " + priceValue);

                //Memory Logic
                //get current date and time
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = now.format(formatter);

                //format sentence we want to save into file
                String dataRecord = timestamp + " | Target Item Price: ₹" + priceValue + "\n";

                //The Pen(FileWriter)
                //True is important = tells Java to append to the end of the file
                //if false,Java would erase history every time it runs
                FileWriter fileWriter = new FileWriter("price_history.txt", true);
                fileWriter.write(dataRecord);
                fileWriter.close();//always put the cap back on pen!
                System.out.println("Data permanently saved to price_history.txt");
                FileWriter fileWriter1 = new FileWriter("last_known_price.txt");
                fileWriter1.write(String.valueOf(cleanPrice));
                fileWriter1.close();

            } catch (Exception e) {
                //if 10 seconds pass and the title never appears, it throws an error here
                System.out.println("Excecution failed or timed out: " + e.getMessage());
                sendEmailAlert("KEEPA BOT CLONE: System Error", "The bot failed to find the price.Amazon may have blocked it. Error: " + e.getMessage());
            } finally {
                //Terminate the session and close the browser completely
                driver.quit();
                System.out.println("Session terminated. Browser closed");
                priceReductionCheck(oldPrice, cleanPrice);
            }
            try {
                Thread.sleep(3600000);
            }
            catch (Exception e)
            {
                //could not go to sleep. Must have drank a lot of coffee!
            }
        }
    }

    static double discountCheck(int oldCost, int newCost)
    {
        double dropAmount = oldCost - newCost;
        double percentageDrop = ((dropAmount)/oldCost)*100;
        return percentageDrop;
    }

    //check if there is a price reduction
    static boolean priceReductionCheck(int oldPrice, int cleanPrice)
    {
        boolean pricelowered = false;
        if(cleanPrice<oldPrice)
            pricelowered = true;
        if(pricelowered==true)
        {
            double discount = discountCheck(oldPrice, cleanPrice);
            System.out.println("ALERT, PRICE LOWERED!!!");
            System.out.println("PRICE DROPPED BY " + discount + "%!");
            sendEmailAlert("KEEPA BOT CLONE: Price Drop!", "The OnePlus Buds 4 dropped by " + discount + "%! Current price: Rs. " + cleanPrice);
        }
        else System.out.println("There are no changes in current prices");
        return pricelowered;
    }

    //sends email when price drops
    static void sendEmailAlert(String subject, String emailBody)
    {
        Properties properties = new Properties(); //instance of properties class
        properties.put("mail.smtp.auth", "true"); //smtp = simple mail transfer protocol
        properties.put("mail.smtp.starttls.enable", "true"); //tls = enable security
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String botmail = "aditya.tester.2026@gmail.com";
        String botPassword = System.getenv("BOT_PASSWORD");

        //creating a session instance == connecting to server
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(botmail, botPassword);
            }
        });

        //sending mail comes under try catch block
        try
        {
            Message message = new MimeMessage(session);

            //who is it from? converts string email to internetaddress
            message.setFrom(new InternetAddress(botmail));

            //who is it going to?
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("reena69r96@gmail.com"));

            //Subject
            message.setSubject(subject);

            //Body
            message.setText(emailBody);

            //SEND BUTTON
            Transport.send(message);

            System.out.println("EMAIL ALERT SENT SUCCESSFULLY");
        }
        catch (Exception e)
        {
            System.out.println("failed to send email. Error: " + e.getMessage());
        }
    }
}