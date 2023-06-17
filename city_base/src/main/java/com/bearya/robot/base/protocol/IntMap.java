package com.bearya.robot.base.protocol;

import android.graphics.Point;

/**
 * Created by yexifeng on 17/11/16.
 */

public abstract class IntMap{
    public static final int MAX_MAP_START_OID = -1;
    public static final int MAX_MAP_COLUMN = 30;
    public abstract int getStartOid();
    public abstract int getRow();
    public abstract int getColumn();

    /**
     * 将地垫上的OID值转为第一象限坐标点起始点1000->点P(0,0)
     * @param oid
     * @return
     */
    public Point converToPoint(int oid){
        return converToPoint(MAX_MAP_START_OID,MAX_MAP_COLUMN,getStartOid(),oid);
    }

    private static Point converToPoint(int startOid, int column, int oid){
        if(oid>=startOid){
            int y = (oid-startOid)/column+1;
            int x = (oid+(column-startOid))%column+1;
            return new Point(x,y);
        }else{
            int y = (oid-startOid+1)/column-1;
            int x = Math.abs((oid-(column-startOid))%column+1);
            return new Point(x,y);
        }
    }

    public static Point converToPoint(int maxColumnStartOid,int maxColumn,int startOid,int oid){
        Point p1 =  converToPoint(startOid,maxColumn,oid);
        if(maxColumnStartOid == -1){
            return p1;
        }
        Point p = converToPoint(maxColumnStartOid,maxColumn,startOid);
        return new Point(p1.x-p.x+1,p1.y-p.y+1);
    }

    public int getCenterOid(int startOid,int endOid){
        Point startP = converToPoint(startOid);
        Point endP = converToPoint(endOid);
        Point centerP = new Point((startP.x+endP.x)/2,(startP.y+endP.y)/2);
        return converToOid(centerP);
    }

    /**
     * 将第一象限坐标点转为地垫上OID的值
     * @param point
     * @return
     */
    public int converToOid(Point point){
        return converToOid(MAX_MAP_START_OID,MAX_MAP_COLUMN,getStartOid(),point);
    }

    /**
     * 将第一象限坐标点转为地垫上OID的值
     * @param point
     * @return
     */
    public static int converToOid(int startOid, int column,Point point){
        return (point.x-1)+(column*(point.y-1))+startOid;
    }

    public static int converToOid(int maxColumnStartOid,int maxColumn,int startOid,Point point){
        if(maxColumnStartOid == -1){
            return converToOid(startOid,maxColumn,point);
        }
        Point worldP = converToPoint(maxColumnStartOid,maxColumn,startOid);
        Point p2 = new Point(worldP.x-1+point.x,worldP.y-1+point.y);
        int oid = converToOid(maxColumnStartOid,maxColumn,p2);
        return oid;
    }

    /**
     * 两点距离
     * @param p1
     * @param p2
     * @return
     */
    public static int distince(Point p1, Point p2){
        return (int)Math.sqrt(Math.abs(Math.pow(p1.x-p2.x,2)+Math.pow(p1.y-p2.y,2)));
    }
    public int distince(int s1, int s2){
        Point p1 = converToPoint(s1);
        Point p2 = converToPoint(s2);
        return distince(p1,p2);
    }

    /**
     * 几个OID距离oid最近的那个点
     * @param oids
     * @param oid
     * @return
     */
    public int minDistince(int[] oids,int oid){
        int minDistinceOid = 0;
        int minDistince = Integer.MAX_VALUE;
        for(int o:oids){
            int d = distince(o,oid);
            if(d<minDistince){
                minDistince = d;
                minDistinceOid = o;
            }
        }
        return minDistinceOid;
    }


    /**
     * 以后OID为顶点计算夹角
     * @param headP
     * @param tailP
     * @param targetP
     * @return
     */
    public int getAngle(Point headP, Point tailP, Point targetP) {
        int angle = angle(tailP,targetP,headP);
        return angle;
    }
    public int getAngle(int head, int tail, int target) {
        Point tailP = converToPoint(tail);
        Point targetP = converToPoint(target);
        Point headP = converToPoint(head);
//        DebugUtil.debug("tail(%d,%d),target(%d,%d),head(%d,%d)",tailP.x,tailP.y,targetP.x,targetP.y,headP.x,headP.y);
        return getAngle(headP,tailP,targetP);

    }

    public int getAngle(int headOid, int tailOid, Point target) {
        Point tailP = converToPoint(tailOid);
        Point headP = converToPoint(headOid);
        return getAngle(headP,tailP,target);
    }

    /**
     * 以后OID为顶点计算前OID与目标点的夹角
     * @param tail
     * @param target
     * @param head
     * @return
     */
    public static int angle(Point tail, Point target, Point head){
        float dx1, dx2, dy1, dy2;
        int angle;
        dx1 = target.x - tail.x;
        dy1 = target.y - tail.y;
        dx2 = head.x - tail.x;
        dy2 = head.y - tail.y;
        float c = (float)Math.sqrt(dx1 * dx1 + dy1 * dy1) * (float)Math.sqrt(dx2 * dx2 + dy2 * dy2);
        if (c == 0) return -1;
        angle = (int)(Math.acos((dx1 * dx2 + dy1 * dy2) / c)*180/Math.PI);
        if(!headInLeft(tail,target,head)){
            angle = 360-angle;
//            DebugUtil.warning("side:在右边");
        }else {
//            DebugUtil.warning("side:在左边");
        }
        return angle;
    }

    /**
     * 判断前OID在后OID与目标点连线的左边或右边
     * @param tail
     * @param target
     * @param head
     * @return true在左边
     */
    public static boolean headInLeft(Point tail, Point target, Point head){
        double tmpx = (tail.y - target.y) * head.x + (target.x-tail.x) * head.y + tail.x * target.y-target.x * tail.y;
        if (tmpx > 0) {//当tmpx>p.x的时候，说明点在线的左边，小于在右边，等于则在线上。
            return true;
        }
        return false;
    }


}
