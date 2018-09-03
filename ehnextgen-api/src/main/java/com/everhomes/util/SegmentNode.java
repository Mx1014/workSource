package com.everhomes.util;

public class SegmentNode {

    private SegmentNode leftNode;
    private SegmentNode rightNode;
    private int maxCover = 0;
    private long lborder;
    private long rborder;
    private int lazyTag = 0;

    public SegmentNode(long lborder, long rborder){
        this.lborder = lborder;
        this.rborder = rborder;
    }

    public SegmentNode(){
        this.lborder = 0l;
        this.rborder = 1l;
    }

    public SegmentNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(SegmentNode leftNode) {
        this.leftNode = leftNode;
    }

    public SegmentNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(SegmentNode rightNode) {
        this.rightNode = rightNode;
    }

    public int getMaxCover() {
        return maxCover;
    }

    public void setMaxCover(int maxCover) {
        this.maxCover = maxCover;
    }

    public long getLborder() {
        return lborder;
    }

    public void setLborder(long lborder) {
        this.lborder = lborder;
    }

    public long getRborder() {
        return rborder;
    }

    public void setRborder(long rborder) {
        this.rborder = rborder;
    }

    public int getLazyTag() {
        return lazyTag;
    }

    public void setLazyTag(int lazyTag) {
        this.lazyTag = lazyTag;
    }
}
