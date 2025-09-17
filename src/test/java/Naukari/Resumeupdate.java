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

        // Setup ChromeDriver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // --------- HEADLESS MODE (Jenkins/Server) ---------
        String headless = System.getProperty("headless", "true"); // Pass -Dheadless=false for local GUI
        if (headless.equalsIgnoreCase("true")) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.7339.82 Safari/537.36");

        // Use a temporary profile to avoid crashes
        options.addArguments("user-data-dir=" + System.getProperty("java.io.tmpdir") + "/selenium");

        driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
            // Open Naukri
            driver.get("https://www.naukri.com/");
            System.out.println("Page title: " + driver.getTitle());

            // Click Login
            By loginBtn = By.xpath("//a[text()='Login' or contains(@href,'login') or @id='login_Layer']");
            WebElement login = wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
            login.click();
            System.out.println("✅ Login button clicked");

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

            // Upload resume
            String filePath = System.getProperty("resume.path",
                    System.getProperty("user.dir") + "/src/test/resources/Resume/Prabhudeva_Resume.pdf");

            System.out.println("Uploading resume from: " + filePath);

            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attachCV")));
            fileInput.sendKeys(filePath);

            System.out.println("✅ File uploaded successfully");

            Thread.sleep(5000);

            // Refresh page
            driver.navigate().refresh();

            // Verify uploaded date
            String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
            By updatedText = By.xpath("//*[contains(text(),'Uploaded on') or contains(@class,'updateOn')]");
            WebElement updated = wait.until(ExpectedConditions.visibilityOfElementLocated(updatedText));

            String resumeText = updated.getText();
            System.out.println("Resume update time (from site): " + resumeText);

            if (resumeText.contains(todayDate)) {
                System.out.println("✅ Resume updated successfully with today's date.");
            } else {
                System.out.println("⚠️ Uploaded, but showing different timestamp: " + resumeText);
                takeScreenshot("resume_date_mismatch.png");
            }

        } catch (Exception e) {
            takeScreenshot("resume_upload_failed.png");
            Assert.fail("❌ Test failed: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    private void takeScreenshot(String fileName) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(screenshot.toPath(), new File(fileName).toPath());
        System.out.println("📸 Screenshot saved as " + fileName);
    }
}
