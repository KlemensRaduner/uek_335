package ch.noseryoung;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.function.Consumer;

import ch.noseryoung.persistence.AppDatabase;
import ch.noseryoung.persistence.Pendenz;
import ch.noseryoung.persistence.PendenzDao;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ListAllPendenzen {

    private  int initalPendenzCount;
    private PendenzDao dao;

   {
        Pendenz testPendenz = new Pendenz();
        testPendenz.setDate(20);
        testPendenz.setTitle("test");
        testPendenz.setDescription("test");
        testPendenz.setId(100000);
        testPendenz.setPriority(1);

        Pendenz testPendenz2 = new Pendenz();
        testPendenz2.setDate(40);
        testPendenz2.setTitle("test2");
        testPendenz2.setDescription("test2");
        testPendenz2.setId(100001);
        testPendenz2.setPriority(1);

        dao = AppDatabase.getAppDb(InstrumentationRegistry.getInstrumentation().getTargetContext()).getPendenzDao();
        dao.insert(testPendenz);
        dao.insert(testPendenz2);

        initalPendenzCount = dao.getAll().size();
    }

    @After
    public void cleanUp() {
        dao.getAll().forEach(new Consumer<Pendenz>() {
            @Override
            public void accept(Pendenz pendenz) {
                dao.delete(pendenz);
            }
        });
    }



    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void listAllPendenzen() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.pendenzen_list),
                        childAtPosition(
                                allOf(withId(R.id.main_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
        recyclerView.check(matches(hasChildCount(initalPendenzCount)));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
