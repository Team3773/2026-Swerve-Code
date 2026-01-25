package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;

public class FuelShooterSubsystem extends SubsystemBase {

    private SparkMax shooterFeedMotor; //Wheel
    private SparkMax shooterSecondFeedMotor; //Stars. Also, THIS IS THE FOURTH MOTOR ON THE SHOOTER, STOP PUTTING MORE!
    private SparkFlex shooterMotor;
    private SparkFlex shooterFollowingMotor;

    public FuelShooterSubsystem() {

        shooterFeedMotor = new SparkMax(Constants.ShooterConstants.shooterFeedID, MotorType.kBrushless);
        shooterSecondFeedMotor = new SparkMax(Constants.ShooterConstants.shooterSecondFeedID, MotorType.kBrushless);
        shooterMotor = new SparkFlex(Constants.ShooterConstants.shooterShooterID, MotorType.kBrushless);
        shooterFollowingMotor = new SparkFlex(Constants.ShooterConstants.shooterShooterFollowingID, MotorType.kBrushless);

    } 

    public void motorControl(boolean rightTriggerPressed) { //This probably needs to be rewritten
        
        if (rightTriggerPressed) {
            shooterFeedMotor.set(ShooterConstants.shooterSpeed);
            shooterSecondFeedMotor.set(ShooterConstants.shooterReverseSpeed); //TODO: Test direction
            shooterMotor.set(ShooterConstants.shooterSpeed);
        }
        else {
            shooterFeedMotor.set(0.0);
            shooterSecondFeedMotor.set(0.0);
            shooterMotor.set(0.0);
        }
    }
    public void runMotor(double speed) {
        shooterFeedMotor.set(speed);
        shooterSecondFeedMotor.set(-speed);
        shooterMotor.set(speed);
    }
    
}
