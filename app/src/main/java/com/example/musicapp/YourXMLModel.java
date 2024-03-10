package com.example.musicapp;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "data")
public class YourXMLModel {
    @ElementList(inline = true, required = false)
    public List<Param> params;

    @Override
    public String toString() {
        return "YourXMLModel{" +
                "params=" + params +
                '}';
    }
}