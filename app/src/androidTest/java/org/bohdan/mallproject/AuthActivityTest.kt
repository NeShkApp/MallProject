package org.bohdan.mallproject

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.bohdan.mallproject.presentation.ui.auth.AuthActivity
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthActivityTest {

    // Тестуємо початковий стан активності
    @Test
    fun testInitialViewState() {
        // Запускаємо AuthActivity
        val scenario = ActivityScenario.launch(AuthActivity::class.java)

        // Перевіряємо, що елементи для входу (поля для email і пароля, кнопка входу) відображаються
        onView(withId(R.id.emailInput)).check(matches(isDisplayed()))  // Поле для email має бути видимим
        onView(withId(R.id.passwordInput)).check(matches(isDisplayed()))  // Поле для пароля має бути видимим
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))  // Кнопка входу має бути видимою

        // Перевіряємо, що кнопка реєстрації на старті не має бути видимою
        onView(withId(R.id.registerButton)).check(matches(not(isDisplayed())))  // Кнопка реєстрації має бути прихована
    }

    // Тестуємо, чи відбувається зміна видимості елементів при перемиканні на режим реєстрації
    @Test
    fun testSwitchToRegisterMode() {
        // Запускаємо AuthActivity
        val scenario = ActivityScenario.launch(AuthActivity::class.java)

        // Натискаємо на текст для перемикання в режим реєстрації
        onView(withId(R.id.switchText)).perform(click())

        // Перевіряємо, що після перемикання в режим реєстрації кнопка реєстрації та поле username стали видимими
        onView(withId(R.id.usernameInput)).check(matches(isDisplayed()))  // Поле для username має бути видимим
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))  // Кнопка реєстрації має бути видимою
        onView(withId(R.id.loginButton)).check(matches(not(isDisplayed())))  // Кнопка входу має бути прихована
    }

    // Тестуємо, чи кнопка входу не відображається після переходу до режиму реєстрації
    @Test
    fun testLoginButtonVisibilityInRegisterMode() {
        // Запускаємо AuthActivity
        val scenario = ActivityScenario.launch(AuthActivity::class.java)

        // Спочатку перевіряємо, що кнопка входу видима, а кнопка реєстрації прихована
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))  // Кнопка входу має бути видимою
        onView(withId(R.id.registerButton)).check(matches(not(isDisplayed())))  // Кнопка реєстрації має бути прихована

        // Натискаємо на текст для перемикання в режим реєстрації
        onView(withId(R.id.switchText)).perform(click())

        // Перевіряємо, що після перемикання кнопка реєстрації стала видимою, а кнопка входу прихована
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))  // Кнопка реєстрації має бути видимою
    }

}
