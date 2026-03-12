package frc.robot;

public final class Constants {
    //Make classes for each subsystem
    //TODO: put in the correct CAN IDs when we actually hook these up
    public static final class IntakeConstants {
        //CAN IDs
        public static final int intakePivotID = 23;
        public static final int intakeGrabID = 20;
        public static final int intakePivotEncoderID = 21;
        public static final double intakePivotEncoderMagneticOffset = 0;
        //Speeds
        public static final double intakeSpeed = -0.85;
    }

    public static final class ShooterConstants {
        //CAN IDs
        public static final int shooterFeedID = 14;
        public static final int shooterSecondFeedID = 15;
        public static final int shooterShooterID = 16;
        public static final int shooterShooterFollowingID = 17;
        public static final int shooterAgitatorID = 22;
        //Speeds
        public static final double shooterSpeed = -0.8;
        public static final double shooterReverseSpeed = 0.8;
        public static final double agitatorSpeed = -0.75;
    }

    public static final class ClimbConstants {
        //CAN IDs
        public static final int winchID = 24;
        public static final int armID = 19;
        public static final int armEncoderID = 18;
        public static final double armEncoderMagneticOffset = 0;
        //Speeds
        public static final double winchMotorSpeed = 0.5;
    }

    public static final class CANdleConstants {
        //CAN IDs
        public static final int CANdleID = 30;
        //LED Count
        public static final int LEDcount = 500;
        //Brightness
        public static final double brightness = 1.0;
    }
}