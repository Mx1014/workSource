package com.everhomes.rest.rentalv2.admin;

/**
 * Created by Administrator on 2018/4/17.
 */
public class Tuple<K, V> {
    private K key;
    private V value;

    public Tuple(K key,V value){
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
