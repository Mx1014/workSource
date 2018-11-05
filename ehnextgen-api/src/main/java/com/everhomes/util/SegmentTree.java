package com.everhomes.util;

import java.util.ArrayList;
import java.util.List;

public class SegmentTree {

    private SegmentNode root;

    public SegmentTree(){
        this.root = new SegmentNode();
    }

    public void putSegment(long lborder, long rborder, int num){
        if (lborder == rborder)
            return;
        modifyTree(rborder);
        putSegment(this.root,lborder,rborder,num);
    }

    private void putSegment(SegmentNode p, long lborder, long rborder, int num){
        if (lborder == p.getLborder() && rborder == p.getRborder()) {
            p.setLazyTag(p.getLazyTag() + num);
            p.setMaxCover(p.getMaxCover() + num);
            return;
        }
        long mid = (p.getLborder() + p.getRborder()) / 2;
        if (p.getLeftNode() == null){
            SegmentNode tmp = new SegmentNode();
            tmp.setLborder(p.getLborder());
            tmp.setRborder(mid);
            p.setLeftNode(tmp);
            tmp = new SegmentNode();
            tmp.setLborder(mid);
            tmp.setRborder(p.getRborder());
            p.setRightNode(tmp);
        }
        modifyNode(p);
        if (lborder < mid){
            if (rborder <= mid)
                putSegment(p.getLeftNode(),lborder,rborder,num);
            else
                putSegment(p.getLeftNode(),lborder,mid,num);
        }
        if (rborder > mid){
            if (lborder >= mid)
                putSegment(p.getRightNode(),lborder,rborder,num);
            else
                putSegment(p.getRightNode(),mid,rborder,num);
        }
        p.setMaxCover(Math.max(p.getLeftNode().getMaxCover(),p.getRightNode().getMaxCover()));
    }

    public int getMaxCover(long lborder, long rborder){
        if (lborder == rborder)
            return 0;
        if (rborder > this.root.getRborder())
            rborder = this.root.getRborder();
        return getMaxCover(this.root,lborder,rborder);
    }

    public List<SegmentNode> getZeroConverNodes(long lborder, long rborder){
        List<SegmentNode> covers = new ArrayList<>();
        if (lborder == rborder)
            return covers;
        if (lborder >= this.root.getRborder()){
            SegmentNode node = new SegmentNode();
            node.setLborder(lborder);
            node.setRborder(rborder);
            covers.add(node);
            return covers;
        }

        covers.addAll(getZeroConverNodes(this.root,lborder,rborder > root.getRborder() ? root.getRborder() : rborder));
        if (rborder > this.root.getRborder()) {
            SegmentNode node = new SegmentNode();
            node.setLborder(this.root.getRborder());
            node.setRborder(rborder);
            covers.add(node);
        }

        //合并时间段
        for (int i = covers.size() - 1;i > 0;i--){
            if (covers.get(i).getLborder() == covers.get(i - 1).getRborder()){
                covers.get(i - 1).setRborder(covers.get(i).getRborder());
                covers.remove(i);
            }
        }
        return covers;
    }

    private int getMaxCover(SegmentNode p, long lborder, long rborder){
        if ((lborder == p.getLborder() && rborder == p.getRborder()) || p.getLeftNode() == null)
            return p.getMaxCover();
        long mid = (p.getLborder() + p.getRborder()) / 2;
        modifyNode(p);
        int maxCover = 0;
        if (lborder < mid){
            if (rborder <= mid)
                maxCover = Math.max(getMaxCover(p.getLeftNode(),lborder,rborder),maxCover);
            else
                maxCover = Math.max(getMaxCover(p.getLeftNode(),lborder,mid),maxCover);
        }
        if (rborder > mid) {
            if (lborder >= mid)
                maxCover = Math.max(getMaxCover(p.getRightNode(), lborder, rborder),maxCover);
            else
                maxCover = Math.max(getMaxCover(p.getRightNode(), mid, rborder),maxCover);
        }
        return maxCover;
    }

    private List<SegmentNode> getZeroConverNodes(SegmentNode p,long lborder, long rborder){
        List<SegmentNode> covers = new ArrayList<>();
        if ( p.getLeftNode() == null)
            if (p.getMaxCover() != 0) {
                return covers;
            }else{
                SegmentNode node = new SegmentNode();
                node.setLborder(lborder);
                node.setRborder(rborder);
                covers.add(node);
                return covers;
            }

        long mid = (p.getLborder() + p.getRborder()) / 2;
        modifyNode(p);
        if (lborder < mid){
            if (rborder <= mid)
                covers.addAll(getZeroConverNodes(p.getLeftNode(),lborder,rborder));
            else
                covers.addAll(getZeroConverNodes(p.getLeftNode(),lborder,mid));
        }
        if (rborder > mid) {
            if (lborder >= mid)
                covers.addAll(getZeroConverNodes(p.getRightNode(), lborder, rborder));
            else
                covers.addAll(getZeroConverNodes(p.getRightNode(), mid, rborder));
        }
        return covers;
    }

    private void modifyNode(SegmentNode p){
        p.getLeftNode().setLazyTag(p.getLeftNode().getLazyTag() + p.getLazyTag());
        p.getLeftNode().setMaxCover(p.getLeftNode().getMaxCover() + p.getLazyTag());
        p.getRightNode().setLazyTag(p.getRightNode().getLazyTag() + p.getLazyTag());
        p.getRightNode().setMaxCover(p.getRightNode().getMaxCover() + p.getLazyTag());
        p.setLazyTag(0);
    }

    private void modifyTree(long rborder){
        while (this.root.getRborder() < rborder){
            SegmentNode tmp = new SegmentNode();
            tmp.setLeftNode(this.root);
            tmp.setLborder(this.root.getLborder());
            tmp.setMaxCover(this.root.getMaxCover());

            SegmentNode rNode = new SegmentNode();
            tmp.setRightNode(rNode);
            rNode.setLborder(this.root.getRborder());
            rNode.setRborder(this.root.getRborder() * 2);
            tmp.setRborder(rNode.getRborder());

            this.root = tmp;
        }
    }
}
