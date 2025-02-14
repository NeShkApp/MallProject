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

    // Testujemy początkowy stan aktywności
    @Test
    fun testInitialViewState() {
        // Uruchamiamy AuthActivity
        val scenario = ActivityScenario.launch(AuthActivity::class.java)

        // Sprawdzamy, czy elementy do logowania (pola na email i hasło, przycisk logowania) są widoczne
        onView(withId(R.id.emailInput)).check(matches(isDisplayed()))  // Pole na email powinno być widoczne
        onView(withId(R.id.passwordInput)).check(matches(isDisplayed()))  // Pole na hasło powinno być widoczne
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))  // Przycisk logowania powinien być widoczny

        // Sprawdzamy, czy przycisk rejestracji nie jest widoczny na początku
        onView(withId(R.id.registerButton)).check(matches(not(isDisplayed())))  // Przycisk rejestracji powinien być ukryty
    }

    // Testujemy, czy zmienia się widoczność elementów po przejściu w tryb rejestracji
    @Test
    fun testSwitchToRegisterMode() {
        // Uruchamiamy AuthActivity
        val scenario = ActivityScenario.launch(AuthActivity::class.java)

        // Klikamy na tekst, aby przełączyć się w tryb rejestracji
        onView(withId(R.id.switchText)).perform(click())

        // Sprawdzamy, czy po przełączeniu w tryb rejestracji przycisk rejestracji i pole username stały się widoczne
        onView(withId(R.id.usernameInput)).check(matches(isDisplayed()))  // Pole na username powinno być widoczne
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))  // Przycisk rejestracji powinien być widoczny
        onView(withId(R.id.loginButton)).check(matches(not(isDisplayed())))  // Przycisk logowania powinien być ukryty
    }

    // Testujemy, czy przycisk logowania nie jest widoczny po przejściu do trybu rejestracji
    @Test
    fun testLoginButtonVisibilityInRegisterMode() {
        // Uruchamiamy AuthActivity
        val scenario = ActivityScenario.launch(AuthActivity::class.java)

        // Najpierw sprawdzamy, czy przycisk logowania jest widoczny, a przycisk rejestracji ukryty
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))  // Przycisk logowania powinien być widoczny
        onView(withId(R.id.registerButton)).check(matches(not(isDisplayed())))  // Przycisk rejestracji powinien być ukryty

        // Klikamy na tekst, aby przełączyć się w tryb rejestracji
        onView(withId(R.id.switchText)).perform(click())

        // Sprawdzamy, czy po przełączeniu przycisk rejestracji stał się widoczny, a przycisk logowania ukryty
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))  // Przycisk rejestracji powinien być widoczny
    }

}