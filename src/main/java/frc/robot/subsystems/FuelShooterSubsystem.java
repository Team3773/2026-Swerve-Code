package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;

public class FuelShooterSubsystem extends SubsystemBase {

    private SparkFlex shooterFeedMotor; //Wheel
    private SparkFlex shooterSecondFeedMotor; //Stars. Also, THIS IS THE FOURTH MOTOR ON THE SHOOTER, STOP PUTTING MORE!
    private SparkFlex shooterMotor;
    private SparkFlex shooterFollowingMotor;

    public FuelShooterSubsystem() {

        shooterFeedMotor = new SparkFlex(Constants.ShooterConstants.shooterFeedID, MotorType.kBrushless);
        shooterSecondFeedMotor = new SparkFlex(Constants.ShooterConstants.shooterSecondFeedID, MotorType.kBrushless);
        shooterMotor = new SparkFlex(Constants.ShooterConstants.shooterShooterID, MotorType.kBrushless);
        shooterFollowingMotor = new SparkFlex(Constants.ShooterConstants.shooterShooterFollowingID, MotorType.kBrushless);

        SparkFlexConfig globalConfig = new SparkFlexConfig();
        SparkFlexConfig leaderConfig = new SparkFlexConfig();
        SparkFlexConfig followerConfig = new SparkFlexConfig();

        globalConfig
            .smartCurrentLimit(80)
            .idleMode(IdleMode.kCoast);
        
        leaderConfig.apply(globalConfig);
        followerConfig.apply(globalConfig).follow(shooterMotor);
        
        shooterMotor.configure(leaderConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
        shooterFollowingMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    } 

    public void motorControl(boolean rightTriggerPressed) { //This probably needs to be rewritten
        
        if (rightTriggerPressed) {
            shooterFeedMotor.set(ShooterConstants.shooterSpeed);
            shooterSecondFeedMotor.set(ShooterConstants.shooterReverseSpeed); //TODO: Test direction
            shooterMotor.set(ShooterConstants.shooterSpeed);
            System.out.println("RT pressed, running shooter motors!");
        }
        else {
            shooterFeedMotor.set(0.0);
            shooterSecondFeedMotor.set(0.0);
            shooterMotor.set(0.0);
            System.out.println("RT is NOT pressed, motors stopped!");
        }
    }
    public void runMotor(double speed) {
        shooterFeedMotor.set(speed);
        shooterSecondFeedMotor.set(-speed);
        shooterMotor.set(speed);
        System.out.println("Shooter motors' speed set to" + speed);
    }
    
}
