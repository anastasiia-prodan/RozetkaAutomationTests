package logic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * Created by Anastasiia Prodan on 18.08.2018.
 */
public class Logic {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String textOfTitle;
    private static String textOfCartTitle;
    private static final String BANNER_POPUP = "//a[@class='exponea-banner exponea-popup-banner exponea-animate']";
    private static final String BANNER_CLOSE_BUTTON = "//span[@class='exponea-close']";
    private static final String SIGN_IN_BUTTON = "//span[@name='auth-btn']";
    private static final String MAIL_FIELD = "//input[@name='login']";
    private static final String INPUT_FIELD = "//input[@name='password']";
    private static final String ENTRANCE_BUTTON = "//button[@type='submit']";
    private static final String CART_LINK = "//div[@name='splash-button']";
    private static final String IMAGE_OF_CART_CLOSE_BUTTON = "//div[@id='cart-popup']//img[@class='popup-close-icon " +
            "sprite']";
    private static final String CART_CONTENT = "//div[@name='content']";
    private static final String BEFORE_DELETING_IMAGE_OF_CART_DELETE_BUTTON = "//img[@class='cart-check-icon sprite']";
    private static final String DELETE_BUTTON = "//a[@name='delete']";
    private static final String BUY_BUTTON = "//button[@name='topurchases']";
    private static final String CART_TITLE = "//div[@class='cart-i-title']";

    public static final String MAIL = "newMail1234@bigmir.net";
    public static final String PASSWORD = "1234New";
    public static final String USER_LINK = "//a[@name='profile']";
    public static final String CATEGORY_GOODS_FOR_BUSINESS = "//li[@id='15422']";
    public static final String URL_CATEGORY_GOODS_FOR_BUSINESS = "https://rozetka.com.ua/tovary-dlya-biznesa/c4627851/";
    public static final String FISCAL_REGISTRARS_LINK = "//li[4][@class='pab-items-i']";
    public static final String FISCAL_REGISTRAR_IKC_MARKET_LINK = "//div[@class='g-i-tile-i-title clearfix']";
    public static final String FISCAL_REGISTRARS_TITLE = "//h1[@itemprop='name']";
    public static final String URL_ROZETKA = "http://rozetka.com.ua";

    public static void setTextOfCartTitle(String textOfCartTitle) {
        Logic.textOfCartTitle = textOfCartTitle;
    }

    public static String getTextOfTitle() {
        return textOfTitle;
    }

    public static void setTextOfTitle(String textOfTitle) {
        Logic.textOfTitle = textOfTitle;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void initChromeDriver() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 30);
        driver.manage().window().maximize();
    }

    public static void navigateTo(String url) {
        driver.navigate().to(url);
    }

    public static void quitDriver() {
        driver.quit();
    }

    private static void findButtonClickAndWaitNextElement(String xPath, String xPathOfExpectedElement) {
        waitButtonAndClick(xPath);
        driver.manage().timeouts().implicitlyWait(200, TimeUnit.MILLISECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPathOfExpectedElement)));
    }

    private static void findButtonClickAndWaitNextElementInvisibility(String xPath, String xPathOfExpectedElement) {
        waitButtonAndClick(xPath);
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xPathOfExpectedElement)));
    }

    private static void waitButtonAndClick(String xPath) {
        driver.manage().timeouts().implicitlyWait(200, TimeUnit.MILLISECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPath)));
        driver.findElement(By.xpath(xPath)).click();
    }

    private static void findFieldAndInputKeys(String xPath, String keys) {
        WebElement field = driver.findElement(By.xpath(xPath));
        field.clear();
        field.sendKeys(keys);
    }

    private static boolean isBannerDisplayed(String banner) {
        return driver.findElement(By.xpath(banner)).isDisplayed();
    }

    private static void deleteBanner(String bannerCloseButton) {
        waitButtonAndClick(bannerCloseButton);
    }

    public static void logIn(String mail, String password) {
        waitButtonAndClick(SIGN_IN_BUTTON);

        if(isBannerDisplayed(BANNER_POPUP)) {
            deleteBanner(BANNER_CLOSE_BUTTON);
        }

        findFieldAndInputKeys(MAIL_FIELD, mail);
        findFieldAndInputKeys(INPUT_FIELD, password);
        findButtonClickAndWaitNextElement(ENTRANCE_BUTTON, USER_LINK);
    }

    private static void checkCartContent() {
        findButtonClickAndWaitNextElement(CART_LINK, IMAGE_OF_CART_CLOSE_BUTTON);
        String cartText = driver.findElement(By.xpath(CART_CONTENT)).getText();
        if(!cartText.contains("Корзина пуста")) {
            clearCart();
        } else {
            waitButtonAndClick(IMAGE_OF_CART_CLOSE_BUTTON);
        }
    }

    private static void clearCart() {
        waitButtonAndClick(BEFORE_DELETING_IMAGE_OF_CART_DELETE_BUTTON);
        findButtonClickAndWaitNextElementInvisibility(DELETE_BUTTON, FISCAL_REGISTRARS_TITLE);
        findButtonClickAndWaitNextElement(IMAGE_OF_CART_CLOSE_BUTTON, CATEGORY_GOODS_FOR_BUSINESS);
    }

    public static void openCategory(String categoryUrl) {
        waitButtonAndClick(categoryUrl);
    }

    public static String getTextOfTitle(String title) {
        return driver.findElement(By.xpath(title)).getText();
    }

    public static boolean isProductAdded(String detailTitle, String cartTitle) {
        return cartTitle.equals(detailTitle);
    }

    public static String getTextOfCartTitle() {
        return driver.findElement(By.xpath(CART_TITLE)).getText();
    }

    public static void addProduct(String category, String productLink, String detailProductLink) {
        openCategory(category);
        waitButtonAndClick(productLink);
        waitButtonAndClick(detailProductLink);
        setTextOfTitle(getTextOfTitle(FISCAL_REGISTRARS_TITLE));
        findButtonClickAndWaitNextElement(BUY_BUTTON, IMAGE_OF_CART_CLOSE_BUTTON);
        waitButtonAndClick(IMAGE_OF_CART_CLOSE_BUTTON);
        navigateTo(URL_ROZETKA);
        findButtonClickAndWaitNextElement(CART_LINK, CART_TITLE);
        setTextOfCartTitle(getTextOfCartTitle());
    }

    public static void openCategoryWithOptionalTaskBeforeAndAfter(String mail, String password, String category,
                                                                  String productLink, String detailProductLink) {
        logIn(mail, password);
        checkCartContent();
        addProduct(category, productLink, detailProductLink);
    }
}

