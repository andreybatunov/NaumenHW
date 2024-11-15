import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import java.time.Duration;
import java.util.List;

public class NaumenTest
{
    static WebDriver driver;

    @BeforeAll
    public static void setUp()
    {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1200, 1200));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @Test
    public void addObject() throws InterruptedException
    {
        //вход в личный кабинет
        login();

        //создание карточки
        createCard();

        //пауза 2 секунды
        Thread.sleep(2000);

        //открыть "Избранное"
        click("//div[@id='gwt-debug-c5af86c7-6e4d-a611-55f9-d3fc8dcc236c']/div");

        //пауза 2 секунды
        Thread.sleep(2000);

        //проверка
        WebElement element = driver.findElement(By.xpath("//a[@id='gwt-debug-title']"));
        String test = element.getText();
        String msg = String.format("Ошибка: название объекта. Ожидалось: %s, Получено: %s", "test", test);
        Assertions.assertEquals("test", test, msg);

        //пауза 2 секунды
        Thread.sleep(2000);

        //удаление карточки
        removeCard();

        //пауза 2 секунды
        Thread.sleep(2000);

        //закрытие "Избранного"
        click("//div[6]/div/div[2]/div");

        //пауза 2 секунды
        Thread.sleep(2000);

        //выход из личного кабинета
        click("//a[contains(text(),'Выйти')]");
    }

    @Test
    public void deleteObject() throws InterruptedException
    {
        //вход в личный кабинет
        login();

        //создание карточки
        createCard();

        //пауза 2 секунды
        Thread.sleep(2000);

        //открыть "Избранное"
        click("//div[@id='gwt-debug-c5af86c7-6e4d-a611-55f9-d3fc8dcc236c']/div");

        //пауза 2 секунды
        Thread.sleep(2000);

        //удаление карточки
        removeCard();

        //пауза 2 секунды
        Thread.sleep(2000);

        //проверка
        List<WebElement> elements = driver.findElements(By.xpath("//a[@id='gwt-debug-title']/div"));
        Assertions.assertTrue(elements.isEmpty(), "Ошибка. Объект не удалён.");

        //пауза 2 секунды
        Thread.sleep(2000);

        //закрытие "Избранного"
        click("//div[6]/div/div[2]/div");

        //пауза 2 секунды
        Thread.sleep(2000);

        //выход из личного кабинета
        click("//a[contains(text(),'Выйти')]");
    }

    private void login()
    {
        driver.get("http://5.181.254.246:8080/sd/");

        //залогиниться
        driver.findElement(By.id("username")).sendKeys("user5");
        driver.findElement(By.id("password")).sendKeys("1234567890@Qq");
        click("//input[@id='submit-button']");
    }

    public void createCard() throws InterruptedException
    {
        //пауза 2 секунды
        Thread.sleep(2000);

        //перейти на вкладку "Autotesting"
        click("//a[contains(text(),'Autotesting')]");

        //пауза 2 секунды
        Thread.sleep(2000);

        //нажать на кнопку "Избранное"
        click("//div[@id='gwt-debug-favorite']");

        //пауза 2 секунды
        Thread.sleep(2000);

        //ввести название карточки
        send("//input[@id='gwt-debug-itemTitle-value']", "test");

        //пауза 2 секунды
        Thread.sleep(2000);

        //нажать на кнопку "Сохранить"
        click("//div[@id='gwt-debug-apply']");
    }

    public void removeCard() throws InterruptedException
    {
        //пауза 2 секунды
        Thread.sleep(2000);
        //нажать на кнопку "Редактировать избранное"
        click("//div[2]/div/div/div/div[2]/span");
        //пауза 2 секунды
        Thread.sleep(2000);
        //удалить карточку
        click("//table[@id='gwt-debug-favoritesEditTable']/tbody/tr/td[6]/div/span");
        //пауза 2 секунды
        Thread.sleep(2000);
        //подтвердить удаление
        click("//div[@id='gwt-debug-yes']");
        //пауза 2 секунды
        Thread.sleep(2000);
        //сохранение изменений
        click("//div[@id='gwt-debug-apply']");
    }


    public WebElement waitElement(String xpath)
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
    }

    public void click(String xpath)
    {
        waitElement(xpath).click();
    }

    public void send(String xpath, String arg)
    {
        driver.findElement(By.xpath(xpath)).clear();
        waitElement(xpath).sendKeys(arg);
    }

    @AfterAll
    public static void close()
    {
        driver.close();
    }
}
