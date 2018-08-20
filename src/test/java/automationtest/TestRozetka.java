package automationtest;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.DataUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by Anastasiia Prodan on 16.08.2018.
 */
public class TestRozetka {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 30);
        driver.manage().window().maximize();
        driver.navigate().to("http://rozetka.com.ua");
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    private void findButtonClickAndWaitNextElement(String xPath, String xPathOfExpectedElement) {
        waitButtonAndClick(xPath);
        driver.manage().timeouts().implicitlyWait(200, TimeUnit.MILLISECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPathOfExpectedElement)));
    }

    private void findButtonClickAndWaitNextElementInvisibility(String xPath, String xPathOfExpectedElement) {
        waitButtonAndClick(xPath);
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xPathOfExpectedElement)));
    }

    private void waitButtonAndClick(String xPath) {
        driver.manage().timeouts().implicitlyWait(200, TimeUnit.MILLISECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPath)));
        driver.findElement(By.xpath(xPath)).click();
    }

    private void findFieldAndInputKeys(String xPath, String keys) {
        WebElement field = driver.findElement(By.xpath(xPath));
        field.clear();
        field.sendKeys(keys);
    }

    private boolean isBannerDisplayed() {
        return driver.findElement(By.xpath(DataUtil.BANNER_POPUP)).isDisplayed();
    }

    private void deleteBanner() {
        waitButtonAndClick(DataUtil.BANNER_CLOSE_BUTTON);
    }

    @Test
    public void logInPositiveTest() {
        waitButtonAndClick(DataUtil.SIGN_IN_BUTTON);

        if(isBannerDisplayed()) {
            deleteBanner();
        }

        findFieldAndInputKeys(DataUtil.MAIL_FIELD, DataUtil.MAIL);
        findFieldAndInputKeys(DataUtil.INPUT_FIELD, DataUtil.PASSWORD);
        findButtonClickAndWaitNextElement(DataUtil.ENTRANCE_BUTTON, DataUtil.USER_LINK);

        Assert.assertTrue("Entrance is not successful!", driver.findElement(By.xpath(DataUtil.USER_LINK)).isEnabled());
    }

    private void checkCartContent() {
        findButtonClickAndWaitNextElement(DataUtil.CART_LINK, DataUtil.IMAGE_OF_CART_CLOSE_BUTTON);
        String cartText = driver.findElement(By.xpath(DataUtil.CART_CONTENT)).getText();
        if(!cartText.contains("Корзина пуста")) {
            clearCart();
        } else {
            waitButtonAndClick(DataUtil.IMAGE_OF_CART_CLOSE_BUTTON);
        }
    }

    private void clearCart() {
        waitButtonAndClick(DataUtil.BEFORE_DELETING_IMAGE_OF_CART_DELETE_BUTTON);
        findButtonClickAndWaitNextElementInvisibility(DataUtil.DELETE_BUTTON, DataUtil.FISCAL_REGISTRARS_TITLE);
        findButtonClickAndWaitNextElement(DataUtil.IMAGE_OF_CART_CLOSE_BUTTON, DataUtil.CATEGORY_GOODS_FOR_BUSINESS);
    }

    @Test
    public void openCategoryWithOptionalTaskBeforeAndAfterTest() {
        logInPositiveTest();
        checkCartContent();
        addProductTest();
    }

    @Test
    public void openCategoryTest() {
        waitButtonAndClick(DataUtil.CATEGORY_GOODS_FOR_BUSINESS);
        Assert.assertEquals("Category goods for business is not opened!", DataUtil.URL_CATEGORY_GOODS_FOR_BUSINESS,
                driver.getCurrentUrl());
    }

    @Test
    public void addProductTest() {
        openCategoryTest();
        waitButtonAndClick(DataUtil.FISCAL_REGISTRARS_LINK);
        waitButtonAndClick(DataUtil.FISCAL_REGISTRAR_IKC_MARKET_LINK);

        String detailTitle = driver.findElement(By.xpath(DataUtil.FISCAL_REGISTRARS_TITLE)).getText();

        findButtonClickAndWaitNextElement(DataUtil.BUY_BUTTON, DataUtil.IMAGE_OF_CART_CLOSE_BUTTON);
        waitButtonAndClick(DataUtil.IMAGE_OF_CART_CLOSE_BUTTON);

        driver.navigate().to("http://rozetka.com.ua");

        findButtonClickAndWaitNextElement(DataUtil.CART_LINK, DataUtil.CART_TITLE);
        String cartTitle = driver.findElement(By.xpath(DataUtil.CART_TITLE)).getText();

        Assert.assertTrue("The product has not added to card!", cartTitle.equals(detailTitle));
    }
}
