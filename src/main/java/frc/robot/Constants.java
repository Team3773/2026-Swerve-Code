package frc.robot;

public final class Constants {
    //Make classes for each subsystem
    //TODO: put in the correct CAN IDs when we actually hook these up
    public static final class IntakeConstants {
        //CAN IDs
        public static final int intakePivotID = 23;
        public static final int intakeGrabID = 20;
        public static final int intakeSecondAgitatorID = 22;
        public static final int intakePivotEncoderID = 21;
        public static final double intakePivotEncoderMagneticOffset = 0;
    }

    public static final class ShooterConstants {
        //CAN IDs
        public static final int shooterFeedID = 14;
        public static final int shooterSecondFeedID = 15;
        public static final int shooterShooterID = 16;
        public static final int shooterShooterFollowingID = 17;
        //Speeds
        public static final double shooterSpeed = 1.0;
        public static final double shooterReverseSpeed = -1.0;
    }

    public static final class ClimbConstants {
        //CAN IDs
        public static final int winchID = 24;
        public static final int armID = 19;
        public static final int armEncoderID = 18;
        //Speeds
        public static final double winchMotorSpeed = 0.5;
    }
}