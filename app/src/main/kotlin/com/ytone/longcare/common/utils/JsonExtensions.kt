package com.ytone.longcare.common.utils

import com.ytone.longcare.BuildConfig
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * Default Json instance for convenience.
 * It's generally better to inject or pass the configured Json instance
 * from your Dagger/Hilt module to ensure consistent configuration
 * (like ignoreUnknownKeys, isLenient, etc.).
 */
val DefaultJson: Json = Json {
    isLenient = true             // 对宽松的JSON格式更友好
    ignoreUnknownKeys = true     // 忽略JSON中存在但DTO中不存在的字段
    coerceInputValues = true     // 如果JSON字段为null，但DTO中对应字段不可空且有默认值，会尝试使用默认值
    encodeDefaults = false       // 序列化时是否包含具有默认值的属性 (false更常见，节省空间)
    prettyPrint = BuildConfig.DEBUG // 在Debug模式下格式化输出JSON，方便阅读
}

/**
 * Converts an object of type [T] to its JSON string representation.
 * @param T The type of the object, must be @Serializable.
 * @param json The Json instance to use for serialization. Defaults to [DefaultJson].
 * @return JSON string or null if serialization fails or the object is null.
 */
inline fun <reified T> T?.toJsonString(json: Json = DefaultJson): String? {
    if (this == null) return null // Handle null input explicitly if desired, though encodeToString might handle it based on T's nullability
    return try {
        json.encodeToString(this)
    } catch (e: SerializationException) {
        // Log error e.g. Log.e("JsonExtensions", "Failed to encode ${T::class.simpleName} to JSON", e)
        null
    }
}

/**
 * Converts a JSON string to an object of type [T].
 * @param T The target type of the object, must be @Serializable.
 * @param json The Json instance to use for deserialization. Defaults to [DefaultJson].
 * @return Object of type [T] or null if deserialization fails or JSON string is null/blank.
 */
inline fun <reified T> String?.toObject(json: Json = DefaultJson): T? {
    if (this.isNullOrBlank()) return null
    return try {
        json.decodeFromString<T>(this)
    } catch (e: SerializationException) {
        // Log error e.g. Log.e("JsonExtensions", "Failed to decode JSON to ${T::class.simpleName}", e)
        null
    }
}

/**
 * Converts a List of objects of type [T] to its JSON string representation.
 * @param T The type of elements in the list, must be @Serializable.
 * @param json The Json instance to use for serialization. Defaults to [DefaultJson].
 * @return JSON string representing the list or null if serialization fails or list is null.
 */
inline fun <reified T> List<T>?.toJsonStringList(json: Json = DefaultJson): String? {
    if (this == null) return null
    return try {
        // For lists (and other collections), kotlinx.serialization needs a KSerializer.
        // We can get it using `serializer<List<T>>()` if T is reified.
        // However, `json.encodeToString(this)` should work directly if T is serializable.
        json.encodeToString(this)
    } catch (e: SerializationException) {
        // Log error
        null
    }
}

/**
 * Converts a JSON string (representing an array) to a List of objects of type [T].
 * @param T The target type of elements in the list, must be @Serializable.
 * @param json The Json instance to use for deserialization. Defaults to [DefaultJson].
 * @return List of objects of type [T] or null if deserialization fails or JSON string is null/blank.
 */
inline fun <reified T> String?.fromJsonToList(json: Json = DefaultJson): List<T>? {
    if (this.isNullOrBlank()) return null
    return try {
        json.decodeFromString<List<T>>(this)
    } catch (e: SerializationException) {
        // Log error
        null
    }
}

// --- Map to/from JSON String ---

/**
 * Converts a Map<K, V> to its JSON string representation.
 * @param K The type of keys in the map, must be @Serializable or a basic type (String, primitives).
 * @param V The type of values in the map, must be @Serializable.
 * @param json The Json instance to use for serialization. Defaults to [DefaultJson].
 * @return JSON string representing the map or null if serialization fails or map is null.
 * Note: For complex keys, ensure they are properly serializable by Kotlinx.Serialization.
 *       JSON object keys are always strings. Non-string keys are typically converted to strings.
 */
inline fun <reified K, reified V> Map<K, V>?.toJsonStringMap(json: Json = DefaultJson): String? {
    if (this == null) return null
    return try {
        json.encodeToString(this)
    } catch (e: SerializationException) {
        // Log error
        null
    }
}

/**
 * Converts a JSON string (representing an object) to a Map<K, V>.
 * @param K The target type of keys in the map, must be @Serializable or a basic type.
 * @param V The target type of values in the map, must be @Serializable.
 * @param json The Json instance to use for deserialization. Defaults to [DefaultJson].
 * @return Map<K, V> or null if deserialization fails or JSON string is null/blank.
 * Note: JSON object keys are always strings. If K is not String, ensure appropriate deserialization.
 */
inline fun <reified K, reified V> String?.fromJsonToMap(json: Json = DefaultJson): Map<K, V>? {
    if (this.isNullOrBlank()) return null
    return try {
        json.decodeFromString<Map<K, V>>(this)
    } catch (e: SerializationException) {
        // Log error
        null
    }
}

// --- Alternative approach: Extensions on Json class ---
// This can be cleaner if you always have the Json instance handy.

/**
 * Converts an object of type [T] to its JSON string representation.
 * Extension on Json class.
 * @param T The type of the object, must be @Serializable.
 * @param value The object to serialize.
 * @return JSON string or null if serialization fails.
 */
inline fun <reified T> Json.tryEncodeToString(value: T): String? {
    return try {
        this.encodeToString(value)
    } catch (e: SerializationException) {
        null
    }
}

/**
 * Converts a JSON string to an object of type [T].
 * Extension on Json class.
 * @param T The target type of the object, must be @Serializable.
 * @param value The JSON string to deserialize.
 * @return Object of type [T] or null if deserialization fails.
 */
inline fun <reified T> Json.tryDecodeFromString(value: String): T? {
    return try {
        this.decodeFromString<T>(value)
    } catch (e: SerializationException) {
        null
    }
}