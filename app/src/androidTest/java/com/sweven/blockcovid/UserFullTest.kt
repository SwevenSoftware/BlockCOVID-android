import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.sweven.blockcovid.R
import com.sweven.blockcovid.StartActivity
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
class UserFullTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(StartActivity::class.java)

    @Test
    fun userFullTest() {
        val textInputEditText = onView(
            allOf(withId(R.id.username),
            childAtPosition(
            childAtPosition(
            withId(R.id.username_layout),
            0),
            0),
            isDisplayed())
        )
        textInputEditText.perform(replaceText("useradmin"), closeSoftKeyboard())
        
        val textInputEditText2 = onView(
            allOf(withId(R.id.password),
            isDisplayed())
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
        
        val bottomNavigationItemView = onView(
allOf(withId(R.id.navigation_settings),
childAtPosition(
childAtPosition(
withId(R.id.nav_view),
0),
2),
isDisplayed()))
        bottomNavigationItemView.perform(click())
        
        val switchMaterial = onView(
allOf(withId(R.id.dark_theme_switch),
childAtPosition(
allOf(withId(R.id.frame_layout_theme),
childAtPosition(
withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
0)),
1),
isDisplayed()))
        switchMaterial.perform(click())
        
        val switchMaterial2 = onView(
allOf(withId(R.id.dark_theme_switch),
childAtPosition(
allOf(withId(R.id.frame_layout_theme),
childAtPosition(
withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
0)),
1),
isDisplayed()))
        switchMaterial2.perform(click())
        
        val materialButton = onView(
allOf(withId(R.id.nfc_reader_button),
childAtPosition(
childAtPosition(
withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
1),
1),
isDisplayed()))
        materialButton.perform(click())
        
        val bottomNavigationItemView2 = onView(
allOf(withId(R.id.navigation_user_rooms),
childAtPosition(
childAtPosition(
withId(R.id.nav_view),
0),
1),
isDisplayed()))
        bottomNavigationItemView2.perform(click())

        Thread.sleep(500)
        
        val materialCardView = onView(
allOf(withId(R.id.room_card),
childAtPosition(
childAtPosition(
withId(R.id.room_recycler_user),
0),
0),
isDisplayed()))
        materialCardView.perform(click())

        onView(withId(1)).check(ViewAssertions.matches(allOf(isEnabled(), isClickable()))).perform(
            object: ViewAction {

                override fun getConstraints(): Matcher<View> {
                    return isEnabled()
                }

                override fun getDescription(): String {
                    return "click plus button"
                }

                override fun perform(uiController: UiController, view:View) {
                    view.performClick()
                }
            }
        )

        Thread.sleep(500)
        
        val extendedFloatingActionButton1 = onView(
allOf(withId(R.id.reserve_button),
childAtPosition(
childAtPosition(
withId(R.id.nav_host_fragment_user),
0),
6),
isDisplayed()))
        extendedFloatingActionButton1.perform(click())

        Thread.sleep(500)
        
        val appCompatImageButton = onView(
allOf(
childAtPosition(
allOf(withId(R.id.action_bar),
childAtPosition(
withId(R.id.action_bar_container),
0)),
2),
isDisplayed()))
        appCompatImageButton.perform(click())
        
        val extendedFloatingActionButton2 = onView(
allOf(withId(R.id.refresh_button),
childAtPosition(
childAtPosition(
withId(R.id.nav_host_fragment_user),
0),
2),
isDisplayed()))
        extendedFloatingActionButton2.perform(click())

        Thread.sleep(500)
        
        val actionMenuItemView = onView(
allOf(withId(R.id.navigation_login),
childAtPosition(
childAtPosition(
withId(R.id.action_bar),
1),
0),
isDisplayed()))
        actionMenuItemView.perform(click())
        
        val materialButton7 = onView(
allOf(withId(R.id.my_reservations_button),
childAtPosition(
childAtPosition(
withId(R.id.nav_host_fragment_user),
0),
1),
isDisplayed()))
        materialButton7.perform(click())

        Thread.sleep(500)

        val materialCardView3 = onView(
allOf(withId(R.id.reservation_card),
childAtPosition(
childAtPosition(
withId(R.id.reservation_recycler_user),
0),
0),
isDisplayed()))
        materialCardView3.perform(click())

        Thread.sleep(500)
        
        val textInputEditText7 = onView(
allOf(withId(R.id.edit_arrival_time),
childAtPosition(
childAtPosition(
withId(R.id.arrival_time),
0),
1),
isDisplayed()))
        textInputEditText7.perform(click())
        
        val materialButton8 = onView(
allOf(withId(R.id.material_timepicker_ok_button), withText("OK"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
6),
isDisplayed()))
        materialButton8.perform(click())
        
        val textInputEditText8 = onView(
allOf(withId(R.id.edit_exit_time),
childAtPosition(
childAtPosition(
withId(R.id.exit_time),
0),
1),
isDisplayed()))
        textInputEditText8.perform(click())

        Thread.sleep(500)

        val materialButton9 = onView(
allOf(withId(R.id.material_timepicker_ok_button), withText("OK"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
6),
isDisplayed()))
        materialButton9.perform(click())
        
        val materialButton10 = onView(
allOf(withId(R.id.edit_reservation_button),
childAtPosition(
childAtPosition(
withId(R.id.nav_host_fragment_user),
0),
6),
isDisplayed()))
        materialButton10.perform(click())

        Thread.sleep(500)
        
        val materialButton11 = onView(
allOf(withId(R.id.my_reservations_button),
childAtPosition(
childAtPosition(
withId(R.id.nav_host_fragment_user),
0),
1),
isDisplayed()))
        materialButton11.perform(click())

        Thread.sleep(500)
        
        val materialCardView4 = onView(
allOf(withId(R.id.reservation_card),
childAtPosition(
childAtPosition(
withId(R.id.reservation_recycler_user),
0),
0),
isDisplayed()))
        materialCardView4.perform(click())

        Thread.sleep(500)
        
        val materialButton12 = onView(
allOf(withId(R.id.remove_reservation_button),
childAtPosition(
childAtPosition(
withId(R.id.nav_host_fragment_user),
0),
7),
isDisplayed()))
        materialButton12.perform(click())

        Thread.sleep(500)
        
        val materialButton13 = onView(
allOf(withId(R.id.change_password_button),
childAtPosition(
childAtPosition(
withId(R.id.nav_host_fragment_user),
0),
2),
isDisplayed()))
        materialButton13.perform(click())
        
        val textInputEditText9 = onView(
allOf(
withId(R.id.edit_old_password),
isDisplayed()))
        textInputEditText9.perform(replaceText("password"), closeSoftKeyboard())
        
        val textInputEditText10 = onView(
            allOf(
                withId(R.id.edit_new_password),
                isDisplayed()))
        textInputEditText10.perform(replaceText("password"), closeSoftKeyboard())
        
        val textInputEditText11 = onView(
            allOf(
                withId(R.id.edit_repeat_password),
                isDisplayed()))
        textInputEditText11.perform(replaceText("password"), closeSoftKeyboard())
        
        val extendedFloatingActionButton3 = onView(
allOf(withId(R.id.change_password_button),
childAtPosition(
childAtPosition(
withId(R.id.nav_host_fragment_user),
0),
3),
isDisplayed()))
        extendedFloatingActionButton3.perform(click())

        Thread.sleep(500)
        
        val materialButton14 = onView(
allOf(withId(R.id.logout_button), withText("Logout"),
childAtPosition(
childAtPosition(
withId(R.id.nav_host_fragment_user),
0),
3),
isDisplayed()))
        materialButton14.perform(click())
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
