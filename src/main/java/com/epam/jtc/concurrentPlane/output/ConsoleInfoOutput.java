package com.epam.jtc.concurrentPlane.output;

public class ConsoleInfoOutput implements InfoOutput {

    private static final String SHOT = "%d gun  shot!\n";
    private static final String SHOOTING_BLOCKED = "%d gun Shooting blocked \n";
    private static final String SHOOTING_ALLOWED = "%d gun Can shoot \n";
    private static final String BLADE_POSITION = "Blade %d in position = %f\n";
    private static final String PROPELLER_BLADES_COUNT_EXCESS =
            "Propeller blades count excess: %d. " +
                    "Propeller blades count will be set to %d.\n";
    private static final String GUNS_COUNT_EXCESS =
            "Max guns count excess: %d. Guns count will be set to %d.\n";
    private static final String UNEXPECTED_SHOT =
            "Something went wrong! Gun wasn't enabled to shoot!\n";

    @Override
    public void showShot(int gunIndex, boolean canShoot) {
        if (canShoot) {
            System.out.printf(SHOT, gunIndex);
        } else {
            System.err.printf(UNEXPECTED_SHOT);
        }
    }

    @Override
    public void showCanShoot(int gunIndex, boolean canShoot) {
        if (canShoot) {
            System.out.printf(SHOOTING_ALLOWED, gunIndex);
        } else {
            System.out.printf(SHOOTING_BLOCKED, gunIndex);
        }
    }

    @Override
    public void showGunsCountExcess(int enteredCount, int maxCount) {
        System.out.printf(GUNS_COUNT_EXCESS, enteredCount, maxCount);
    }

    @Override
    public void showPropellerBladesCountExcess(int enteredCount, int maxCount) {
        System.out.printf(PROPELLER_BLADES_COUNT_EXCESS, enteredCount,
                maxCount);
    }

    @Override
    public void showPropellerBladesPositions(double... blades) {
        for (int i = 0; i < blades.length; i++) {
            System.out.format(BLADE_POSITION, i + 1, blades[i]);
        }
    }

}
