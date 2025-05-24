package com.ytone.longcare.network.interceptor

import android.util.Log
import com.ytone.longcare.BuildConfig
import com.ytone.longcare.common.security.AESMode
import com.ytone.longcare.common.security.CryptoUtils
import com.ytone.longcare.common.security.RSAMode
import com.ytone.longcare.common.utils.DeviceUtils
import com.ytone.longcare.common.utils.RandomUtils
import com.ytone.longcare.common.utils.TimeUtils
import com.ytone.longcare.common.utils.toJsonStringMap
import okhttp3.Interceptor
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class RequestInterceptor @Inject constructor(private val deviceUtils: DeviceUtils) : Interceptor {

    companion object {
        val TAG: String = RequestInterceptor::class.java.simpleName
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        val requestBody = request.body
        if (!url.toString().startsWith(BuildConfig.BASE_URL)) {
            return chain.proceed(request)
        }

        var charset = StandardCharsets.UTF_8
        val method = request.method.lowercase().trim()
        val newRequestBuilder = request.newBuilder().header("Content-Type", "application/json")
        val randomString = RandomUtils.generateRandomStringKotlin(32)
        val map = iniHttpHeader(randomString)
        if (map.isNotEmpty()) {
            for (head in map) {
                newRequestBuilder.addHeader(head.key, head.value)
            }
        }

        /*判断请求体是否为空  不为空则执行以下操作*/
        if (requestBody != null) {
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(charset)/*如果是二进制上传  则不进行加密*/
                if (contentType.type.lowercase() == "multipart") {
                    return chain.proceed(newRequestBuilder.build())
                }
            }

            /*获取请求的数据*/
            try {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                val requestData = URLDecoder.decode(buffer.readString(charset).trim(), "utf-8")
                val bodyMap = HashMap<String, Any>()
                bodyMap["ParamJsonString"] = encryptRequest(randomString, requestData)
                val encryptData = bodyMap.toJsonStringMap().orEmpty()
                val newRequestBody = encryptData.toRequestBody(contentType)

                //根据请求方式构建相应的请求
                when (method) {
                    "post" -> newRequestBuilder.post(newRequestBody)
                    "put" -> newRequestBuilder.put(newRequestBody)
                }

            } catch (e: Exception) {
                Log.e("加密异常====》", "$e")
                return chain.proceed(newRequestBuilder.build())
            }
        }
        val build = newRequestBuilder.build()
        return chain.proceed(build)
    }

    private fun iniHttpHeader(randomString: String): HashMap<String, String> {
        val map = HashMap<String, Any>()
        map["userId"] = 1L //TODO 参数
        map["token"] = "token"//TODO 参数
        map["nonce"] = randomString
        map["timeSpan"] = TimeUtils.getCurrentEpochMilliseconds()
        map["platform"] = "android"
        map["versionCode"] = deviceUtils.getAppVersionCode()
        map["versionName"] = deviceUtils.getAppVersionName()
        map["deviceId"] = deviceUtils.getAppInstanceId()
        map["channel"] = "office"
        val headerInfo = map.toJsonStringMap().orEmpty()
        return HashMap<String, String>().also {
            it["AesKeyString"] = getAKHead(randomString)
            it["BaseParamString"] = encryptRequest(randomString, headerInfo)
        }
    }

    private fun getAKHead(data: String): String {
        return CryptoUtils.rsaEncrypt(data, BuildConfig.PUBLIC_KEY, RSAMode.NONE_PKCS1_PADDING)
            ?.getCipherTextHex().orEmpty()
    }

    private fun encryptRequest(key: String, data: String): String {
        return CryptoUtils.aesEncrypt(
            data,
            key,
            AESMode.CBC_PKCS7_PADDING,
            if (key.length > 16) getInitializationVectorConcise() else key.toByteArray()
        )?.getCipherTextHex().orEmpty()
    }

    private fun getInitializationVectorConcise(): ByteArray {
        return byteArrayOf(
            0x41.toByte(),
            0x72.toByte(),
            0x65.toByte(),
            0x79.toByte(),
            0x6F.toByte(),
            0x75.toByte(),
            0x6D.toByte(),
            0x79.toByte(),
            0x53.toByte(),
            0x6E.toByte(),
            0x6F.toByte(),
            0x77.toByte(),
            0x6D.toByte(),
            0x61.toByte(),
            0x6E.toByte(),
            0x3F.toByte()
        )
    }
}