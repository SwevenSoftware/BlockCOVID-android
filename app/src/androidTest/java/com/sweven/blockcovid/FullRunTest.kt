package com.sweven.blockcovid


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FullRunTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(StartActivity::class.java)

    @Test
    fun fullRunTest() {

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.username),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(click())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.username),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("user"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.password),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("password"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.login_button), withText("Accedi"),
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
        materialButton.perform(click())

        Thread.sleep(1000)

        val materialButton2 = onView(
            allOf(
                withId(R.id.scanner_button), withText("Scanner"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
        Thread.sleep(1000)
        val materialButton3 = onView(
            allOf(
                withId(R.id.log_button), withText("Log"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
        Thread.sleep(1000)
        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Torna indietro"),
                childAtPosition(
                    allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())
        Thread.sleep(1000)
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_help), withContentDescription("Aiuto"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.navigation_settings), withContentDescription("Impostazioni"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.navigation_home), withContentDescription("Home"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())

        val materialButton4 = onView(
            allOf(
                withId(R.id.desks_button), withText("Postazioni"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
        Thread.sleep(1000)
        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.imageButton00), withContentDescription("0"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.otaliastudios.zoom.ZoomLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())
        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.edit_arrival_time),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(click())

        val materialButton5 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())
        Thread.sleep(1000)
        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.edit_exit_time),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(click())
        Thread.sleep(1000)
        val materialButton6 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        Thread.sleep(1000)

        val calendar = onView(
            allOf(
                withId(R.id.select_date),
                isDisplayed()
            )
        )

        calendar.perform(PickerActions.setDate(2030, 10, 20))


        Thread.sleep(1000)
        val materialButton7 = onView(
            allOf(
                withId(R.id.reserve_button), withText("Prenota"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        materialButton7.perform(click())

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.navigation_login), withContentDescription("Login"),
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

        val materialButton8 = onView(
            allOf(
                withId(R.id.change_password_button), withText("Cambia Password"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton8.perform(click())

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.edit_old_password),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText7.perform(replaceText("password"), closeSoftKeyboard())

        val appCompatEditText8 = onView(
            allOf(
                withId(R.id.edit_new_password),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText8.perform(replaceText("password"), closeSoftKeyboard())

        val appCompatEditText9 = onView(
            allOf(
                withId(R.id.edit_repeat_password),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatEditText9.perform(replaceText("password"), closeSoftKeyboard())
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(1000)
        val materialButton9 = onView(
            allOf(
                withId(R.id.change_password_button), withText("Cambia Password"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton9.perform(click())
        Thread.sleep(1000)
        val materialButton10 = onView(
            allOf(
                withId(R.id.logout_button), withText("Logout"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton10.perform(click())
    }
    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

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
