package com.wisdom.result;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class PageResult implements Serializable {

    private long total;
    private List records;

    public PageResult(long total, List records) {
        this.total = total;
        this.records = records;
    }

}