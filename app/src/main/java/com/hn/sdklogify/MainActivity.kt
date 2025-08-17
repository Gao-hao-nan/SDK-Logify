package com.hn.sdklogify

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hn.lib_sdklogify.Logger
import com.hn.lib_sdklogify.callback.LoggedCallback
import com.hn.lib_sdklogify.callback.SdkCallback
import com.hn.lib_sdklogify.enum.LogLevel
import java.io.File

class MainActivity : AppCompatActivity() {

    private val payService: PayService by lazy { providePayService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvTitle   = findViewById<TextView>(R.id.tvTitle)
        val tvResult  = findViewById<TextView>(R.id.tvResult)
        val tvLogPath = findViewById<TextView>(R.id.tvLogPath)
        val btnOk     = findViewById<Button>(R.id.btnPayOk)
        val btnFail   = findViewById<Button>(R.id.btnPayFail)

        // 可选：启动打一条
        Logger.autoReport(code = BizCode.APP_START, msg = BizMsg.AppLaunched, level = LogLevel.INFO)

        // 原始回调：只处理业务UI
        val origin: SdkCallback<PayResult> = object : SdkCallback<PayResult> {
            override fun onSuccess(data: PayResult) {
                tvResult.text = "结果：成功（orderId=${data.orderId}, amount=${data.amount}）"
                showLogPath(tvLogPath)
            }

            override fun onError(code: Any, msg: Any) {
                tvResult.text = "结果：失败（code=$code, msg=$msg）"
                showLogPath(tvLogPath)
            }
        }

        // 用 LoggedCallback 包装：lambda 必须返回 Pair(code,msg)，不要在 lambda 里 autoReport
        fun wrapped(origin: SdkCallback<PayResult>): SdkCallback<PayResult> =
            LoggedCallback(
                origin = origin,
                onSuccessLog = { _: PayResult ->
                    BizCode.PAY_OK to BizMsg.PaySuccess
                },
                onErrorLog = { _: Any, message: Any ->
                    BizCode.PAY_FAIL to "${BizMsg.PayError.text}: $message"
                }
            )

        btnOk.setOnClickListener {
            Logger.autoReport(code = BizCode.PAY_START, msg = BizMsg.PayBegin, level = LogLevel.INFO)
            payService.pay(amount = 100, method = "CARD", callback = wrapped(origin))
        }

        btnFail.setOnClickListener {
            Logger.autoReport(code = BizCode.PAY_START, msg = BizMsg.PayBegin, level = LogLevel.INFO)
            payService.pay(amount = 0, method = "CARD", callback = wrapped(origin))
        }

        tvTitle.text = "SDK-Logify Demo（Click 按钮触发）"
        showLogPath(tvLogPath)
    }

    private fun showLogPath(tv: TextView) {
        val file = File(filesDir, "sdk_logs/sdk_log.txt")
        tv.text = "日志路径：${file.absolutePath}"
    }
}