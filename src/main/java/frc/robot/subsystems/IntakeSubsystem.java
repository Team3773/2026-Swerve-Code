package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {

    private SparkMax intakePivotMotor; //CTRE Minion, used for raising/lowering the intake axle
    private TalonFX intakeGrabMotor; //Kraken X44, used for driving the intake wheels and also powering the Hopper Agitator
    private TalonFX intakeSecondAgitator; //Another X44 used exclusively for the agitator near the Shooter

    private SparkClosedLoopController sparkClosedLoopController;
    private RelativeEncoder relativeEncoder;

    private double currentSetpoint = 0.0;

    public IntakeSubsystem() {
        intakePivotMotor = new SparkMax(Constants.IntakeConstants.intakePivotID, MotorType.kBrushless);
        intakeGrabMotor = new TalonFX(Constants.IntakeConstants.intakeGrabID, "rio");
        intakeSecondAgitator = new TalonFX(Constants.IntakeConstants.intakeSecondAgitatorID, "rio");

        sparkClosedLoopController = intakePivotMotor.getClosedLoopController();
        relativeEncoder = intakePivotMotor.getEncoder();

        SparkMaxConfig intakePivotConfig = new SparkMaxConfig();
        SparkMaxConfig globalConfig = new SparkMaxConfig();

        relativeEncoder = intakePivotMotor.getEncoder();

        intakePivotConfig.encoder
      .positionConversionFactor(1.0/324.0)
      .velocityConversionFactor(1.0/324.0);

        intakePivotConfig.closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        // Set PID values for position control. We don't need to pass a closed loop
        // slot, as it will default to slot 0.
        .p(1)
        .i(0)
        .d(0)
        .outputRange(-1, 1)
        // Set PID values for velocity control in slot 1
        .p(1, ClosedLoopSlot.kSlot1)
        .i(0, ClosedLoopSlot.kSlot1)
        .d(0, ClosedLoopSlot.kSlot1)
        .velocityFF(1.0 / 5767, ClosedLoopSlot.kSlot1)
        .outputRange(-1, 1, ClosedLoopSlot.kSlot1);


         globalConfig
            .smartCurrentLimit(50)
            .idleMode(IdleMode.kBrake);
    
        intakePivotConfig.apply(globalConfig);

        intakePivotMotor.configure(intakePivotConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        // Initialize dashboard values
        SmartDashboard.setDefaultNumber("Intake Pivot Target Position", 0);
        SmartDashboard.setDefaultNumber("Intake Pivot Target Velocity", 0);
        
        
    }

    public void runIntakeGrabber(boolean leftTriggerPressed) {
        
        if (leftTriggerPressed) {
            intakeGrabMotor.set(Constants.IntakeConstants.intakeGrabSpeed);
            intakeSecondAgitator.set(Constants.IntakeConstants.intakeSecondAgitatorSpeed);
        }
        else {
            intakeGrabMotor.set(0.0);
            intakeSecondAgitator.set(0.0);
        }
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("Intake Pivot Setpoint", currentSetpoint);
        SmartDashboard.putNumber("Intake Pivot Position", getCurrentPosition());
        SmartDashboard.putNumber("Intake Pivot Velocity", relativeEncoder.getVelocity());
    }

    public void resetEncoder() {
        relativeEncoder.setPosition(0.0);
    }

    public void incrementPosition() {
        currentSetpoint += Constants.IntakeConstants.intakePivotIncrement;
        goToPosition(currentSetpoint);
    }
    
    public void decrementPosition() {
        currentSetpoint -= Constants.IntakeConstants.intakePivotIncrement;
        goToPosition(currentSetpoint);
    }

    public void goToPosition(double value) {
        currentSetpoint = value;
        sparkClosedLoopController.setSetpoint(value, ControlType.kPosition, ClosedLoopSlot.kSlot1);
    }

    public double getCurrentPosition() {
        return relativeEncoder.getPosition();
    }

    public void stopMotor() {
        intakePivotMotor.stopMotor();
    }
}

