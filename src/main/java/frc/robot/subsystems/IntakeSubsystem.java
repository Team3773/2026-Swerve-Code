package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.MotorArrangementValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.PositionVoltage;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {

    private TalonFX intakePivotMotor;   // Kraken X44, raises/lowers the intake axle
    private CANcoder intakeCancoder;     // CANcoder for absolute pivot position
    private TalonFXS intakeGrabMotor;   // CTRE Minion, drives intake wheels + hopper agitator

    // Plain position closed-loop request — no motion profile, just direct PID.
    private final PositionVoltage m_request = new PositionVoltage(0).withSlot(0);

    private double requestedPos = 0.0;

    public IntakeSubsystem() {
        intakePivotMotor = new TalonFX(Constants.IntakeConstants.intakePivotID, "rio");
        intakeGrabMotor  = new TalonFXS(Constants.IntakeConstants.intakeGrabID, "rio");
        intakeCancoder   = new CANcoder(Constants.IntakeConstants.intakePivotEncoderID);

        // --- TalonFXS (Minion grab motor) ---
        TalonFXSConfiguration talonFXSConfiguration = new TalonFXSConfiguration();
        talonFXSConfiguration.Commutation.MotorArrangement = MotorArrangementValue.Minion_JST;
        talonFXSConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        intakeGrabMotor.getConfigurator().apply(talonFXSConfiguration);

        // --- CANcoder ---
        CANcoderConfiguration cc_cfg = new CANcoderConfiguration();
        cc_cfg.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 0.5;
        cc_cfg.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
        cc_cfg.MagnetSensor.MagnetOffset = Constants.IntakeConstants.intakePivotEncoderMagneticOffset;
        intakeCancoder.getConfigurator().apply(cc_cfg);

        // --- TalonFX (pivot motor) ---
        TalonFXConfiguration fx_cfg = new TalonFXConfiguration();
        fx_cfg.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        fx_cfg.Feedback.FeedbackRemoteSensorID = intakeCancoder.getDeviceID();
        fx_cfg.Feedback.FeedbackSensorSource   = FeedbackSensorSourceValue.SyncCANcoder;
        fx_cfg.Feedback.SensorToMechanismRatio = 1.0;  // Change as needed
        fx_cfg.Feedback.RotorToSensorRatio     = 12.8;

        // PIDs — don't touch unless setpoints aren't being reached.
        // If it won't reach the DOWN setpoint, try increasing kD first.
        // These values were moved into Constants.
        Slot0Configs slot0Configs = new Slot0Configs();
        slot0Configs.kP = Constants.IntakeConstants.intakePivot_kP;
        slot0Configs.kI = Constants.IntakeConstants.intakePivot_kI;
        slot0Configs.kD = Constants.IntakeConstants.intakePivot_kD;

        intakePivotMotor.getConfigurator().apply(fx_cfg);
        intakePivotMotor.getConfigurator().apply(slot0Configs);
    }

    // ---------------------------------------------------------------------------
    // Grabber control
    // ---------------------------------------------------------------------------

    public void runIntakeGrabber(boolean leftTriggerPressed, boolean leftBumperPressed) {
        if (leftTriggerPressed) {
            intakeGrabMotor.set(Constants.IntakeConstants.intakeSpeed);
        } else if (leftBumperPressed) {
            intakeGrabMotor.set(-Constants.IntakeConstants.intakeSpeed);
        } else {
            intakeGrabMotor.set(0.0);
        }
    }

    public void runIntakeGrabberVariable(double speed) {
        intakeGrabMotor.set(speed);
    }

    // ---------------------------------------------------------------------------
    // Pivot position control
    // ---------------------------------------------------------------------------

    /**
     * Sends a direct position PID request to the pivot motor.
     * The motor controller's onboard PID loop handles the rest — no profile math here.
     */
    public void intakeGoToPosition(double position) {
        requestedPos = position;
        intakePivotMotor.setControl(m_request.withPosition(position));
    }

    // ---------------------------------------------------------------------------
    // Subsystem periodic
    // ---------------------------------------------------------------------------

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Intake/Requested Pos (rot)",   requestedPos);

        SmartDashboard.putNumber("Intake/CANcoder Pos (rot)",    intakeCancoder.getAbsolutePosition().getValueAsDouble());
        SmartDashboard.putNumber("Intake/CANcoder Vel (rps)",    intakeCancoder.getVelocity().getValueAsDouble());

        SmartDashboard.putNumber("Intake/Pivot Motor Pos (rot)", intakePivotMotor.getPosition().getValueAsDouble());
        SmartDashboard.putNumber("Intake/Pivot Motor Vel (rps)", intakePivotMotor.getVelocity().getValueAsDouble());
    }
}