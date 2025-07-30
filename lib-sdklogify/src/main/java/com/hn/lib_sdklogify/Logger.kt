package com.hn.lib_sdklogify

import android.annotation.SuppressLint
import android.content.Context
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.TimeUtils
import com.hn.lib_sdklogify.enum.LogLevel
import java.io.File

/**
 * @author 浩楠
 * @date 2025/7/28
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/
 *  描述: TODO
 */
@SuppressLint("StaticFieldLeak")
object Logger {
    private lateinit var context: Context
    private var codeResolver: ((Any?) -> String)? = null
    private var msgResolver: ((Any?) -> String)? = null

    fun init(
        context: Context,
        codeResolver: (Any?) -> String,
        msgResolver: (Any?) -> String
    ) {
        this.context = context.applicationContext
        this.codeResolver = codeResolver
        this.msgResolver = msgResolver
    }

    fun autoReport(code: Any?, msg: Any?, data: Any? = null, level: LogLevel = LogLevel.INFO) {
        val codeStr = codeResolver?.invoke(code) ?: code.toString()
        val msgStr = msgResolver?.invoke(msg) ?: msg.toString()
        val time = TimeUtils.getNowString()
        val tag = getCallerTag()
        val dataStr = data?.toString() ?: ""
        val line = "[$time][$level][$tag][$codeStr] $msgStr $dataStr"

        println(line)
        val logFile = getLogFile(context)
        FileIOUtils.writeFileFromString(logFile, "$line\n", true)
    }

    fun getLogFile(context: Context): File {
        val logDir = File(context.filesDir, "sdk_logs")
        FileUtils.createOrExistsDir(logDir)
        return File(logDir, "sdk_log.txt")
    }


    private fun getCallerTag(): String {
        return Thread.currentThread().stackTrace
            .firstOrNull { !it.className.contains("Logger") }
            ?.className
            ?.substringAfterLast(".")
            ?: "Logger"
    }
}