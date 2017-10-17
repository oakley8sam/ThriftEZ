package com.oakley8sam.thriftez;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmInit extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("ThriftEZRealm.realm").build();
        Realm.setDefaultConfiguration(config);
    }
}
