package Naukari;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Resumeupdate {
    
    WebDriver driver;
    
    @Test
    public void naukarilogin() throws InterruptedException {
        
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        
        driver.get("https://www.naukri.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        
        // Login
        WebElement login = driver.findElement(By.xpath("//a[contains(text(),'Login')]"));
        login.click();
        
        WebElement username = driver.findElement(By.xpath("//input[@placeholder='Enter your active Email ID / Username']"));
        username.sendKeys("prabhudeva.lsndc@gmail.com");
        
        WebElement password = driver.findElement(By.xpath("//input[@placeholder='Enter your password']"));
        password.sendKeys("Satyam484@L#");
        
        WebElement button = driver.findElement(By.xpath("(//button[contains(text(),'Login')])[1]"));
        button.click();
        
        WebElement profilepic=driver.findElement(By.xpath("//img[@class=\"nI-gNb-icon-img\"]"));
        profilepic.click();
        
        WebElement viewandprofileupdate=driver.findElement(By.xpath("//a[@class=\"nI-gNb-info__sub-link\"]"));
        viewandprofileupdate.click();
        
        
        
        
        WebElement updateresume=driver.findElement(By.xpath("//input[@value=\"Update resume\"]"));
        
        Actions ac=new Actions(driver);
        ac.moveToElement(updateresume).perform();
        
      
        
     // Correct absolute path
        String filePath = "C:\\Users\\ADMIN\\eclipse-workspace_new\\Naukariproject\\Resume\\Prabhudeva_Resume.pdf";

        // Locate file input (after hover if needed)
        WebElement fileInput = driver.findElement(By.id("attachCV"));
        fileInput.sendKeys(filePath);

        System.out.println("✅ File uploaded successfully");

        
        
        
        
        
        
        
    }
}
