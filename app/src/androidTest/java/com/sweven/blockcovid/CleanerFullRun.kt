package com.sweven.blockcovid

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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
class CleanerFullRun {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(StartActivity::class.java)

    @Test
    fun cleanerFullRun() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.username),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("cleaner"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.password),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("password"), closeSoftKeyboard())

        val extendedFloatingActionButton = onView(
            allOf(
                withId(R.id.login_button),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        extendedFloatingActionButton.perform(click())

        Thread.sleep(500)

        val extendedFloatingActionButton1 = onView(
            allOf(
                withId(R.id.refresh_button),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_cleaner),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        extendedFloatingActionButton1.perform(click())

        Thread.sleep(500)

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.navigation_login),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.change_password_button),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_cleaner),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.edit_old_password),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("password"), closeSoftKeyboard())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.edit_new_password),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("password"), closeSoftKeyboard())

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.edit_repeat_password),
                isDisplayed()
            )
        )
        textInputEditText5.perform(replaceText("password"), closeSoftKeyboard())

        val extendedFloatingActionButton3 = onView(
            allOf(
                withId(R.id.change_password_button),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_cleaner),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        extendedFloatingActionButton3.perform(click())

        Thread.sleep(500)

        val materialButton2 = onView(
            allOf(
                withId(R.id.logout_button), withText("Logout"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_cleaner),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>,
        position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent) &&
                    view == parent.getChildAt(position)
            }
        }
    }
}
