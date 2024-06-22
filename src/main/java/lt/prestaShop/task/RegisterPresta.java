package lt.prestaShop.task;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RegisterPresta extends BasePage{

    @FindBy(css =".user-info [title]")
    WebElement signInIcon;

    @FindBy(css = "[data-link-action='display-register-form']")
    WebElement createAccountBnt;

    @FindBy(css = "input#field-firstname")
    WebElement userFName;

    @FindBy(css = "input#field-lastname")
    WebElement userLName;

    @FindBy(css = "input#field-email")
    WebElement userEMail;

    @FindBy(css = "input#field-password")
    WebElement userPasswd;

    @FindBy(css = "[for='field-id_gender-1'] [type]")
    WebElement checkSexCheckMale;

    @FindBy(css ="input[name='psgdpr']")
    WebElement checkTermsCond;

    @FindBy(css ="[name='customer_privacy']")
    WebElement checkDataPriva;

    @FindBy(css = "[data-link-action]")
    WebElement pushSeveBtn;

    @FindBy(css = "[title] .hidden-sm-down")
    WebElement customerAccountName;

    public RegisterPresta(WebDriver driver) {
        super(driver);
    }

    public void navigateToRegisterPage() {
        signInIcon.click();
        createAccountBnt.click();
    }

    public void fillTeFormWithData(String firstName, String lastName, String userEmail, String userPassword) {
        userFName.sendKeys(firstName);
        userLName.sendKeys(lastName);
        userEMail.sendKeys(userEmail);
        userPasswd.sendKeys(userPassword);
    }

    public void clickNecessaryCheckbox() {
        checkSexCheckMale.click();
        checkTermsCond.click();
        checkDataPriva.click();
        pushSeveBtn.click();
    }

    public String chekUserName() {
        String name = customerAccountName.getText();
        return name;
    }

    void takeScreenshot(String description, Exception e) {
        System.out.println("Test fail. Taking screenshot...");
        TakesScreenshot ts = (TakesScreenshot) driver;
        File srcFile = ts.getScreenshotAs(OutputType.FILE);
        try {
            Files.createDirectories(Paths.get("screenshots"));
            Files.copy(srcFile.toPath(), Paths.get("screenshots", description + ".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        throw new RuntimeException(e);
    }

}
