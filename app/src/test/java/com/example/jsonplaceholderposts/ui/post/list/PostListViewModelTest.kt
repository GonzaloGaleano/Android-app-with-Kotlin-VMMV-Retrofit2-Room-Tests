package com.example.jsonplaceholderposts.ui.post.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Favorite
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User
import com.example.jsonplaceholderposts.repository.PostRepository
import com.example.jsonplaceholderposts.utils.FakePosts
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test


internal class PostListViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getCommentsOfAPostIsCorrect() {
        FakePosts.initData()
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
                return MutableLiveData(listOf())
            }

            override fun getUsers(): LiveData<List<User>> {
                return MutableLiveData(FakePosts.users)
            }

            override fun getComments(): LiveData<List<Comment>> {
                return MutableLiveData(FakePosts.comments)
            }

        }
        val viewModel = PostListViewModel(repository)
        val posts = viewModel.posts.value
        val comments = posts?.get(0)?.comments ?: listOf()
        val commentIds = comments.map { it.id }
        assertThat(commentIds, hasItem(2))

        // assert without hamcrest lib
        /*val expectTotalComments = 3
        val resultTotalComments = posts?.get(0)?.comments?.size ?: 0
        assert(expectTotalComments == resultTotalComments) {
            "resultTotalComments had to be $expectTotalComments but was $resultTotalComments"
        }*/
    }
}