package com.example.jsonplaceholderposts.data

import org.junit.Test

internal class UserTest {

    @Test
    fun getAtUsernameReturnAtPlushUsernameConcatenated() {
        val user = User(
                id = 0,
                name = "Fake Name",
                username = "fakeusername",
                email = "fake@example.com",
                phone = "+999999999",
                website = "www.example.com",
            )
        assert("@fakeusername" == user.getAtUsername()) { "Error getting user.getAtUsername()" }
    }
}