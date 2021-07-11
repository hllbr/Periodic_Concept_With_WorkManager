package com.hllbr.periodic_request

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class RefreshDatabase(val context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {


    override fun doWork(): Result {
        val getData = inputData //Main activite üzerinden veri almak için kullandığımız bir yöntem constructor içerisinde bir değer oluşturmadan veri almak için ideal bir yol oalrak düşünülebilir.
        //intent ile veri gönderip almak gibi benzer bir yöntem olarak düşünülebilir.
        val myNumber = getData.getInt("intKey",0)
        refreshDataBase(myNumber)
        return Result.success()
    }
    private fun refreshDataBase(myNumber : Int){
        val sharedPreferences = context.getSharedPreferences("com.hllbr.periodic_request",Context.MODE_PRIVATE)
        var mySavedNumber = sharedPreferences.getInt("myNumber",0)
        mySavedNumber += myNumber
        println(mySavedNumber)
        sharedPreferences.edit().putInt("myNumber",mySavedNumber).apply()
    }
}