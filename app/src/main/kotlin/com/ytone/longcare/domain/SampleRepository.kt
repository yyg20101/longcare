package com.ytone.longcare.domain

interface SampleRepository {
    // Define methods for data operations
    suspend fun getSampleData(): String
}
