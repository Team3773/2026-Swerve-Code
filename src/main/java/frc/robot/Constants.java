package frc.robot;

public final class Constants {
    //Make classes for each subsystem
    //TODO: put in the correct CAN IDs when we actually hook these up
    public static final class IntakeConstants {
        public static final int intakePivotID = 20;
        public static final int intakeAxleID = 21;
    }

    public static final class ShooterConstants {
        public static final int shooterID = 22;
    }

    public static final class ClimbConstants {
        public static final int winchID = 23;
    }

    public static final class AutoConstants {
        public static final double intakeAxleSpeed = 0.5; // Speed to run the intake axle during auto
        public static final double shooterSpeed = 0.5; // Speed to run the shooter during auto
        public static final int autoShootDuration = 15; // Duration to run the shooter and intake during auto, in seconds
    }
}
