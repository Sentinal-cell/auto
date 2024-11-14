package com.auto;
import java.sql.*;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebAutomationExample {
    private static final String url = "jdbc:mysql://localhost:3306/scrape";
    private static final String usr = "root";
    private static final String pass = "root";

    public static void main(String[] args) {
        int fad = 2461;
        boolean pro = false;

        // Loop to repeat the process for different sessions
        while (!pro) {
            // Set up WebDriver
            System.setProperty("webdriver.chrome.driver", "C:/Users/Ahmad/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe");

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--headless"); // Running in headless mode (optional)

            WebDriver driver = new ChromeDriver(options);
            Connection connection = null;
            Statement statement = null;

            try {
                connection = DriverManager.getConnection(url, usr, pass);
                statement = connection.createStatement();
                driver.get("https://nhcminna.org/resultchecker.php");

                // Perform actions for this iteration
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='form-name']")));
                String adm = "NHC/" + String.valueOf(fad);
                usernameField.sendKeys(adm);
                
                WebElement reportSelectElement = driver.findElement(By.id("report"));
                Select reportSelect = new Select(reportSelectElement);
                reportSelect.selectByValue("pm");

                WebElement submitButton = driver.findElement(By.xpath("//button[text()='Submit']"));
                submitButton.click();

                // Wait for the page to load
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                String cssSelector = "div.alert.alert-danger";
                boolean alertPresent = isAlertPresent(driver, ".alert.alert-danger");
                                if (alertPresent) {
                                    System.out.println("Alert is present: " +adm);
                                    System.out.println("False ");
                                } else {
                                String name = driver.findElement(By.xpath("//strong[contains(text(),'NAME:')]")).getText().replaceFirst("NAME: ", "");
                                String a = driver.findElement(By.xpath("//h2[contains(text(),'N')]")).getText().replaceFirst("N", "").replace(",", "");
                                float am = Float.parseFloat(a);
                                int amount = (int) am;
                                System.out.println("Name: " + name);
                                System.out.println("Amount: " + a);
                                String update = "INSERT INTO students (name, admission_number, balance) VALUES ('" + name + "', '" + adm + "', " + amount + ")";
                                statement.executeUpdate(update);
                                }
                                // Increment fad for next iteration or stop the loop
                                if (fad < 2700) {
                                    fad = fad + 1;
                                } else {
                                    pro = true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                // Quit the current driver (close the browser)
                                if (driver != null) {
                                    driver.quit();
                                }
                                // Close the DB connection
                                try {
                                    if (connection != null) {
                                        connection.close();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    public static boolean isAlertPresent(WebDriver driver, String cssSelector) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
            return true; // Element is present
        } catch (Exception e) {
            return false; // Element is not present
        }
    }
    
}
