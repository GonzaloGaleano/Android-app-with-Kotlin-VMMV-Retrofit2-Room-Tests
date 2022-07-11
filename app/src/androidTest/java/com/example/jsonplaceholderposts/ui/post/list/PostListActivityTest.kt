package com.example.jsonplaceholderposts.ui.post.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jsonplaceholderposts.R
import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Favorite
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User
import com.example.jsonplaceholderposts.repository.PostRepository
import com.example.jsonplaceholderposts.repository.ServiceLocator
import com.example.jsonplaceholderposts.utils.FakePosts
import com.example.jsonplaceholderposts.utils.inThePosition
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostListActivityTest {
    private val wannaWatch: Boolean = true
    private val generalWait = 1500L

    @Test
    fun testingRecyclerViewInActivity() {
        FakePosts.totalPosts = 22
        FakePosts.initData()
        val lastRow = 21
        val repository = object : PostRepository {
            override val loadingPostList: LiveData<Boolean>
                get() = MutableLiveData(false)
            override val loadingComments: LiveData<Boolean>
                get() = MutableLiveData(false)
            override val loadingUsers: LiveData<Boolean>
                get() = MutableLiveData(false)

            override fun refreshData() {
                // refresh ...
            }

            override fun getPostList(): LiveData<List<Post>> {
                return MutableLiveData(FakePosts.posts)
            }

            override fun getFavorites(): LiveData<List<Favorite>> {
                return MutableLiveData(listOf(Favorite(1), Favorite(3)))
            }

            override fun getUsers(): LiveData<List<User>> {
                return MutableLiveData(FakePosts.users)
            }

            override fun getComments(): LiveData<List<Comment>> {
                return MutableLiveData(FakePosts.comments)
            }

        }
        ServiceLocator.postsRepository = repository
        launchActivity<PostListActivity>()

        // checking first row content
        onView(withId(R.id.postsRecyclerView))
            .check(
                matches(
                    inThePosition(0, hasDescendant(withText(FakePosts.posts[0].title)))
                )
            )

        // scroll down
        onView(withId(R.id.postsRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(lastRow))
        intermission()

        // scroll up
        onView(withId(R.id.postsRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        intermission()

        // perform click filter favorites
        onView(withId(R.id.btnFavoritesPosts)).perform(click())
        intermission()

        // perform click on recyclerview item / show a post
        onView(withId(R.id.postsRecyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )
        intermission()

        onView(withId(R.id.postTitle)).check(matches(withText(FakePosts.posts[0].title)))

        onView(withId(R.id.btnRemoveFromFav)).perform(click())
        onView(withId(R.id.btnAddToFav)).check(matches(isDisplayed()))
        intermission()

        onView(withId(R.id.btnAddToFav)).perform(click())
        onView(withId(R.id.btnRemoveFromFav)).check(matches(isDisplayed()))
        intermission()

        // device back action / go back to postListScreen
        Espresso.pressBack()
        intermission()

        // perform click show all posts
        onView(withId(R.id.btnAllPosts)).perform(click())
        intermission()

        ServiceLocator.resetRepository()
    }

    private fun intermission() {
        if (wannaWatch) {
            Thread.sleep(generalWait)
        }
    }
}