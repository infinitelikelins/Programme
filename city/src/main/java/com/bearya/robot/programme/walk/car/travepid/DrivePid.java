package com.bearya.robot.programme.walk.car.travepid;

import com.bearya.robot.base.util.DebugUtil;

import static com.bearya.robot.programme.walk.car.travel.TWheelController.MAX_SPEECH;

public class DrivePid {

    private final String name;
    //double Kp=3.23;  //50  60  70
    double Kp=3.23;
    double Ki=0.01;
    double Kd=0.03;
    double err_last=0;
    double err_sum=0;
    private int basespeed;

    public DrivePid( double Kp,double Ki,double Kd , int base,String name)
    {
        this.basespeed = base;
        this.Kp=Kp;
        this.Ki=Ki;
        this.Kd=Kd;
        this.name = name;
    }
    public void reset()
    {
        DebugUtil.debug("%s reset",this.name);
        this.err_last=0;
        this.err_sum=0;
    }

    public String getName()
    {
        return this.name;
    }

    public static double angle2error( int angle )
    {
        double err=0;
        if(angle > 180){
            err = (angle - 360);
        }
        else{
            err = angle;
        }
        return err;
    }

    public double _loc_accel( int angle )
    {
        double out_accel;
        double err=0;
        err = angle2error( angle);
        this.err_sum = this.err_sum + err;
        double err_diff = err - this.err_last;
        out_accel = this.Kp * err + this.Ki* this.err_sum + this.Kd*err_diff;
        this.err_last = err;
        if ( Math.abs( err) > 1000){
            this.reset();
        }
        this.setaccell( out_accel);
        DebugUtil.debug("_loc_accel\t%s\t%d\t%f\t%f\t%d\t%d",this.name,angle,err,out_accel,this.leftspeed,this.rightspeed);

        return out_accel;
    }


    static final int maxspeed = 150;
    int accel=0;

    int leftspeed = this.basespeed;
    int rightspeed = this.basespeed;


    public int getleft()
    {
        return this.leftspeed;
    }
    public int getright()
    {
        return this.rightspeed;
    }
    private int getSpeed(int speed){
        if(speed<=maxspeed*-1){
            speed = maxspeed*-1;
        }
        if(speed>maxspeed){
            speed = maxspeed;
        }
        return speed;
    }
    private int setaccell( double out_accel )
    {
        this.accel = (int)out_accel;

        this.leftspeed = basespeed - accel;
        this.rightspeed = basespeed + accel;

        if( this.accel > 0 && this.rightspeed > maxspeed ){
            this.leftspeed -= this.rightspeed - maxspeed;
            this.rightspeed = maxspeed;
        }
        if( this.accel < 0 && this.leftspeed > maxspeed ){
            this.rightspeed -= this.leftspeed - maxspeed;
            this.leftspeed = maxspeed;
        }

        this.leftspeed = getSpeed(this.leftspeed);
        this.rightspeed = getSpeed(this.rightspeed);
        return this.accel;
    }

}
