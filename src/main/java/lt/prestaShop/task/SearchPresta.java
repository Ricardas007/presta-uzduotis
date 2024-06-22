package lt.prestaShop.task;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class SearchPresta extends LoginPresta {

    @FindBy(css = "input[name='s']")
    WebElement searchCatalogInput;

    @FindBy(css = ".js-product-miniature h2.product-title a")
    List<WebElement> productNames;

    @FindBy(css = "#product-search-no-matches")
    WebElement noProductMessage;

    @FindBy(css = ".h1")
    WebElement productTitle;

    public SearchPresta(WebDriver driver) {
        super(driver);
    }

    public void pressSearchInput(String product) {
        searchCatalogInput.sendKeys(product, Keys.ENTER);
    }

    public List<WebElement> getProductNames() {
        return wait.until(ExpectedConditions.visibilityOfAllElements(productNames));
    }

    public boolean isNoProductMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(noProductMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clear() {
        searchCatalogInput.clear();
    }

    public void clickOnFirstProduct() {
        if (!productNames.isEmpty()) productNames.get(0).click();
    }

    public String getProductTitle() {
        return wait.until(ExpectedConditions.visibilityOf(productTitle)).getText();
    }
}