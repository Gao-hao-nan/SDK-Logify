package com.hn.lib_sdklogify.callback

import com.hn.lib_sdklogify.Logger
import com.hn.lib_sdklogify.enum.LogLevel

/**
 * @author 浩楠
 * @date 2025/7/28
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/
 *  描述:
 */
class LoggedCallback<T>(
    private val origin: SdkCallback<T>,
    private val onSuccessLog: (T) -> Pair<Any, Any>,
    private val onErrorLog: (Any, Any) -> Pair<Any, Any>
) : SdkCallback<T> {

    override fun onSuccess(data: T) {
        val (code, msg) = onSuccessLog(data)
        Logger.autoReport(code = code, msg = msg, data = data, level = LogLevel.INFO)
        origin.onSuccess(data)
    }

    override fun onError(code: Any, msg: Any) {
        val (resolvedCode, resolvedMsg) = onErrorLog(code, msg)
        Logger.autoReport(code = resolvedCode, msg = resolvedMsg, level = LogLevel.ERROR)
        origin.onError(code, msg)
    }
}
