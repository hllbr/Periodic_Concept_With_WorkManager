package com.hllbr.periodic_request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = Data.Builder().putInt("intKey",1).build()
        /*
        Telefon sadece şarj durumunda olduğunda sadece internet açıkken gibi koşullar belirleyerek istediğimiz koşullarda bu constraitlerimizi çalıştırabiliriz.

         */
        val constraints = Constraints.Builder()
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

    //Periodic olarak yapıyı kurduğumuzda ne kadar sürede bri gerçekleştirileceğini ifade etmemiz gerekiyor.
        val myWorkRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<RefreshDatabase>(15,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(data)
            .build()
        //Bu işlemi en az 15 dakikada bir yapabiliriz daha alt bir zaman dilimine izin verilmiyor.
        WorkManager.getInstance(this).enqueue(myWorkRequest)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id).observe(this,
            Observer {
                if (it.state == WorkInfo.State.RUNNING){
                    println("running")
                }else if (it.state == WorkInfo.State.FAILED){
                    println("failed")
                }else if (it.state == WorkInfo.State.SUCCEEDED){
                    println("Succeded")
                }
            })
      //  WorkManager.getInstance(this).cancelAllWork()//hepsini iptal edebiliriz.

        //Chaining = WorkManager x işlemle başla o bittiğinde y yi yap y bittiğinde z yi yap diyebildiğimizi bir yapı
        //Periodic requestler ile bu yapıyı kullanamıyoruz sadece OneTimeRequest ile yapılabiliyor.

        val oneTimeRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

      /*  WorkManager.getInstance(this).beginWith(oneTimeRequest)//beginWith başla neyle başla :)
            .then(oneTimeRequest)
            .then(oneTimeRequest)
            .enqueue()*/

    }
}