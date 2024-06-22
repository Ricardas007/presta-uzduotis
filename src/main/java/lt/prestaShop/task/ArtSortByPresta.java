package lt.prestaShop.task;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArtSortByPresta extends SearchPresta{

    @FindBy (id = "category-9")
    WebElement navigateToArtPage;

    @FindBy(css = ".dropdown-menu > a")
    List<WebElement> options;

    @FindBy(css = ".js-product-miniature.product-miniature .price")
    List<WebElement> productPrices;

    @FindBy (css = ".js-product-flags.product-flags > .discount.product-flag")
    WebElement discountPercent;

    @FindBy (css = ".js-product-miniature.product-miniature h2")
    List<WebElement> productName;


    public ArtSortByPresta(WebDriver driver) {
        super(driver);
    }


    public void navigateToArtPage() {
        navigateToArtPage.click();
    }

    public boolean isDiscountPresent(WebElement product) {
        try {
            return discountPercent.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void selectSort(int index) {
        if (index >= 0 && index < options.size()) {
            WebElement option = options.get(index);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click;", option);
        } else {
            throw new IllegalArgumentException("Invalid index: " + index);
        }
    }

    public List<String> sortedPrices() {
        return productPrices.stream().map(WebElement::getText).collect(Collectors.toList());
    }

//    public void byNameAToZ() {
//        for (int i = 0; i < productName.size(); i++) {
//
//        }
//    }

    public List<Double> getProductPrices() {
        return productPrices.stream()
                .map(WebElement::getText)
                .map(price -> price.replace("$", "").trim())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }
}
