package com.likeit.currenciesapp.ui.chat.server;


public interface UpdateProgressListener {
    /**
     * download start
     */
    void start();

    /**
     * update download progress
     * @param progress
     */
    void update(int progress);

    /**
     * download success
     */
    void success();

    /**
     * download error
     */
    void error();
}
