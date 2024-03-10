package com.example.musicapp;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "i")
class YourData {
    @Attribute(name = "va")
    private float time;
    @Text
    private String content; // Trường dữ liệu tương ứng với nội dung của phần tử <i>

    public float getTime() {
        return time;
    }

    public void setVa(float va) {
        this.time = va;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "YourData{" +
                "va='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}