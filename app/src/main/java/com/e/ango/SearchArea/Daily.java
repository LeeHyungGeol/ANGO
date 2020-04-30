package com.e.ango.SearchArea;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Daily {
    protected ArrayList<Datum> data;

    @JsonProperty("data")
    public ArrayList<Datum> getData() { return data; }
    @JsonProperty("data")
    public void setData(ArrayList<Datum> value) { this.data = value; }
}
