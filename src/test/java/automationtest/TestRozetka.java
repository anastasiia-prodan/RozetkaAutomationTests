package automationtest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import logic.Logic;

/**
 * Created by Anastasiia Prodan on 16.08.2018.
 */
public class TestRozetka {
    @Before
    public void setUp() {
        Logic.initChromeDriver();
        Logic.navigateTo(Logic.URL_ROZETKA);
    }

    @After
    public void tearDown() {
        Logic.quitDriver();
    }

    @Test
    public void logInPositiveTest() {
        Logic.logIn(Logic.MAIL, Logic.PASSWORD);
        Assert.assertTrue("Entrance is not successful!", Logic.getDriver().findElement(By.xpath(Logic.USER_LINK))
                .isEnabled());
    }

    @Test
    public void openCategoryWithOptionalTaskBeforeAndAfterTest() {
        Logic.openCategoryWithOptionalTaskBeforeAndAfter(Logic.MAIL, Logic.PASSWORD, Logic.CATEGORY_GOODS_FOR_BUSINESS,
                Logic.FISCAL_REGISTRARS_LINK, Logic.FISCAL_REGISTRAR_IKC_MARKET_LINK);
        Assert.assertTrue("Entrance is not successful!", Logic.getDriver().findElement(By.xpath(Logic.USER_LINK))
                .isEnabled());
        Assert.assertTrue("The product has not added to card!", Logic.isProductAdded(Logic.getTextOfTitle(),
                Logic.getTextOfCartTitle()));
    }

    @Test
    public void openCategoryTest() {
        Logic.openCategory(Logic.CATEGORY_GOODS_FOR_BUSINESS);
        Assert.assertEquals("Category goods for business is not opened!", Logic.URL_CATEGORY_GOODS_FOR_BUSINESS,
                Logic.getDriver().getCurrentUrl());
    }

    @Test
    public void addProductTest() {
        Logic.addProduct(Logic.CATEGORY_GOODS_FOR_BUSINESS, Logic.FISCAL_REGISTRARS_LINK, Logic.
                FISCAL_REGISTRAR_IKC_MARKET_LINK);
        Assert.assertTrue("The product has not added to card!", Logic.isProductAdded(Logic.getTextOfTitle(),
                Logic.getTextOfCartTitle()));
    }
}
