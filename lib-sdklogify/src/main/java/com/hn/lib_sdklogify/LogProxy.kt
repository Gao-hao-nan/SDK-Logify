package com.hn.lib_sdklogify

import com.hn.lib_sdklogify.enum.LogLevel


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
object LogProxy {
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> wrap(
        target: T,
        logLevel: LogLevel = LogLevel.INFO,
        logger: ((methodName: String, args: Array<out Any?>?) -> Unit)? = null
    ): T {
        val clazz = target::class.java.interfaces.firstOrNull()
            ?: throw IllegalArgumentException("Target must implement an interface")

        return java.lang.reflect.Proxy.newProxyInstance(
            clazz.classLoader,
            arrayOf(clazz)
        ) { _, method, args ->
            val methodName = method.name
            logger?.invoke(methodName, args)

            val argsStr = args?.joinToString(", ") ?: "no-args"
            Logger.autoReport(
                code = "METHOD_CALL",
                msg = "Call: $methodName($argsStr)",
                data = null,
                level = logLevel
            )

            method.invoke(target, *(args ?: emptyArray()))
        } as T
    }
}
