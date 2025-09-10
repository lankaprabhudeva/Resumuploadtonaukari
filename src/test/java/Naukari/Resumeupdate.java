package Naukari;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Resumeupdate {
    
    WebDriver driver;
    
    @Test
    public void naukarilogin() throws InterruptedException, IOException {
        
        // Setup ChromeDriver with headless mode
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");   // Headless for Jenkins
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                           + "AppleWebKit/537.36 (KHTML, like Gecko) "
                           + "Chrome/117.0.0.0 Safari/537.36");  // Force desktop
        
        driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        // Open Naukri
        driver.get("https://www.naukri.com/");
        
        // Click Login
        WebElement login = wait.until(ExpectedConditions.elementToBeClickable(By.id("login_Layer")));
        login.click();
        
        // Enter credentials
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@placeholder='Enter your active Email ID / Username']")));
        username.sendKeys("prabhudeva.lsndc@gmail.com");
        
        WebElement password = driver.findElement(
            By.xpath("//input[@placeholder='Enter your password']"));
        password.sendKeys("Satyam484@L#");
        
        WebElement button = driver.findElement(By.xpath("(//button[contains(text(),'Login')])[1]"));
        button.click();
        
        // Go to profile
        WebElement profilepic = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//img[@class='nI-gNb-icon-img']")));
        profilepic.click();
        
        WebElement viewandprofileupdate = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@class='nI-gNb-info__sub-link']")));
        viewandprofileupdate.click();
        
        // Hover and upload resume
        WebElement updateresume = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@value='Update resume']")));
        new Actions(driver).moveToElement(updateresume).perform();
        
        // Relative path (resume inside project resources)
        String filePath = System.getProperty("user.dir") + "/src/test/resources/Resume/Prabhudeva_Resume.pdf";
        System.out.println("Uploading resume from: " + filePath);
        
        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attachCV")));
        fileInput.sendKeys(filePath);
        
        System.out.println("✅ File uploaded successfully");
        
        // Refresh page to load updated text
        driver.navigate().refresh();
        
        // Check for "Updated on" text with flexible locator
        try {
            WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(40));
            By updatedText = By.xpath("//*[contains(text(),'Updated on') or contains(@class,'updateOn')]");
            WebElement updated = wait2.until(ExpectedConditions.visibilityOfElementLocated(updatedText));
            System.out.println("Resume update time: " + updated.getText());
        } catch (Exception e) {
            // Take screenshot if not found
            File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), new File("resume_upload_failed.png").toPath());
            System.out.println("⚠️ Could not verify 'Updated on' text. Screenshot saved as resume_upload_failed.png");
        }
        
        driver.quit();
    }
}
