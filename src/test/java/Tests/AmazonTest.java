package Tests;

import Utils.ExcelRead;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;


public class AmazonTest implements SearchPage, SearchLandingPage, ProductInformationPage, ItemsAddedPage {

    public String pathChromeDriver ="C:\\Users\\Knightmarez\\IdeaProjects\\amazon.test.script\\chromedriver\\chromedriver.exe";

    //primitive types needed for tests
    public int quantity;
    public boolean isDiplayed = false;

    //instantiation of webdriver
    public static WebDriver driver;


    //search page elements
    @FindBy(how = How.XPATH, using= searchBox) WebElement searchBoxElement;

    //After search landing page items
    //@FindBy(how=How.XPATH, using = pathLandingPageText)WebElement landingPageText;
    @FindBy(how=How.XPATH, using = pathLandingPageHyperLink)WebElement landingPageHyperLink;

    //WebElements for product information page
    @FindBy(how = How.XPATH, using=pathAddToCartBtn) WebElement addToCartBtn;
    @FindBy(how=How.XPATH, using=pathDeclineProtectionPlanBtn) WebElement declineProtectionPlanBtn;
    @FindBy(how=How.XPATH, using = pathItemQuantityPullDown)WebElement itemQuantityPullDown;

    //WebElements once items have been added to cart
    @FindBy(how=How.XPATH, using = pathValidateItemAddedSuccessfully)WebElement validateItemAddedSuccessfully;
    @FindBy(how=How.XPATH, using = pathCartSubtotalValue)WebElement cartSubtotalValue;

    //WebElements for cleanup on cart page

    @BeforeTest
    public void BeforeTest(){
        //Instantiate the webdriver, set variables, and retrieve URL
        System.setProperty("webdriver.chrome.driver", pathChromeDriver);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(11, TimeUnit.SECONDS);
        driver.get(SearchPage.amazonURL);
        PageFactory.initElements(driver, this);

        ExcelRead.setPathWorkBook("C:\\Users\\Knightmarez\\IdeaProjects\\amazon.test.script\\book1.xlsx");
        ExcelRead.setWorkSheetName("Sheet1");
    }

    @AfterTest
    public void AfterTest(){
        //cleans cart items and chromedriver before searching next item
        driver.quit();
    }

   @Test(dataProvider = "data")
    public void amazonTest(String EXECUTE_TC, String TC_ID, String ASIN_NUM, String PRICE){
           //object that'll be used to test the DOM for WebElements
        if(EXECUTE_TC.equalsIgnoreCase("yes") || EXECUTE_TC.equalsIgnoreCase("y")) {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .pollingEvery(250, TimeUnit.MILLISECONDS)
                    .withTimeout(10, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class);

            waitForPageLoaded();

            //search for product
            searchBoxElement.click();
            searchBoxElement.sendKeys(ASIN_NUM);
            searchBoxElement.sendKeys(Keys.ENTER);

            waitForPageLoaded();
            searchBoxElement.clear();

            //wait until landing page has loaded
            isDiplayed = wait.until(ExpectedConditions.presenceOfElementLocated(
                                        By.xpath(pathLandingPageTextPt1 + ASIN_NUM + pathLandingPageTextPt2)))
                                        .isDisplayed();

            //if landing page loaded.  Click link to proceed to product information. Else log error
            if (isDiplayed) {
                landingPageHyperLink.click();
                isDiplayed = false;
            } else {
                //todo log error
            }

            waitForPageLoaded();
            //Wait until product information page has loaded
            isDiplayed = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pathItemQuantityPullDown))).isDisplayed();

            //Test that page successfully loaded, else log error
            if (isDiplayed) {


                isDiplayed = false;

                //set quantity of item
                itemQuantityPullDown.click();
                itemQuantityPullDown.sendKeys("2");
                itemQuantityPullDown.sendKeys(Keys.ENTER);

                addToCartBtn.click();

                waitForPageLoaded();
                //test for protection plan frame
                isDiplayed = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pathDeclineProtectionPlanBtn))).isEnabled();

                //if page displayed click decline.
                if (isDiplayed) {
                    isDiplayed = false;
                    declineProtectionPlanBtn.click();
                }

                waitForPageLoaded();
                //Wait until page for successful addition to cart has been displayed
                isDiplayed = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pathValidateItemAddedSuccessfully))).isDisplayed();

                //If displayed check price, else log errors
                if (isDiplayed) {
                    isDiplayed = false;
                    if (cartSubtotalValue.getText().equals(PRICE)) {
                        //todo write success;
                    } else {
                        //todo write failure and display expected / actual values
                    }
                } else {
                    //todo log error
                }
            } else {
                //todo log error
            }
        }
    }

    @DataProvider (name="data")
    public Object[][] provider(){
        Object[][] testObjArray = ExcelRead.getTableArray();
        return testObjArray;
    }

    public void waitForPageLoaded() {

        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };

        WebDriverWait wait = new WebDriverWait(driver, 15);
        try {
            wait.until(expectation);
        } catch (Exception ex) {
           ex.printStackTrace();

        }
    }
}
