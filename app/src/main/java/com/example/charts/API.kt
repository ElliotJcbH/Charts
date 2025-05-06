package com.example.charts

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.json.JSONObject
import java.util.concurrent.Executors
import com.example.charts.AlbumInfo

suspend fun fetch_top_10():  List<AlbumInfo> = coroutineScope {
    try {
        val album_info = supabase.from("album_top_10_weekly").select().decodeList<AlbumInfo>()
        print(album_info)
        return@coroutineScope album_info
    } catch(e: Exception){
        print("Error fetching top 10 weekly: ${e.message}")
        throw(e)
    }
}

suspend fun fetch_worst_album(): AlbumInfo = coroutineScope {
    try {
        val result = supabase.postgrest.rpc("get_worst_album")
        val worst_album = result.decodeSingle<AlbumInfo>()
        return@coroutineScope worst_album

    } catch (e: Exception) {
        print("Error fetching worst album ${e.message}")
        throw(e)
    }
}

suspend fun fetch_best_album(): AlbumInfo = coroutineScope {
    try {
        val result = supabase.postgrest.rpc("get_best_album")
        val best_album = result.decodeSingle<AlbumInfo>()
        return@coroutineScope best_album

    } catch (e: Exception) {
        print("Error fetching best album ${e.message}")
        throw(e)
    }
}


suspend fun fetch_most_popular_album(): AlbumInfo = coroutineScope {
    try {
        val result = supabase.postgrest.rpc("get_most_popular_album")
        val popular_album = result.decodeSingle<AlbumInfo>()
        return@coroutineScope popular_album
    } catch (e: Exception) {
        print("Error fetching worst album ${e.message}")
        throw(e)
    }
}

suspend fun fetch_rising_star_album(): AlbumInfo = coroutineScope {
    try {
        val result = supabase.postgrest.rpc("get_rising_star_album")
        val rising_album = result.decodeSingle<AlbumInfo>()
        return@coroutineScope rising_album
    } catch (e: Exception) {
        print("Error fetching worst album ${e.message}")
        throw(e)
    }
}

suspend fun fetch_full_artist_info(id: String): Artist = coroutineScope {
    try {
        val artist_info = supabase.from("full_artist_info")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeSingle<Artist>()

        print(artist_info)
        return@coroutineScope artist_info
    } catch (e: Exception) {
        print("Error fetching artist info: ${e.message}")
        throw(e)
    }
}

suspend fun fetch_friends(): List<Friend> = coroutineScope {
    try {
        val res = supabase.postgrest.rpc("get_friends")
        val friends = res.decodeList<Friend>()
        print(friends)
        return@coroutineScope friends
    } catch (e: Exception) {
        print("Error fetching friend info: ${e.message}")
        throw(e)
    }
}

suspend fun fetch_reviews(): List<Review> = coroutineScope {
    try {
        val res = supabase.postgrest.rpc("get_reviews")
        val reviews = res.decodeList<Review>()
        print(reviews)
        return@coroutineScope reviews
    } catch (e: Exception) {
        print("Error fetching reviews: ${e.message}")
        throw(e)
    }
}

suspend fun fetch_reviews_for_album(album_id: String): List<Review> = coroutineScope {
    try {
        val rpcParams: JsonObject = buildJsonObject {
            put("input_album_id", album_id)
        }
        val res = supabase.postgrest.rpc(
            "get_reviews_with_album_id",
            parameters = rpcParams
        )
        val reviews = res.decodeList<Review>()
        print(reviews)
        return@coroutineScope reviews
    } catch (e: Exception) {
        print("Error fetching reviews: ${e.message}")
        throw(e)
    }
}

suspend fun find_albums(album_name: String): List<AlbumInfo> = coroutineScope {
    try {
        val rpcParams = buildJsonObject {
            put("input_album_name", album_name)
        }

        val res = supabase.postgrest.rpc(
            "find_albums",
            parameters = rpcParams
        )

        val albums = res.decodeList<AlbumInfo>()
        print(albums)
        return@coroutineScope albums
    } catch (e: Exception) {
        print("Error fetching search: ${e.message}")
        throw(e)
    }
}