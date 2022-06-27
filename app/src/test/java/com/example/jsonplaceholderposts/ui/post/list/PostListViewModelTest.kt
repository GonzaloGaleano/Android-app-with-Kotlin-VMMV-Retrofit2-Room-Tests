package com.example.jsonplaceholderposts.ui.post.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Favorite
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User
import com.example.jsonplaceholderposts.repository.PostRepository
import org.junit.Rule
import org.junit.Test

internal class PostListViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val user1 = User(1, "User1", "user1", "user1@example.com", "+0000000", "http://example.com")
    val user2 = User(2, "User2", "user2", "user2@example.com", "+0000000", "http://example.com")
    val user3 = User(3, "User3", "user3", "user3@example.com", "+0000000", "http://example.com")

    val comment1 = Comment(1, "lalala", "email1", "body1", 1)
    val comment2 = Comment(2, "lalala", "email2", "body2", 1)
    val comment3 = Comment(3, "lalala", "email3", "body3", 1)

    val post1 = Post(1, "Post 1 title", "post 2 body", 1).apply {
        comments = listOf(comment1, comment2, comment3)
    }
    val post2 = Post(2, "Post 2 title", "post 2 body", 2)
    val post3 = Post(3, "Post 3 title", "post 2 body", 3)

    @Test
    fun getCommentsOfAPostIsCorrect() {
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
                return MutableLiveData(listOf(post1, post2, post3))
            }

            override fun getFavorites(): LiveData<List<Favorite>> {
                return MutableLiveData(listOf())
            }

            override fun getUsers(): LiveData<List<User>> {
                return MutableLiveData(listOf(user1, user2, user3))
            }

            override fun getComments(): LiveData<List<Comment>> {
                return MutableLiveData(listOf(comment1, comment2, comment3))
            }

        }
        val viewModel = PostListViewModel(repository)
        val posts = viewModel.posts.value
        val expectTotalComments = 3
        val resultTotalComments = posts?.get(0)?.comments?.size ?: 0
        assert(expectTotalComments == resultTotalComments) {
            "resultTotalComments had to be $expectTotalComments but was $resultTotalComments"
        }
    }
}