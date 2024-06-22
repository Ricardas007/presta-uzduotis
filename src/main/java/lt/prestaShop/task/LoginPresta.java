package lt.prestaShop.task;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPresta extends RegisterPresta{

    @FindBy (css = "button#submit-login")
    WebElement pressSubmitBtn;

    @FindBy (css = "[title] .hidden-sm-down")
    WebElement accountOwnerName;

    @FindBy (css = "#identity-link .material-icons")
    WebElement userAccountinformationSection;

    @FindBy (css = "input#field-email")
    WebElement userDetailEmail;

    @FindBy (css = ".hidden-sm-down.logout")
    WebElement userSignOut;



    @Override
    public void navigateToRegisterPage() {
        signInIcon.click();
    }

    public LoginPresta(WebDriver driver) {
        super(driver);
    }

    public void fillTeFormWithDataAndSubmit(String userEmail, String userPassword) {
        userEMail.sendKeys(userEmail);
        userPasswd.sendKeys(userPassword);
        pressSubmitBtn.click();
    }

    public void navigatetoUserDetailPage() {
        accountOwnerName.click();
    }

    public void navigateToInfoSec(){
        userAccountinformationSection.click();
    }

    public String ownerEmail() {
        String name = userDetailEmail.getAttribute("value");
        return name;
    }

    public void signOutPage() {
        userSignOut.click();
    }

}
