package lt.prestaShop.task;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.*;
import java.util.stream.Collectors;

public class ClothesSortByPresta extends ArtSortByPresta {

    @FindBy(id = "category-3")
    WebElement navigateToClothesPage;

    @FindBy (css = ".js-product-flags.product-flags > .discount.product-flag")
    WebElement discountPercent;

    @FindBy(css = ".dropdown-menu > a")
    List<WebElement> options;

    @FindBy(css = ".js-product-miniature.product-miniature .price")
    List<WebElement> productLocator;

    public ClothesSortByPresta(WebDriver driver) {
        super(driver);
    }

    public void navigateToClothesPage() {
        navigateToClothesPage.click();
    }

    public boolean isDiscountPresent(WebElement product) {
        try {
            return discountPercent.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void selectSort(int i) {
        if (i >= 0 && 0 < options.size()) {
            WebElement option = options.get(i);
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("arguments[0].click();", option);
        } else {
            throw new IllegalArgumentException("Invalid index: " + i);
        }
    }
    public List<String> sortedPrices() {
        return productLocator.stream().map(WebElement::getText).collect(Collectors.toList());

//    public List<String> sortedPrices() {
//        return productLocator.stream()
//                .map(WebElement::getText)
//                .sorted(Comparator.reverseOrder())
//                .collect(Collectors.toList());
    }

    public List<Map.Entry<WebElement,Double>> sortedProducts() {
        return productLocator.stream()
                .filter(this::isDiscountPresent)
                .map(product -> {
                    String priceText = product.getText().replaceAll("[^\\d.]","");
                    double price = Double.parseDouble(priceText);
                    return new AbstractMap.SimpleEntry<>(product, price);
                })
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }


}
