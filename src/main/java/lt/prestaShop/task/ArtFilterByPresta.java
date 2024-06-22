package lt.prestaShop.task;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ArtFilterByPresta extends ArtSortByPresta{
    @FindBy (xpath = "//div[@id='js-product-list']/div[@class='products row']/div")
    List<WebElement> productImages;

    @FindBy (xpath = "//div[@id='js-product-list']/div[@class='products row']/div")
    List<WebElement> productFiltered;

    @FindBy (css = "section:nth-of-type(1) > .collapse .custom-checkbox")
    WebElement checkboxInStock;

    @FindBy (css = "section:nth-of-type(2) > .collapse .custom-checkbox")
    WebElement checkboxNewProduct;

    @FindBy (css = "section:nth-of-type(4) > .collapse .custom-checkbox")
    WebElement checkboxMattPaper;

    @FindBy (css = "section:nth-of-type(5) > .collapse .custom-checkbox")
    WebElement checkboxGraphicCorner;

    @FindBy (css = "section:nth-of-type(6) > .collapse .custom-checkbox")
    WebElement checkboxDem40x60;

    @FindBy (css = "li:nth-of-type(2) > .facet-label > .custom-checkbox")
    WebElement checkboxDem60x90;

    @FindBy (css = "li:nth-of-type(3) > .facet-label > .custom-checkbox")
    WebElement checkboxDem80x120;

    @FindBy (css = "div[role='document'] .h1")
    WebElement productName;

    @FindBy (css = "[data-button-action]")
    WebElement addToCartButton;

    @FindBy (css = ".cart-content-btn [data-dismiss]")
    WebElement continueShoppingBtn;

    @FindBy (css = ".cart-products-count")
    WebElement cartProductNum;

    @FindBy (xpath = "//div[@id='js-product-list']/div[@class='products row']/div")
    List<WebElement> products;

    @FindBy (css = ".new.product-flag")
    WebElement productLableNew;

    @FindBy (css = "li:nth-of-type(2) > a[role='tab']")
    WebElement productDetailsButton;

    @FindBy (xpath = "//div[@id='product-details']/section[@class='product-features']//dd[@class='value']")
    WebElement mattPaperDatails;

    @FindBy (css = "img[alt='Graphic Corner']")
    WebElement graphicCornerImageText;

    @FindBy (css = ".product-variants-item > .control-label")
    WebElement productDemensions;

    @FindBy (css = ".filter-block")
    WebElement filterAppears;

    @FindBy (xpath = "//div[@id='_desktop_user_info']//a[@href='http://192.168.0.27/?mylogout=']")
    WebElement singOut;


    public ArtFilterByPresta(WebDriver driver) {
        super(driver);
    }

    public void addProductsToShoppingBasket() {
        Actions actions = new Actions(driver);
        for (int i = 0; i < productFiltered.size(); i++){
            wait.until(ExpectedConditions.visibilityOf(filterAppears));
            WebElement product = products.get(i);

            actions.moveToElement(product).perform();
            product.click();

            wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
            addToCartButton.click();
            continueShoppingBtn.click();
            driver.navigate().back();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='js-product-list']/div[@class='products row']/div")));
        }
    }

    public String checkAmountOfProductsInShoppingBasket() {
        return cartProductNum.getText();
    }

    public void markInStockCheckbox() {
        checkboxInStock.click();
    }

    public void markNewProductCheckbox() {
        checkboxNewProduct.click();
    }

    public boolean checkNewProductLabel() {
        for (WebElement product : productFiltered) {
            try {
                WebElement newLabel = product.findElement(By.cssSelector(".new.product-flag"));
                if (newLabel.isDisplayed()) {
//                    System.out.println("New label is present for this product");
                    return true;
                }
            } catch (NoSuchElementException e) {
//                System.out.println("New lable is not present for this product");
                return false;
            }
        }
        return false;
    }


    public void marMattPaperCheckbox() {
        checkboxMattPaper.click();
    }

    public List<String> checkProductIfMadeFromMattPaper() {
        Actions actions = new Actions(driver);
//        boolean isMattPapperFound = false;
        List<String> productDetailsList = new ArrayList<>();


        for (int i = 0; i < productFiltered.size(); i++) {
            wait.until(ExpectedConditions.visibilityOf(filterAppears));

            System.out.println(productFiltered.size());
            List<WebElement> products = driver.findElements(By.xpath("//div[@id='js-product-list']/div[@class='products row']/div"));

            WebElement product = products.get(i);
            actions.moveToElement(product).perform();
            product.click();

            wait.until(ExpectedConditions.elementToBeClickable(productDetailsButton));
            productDetailsButton.click();
            wait.until(ExpectedConditions.visibilityOf(mattPaperDatails));

//            String mattPaperText = mattPaperDatails.getText();
            String mattPaperText = mattPaperDatails.getText().trim();

            productDetailsList.add(mattPaperText);
//            boolean isMattPaperPresent = mattPaperText.contains("Matt");
            System.out.println("Product " +(i+1)+ ": " + mattPaperText);
//            System.out.println("Is Matt Paper present: " + isMattPaperPresent);
            System.out.println("Is Matt Paper present: " + mattPaperText.contains("Matt"));
//            if (isMattPaperPresent) {
//                isMattPapperFound = true;
//            }

            driver.navigate().back();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='js-product-list']/div[@class='products row']/div")));
        }
//        return isMattPapperFound;
        return productDetailsList;
    }

    public boolean isPositiveStatementFound() {
        List<String> productDetails = checkProductIfMadeFromMattPaper();
        boolean isMattPaperFound = productDetails.stream().allMatch(detail -> detail.contains("Matt"));
        return isMattPaperFound;
    }

    public void graphicCornerCheckbox() {
        checkboxGraphicCorner.click();
    }

    public List<String> checkProductMmnufacturer() {
        Actions actions = new Actions(driver);
//        boolean isMattPapperFound = false;
        List<String> productDetailsList = new ArrayList<>();


        for (int i = 0; i < productFiltered.size(); i++) {
            wait.until(ExpectedConditions.visibilityOf(filterAppears));

            System.out.println(productFiltered.size());
            List<WebElement> products = driver.findElements(By.xpath("//div[@id='js-product-list']/div[@class='products row']/div"));

            WebElement product = products.get(i);
            actions.moveToElement(product).perform();
            product.click();

            wait.until(ExpectedConditions.elementToBeClickable(productDetailsButton));
            productDetailsButton.click();
            wait.until(ExpectedConditions.visibilityOf(graphicCornerImageText));

            String graphicCornerText = graphicCornerImageText.getAttribute("alt").trim();
            productDetailsList.add(graphicCornerText);

            System.out.println("Product " +(i+1)+ ": " + graphicCornerText);
            System.out.println("Is Matt Paper present: " + graphicCornerText.contains("Graphic"));

            driver.navigate().back();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='js-product-list']/div[@class='products row']/div")));
        }
    return productDetailsList;
    }

public boolean isPositiveStatementFoundGraficCorner() {
    List<String> productDetails = checkProductMmnufacturer();
    boolean isGraphicCornerFound = productDetails.stream().allMatch(detail -> detail.contains("Graphic"));
    return isGraphicCornerFound;

    }


    public void applyDimensionFilter(String expectedDimensions) {
        switch (expectedDimensions) {
            case "40x60cm":
                wait.until(ExpectedConditions.elementToBeClickable(checkboxDem40x60)).click();
                break;
            case "60x90cm":
                wait.until(ExpectedConditions.elementToBeClickable(checkboxDem60x90)).click();
                break;
            case "80x120cm":
                wait.until(ExpectedConditions.elementToBeClickable(checkboxDem80x120)).click();
                break;
        }
        wait.until(ExpectedConditions.visibilityOfAllElements(productFiltered));
    }

    public List<String> getFilteredProductDimensions() {
        Actions actions = new Actions(driver);
        List<String> productDetailsList = new ArrayList<>();

        // Find the initial list of products
        List<WebElement> products = driver.findElements(By.xpath("//div[@id='js-product-list']/div[@class='products row']/div"));

        for (int i = 0; i < products.size(); i++) {
            // Re-locate the product elements to avoid stale element reference
            products = driver.findElements(By.xpath("//div[@id='js-product-list']/div[@class='products row']/div"));
            WebElement product = products.get(i);

            actions.moveToElement(product).perform();
            product.click();

            // Wait for the dimensions to be visible
            wait.until(ExpectedConditions.visibilityOf(productDemensions));
            String productDemensionsText = productDemensions.getText().trim();
            productDetailsList.add(productDemensionsText);

            driver.navigate().back();
            // Wait for the product list to be visible again
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='js-product-list']/div[@class='products row']/div")));
        }

        return productDetailsList;
    }


    public void singOut() {
        wait.until(ExpectedConditions.elementToBeClickable(singOut));
        singOut.click();
    }
}


