package com.example.tencentmeeting.presenter

import com.example.tencentmeeting.contract.RecordContract
import com.example.tencentmeeting.data.DataRepository

/**
 * 录制页面的Presenter实现
 * Record page Presenter implementation
 */
class RecordPresenter(
    private val repository: DataRepository
) : RecordContract.Presenter {

    private var view: RecordContract.View? = null

    override fun attachView(view: RecordContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadStorageInfo() {
        try {
            // 默认存储信息：0MB已用，1GB总容量，0个文件
            view?.showStorageInfo(usedMB = 0, totalMB = 1024, fileCount = 0)
            view?.showEmptyState()
        } catch (e: Exception) {
            view?.showError("加载存储信息失败：${e.message}")
        }
    }

    override fun onDestroy() {
        detachView()
    }
}
