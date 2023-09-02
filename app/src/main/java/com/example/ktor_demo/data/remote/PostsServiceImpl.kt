package com.example.ktor_demo.data.remote

import com.example.ktor_demo.data.remote.dto.PostRequest
import com.example.ktor_demo.data.remote.dto.PostResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.call.receive
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.util.InternalAPI

class PostsServiceImpl(private val client: HttpClient) : PostsService {
    override suspend fun getPosts(): List<PostResponse> {
        return try {
            val response = client.get { url(HttpRoutes.POSTS) }
            if (response.status.isSuccess()) {
                response.body()
            } else {
                println("Error: ${response.status.description}")
                emptyList()
            }
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun createPost(postRequest: PostRequest): PostResponse? {
        return try {
            val response = client.post {
                url(HttpRoutes.POSTS)
                contentType(ContentType.Application.Json)
                body = postRequest
            }
            if (response.status.isSuccess()) response.body()
            else null
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }
}
