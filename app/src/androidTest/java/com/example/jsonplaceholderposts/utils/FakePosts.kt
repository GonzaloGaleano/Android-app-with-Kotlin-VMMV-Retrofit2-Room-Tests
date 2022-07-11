package com.example.jsonplaceholderposts.utils

import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User

object FakePosts {

    val users: MutableList<User> = mutableListOf()
    val posts: MutableList<Post> = mutableListOf()
    val comments: MutableList<Comment> = mutableListOf()

    private const val totalUsers = 3
    var totalPosts = 3
    private const val totalComments = 3

    private var lastUserId = 0
    private var lastPostId = 0
    private var lastCommentId = 0

    fun initData() {
        lastUserId = 0
        lastPostId = 0
        lastCommentId = 0
        users.clear()
        posts.clear()
        comments.clear()
        lastUserId ++
        for (userId in lastUserId..totalUsers) {
            lastUserId ++
            users.add(
                User(
                    userId,
                    "User$userId",
                    "user$userId",
                    "user$userId@example.com",
                    "+0000000",
                    "http://example.com"
                ).apply {
                    lastPostId++
                    for (postId in lastPostId..totalPosts) {
                        lastPostId++
                        FakePosts.posts.add(
                            Post(
                                postId,
                                "Post$postId title",
                                "post$postId body",
                                userId
                            ).apply {
                                lastCommentId++
                                for (commentId in lastCommentId .. lastCommentId + totalComments) {
                                    lastCommentId++
                                    FakePosts.comments.add(
                                        Comment(
                                            commentId,
                                            "this is a test comment",
                                            "email$commentId",
                                            "body$commentId",
                                            postId
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            )
        }
    }

}