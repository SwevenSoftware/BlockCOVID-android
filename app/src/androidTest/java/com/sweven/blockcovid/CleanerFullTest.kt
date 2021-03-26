package com.sweven.blockcovid


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CleanerFullTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(StartActivity::class.java)

    @Test
    fun cleanerTestRun() {
        val textInputEditText = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.username_layout),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText.perform(replaceText("cleaner"), closeSoftKeyboard())

        val textInputEditText2 = onView(
                allOf(withId(R.id.password),
                isDisplayed()))
        textInputEditText2.perform(replaceText("password"), closeSoftKeyboard())

        val extendedFloatingActionButton = onView(
                allOf(withId(R.id.login_button), withText("Accedi"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()))
        extendedFloatingActionButton.perform(click())

        Thread.sleep(2000)

        val materialCardView = onView(
                allOf(withId(R.id.room_card),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.room_recycler_cleaner),
                                        0),
                                0),
                        isDisplayed()))
        materialCardView.perform(click())

        val extendedFloatingActionButton2 = onView(
                allOf(withId(R.id.refresh_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                2),
                        isDisplayed()))
        extendedFloatingActionButton2.perform(click())

        val actionMenuItemView = onView(
                allOf(withId(R.id.navigation_login), withContentDescription("Profilo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()))
        actionMenuItemView.perform(click())

        val materialButton = onView(
                allOf(withId(R.id.change_password_button), withText("Cambia Password"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()))
        materialButton.perform(click())

        val textInputEditText3 = onView(
                allOf(withId(R.id.edit_old_password),
                isDisplayed()))
        textInputEditText3.perform(replaceText("password"), closeSoftKeyboard())

        val textInputEditText4 = onView(
                allOf(withId(R.id.edit_new_password),
                isDisplayed()))
        textInputEditText4.perform(replaceText("password"), closeSoftKeyboard())

        val textInputEditText5 = onView(
                allOf(withId(R.id.edit_repeat_password),
                isDisplayed()))
        textInputEditText5.perform(replaceText("password"), closeSoftKeyboard())

        val extendedFloatingActionButton3 = onView(
                allOf(withId(R.id.change_password_button), withText("Cambia Password"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                3),
                        isDisplayed()))
        extendedFloatingActionButton3.perform(click())

        Thread.sleep(2000)

        val materialButton2 = onView(
                allOf(withId(R.id.logout_button), withText("Logout"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                2),
                        isDisplayed()))
        materialButton2.perform(click())
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
