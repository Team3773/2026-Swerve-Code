package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.MotorArrangementValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.ShooterConstants;

public class FuelShooterSubsystem extends SubsystemBase {

    private SparkFlex shooterFeedMotor; //Wheel
    private SparkMax shooterSecondFeedMotor; //Another Wheel
    private SparkFlex shooterMotor;
    private SparkFlex shooterFollowingMotor;
    private TalonFXS intakeSecondAgitator; //Now the Spindexer

    // keep timing state in the subsystem
    private boolean triggerWasPressed = false;
    private double triggerStartTime = 0.0;
    private double variableStartTime = 0.0;

    //Shooter telemetry variables
    double shooterVelocity = shooterMotor.getAbsoluteEncoder().getVelocity();
    double shooterFeedV = shooterFeedMotor.getAbsoluteEncoder().getVelocity(); //These three can break the code if the motors aren't connected
    double shooterSecondFeedV = shooterSecondFeedMotor.getAbsoluteEncoder().getVelocity();
    boolean readyToShoot = false;
    boolean shooterJammed = false;
    String shooterStatus = "FuelShooterSubsystem Init";

    public FuelShooterSubsystem() {

        shooterFeedMotor = new SparkFlex(Constants.ShooterConstants.shooterFeedID, MotorType.kBrushless);
        shooterSecondFeedMotor = new SparkMax(Constants.ShooterConstants.shooterSecondFeedID, MotorType.kBrushless);
        shooterMotor = new SparkFlex(Constants.ShooterConstants.shooterShooterID, MotorType.kBrushless);
        shooterFollowingMotor = new SparkFlex(Constants.ShooterConstants.shooterShooterFollowingID, MotorType.kBrushless);
        intakeSecondAgitator = new TalonFXS(Constants.ShooterConstants.shooterAgitatorID);

        TalonFXSConfiguration talonFXSConfiguration = new TalonFXSConfiguration();
        talonFXSConfiguration.Commutation.MotorArrangement = MotorArrangementValue.Minion_JST;
        talonFXSConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        intakeSecondAgitator.getConfigurator().apply(talonFXSConfiguration);

        SparkMaxConfig globalConfig = new SparkMaxConfig();
        /*SparkFlexConfig leaderConfig = new SparkFlexConfig();
        SparkFlexConfig followerConfig = new SparkFlexConfig();*/

        globalConfig
            .smartCurrentLimit(25)
            .idleMode(IdleMode.kCoast);

        shooterFeedMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
        shooterSecondFeedMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
        
        /*leaderConfig.apply(globalConfig);
        followerConfig.apply(globalConfig).follow(shooterMotor);
        
        shooterMotor.configure(leaderConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
        shooterFollowingMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);*/
    } 

    // now the subsystem tracks how long the trigger has been held
    public void motorControl(boolean rightTriggerPressed, boolean rightBumperPressed, boolean startPressed, boolean backPressed) {
        if (rightTriggerPressed) {
            // started pressing this cycle
            if (!triggerWasPressed) {
                triggerStartTime = Timer.getFPGATimestamp();
                triggerWasPressed = true;
                System.out.println("RT pressed, starting shooter motors");
            }

            // keep shooter motors running while trigger held
            shooterFeedMotor.set(ShooterConstants.shooterFeedSpeed);
            shooterSecondFeedMotor.set(-ShooterConstants.shooterFeedSpeed);

            if (rightBumperPressed && startPressed) { //Max (+RB+Start). Not needed in matches, given how far the fuel is shot
                shooterMotor.set(ShooterConstants.shooterMaxReverseSpeed);
                shooterFollowingMotor.set(ShooterConstants.shooterMaxSpeed);
                System.out.println("MAX SPEED ACTIVATED!");
            } else if (rightBumperPressed) { //Fast (+RB)
                shooterMotor.set(ShooterConstants.shooterFastReverseSpeed);
                shooterFollowingMotor.set(ShooterConstants.shooterFastSpeed);
                System.out.println("Fast speed activated!");
            }
            else { //Normal
                shooterMotor.set(ShooterConstants.shooterReverseSpeed);
                shooterFollowingMotor.set(ShooterConstants.shooterSpeed);
            }

            if (backPressed) {
                intakeSecondAgitator.set(-ShooterConstants.agitatorSpeed);
            }
            // After 1 second, start the agitator.
            // This is to ensure that the shooter has ramped up to full speed.
            // TODO: Consider making this based on when the shooter is ready
            // Telemetry code must be tested first
            else if (Timer.getFPGATimestamp() - triggerStartTime >= 1.0) {
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
        System.out.println("Shooter motors' speed set to" + speed);
    }
    
    public void runAgitator(double speed) {
        intakeSecondAgitator.set(speed);
        System.out.println("Agitator motor's speed set to" + speed);
    }

    //Speed checks
    public void shooterStatus() {
        /*
         * --------------------
         * Shooter Motor Layout
         * --------------------
         *
         *       ____________
         *       |          |
         *       |       O  |  Left = 17: Shooter Follower | Right = 16: Shooter (Both FLEX)
         *        \      O  |  Right = 15: Second Feed (MAX)
         *         |        /
         *        / O      /   Left = 14: Feed (FLEX)
         *       |_________|
         *         FRONT TOWARDS
         *       ---------------->
         *             ENEMY
         *
         * --------------------
         * Possible conditions
         * --------------------
         */
         //Shooter is at full speed and the trigger is pressed
        if (shooterVelocity >= 1.0 && triggerWasPressed) { 
            readyToShoot = true;
            shooterJammed = false;
            shooterStatus = "Shooting";
        }
        //Shooter is not at a significant speed and the trigger is held and the Spindexer is running 
        else if (shooterVelocity <= 0.2 && triggerWasPressed && Timer.getFPGATimestamp() - triggerStartTime >= 1.0) {
            readyToShoot = false;
            shooterJammed = true;
            shooterStatus = "!?!? Shooter jammed !?!?";
        }
        //Either of the Shooter feed motors is not at a significant speed and the trigger is held
        else if ((shooterFeedV <= 1.0 || shooterSecondFeedV <=1.0) && triggerWasPressed 
                  && Timer.getFPGATimestamp() - triggerStartTime <= 1.0) {
            readyToShoot = false;
            shooterJammed = true;
            shooterStatus = "!!! Shooter Feed jammed !!!";
        }
        //Shooter is not up to speed and the trigger is being held and the Spindexer isn't running yet
        else if (shooterVelocity <= 1.0 && triggerWasPressed && Timer.getFPGATimestamp() - triggerStartTime <= 1.0) {
            readyToShoot = false;
            shooterJammed = false;
            shooterStatus = "Spinning up...";
        }
        //Shooter is not at full speed yet and the trigger is held and the Spindexer is running
        else if (shooterVelocity <= 1.0 && triggerWasPressed && Timer.getFPGATimestamp() - triggerStartTime >= 1.0) {
            readyToShoot = false;
            shooterJammed = true;
            shooterStatus = "!!! Shooter has not fully spun up !!!";
        }
        //Shooter is still at significant speed after the trigger has been released
        else if (shooterVelocity >= 1.0 && !triggerWasPressed) {
            readyToShoot = false;
            shooterJammed = false;
            shooterStatus = "Spinning down...";
        }
        //No condition above is met, which likely means the shooter is inactive
        else {
            readyToShoot = false;
            shooterJammed = false;
            shooterStatus = "Shooter inactive";
        }

    }

    //Telemetry (Shooter and Agitator Speed)
    @Override
    public void periodic() { //TODO, DO FIRST: check if thing this works
        //Run shooterStatus to update the telemetry
        shooterStatus();
        //Speeds (RPS)
        SmartDashboard.putNumber("Shooter Vel (rps)", shooterVelocity); 
        SmartDashboard.putNumber("Agitator Vel (rps)", intakeSecondAgitator.getVelocity().getValueAsDouble());
        //Booleans
        SmartDashboard.putBoolean("Trigger was pressed", triggerWasPressed);
        SmartDashboard.putBoolean("Ready to Shoot", readyToShoot);
        SmartDashboard.putBoolean("Shooter Jammed", shooterJammed);
        //Strings
        SmartDashboard.putString("Shooter Status", shooterStatus);
    }
}
