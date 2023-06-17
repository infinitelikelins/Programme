package com.bearya.robot.programme.walk.car.travel;


public abstract class BaseTravelState implements IState {
    private boolean perform;

    @Override
    public void recognitionLoad() {

    }

    @Override
    public void newLoad() {

    }


    @Override
    public void computeExitPath() {

    }

    @Override
    public void unLocking() {

    }

    @Override
    public void travel() {

    }

    @Override
    public void arriveTarget() {

    }

    @Override
    public void exitLoad() {

    }

    @Override
    public void inObstacle() {

    }

    @Override
    public void outOfLoad() {

    }

    public synchronized void reset(){
        perform = false;
    }

    protected synchronized void doPerform(){
        perform = true;
    }

    protected boolean isPerform(){
        return perform;
    }

}
