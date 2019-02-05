package com.epam.jtc.concurrentPlane.Output;

public class ConsoleInfoOutput implements InfoOutput {

    private final static String SHOT = "Gun shot! %d  Synchronizer: %s \n";
    private final static String SHOOTING_BLOCKED = "Shooting blocked";
    private final static String SHOOTING_ALLOWED = "Shooting allowed";
    private final static String BLADE_POSITION = "Blade %d in position = %f\n";
    private int shotsCount = 1;

    @Override
    public void showShot(String qwe) {
        System.out.printf(SHOT, shotsCount++, qwe);
        if (Integer.parseInt(qwe)>0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void showCanShoot(boolean canShoot) {
        if (canShoot) {
            System.out.println(SHOOTING_ALLOWED);
        } else {
            System.out.println(SHOOTING_BLOCKED);
        }
    }

    @Override
    public void showPropellerBladesPositions(double... blades) {
        for (int i = 0; i < blades.length; i++) {
            System.out.format(BLADE_POSITION, i + 1, blades[i]);
        }
    }

}
