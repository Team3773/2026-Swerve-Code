package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.ShooterConstants;

public class FuelShooterSubsystem extends SubsystemBase {

    private SparkMax shooterFeedMotor; //Wheel
    private SparkMax shooterSecondFeedMotor; //Stars. Also, THIS IS THE FOURTH MOTOR ON THE SHOOTER, STOP PUTTING MORE!
    private SparkFlex shooterMotor;
    private SparkFlex shooterFollowingMotor;
    private SparkMax intakeSecondAgitator;

    // keep timing state in the subsystem
    private boolean triggerWasPressed = false;
    private double triggerStartTime = 0.0;

    public FuelShooterSubsystem() {

        shooterFeedMotor = new SparkMax(Constants.ShooterConstants.shooterFeedID, MotorType.kBrushless);
        shooterSecondFeedMotor = new SparkMax(Constants.ShooterConstants.shooterSecondFeedID, MotorType.kBrushless);
        shooterMotor = new SparkFlex(Constants.ShooterConstants.shooterShooterID, MotorType.kBrushless);
        shooterFollowingMotor = new SparkFlex(Constants.ShooterConstants.shooterShooterFollowingID, MotorType.kBrushless);
        intakeSecondAgitator = new SparkMax(Constants.ShooterConstants.shooterAgitatorID, MotorType.kBrushless);

        /*SparkFlexConfig globalConfig = new SparkFlexConfig();
        SparkFlexConfig leaderConfig = new SparkFlexConfig();
        SparkFlexConfig followerConfig = new SparkFlexConfig();

        globalConfig
            .smartCurrentLimit(120)
            .idleMode(IdleMode.kCoast);
        
        leaderConfig.apply(globalConfig);
        followerConfig.apply(globalConfig).follow(shooterMotor);
        
        shooterMotor.configure(leaderConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
        shooterFollowingMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);*/
    } 

    // now the subsystem tracks how long the trigger has been held
    public void motorControl(boolean rightTriggerPressed) {
        if (rightTriggerPressed) {
            // started pressing this cycle
            if (!triggerWasPressed) {
                triggerStartTime = Timer.getFPGATimestamp();
                triggerWasPressed = true;
                System.out.println("RT pressed, starting shooter motors");
            }

            // keep shooter motors running while trigger held
            shooterFeedMotor.set(ShooterConstants.shooterSpeed);
            shooterSecondFeedMotor.set(ShooterConstants.shooterReverseSpeed); //TODO: Test direction
            shooterMotor.set(ShooterConstants.shooterReverseSpeed);
            shooterFollowingMotor.set(ShooterConstants.shooterSpeed);

            // after ~2 seconds, start the agitator
            if (Timer.getFPGATimestamp() - triggerStartTime >= 1.0) {
                intakeSecondAgitator.set(ShooterConstants.agitatorSpeed);
            } else {
                intakeSecondAgitator.set(0.0);
            }
        } else {
            // trigger released: reset state and stop everything
            if (triggerWasPressed) {
                System.out.println("RT released, stopping shooter and agitator");
            }
            triggerWasPressed = false;
            triggerStartTime = 0.0;

            shooterFeedMotor.set(0.0);
            shooterSecondFeedMotor.set(0.0);
            shooterMotor.set(0.0);
            shooterFollowingMotor.set(0.0);
            intakeSecondAgitator.set(0.0);
        }
    }

    public void runMotor(double speed) {
        shooterFeedMotor.set(speed);
        shooterSecondFeedMotor.set(-speed);
        shooterMotor.set(-speed);
        shooterFollowingMotor.set(speed);
        System.out.println("Shooter motors' speed set to " + speed);
    }
    
}
