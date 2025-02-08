import dev.failsafe.internal.util.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TestSelenium {
    static FirefoxDriver driver;

    @BeforeTest
    public void testPrep(){
        System.out.println("BeforeTest");
        //Setup Driver
        driver = new FirefoxDriver();

        // Add implicit wait
        Duration waitDuration = Duration.ofSeconds(15);
        driver.manage().timeouts().implicitlyWait(waitDuration);

    }

    @Test
    public void guruTest(){
        System.out.println("Test");

        // Launch URL
        driver.get("https://demo.guru99.com/");

        // Deal with privacy popup
        WebElement iframe = driver.findElement(By.xpath("//iframe[@id='ccpa-consent-notice']"));
        driver.switchTo().frame(iframe);
        WebElement privacySaveAndExitButton = driver.findElement(By.xpath("//strong[normalize-space()='Save and Exit']"));
        if(privacySaveAndExitButton.isDisplayed()){
            privacySaveAndExitButton.click();
        }
        driver.switchTo().defaultContent();

        //Input login credential
        WebElement element=driver.findElement(By.xpath("//input[@name='emailid']"));
        element.sendKeys("abc@gmail.com");
        WebElement button=driver.findElement(By.xpath("//input[@name='btnLogin']"));

        // Click submit button to login
        button.click();

        // Collect information
        SoftAssert softAssert = new SoftAssert();
        WebElement userIdValueElement = driver.findElement(By.xpath("//td[normalize-space()='User ID :']/following-sibling::td"));
        String userIdValue = userIdValueElement.getText();
        softAssert.assertTrue(userIdValue.contains("mngr"), "userIdValue did not contain 'mngr'");
        System.out.println("UserIdValue: " + userIdValue);
        WebElement userPasswordValueElement = driver.findElement(By.xpath("//td[normalize-space()='Password :']/following-sibling::td"));
        String userPasswordValue = userPasswordValueElement.getText();
        System.out.println("UserPasswordValue: " + userPasswordValue);
        softAssert.assertTrue(userPasswordValue.contains("123456!"), "userPasswordValue did not contain '123456!'");
        WebElement headerElement = driver.findElement(By.cssSelector(".barone"));
        String headerValue = headerElement.getText();
        softAssert.assertTrue(headerValue.equals("Guru99 Bank"), "headerElement was '" + headerValue + "' instead of 'Guru99 Bank'");
        softAssert.assertAll();

    }

    @AfterTest
    public void tearDown(){
        System.out.println("AfterTest");
        driver.quit();
    }

}
