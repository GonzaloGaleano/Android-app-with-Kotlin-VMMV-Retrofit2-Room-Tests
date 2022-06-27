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
        val expectResult = "@fakeusername"
        val result = user.getAtUsername()
        assert( expectResult == result) { "Error getting user.getAtUsername(). Expect $expectResult but returned $result" }
    }
}