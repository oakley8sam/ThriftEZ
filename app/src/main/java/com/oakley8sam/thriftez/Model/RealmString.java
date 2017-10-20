package com.oakley8sam.thriftez.Model;

import io.realm.RealmObject;

/**
 * Created by oakle on 10/19/2017.
 */

public class RealmString extends RealmObject {
    private String val;

    public String getValue() {
        return val;
    }

    public void setValue(String value) {
        this.val = value;
    }
}
