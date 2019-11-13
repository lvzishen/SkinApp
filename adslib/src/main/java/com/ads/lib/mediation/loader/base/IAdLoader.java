package com.ads.lib.mediation.loader.base;

public interface IAdLoader<Listener extends AdListener> {

    void load();

    void setAdListener(Listener listener);

    void destory();
}
