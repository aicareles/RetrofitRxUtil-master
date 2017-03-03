package com.liulei.retrofitrxdemoo.model.tngou;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 * {
 *  "status":true,
 *  "total":101254,
 *  "tngou":Array[20]
 *  }
 *
 */

public class TngouResponse<T> implements Serializable {

    @SerializedName("status")
    public boolean status;

    @SerializedName("total")
    public int total;

    @SerializedName("tngou")
    public T tngou;

}
