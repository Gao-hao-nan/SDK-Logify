package com.hn.sdklogify

/**
 * @author 浩楠
 *
 * @date 2025/8/17-10:50
 *
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/
 * @Description: TODO 业务枚举
 */

enum class BizCode(val value: String) {
    APP_START("APP_START"),
    PAY_START("PAY_START"),
    PAY_OK("PAY_OK"),
    PAY_FAIL("PAY_FAIL")
}

enum class BizMsg(val text: String) {
    AppLaunched("App launched"),
    PayBegin("Start paying"),
    PaySuccess("Pay success"),
    PayError("Pay failed")
}