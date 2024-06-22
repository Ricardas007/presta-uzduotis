package lt.prestaShop.task;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrestaShopPageTest extends BaseTest{

    @ParameterizedTest(name = "Test{index} => firstName={0}, lastName{1}, email{2}, password{3}")
    @CsvFileSource(files = "src/main/resources/register.csv", numLinesToSkip = 1)
    void registerUser(ArgumentsAccessor arguments)  {
        String firstName = arguments.getString(0);
        String lastName = arguments.getString(1);
        String userEmail = arguments.getString(2);
        String userPassword = arguments.getString(3);

        RegisterPresta register = new RegisterPresta(driver);

    try {
        register.navigateToRegisterPage();
        register.fillTeFormWithData(firstName, lastName, userEmail, userPassword);
        register.clickNecessaryCheckbox();
        String name = register.chekUserName();
        String expectedName = firstName + " " + lastName;
        System.out.println(name);
        Assertions.assertEquals(expectedName, name, "User first and last name should match user account name");
    } catch (Exception e) {
        register.takeScreenshot(firstName, e);
        }
    }

    @ParameterizedTest(name = "Test{index} => email{2}, password{3}")
    @CsvFileSource(files = "src/main/resources/register.csv", numLinesToSkip = 1)
    void loginUser(ArgumentsAccessor arguments) throws InterruptedException {

        String userEmail = arguments.getString(2);
        String userPassword = arguments.getString(3);

        LoginPresta login = new LoginPresta(driver);

        login.navigateToRegisterPage();
        login.fillTeFormWithDataAndSubmit(userEmail,userPassword);
        login.navigatetoUserDetailPage();
        login.navigateToInfoSec();
        login.ownerEmail();
        String ownerEmail = login.ownerEmail();
        login.signOutPage();

        Assertions.assertEquals(ownerEmail, userEmail, "Email should match the account owner email");
    }

    @ParameterizedTest(name = "{index} => description={0}, keyword={1}, expectedResult={2}")
    @CsvFileSource(files = "src/main/resources/search_a_catalog.csv", numLinesToSkip = 1)
    public void testSearchACatalog(String description, String keyword, String expectedResult) {

        SearchPresta search = new SearchPresta(driver);

        search.clear();
        search.pressSearchInput(keyword);

        try {

            System.out.println("Expected Result: '" + expectedResult + "'");
            if ("Prompt to enter search term".equals(expectedResult)) {
                assertTrue(search.isNoProductMessageDisplayed(), "Expected prompt to enter a search term.");
            } else if (expectedResult.startsWith("No products found")) {
                validateNoProductsFound(search);
            } else if ("Navigate to product detail page".equals(expectedResult)) {
                validateProductDetailNavigation(search);
            } else if (expectedResult.startsWith("List of")) {
                validateProductList(search, expectedResult);
            } else {
                fail("Unexpected expected result: " + expectedResult);
            }
        } catch (AssertionError e) {
            takeScreenshot(description);
            throw e;
        }
    }

    private void validateNoProductsFound(SearchPresta search) {
        List<WebElement> products = search.getProductNames();
        if (!products.isEmpty()) {
            takeScreenshot("Unexpected products found");
            fail("Expected no products but found some. Number of products found: " + products.size());
        } else {
            assertTrue(search.isNoProductMessageDisplayed(), "Expected no products message but it was not displayed.");
        }
    }

    private void validateProductList(SearchPresta search, String expectedResult) {
        List<WebElement> products = search.getProductNames();
        if (products.isEmpty()) {
            fail("Expected products but found none. Expected: " + expectedResult);
        } else {
            assertTrue(products.size() > 0, "Products found as expected.");
        }
    }

    private void validateProductDetailNavigation(SearchPresta search) {
        List<WebElement> products = search.getProductNames();
        if (products.isEmpty()) {
            fail("Expected products but found none.");
        } else {
            search.clickOnFirstProduct();
            String productTitle = search.getProductTitle();
            assertNotNull(productTitle, "Expected product detail page but none found.");
        }
    }

    private void takeScreenshot(String description) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File srcFile = ts.getScreenshotAs(OutputType.FILE);
        try {
            Files.createDirectories(Paths.get("screenshots"));
            FileHandler.copy(srcFile, new File("screenshots/" + description + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @ParameterizedTest(name = "Test: {index} => email: {2}, password: {3}, sort: {4}")
    @CsvFileSource(files = "src/main/resources/log.csv", numLinesToSkip = 1)
    void artPageSortBy(ArgumentsAccessor arguments) throws InterruptedException {

        String userEmail = arguments.getString(2);
        String userPassword = arguments.getString(3);
        LoginPresta login = new LoginPresta(driver);
        login.navigateToRegisterPage();
        login.fillTeFormWithDataAndSubmit(userEmail,userPassword);

        ArtSortByPresta sort = new ArtSortByPresta(driver);
        sort.navigateToArtPage();
        Thread.sleep(2000);
        sort.selectSort(0);
        Thread.sleep(2000);

        List<WebElement> products = driver.findElements(By.cssSelector(".js-product-miniature.product-miniature"));
        for (WebElement product : products) {
            boolean isDiscountPresent = sort.isDiscountPresent(product);
            Assertions.assertTrue(isDiscountPresent, "Discount percentage label should be present");
        }

        List<Double> sortedPrices = sort.getProductPrices();
        System.out.println("sortedProducts: " + sortedPrices);


        List<Double> sortedPricesCopy = new ArrayList<>(sortedPrices);
        Collections.sort(sortedPricesCopy, Collections.reverseOrder());
        System.out.println("sortedProductsCopy: " + sortedPricesCopy);
        Assertions.assertEquals(sortedPricesCopy, sortedPrices, "Products should be sorted according price after discount");

    }

    @ParameterizedTest(name = "Test{index} => email{2}, password{3}")
    @CsvFileSource(files = "src/main/resources/log.csv", numLinesToSkip = 1)
    void clothesPageSortBy(ArgumentsAccessor arguments) throws InterruptedException {

        String userEmail = arguments.getString(2);
        String userPassword = arguments.getString(3);

        LoginPresta login = new LoginPresta(driver);
        login.navigateToRegisterPage();
        login.fillTeFormWithDataAndSubmit(userEmail,userPassword);

        ClothesSortByPresta sort = new ClothesSortByPresta(driver);
        sort.navigateToClothesPage();
        sort.selectSort(0);
        Thread.sleep(2000);

        List<Map.Entry<WebElement, Double>> products = sort.sortedProducts();
        boolean isDiscountPresent = products.stream().allMatch(entery -> sort.isDiscountPresent(entery.getKey()));
        Assertions.assertTrue(isDiscountPresent, "All products should have a discount percentage label");

        System.out.println("All products should have a discount percentage label");

//        List<String> sortedProducts = sort.sortedPrices();
//        System.out.println("sortedProducts: " + sortedProducts);

        List<Double> sortedPrices = products.stream().map(Map.Entry::getValue).collect(Collectors.toList());
        String sortedProductsStr = sortedPrices.stream().map(String::valueOf).collect(Collectors.joining(", "));
        System.out.println("sortedProducts: " + sortedProductsStr);


        List<Double> sortedPricesCopy = new ArrayList<>(sortedPrices);
        Collections.sort(sortedPricesCopy, Collections.reverseOrder());
        String sortedPricesCopyStr = sortedPricesCopy.stream().map(String::valueOf).collect(Collectors.joining(", "));
        System.out.println("sortedProductsCopy: " + sortedPricesCopyStr);
        Assertions.assertEquals(sortedPrices, sortedPricesCopy, "Products should be sorted according price after discount");

        login.signOutPage();
        Thread.sleep(2000);

    }

    @ParameterizedTest(name = "Test{index} => email{2}, password{3}")
    @CsvFileSource(files = "src/main/resources/log.csv", numLinesToSkip = 1)
    void artPageFilterAvailabilityTest(ArgumentsAccessor arguments) {

//        String userEmail = arguments.getString(2);
//        String userPassword = arguments.getString(3);

//        LoginPresta login = new LoginPresta(driver);
//        login.navigateToRegisterPage();
//        login.fillTeFormWithDataAndSubmit(userEmail,userPassword);
        ArtSortByPresta sort = new ArtSortByPresta(driver);
        sort.navigateToArtPage();
        ArtFilterByPresta filter = new ArtFilterByPresta(driver);
        filter.markInStockCheckbox();
        filter.addProductsToShoppingBasket();
        String numberOfAmountProductsInBasket =filter.checkAmountOfProductsInShoppingBasket();
        numberOfAmountProductsInBasket = numberOfAmountProductsInBasket.replace("(", "").replace(")", "");
        int amountOfProductsInBasket = Integer.parseInt(numberOfAmountProductsInBasket);
        int amountProductsDisplayedOnPage = filter.productPrices.size();
        Assertions.assertEquals(amountOfProductsInBasket, amountProductsDisplayedOnPage, "Products displayed on page must equals products in basket");

        filter.markNewProductCheckbox();

    }

    @ParameterizedTest(name = "Test{index} => email{2}, password{3}")
    @CsvFileSource(files = "src/main/resources/log.csv", numLinesToSkip = 1)
    void artPageFilterSelectionsTest(ArgumentsAccessor arguments) throws InterruptedException {

//        String userEmail = arguments.getString(2);
//        String userPassword = arguments.getString(3);

//        LoginPresta login = new LoginPresta(driver);
//        login.navigateToRegisterPage();
//        login.fillTeFormWithDataAndSubmit(userEmail,userPassword);
        ArtSortByPresta sort = new ArtSortByPresta(driver);
        sort.navigateToArtPage();
        ArtFilterByPresta filter = new ArtFilterByPresta(driver);
        filter.markNewProductCheckbox();
//        Thread.sleep(2000);
        filter.checkNewProductLabel();
        System.out.println(filter.checkNewProductLabel());
        boolean message = filter.checkNewProductLabel();
        Assertions.assertTrue(message,"Not all element are present on the pagae");

    }
    @ParameterizedTest(name = "Test{index} => email{2}, password{3}")
    @CsvFileSource(files = "src/main/resources/log.csv", numLinesToSkip = 1)
    void artPageFilterCompositionTest(ArgumentsAccessor arguments) throws InterruptedException {

//        String userEmail = arguments.getString(2);
//        String userPassword = arguments.getString(3);

//        LoginPresta login = new LoginPresta(driver);
//        login.navigateToRegisterPage();
//        login.fillTeFormWithDataAndSubmit(userEmail,userPassword);
        ArtSortByPresta sort = new ArtSortByPresta(driver);
        sort.navigateToArtPage();

        ArtFilterByPresta filter = new ArtFilterByPresta(driver);
        filter.marMattPaperCheckbox();
//        filter.checkProductIfMadeFromMattPaper();
//        boolean result = filter.checkProductIfMadeFromMattPaper();
        boolean answer = filter.isPositiveStatementFound();
        Assertions.assertTrue(answer, "Matt Papper was not found in any of the products");
    }

    @ParameterizedTest(name = "Test{index} => email{2}, password{3}")
    @CsvFileSource(files = "src/main/resources/log.csv", numLinesToSkip = 1)
    void artPageFilterBrandTest(ArgumentsAccessor arguments) throws InterruptedException {

//        String userEmail = arguments.getString(2);
//        String userPassword = arguments.getString(3);

//        LoginPresta login = new LoginPresta(driver);
//        login.navigateToRegisterPage();
//        login.fillTeFormWithDataAndSubmit(userEmail,userPassword);
        ArtSortByPresta sort = new ArtSortByPresta(driver);
        sort.navigateToArtPage();

        ArtFilterByPresta filter = new ArtFilterByPresta(driver);
        filter.graphicCornerCheckbox();
        boolean message = filter.isPositiveStatementFoundGraficCorner();
        Assertions.assertTrue(message, "Graphic Corner was not found in any of the products");

//        filter.checkProductIfMadeFromMattPaper();
//        boolean result = filter.checkProductIfMadeFromMattPaper();
//        boolean answer = filter.isPositiveStatementFound();
    }

    @ParameterizedTest(name = "Test: {index} => email: {2}, password: {3}, dimensions: {4}")
    @CsvFileSource(files = "src/main/resources/dimens.csv", numLinesToSkip = 1)
    void artPageFilterDimensionsTest(ArgumentsAccessor arguments) {
        String userEmail = arguments.getString(2);
        String userPassword = arguments.getString(3);
        String dimensions = arguments.getString(4);

        LoginPresta login = new LoginPresta(driver);
        login.navigateToRegisterPage();
        login.fillTeFormWithDataAndSubmit(userEmail,userPassword);
        ArtSortByPresta sort = new ArtSortByPresta(driver);
        sort.navigateToArtPage();
        ArtFilterByPresta filter = new ArtFilterByPresta(driver);
        filter.applyDimensionFilter(dimensions);
        List<String> productDimensions = filter.getFilteredProductDimensions();
        for (String productDimension : productDimensions) {
            Assertions.assertTrue(productDimension.contains(dimensions),"Product dimensions does not match expected dimensiosn: " + dimensions + ", found " + productDimensions);
        }
        filter.singOut();

    }


}
