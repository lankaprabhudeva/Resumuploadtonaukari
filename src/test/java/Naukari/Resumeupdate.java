package Naukari;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Resumeupdate {
    
    WebDriver driver;
    
    @Test
    public void naukarilogin() throws InterruptedException, IOException {
        
        // Setup ChromeDriver in headless mode for Jenkins
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");   // Headless for Jenkins
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
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
        
        // Upload resume (path from Jenkins property OR fallback to local path)
        String filePath = System.getProperty("resume.path",
                System.getProperty("user.dir") + "/src/test/resources/Resume/Prabhudeva_Resume.pdf");
        
        System.out.println("Uploading resume from: " + filePath);
        
        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attachCV")));
        fileInput.sendKeys(filePath);
        
        System.out.println("✅ File uploaded successfully");
        
        // Wait after upload
        Thread.sleep(5000);

        // Refresh page to load updated text
        driver.navigate().refresh();
        
        // Expected today date
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
        
        // Check for "Uploaded on" text
        try {
            WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(40));
            By updatedText = By.xpath("//*[contains(text(),'Uploaded on') or contains(@class,'updateOn')]");
            WebElement updated = wait2.until(ExpectedConditions.visibilityOfElementLocated(updatedText));
            
            String resumeText = updated.getText();
            System.out.println("Resume update time: " + resumeText);
            
            // Validate today's date
            if (resumeText.contains(today)) {
                System.out.println("✅ Resume updated successfully with today's date: " + today);
            } else {
                System.out.println("⚠️ Uploaded, but showing different date: " + resumeText);
                takeScreenshot("resume_date_mismatch.png");
            }
        } catch (Exception e) {
            takeScreenshot("resume_upload_failed.png");
            Assert.fail("⚠️ Could not verify 'Uploaded on' text. Screenshot saved.");
        }
        
        driver.quit();
    }
    
    private void takeScreenshot(String fileName) throws IOException {
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        Files.copy(screenshot.toPath(), new File(fileName).toPath());
        System.out.println("📸 Screenshot saved as " + fileName);
    }
}
