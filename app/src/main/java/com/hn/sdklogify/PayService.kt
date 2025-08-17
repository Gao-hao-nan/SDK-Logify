package com.hn.sdklogify

import com.hn.lib_sdklogify.LogProxy
import com.hn.lib_sdklogify.callback.SdkCallback as LibCallback

/**
 * @author 浩楠
 *
 * @date 2025/8/17-10:51
 *
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/
 * @Description: TODO 接口 + 动态代理拦截：LogProxy.wrap
 */
interface PayService {
    fun pay(amount: Int, method: String, callback: LibCallback<PayResult>)
}

class RealPayService : PayService {
    override fun pay(amount: Int, method: String, callback: LibCallback<PayResult>) {
        // 这里模拟“成功/失败”，真实项目里是你的 SDK 逻辑
        if (amount > 0) {
            callback.onSuccess(PayResult(orderId = "ORD-${System.currentTimeMillis()}", amount = amount))
        } else {
            // 库里的 onError 签名是 (code: Any, msg: Any)，Int/String 直接传没问题
            callback.onError(code = -1, msg = "amount must > 0")
        }
    }
}

// 对接口做代理，自动把每次方法调用 + 参数 打进日志
fun providePayService(): PayService {
    val real = RealPayService()
    return LogProxy.wrap(real)
}

data class PayResult(val orderId: String, val amount: Int)