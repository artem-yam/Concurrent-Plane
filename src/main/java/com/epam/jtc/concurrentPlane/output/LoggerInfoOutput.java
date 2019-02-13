package com.epam.jtc.concurrentPlane.output;

import org.apache.log4j.Logger;

public class LoggerInfoOutput implements InfoOutput {

    private static final String SHOT = "%d gun shot!";
    private static final String SHOOTING_BLOCKED = "%d gun Shooting blocked ";
    private static final String SHOOTING_ALLOWED = "%d gun Can shoot ";
    private static final String BLADE_POSITION = "Blade %d in position = %f";
    private static final String PROPELLER_BLADES_COUNT_EXCESS =
            "Propeller blades count excess: %d. " +
                    "Propeller blades count will be set to %d.";
    private static final String GUNS_COUNT_EXCESS =
            "Max guns count excess: %d. Guns count will be set to %d.";
    private static final String UNEXPECTED_SHOT =
            "Something went wrong! Gun wasn't enabled to shoot!";
    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void showShot(int gunIndex, boolean canShoot) {
        if (canShoot) {
            logger.info(String.format(SHOT, gunIndex));
        } else {
            logger.error(UNEXPECTED_SHOT);
        }
    }

    @Override
    public void showCanShoot(int gunIndex, boolean canShoot) {
        if (canShoot) {
            logger.info(String.format(SHOOTING_ALLOWED, gunIndex));
        } else {
            logger.info(String.format(SHOOTING_BLOCKED, gunIndex));
        }
    }

    @Override
    public void showGunsCountExcess(int enteredCount, int maxCount) {
        logger.info(String.format(GUNS_COUNT_EXCESS, enteredCount, maxCount));
    }

    @Override
    public void showPropellerBladesCountExcess(int enteredCount, int maxCount) {
        logger.info(String.format(PROPELLER_BLADES_COUNT_EXCESS, enteredCount,
                maxCount));
    }

    @Override
    public void showPropellerBladesPositions(double... blades) {
        for (int i = 0; i < blades.length; i++) {
            logger.info(String.format(BLADE_POSITION, i + 1, blades[i]));
        }
    }

}
