package com.example.demo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {

    private val channel = "ro-fe.com/native-code"

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger,channel).setMethodCallHandler{
            call,result ->
            if (call.method == "getMessageFromNativeCode"){
                getFOlderPermission()
            }
            else{
                result.notImplemented()

            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getFOlderPermission()
    {
        val storageManager = application.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val intent = storageManager.primaryStorageVolume.createOpenDocumentTreeIntent()
        val targetDirectory = "Android%2Fmedia%2F%com.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"
        val uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI") as Uri
        var scheme = uri.toString()
        scheme = scheme.replace("/root/","/tree/")
        scheme += "%3A$targetDirectory"
        intent.putExtra("android.provide.extra.INITIAL_URI",uri)
        intent.putExtra("android.content.extra.SHOW_ADVANCED",true)
        startActivityForResult(intent,1234)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_OK){
            val treeUri = data?.data

            if (treeUri !=null) {
                contentResolver.takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                var fileDoc = DocumentFile.fromTreeUri(applicationContext, treeUri)
                print("file $fileDoc")
                print("tree Uri $treeUri")
            }
        }
    }
}
