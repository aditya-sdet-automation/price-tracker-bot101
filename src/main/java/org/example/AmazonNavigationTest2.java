package org.example;
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
import java.util.Scanner;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AmazonNavigationTest2
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
                System.out.println("Initialising direct link connection");
                driver.get("https://www.amazon.in/OnePlus-Real-time-Adaptive-Dual-Device-connectivity/dp/B0FCMPJQ1J");

                //maximize the browser window
                driver.manage().window().maximize();

                //Look for  price element that appears in target site
                System.out.println("Page loaded, hunting for price element");
                WebElement priceElement = wait.until(d -> d.findElement(By.className("a-price-whole")));

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
          String botPassword = "lxczvwkeeuwpxzhh";

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