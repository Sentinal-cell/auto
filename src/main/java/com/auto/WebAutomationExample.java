package com.auto;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebAutomationExample {
    public static void main(String[] args) {
        // Set the path to your WebDriver executable without the trailing slash
        System.setProperty("webdriver.chrome.driver", "C:/Users/Ahmad/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe");

        // Configure ChromeOptions for compatibility and stability
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");  // Run in headless mode
        // Initialize WebDriver with ChromeOptions
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://nhcminna.org/resultchecker.php");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"form-name\"]")));
            usernameField.sendKeys("NHC/2538");

            WebElement reportSelectElement = driver.findElement(By.id("report"));
            Select reportSelect = new Select(reportSelectElement);
            reportSelect.selectByValue("pm");

            WebElement submitButton = driver.findElement(By.xpath("//button[text()='Submit']"));
            submitButton.click();

            // Wait for the results page to load and display the HTML content
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            String pageSource = driver.getPageSource();
            String name = driver.findElement(By.xpath("//strong[contains(text(),'NAME:')]")).getText();
            String amount = driver.findElement(By.xpath("//h2[contains(text(),'N')]")).getText();
            System.out.println("Name: " + name);
            System.out.println("Amount: " + amount);
            //System.out.println(pageSource);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure the driver quits properly
            driver.quit();
        }
    }
}
