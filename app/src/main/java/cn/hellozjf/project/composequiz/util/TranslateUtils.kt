package cn.hellozjf.project.composequiz.util

import com.aliyun.alimt20181012.models.TranslateGeneralRequest

/**
 * 阿里云机器翻译工具
 */
class TranslateUtils {
  companion object {

    /**
     * 创建阿里云机器翻译客户端
     */
    fun createClient(): com.aliyun.alimt20181012.Client {
      val accessKeyId = System.getenv("TRANSLATE_ACCESS_KEY_ID")
      val accessKeySecret = System.getenv("TRANSLATE_ACCESS_KEY_SECRET")
      val config = com.aliyun.teaopenapi.models.Config()
        .setAccessKeyId(accessKeyId)
        .setAccessKeySecret(accessKeySecret)
        .setEndpoint("mt.cn-hangzhou.aliyuncs.com")
      return com.aliyun.alimt20181012.Client(config)
    }

    /**
     * 将英文翻译成中文
     */
    fun en2zh(
      client: com.aliyun.alimt20181012.Client,
      english: String
    ): String {
      val request = TranslateGeneralRequest()
        .setFormatType("text")
        .setSourceLanguage("en")
        .setTargetLanguage("zh")
        .setSourceText(english)
        .setScene("general")
      val response = client.translateGeneral(request)
      return response.body.data.translated
    }
  }
}