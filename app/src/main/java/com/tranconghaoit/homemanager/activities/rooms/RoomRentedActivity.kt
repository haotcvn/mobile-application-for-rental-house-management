package com.tranconghaoit.homemanager.activities.rooms

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.tranconghaoit.homemanager.adapters.RoomRentedAdapter
import com.tranconghaoit.homemanager.api.RetrofitClient
import com.tranconghaoit.homemanager.databinding.ActivityRoomRentedBinding
import com.tranconghaoit.homemanager.models.RoomRentedModel
import com.tranconghaoit.homemanager.utils.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RoomRentedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoomRentedBinding
    private lateinit var mAdapter: RoomRentedAdapter
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomRentedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setupEvents()

        setupToolbar()
        mAdapter = RoomRentedAdapter(this, arrayListOf())
        binding.rcvRoomRented.apply {
            layoutManager = GridLayoutManager(this@RoomRentedActivity, 1)
            adapter = mAdapter
        }

        performGetRoomRented()

        mAdapter.listener = object : RoomRentedAdapter.OnItemClickListener {
            override fun onItemClick(contractID: Int, roomID: Int) {
                val intent = Intent()
                intent.putExtra("contractID", contractID)
                intent.putExtra("roomID", roomID)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

        }
    }

    private fun performGetRoomRented() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            RetrofitClient.apiService.getRoomRented()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<RoomRentedModel>> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(list: List<RoomRentedModel>) {
                        if (list.isNotEmpty()) {
                            //binding.progressBar.visibility = View.GONE
                            mAdapter.updateData(list)
                        } else {
                            //showNoData()
                        }
                    }

                    override fun onError(e: Throwable) {
                        //showNoData()
                    }

                    override fun onComplete() {

                    }
                })

        } else {
            NetworkUtils.showToast(this)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}