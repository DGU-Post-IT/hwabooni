package com.postit.hwabooni.service;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatUploadService extends Service {

    private static final String TAG = "StatUploadService";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("onStartC", "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (auth.getCurrentUser() == null) stopSelf();
        if (!checkStatPermission()) stopSelf();
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)) stopSelf();
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)) return START_NOT_STICKY;

        UsageStatsManager usm = getSystemService(UsageStatsManager.class);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,-1);
        long millis = System.currentTimeMillis();
        List<UsageStats> result = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, cal.getTimeInMillis(), millis);
        long usage = -1;
        for (UsageStats stat : result) {
            if(stat.getPackageName().equals("com.postit.hwabooni")){
                Log.d(TAG, "onStartCommand: " + stat.getTotalTimeVisible());
                usage = stat.getTotalTimeVisible() / 1000;
            }
        }
        if(usage == -1) stopSelf();

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String docSubject = format.format(today);

        Map<String,Object> data = new HashMap<>();
        data.put("usage",usage);
        data.put("timestamp",new Timestamp(today));

        db.collection("User").document(auth.getCurrentUser().getEmail())
                .collection("usage").document(docSubject)
                .set(data).addOnCompleteListener((task)->{
                    stopSelf();
                });

        return START_STICKY;
    }

    private boolean checkStatPermission() {
        AppOpsManager aom = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = aom.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), "com.postit.hwabooni");
        return mode == MODE_ALLOWED;
    }
}
