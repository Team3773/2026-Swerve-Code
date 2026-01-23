package frc.robot;

public final class Constants {
    //Make classes for each subsystem
    //TODO: put in the correct CAN IDs when we actually hook these up
    public static final class IntakeConstants {
        public static final int intakePivotID = 20;
        public static final int intakeGrabID = 21;
        public static final int intakeSecondAgitatorID = 22;
    }

    public static final class ShooterConstants {
        public static final int shooterFeedID = 23;
        public static final int shooterShooterID = 24;
        public static final double shooterSpeed = 0.5; //TODO: Change this to 1.0 when ready
    }

    public static final class ClimbConstants {
        public static final int winchID = 25;
    }
}
