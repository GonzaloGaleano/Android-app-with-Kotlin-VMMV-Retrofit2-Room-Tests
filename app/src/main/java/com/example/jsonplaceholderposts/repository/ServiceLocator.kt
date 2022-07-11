package com.example.jsonplaceholderposts.repository

object ServiceLocator {
    @Volatile
    var postsRepository: PostRepository? = null
    private val lock = Any()

    fun resetRepository() {
        synchronized(lock) {
            postsRepository = null
        }
    }

    fun getThePostsRepository(): PostRepository {
        synchronized(this) {
            return postsRepository ?: createPostsRepository()
        }
    }

    private fun createPostsRepository(): PostRepository {
        postsRepository = ThePostDBRepository
        return ThePostDBRepository
    }
}