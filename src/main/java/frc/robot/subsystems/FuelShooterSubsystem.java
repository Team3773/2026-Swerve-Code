package frc.robot.subsystems;



import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;

public class FuelShooterSubsystem extends SubsystemBase {

    private SparkMax shooterFeedMotor; //Wheel
    private SparkMax shooterSecondFeedMotor; //Stars. Also, THIS IS THE FOURTH MOTOR ON THE SHOOTER, STOP PUTTING MORE!
    private SparkFlex shooterMotor;
    private SparkFlex shooterFollowingMotor;

    public FuelShooterSubsystem() {
        // Feed motors
        shooterFeedMotor = new SparkMax(Constants.ShooterConstants.shooterFeedID, MotorType.kBrushless);
        shooterSecondFeedMotor = new SparkMax(Constants.ShooterConstants.shooterSecondFeedID, MotorType.kBrushless);
        // Main shooter motor and its follower
        shooterMotor = new SparkFlex(Constants.ShooterConstants.shooterShooterID, MotorType.kBrushless);
        shooterFollowingMotor = new SparkFlex(Constants.ShooterConstants.shooterShooterFollowingID, MotorType.kBrushless);

    } 

    public void motorControl(boolean rightTriggerPressed) { //This probably needs to be rewritten
        
        if (rightTriggerPressed) {
            // Feed motors
            shooterFeedMotor.set(ShooterConstants.shooterSpeed);
            shooterSecondFeedMotor.set(ShooterConstants.shooterReverseSpeed); //TODO: Test direction
            // Main shooter motors
            shooterFollowingMotor.set(ShooterConstants.shooterReverseSpeed);
            shooterMotor.set(ShooterConstants.shooterSpeed);
        }
        else {
            // Feed motors
            shooterFeedMotor.set(0.0);
            shooterSecondFeedMotor.set(0.0);
            // Main shooter motors
            shooterFollowingMotor.set(0.0);
            shooterMotor.set(0.0);
        }
    }
    public void runMotor(double speed) {
        // Feed motors
        shooterFeedMotor.set(speed);
        shooterSecondFeedMotor.set(-speed);
        // Main shooter motors
        shooterFollowingMotor.set(-speed);
        shooterMotor.set(speed);
    }
    
}
