package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.ExternalFeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimbSubsystem extends SubsystemBase {

    private SparkMax winchMotor;
    private SparkMax armMotor;
    private CANcoder armCancoder; //CANcoder

    private SparkClosedLoopController closedLoopController;
    private RelativeEncoder encoder;
    
    private final double winchMotorSpeed = Constants.ClimbConstants.winchMotorSpeed;
    private double requestedPos;

    public ClimbSubsystem() {
        //Configure the winch motor
        winchMotor = new SparkMax(Constants.ClimbConstants.winchID, MotorType.kBrushless);
        armMotor = new SparkMax(Constants.ClimbConstants.armID, MotorType.kBrushless);
        armCancoder = new CANcoder(Constants.IntakeConstants.intakePivotEncoderID);

        closedLoopController = winchMotor.getClosedLoopController();
    
        encoder = winchMotor.getEncoder();

        SparkMaxConfig winchConfig = new SparkMaxConfig();

        /* 
        winchConfig.encoder
        .positionConversionFactor(1.0/324.0)
        .velocityConversionFactor(1.0/324.0);

        winchConfig.closedLoop
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

        */
        winchConfig
                .smartCurrentLimit(50)
                .idleMode(IdleMode.kBrake);
        
        winchConfig.apply(winchConfig);
        
        winchMotor.configure(winchConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        //Configure the encoder
        CANcoderConfiguration cc_cfg = new CANcoderConfiguration();
        cc_cfg.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 0.5;
        cc_cfg.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
        cc_cfg.MagnetSensor.MagnetOffset = Constants.IntakeConstants.intakePivotEncoderMagneticOffset;
        armCancoder.getConfigurator().apply(cc_cfg);

        //Configure the arm motor
        closedLoopController = armMotor.getClosedLoopController();
    
        encoder = armMotor.getEncoder();

        SparkMaxConfig armConfig = new SparkMaxConfig();

        armConfig.encoder
        .positionConversionFactor(1.0/324.0)
        .velocityConversionFactor(1.0/324.0);

        armConfig.closedLoop
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


        armConfig
                .smartCurrentLimit(50)
                .idleMode(IdleMode.kBrake);
        
        armConfig.apply(armConfig);
        
        armMotor.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        /*armMotor.configSelectedFeedbackSensor(      //This doesn't work because it isn't defined for TalonFXS
                  FeedbackDevice.Analog,				// Local Feedback Source
									Constants.ClimbConstants.winchID,					// PID Slot for Source [0, 1]
									0.100);					// Configuration Timeout
        */
        // in init function, set slot 0 gains
    }

public void runWinch(){
    winchMotor.set(winchMotorSpeed);
  }
public void stopWinch(){
    winchMotor.set(0.0);
  }
public void releaseWinch(){
    winchMotor.set(-winchMotorSpeed);
  }
  public void armGoToPosition(double position) {
    //just gonna yoink the pos for SmartDashboard
    requestedPos = position;
    //Set the setpoint
    closedLoopController.setSetpoint(position, ControlType.kPosition, ClosedLoopSlot.kSlot1);
  }

  public void periodic() {
        SmartDashboard.putNumber("Climb Requested Pos", 
            requestedPos);
        SmartDashboard.putNumber("Climb Arm CANcoder Pos", 
            armCancoder.getAbsolutePosition().getValueAsDouble());
        SmartDashboard.putNumber("Climb Arm CANcoder Vel", 
            armCancoder.getVelocity().getValueAsDouble());
  }
}


