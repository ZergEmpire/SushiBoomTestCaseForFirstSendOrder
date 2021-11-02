package FirstTestCase;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit.TextReport;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class TestBase {
    WebDriver driver;
    private static final String PHONE_NUMBER_LOGIN = "+79969797537";
    private static final String TEST_NAME = "Test";
    private static final String TEST_EMAIL_ADRESS = "test@test.ru";
    private static final String TEST_PHONE_NUMBER_LOGIN = "+77777777777";
    private static final String ADRESS_STREET = "улица Говорова, Одинцово";
    private static final String ADRESS_HOME = "85";
    private static final String ORDER_COMMENT = "Тест. Не готовить";
    private static final String HOW_MONEY_TO_COURIER = "5000";
    private static final String HOW_MANY_USERS = "3";
    private static final String FIRST_URL = "https://kishmishekb.ru/";
    private static final String SECOND_URL = "https://kishmish72.ru/";

    @Before
    public void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("videoName", "KishMishTestCaseForFirstSendOrder.mp4");
        capabilities.setCapability("name", "KishMishTestCaseForFirstSendOrder");
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("browserVersion", "91.0");

        capabilities.setCapability("moon:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true

        ));
        RemoteWebDriver driver = null;
        try {
            driver = new RemoteWebDriver(
                    new URL("http://192.168.1.17:30901/wd/hub"),
                    capabilities
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        WebDriverRunner.setWebDriver(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(70, TimeUnit.SECONDS);

    }

    @After
    public void end() {
        closeWebDriver();
    }
    @Rule
    public TextReport report = new TextReport();

    @Step("Открываю сайт")
    public void openURL()
    {
        open("http://sushi-boom.com/");
    }

    @Step("Перехожу в рандомный пункт меню для оформления тестового заказа")
    public void mathRandomHead() {
        List<SelenideElement> mathRandomHead = elements(By.xpath("//div[@class = \"cat-list\"]/ul/li/a[contains(text(), \"\") and not (contains(@href, \"/actions\"))]"));
        int i = (int) (Math.random() * mathRandomHead.size());
        mathRandomHead.get(i).click();
    }

    @Step("Добавляю в корзину карточку товара")
    public void pickRandCards() {
        List<SelenideElement> clickRandomCards = elements(By.xpath("//div[contains(@class, \"productBox\") and not (contains(@class, \"action-wrapper\")) and not (contains(@class, \"in-stop-list\"))]//a[contains(@class, \"add-cart\")]"));
        int i = (int) (Math.random() * clickRandomCards.size());
        SelenideElement randCard = $(clickRandomCards.get(i));
        randCard.closest(".item").scrollIntoView(true);
        randCard.click();


    }

    @Step("Перехожу в корзину")
    public void goBasket() {

        $x("//a[contains(@class, \"price-float\") and (contains(@href, \"/order\"))]").scrollTo().click();
    }

    @Step("Выбираю тип доставки самовывоз")
    public void selectDeliveryTypePickUp() {
        SelenideElement typePickUp = $(By.xpath("//div[@class = \"radio-toggler\"]/label[@class = \"last\"]"));
        typePickUp.closest(".address-selector-container").scrollIntoView(false);
        typePickUp.click();
    }


    @Step("Выбираю пункт самовывоза")
    public void selectTerminalForPickUp() {
        List<SelenideElement> options = elements(By.xpath("//select[@id = \"order_terminal-no-shipment\"]/option"));
        int i = (int) (Math.random() * options.size());
        options.get(i).click();
    }

    @Step("Заполняю поля")
    public void fillInFields() {
        $x("//input[@id = \"order_name\"]").scrollTo().setValue(TEST_NAME);
        $x("//input[@id = \"order_phone\"]").scrollTo().setValue(TEST_PHONE_NUMBER_LOGIN);
        $x("//input[@id = \"order_email\"]").scrollTo().setValue(TEST_EMAIL_ADRESS);
        $x("//input[@id = \"order_comment\"]").scrollTo().setValue(ORDER_COMMENT);
    }

    @Step("Выбираю способ оплаты (Наличными); указываю сдачу с 5000р")
    public void selectPayType() {
        SelenideElement CHANGE_INPUT = $("#change");
        $(CHANGE_INPUT).closest(".address-selector-container").scrollIntoView(false).click();
        $(CHANGE_INPUT).setValue(HOW_MONEY_TO_COURIER);
    }


    @Step("Кликаю на отправку заказа")
    public void sendOrder() {
 /* $x("//div[@id = \"pay-info\"]").scrollTo();
        $x("//div[@class = \"item-cart-buttons\" ]/button[contains(@class, \"btn\") and not (@id)]").shouldBe(visible).click();*/
        $x("//button[@id = \"sendOrder\"]").scrollTo().click();
    }

    @Step("Жду перехода в статус принят")
    public void waitForComplete() {
        $x("//span[contains(text(), \"Принят\") or (contains(text(), \"Поступил\"))]").shouldBe(visible);
    }


}


