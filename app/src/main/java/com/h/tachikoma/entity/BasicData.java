package com.h.tachikoma.entity;

import java.util.List;

/**
 * 基本数据bean
 * Created by tony on 2016/4/13.
 */
public class BasicData<T> {

    /**
     * error :
     * results :
     */

    private boolean error;

    private List<T> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }


}
