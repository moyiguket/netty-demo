package com.sd.demo.domain;

public class FeedBack {
    public long feedId;

    private int titleLen;
    private String title;

    private int contentLen;
    private String content;


    public long getFeedId() {
        return feedId;
    }

    public int getTitleLen() {
        return titleLen;
    }

    public void setTitleLen(int titleLen) {
        this.titleLen = titleLen;
    }

    public int getContentLen() {
        return contentLen;
    }

    public void setContentLen(int contentLen) {
        this.contentLen = contentLen;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        setTitleLen(title.length());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        setContentLen(content.length());
    }

    @Override
    public String toString() {
        return "FeedBack{" +
                "feedId=" + feedId +
                ", titleLen=" + titleLen +
                ", title='" + title + '\'' +
                ", contentLen=" + contentLen +
                ", content='" + content + '\'' +
                '}';
    }
}
