package com.yjdev.goforawalk.repository

import com.yjdev.goforawalk.ApiService
import javax.inject.Inject

class FootstepRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun deleteFootstep(auth: String, id: Int) = api.deleteFootstep(auth, id)
}
