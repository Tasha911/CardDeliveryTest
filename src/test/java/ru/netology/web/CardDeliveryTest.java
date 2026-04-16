package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardDeliveryTest {

    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }                                                                                     //настройка метода даты

    @Test
    void meetingSuccessfullyBooked() {
        Selenide.open("http://localhost:9999"); //запущен app-card-delivery.jar

        SelenideElement form = $$("form").find(Condition.visible); //найти form и проверить на видимость

        form.$("[data-test-id='city'] input").press(Keys.SHIFT, Keys.HOME).press(Keys.BACK_SPACE).setValue("Барнаул");
        //в form нахдом поле city, очищаем его, заполняем

        String planningDate = generateDate(4, "dd.MM.yyyy"); //метод генерации даты
        form.$("[data-test-id='date'] input").press(Keys.SHIFT, Keys.HOME).press(Keys.BACK_SPACE).setValue(planningDate);
        //в form нахдом поле date, очищаем его, заполняем сгенерированной датой

        form.$("[data-test-id='name'] input").setValue("Иванов Иван"); //в form заполнено поде Фамилия и имя

        form.$("[data-test-id='phone'] input").setValue("+79200000000"); //в form заполнено поде Мобильный телефон

        form.$("[data-test-id='agreement']").click(); //в form кликаем на checkbox agreement (setSelected(true);)

        form.$(".button").shouldHave(text("Забронировать")).click(); //в form кликаем на кнопку с надписью "Забронировать"

        $("[data-test-id='notification']").shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно! Встреча успешно забронирована на " + planningDate));
        //находим уведомление, оно видимо, время загрузки до 15 сек., имеет надпись "Успешно! Встреча... + Дата"
    }
}