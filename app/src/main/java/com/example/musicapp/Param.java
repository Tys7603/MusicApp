package com.example.musicapp;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "param")
class Param {

    @Attribute(name = "s")
    private String s;

    @ElementList(inline = true, required = false)
    public List<YourData> yourData;

    @Override
    public String toString() {
        return "Param{" +
                "s='" + s + '\'' +
                ", yourData=" + yourData +
                '}';
    }
}

