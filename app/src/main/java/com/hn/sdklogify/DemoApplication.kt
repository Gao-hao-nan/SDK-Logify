package com.hn.sdklogify

import android.app.Application
import com.hn.lib_sdklogify.Logger
import com.hn.lib_sdklogify.enum.LogLevel

/**
 * @author 浩楠
 *
 * @date 2025/8/17-10:48
 *
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/
 * @Description: TODO
 */
class DemoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        this.initResolver()
        this.initLog()
    }

    private fun initResolver() {

    }

    /**
     * 核心初始化
     */
    private fun initLog() {
        // 业务 Code/Msg（可以是枚举/类），通过 resolver 统一转字符串
        val codeResolver: (Any?) -> String = { code ->
            when (code) {
                is BizCode -> code.value
                is Int     -> code.toString()
                is String  -> code
                else       -> code?.toString() ?: "UNKNOWN_CODE"
            }
        }

        val msgResolver: (Any?) -> String = { msg ->
            when (msg) {
                is BizMsg  -> msg.text
                is String  -> msg
                else       -> msg?.toString() ?: "UNKNOWN_MSG"
            }
        }

        Logger.init(
            context = this,
            codeResolver = codeResolver,
            msgResolver = msgResolver
        )
    }
}