package com.sweven.blockcovid


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class UserFullTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(StartActivity::class.java)

    @Test
    fun userFullTest() {
        val textInputEditText = onView(
                allOf(
                        withId(R.id.username),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.username_layout),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText.perform(replaceText("admin"), closeSoftKeyboard())

            Thread.sleep(500)

        val textInputEditText2 = onView(
                allOf(
                        withId(R.id.password),
                        isDisplayed()
                )
        )
        textInputEditText2.perform(replaceText("password"), closeSoftKeyboard())

            Thread.sleep(500)

        val extendedFloatingActionButton = onView(
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
        extendedFloatingActionButton.perform(click())

        Thread.sleep(2000)

        val bottomNavigationItemView = onView(
                allOf(
                        withId(R.id.navigation_user_rooms), withContentDescription("Stanze"),
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

        val extendedFloatingActionButton2 = onView(
                allOf(
                        withId(R.id.refresh_button),
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
        extendedFloatingActionButton2.perform(click())

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

        val switchMaterial = onView(
                allOf(
                        withId(R.id.dark_theme_switch),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        switchMaterial.perform(click())

        val switchMaterial2 = onView(
                allOf(
                        withId(R.id.dark_theme_switch),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        switchMaterial2.perform(click())

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

        val materialButton = onView(
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
        materialButton.perform(click())

        val materialButton2 = onView(
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
        materialButton2.perform(click())

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

        val materialButton3 = onView(
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
        materialButton3.perform(click())

        val materialButton4 = onView(
                allOf(
                        withId(R.id.room2_button), withText("Vai a Stanza 2"),
                        childAtPosition(
                                allOf(
                                        withId(R.id.room_view),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0
                                        )
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        materialButton4.perform(click())

            onView(withId(R.id.imageButton11)).check(ViewAssertions.matches(allOf(isEnabled(), isClickable()))).perform(
                    object: ViewAction {

                            override fun getConstraints(): Matcher<View> {
                                    return ViewMatchers.isEnabled()
                            }

                            override fun getDescription(): String {
                                    return "click plus button"
                            }

                            override fun perform(uiController: UiController, view:View) {
                                    view.performClick()
                            }
                    }
            )

        val bottomNavigationItemView4 = onView(
                allOf(
                        withId(R.id.navigation_user_rooms), withContentDescription("Stanze"),
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
        bottomNavigationItemView4.perform(click())

            Thread.sleep(1000)

        val materialCardView = onView(
                allOf(
                        withId(R.id.room_card),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.room_recycler_user),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        materialCardView.perform(click())

            onView(withId(R.id.imageButton01)).check(ViewAssertions.matches(allOf(isEnabled(), isClickable()))).perform(
                    object: ViewAction {

                            override fun getConstraints(): Matcher<View> {
                                    return ViewMatchers.isEnabled()
                            }

                            override fun getDescription(): String {
                                    return "click plus button"
                            }

                            override fun perform(uiController: UiController, view:View) {
                                    view.performClick()
                            }
                    }
            )

        val textInputEditText3 = onView(
                allOf(
                        withId(R.id.edit_arrival_time),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.arrival_time),
                                        0
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        textInputEditText3.perform(click())

        val materialButton5 = onView(
                allOf(
                        withId(R.id.material_timepicker_ok_button), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0
                                ),
                                6
                        ),
                        isDisplayed()
                )
        )
        materialButton5.perform(click())

        val textInputEditText4 = onView(
                allOf(
                        withId(R.id.edit_exit_time),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exit_time),
                                        0
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        textInputEditText4.perform(click())

        val materialButton6 = onView(
                allOf(
                        withId(R.id.material_timepicker_ok_button), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0
                                ),
                                6
                        ),
                        isDisplayed()
                )
        )
        materialButton6.perform(click())

        val textInputEditText5 = onView(
                allOf(
                        withId(R.id.edit_reservation_date),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.reservation_date),
                                        0
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        textInputEditText5.perform(click())

        val materialButton7 = onView(
                allOf(
                        withId(R.id.month_navigation_fragment_toggle),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.month_navigation_bar),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        materialButton7.perform(click())

        val recyclerView = onView(
                allOf(
                        withId(R.id.mtrl_calendar_year_selector_frame),
                        childAtPosition(
                                withId(R.id.mtrl_calendar_selection_frame),
                                0
                        )
                )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(140, click()))

        val materialTextView = onData(anything())
            .inAdapterView(
                    allOf(
                            withId(R.id.month_grid),
                            childAtPosition(
                                    withClassName(`is`("android.widget.LinearLayout")),
                                    1
                            ),
                            hasChildCount(31)
                    )
            )
            .atPosition(20)
        materialTextView.perform(click())

        val materialButton8 = onView(
                allOf(
                        withId(R.id.confirm_button), withText("OK"),
                        childAtPosition(
                                allOf(
                                        withId(R.id.date_picker_actions),
                                        childAtPosition(
                                                withId(R.id.mtrl_calendar_main_pane),
                                                1
                                        )
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        materialButton8.perform(click())

        val extendedFloatingActionButton3 = onView(
                allOf(
                        withId(R.id.reserve_button), withText("Prenota"),
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
        extendedFloatingActionButton3.perform(click())

        Thread.sleep(2000)

        val actionMenuItemView = onView(
                allOf(
                        withId(R.id.navigation_login), withContentDescription("Profilo"),
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

        val materialButton9 = onView(
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
        materialButton9.perform(click())

        val textInputEditText6 = onView(
                allOf(
                        withId(R.id.edit_old_password),
                        isDisplayed()
                )
        )
        textInputEditText6.perform(replaceText("password"), closeSoftKeyboard())

        val textInputEditText7 = onView(
                allOf(
                        withId(R.id.edit_new_password),
                        isDisplayed()
                )
        )
        textInputEditText7.perform(replaceText("password"), closeSoftKeyboard())

        val textInputEditText8 = onView(
                allOf(
                        withId(R.id.edit_repeat_password),
                        isDisplayed()
                )
        )
        textInputEditText8.perform(replaceText("password"), closeSoftKeyboard())

        val extendedFloatingActionButton4 = onView(
                allOf(
                        withId(R.id.change_password_button), withText("Cambia Password"),
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
        extendedFloatingActionButton4.perform(click())

        Thread.sleep(2000)

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
