package com.tranconghaoit.homemanager.databases

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.models.BillModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody

class BillDAO(val context: Context) {
    private var disposable: Disposable? = null

    fun performAddBill(bill: BillModel) {
        if (NetworkUtils.haveNetworkConnection(context)) {
            RetrofitClient.apiService.addBill(bill)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        val response = responseBody.string()
                        toast(response)
                        if (response == "Tạo hóa đơn thành công") {
                            Activity().finish()
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                    }
                })
        } else {
            NetworkUtils.showToast(context)
        }
    }/* END performAddBill() */

    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}