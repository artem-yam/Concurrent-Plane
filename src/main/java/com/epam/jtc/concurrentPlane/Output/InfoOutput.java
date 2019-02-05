package com.epam.jtc.concurrentPlane.Output;

public interface InfoOutput {

    void showShot(String qwe);

    void showCanShoot(boolean canShoot);

    void showPropellerBladesPositions(double... blades);
    
}
