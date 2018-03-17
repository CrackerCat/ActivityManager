package com.sdex.activityrunner.intent;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.sdex.activityrunner.db.AppDatabase;
import com.sdex.activityrunner.db.history.HistoryModel;

public class LaunchParamsViewModel extends AndroidViewModel {

  private final AppDatabase appDatabase;

  public LaunchParamsViewModel(@NonNull Application application) {
    super(application);
    appDatabase = AppDatabase.getDatabase(application);
  }

  public void addToHistory(LaunchParams launchParams) {
    new InsertAsyncTask(appDatabase).execute(launchParams);
  }

  private static class InsertAsyncTask extends AsyncTask<LaunchParams, Void, Void> {

    private AppDatabase database;

    InsertAsyncTask(AppDatabase appDatabase) {
      database = appDatabase;
    }

    @Override
    protected Void doInBackground(final LaunchParams... params) {
      LaunchParamsHistoryConverter historyConverter =
        new LaunchParamsHistoryConverter(params[0]);
      final HistoryModel historyModel = historyConverter.convert();
      database.getHistoryRecordDao().insert(historyModel);
      return null;
    }

  }

}