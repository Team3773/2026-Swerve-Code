package frc.robot.subsystems;


import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

public class ClimbSubsystem extends SubsystemBase {

    // ── Motors ────────────────────────────────────────────────────────────────
    private final SparkMax winchMotor;
    private final SparkMax armMotor;

    // ── Sensors ───────────────────────────────────────────────────────────────
    private final CANcoder armCancoder;

    // ── Software PID (runs in periodic, uses CANcoder as feedback) ────────────
    private final PIDController armPID;

    // Tune these! Start with a small kP and work up.
    private static final double kP = 5.0;
    private static final double kD = 0.1;
    // kI is usually not needed for position control and can cause windup — leave at 0
    private static final double kI = 0.0;

    // Output clamp — limits how hard the arm can drive (0.0–1.0)
    private static final double MAX_ARM_OUTPUT = 0.5;

    // Deadband — stop correcting if error is within this range (in rotations)
    private static final double POSITION_TOLERANCE = 0.005; // ~1.8 degrees

    // ── State ─────────────────────────────────────────────────────────────────
    private final double winchMotorSpeed = Constants.ClimbConstants.winchMotorSpeed;
    private double requestedPos = 0.0;
    private boolean closedLoopEnabled = false;

    // ─────────────────────────────────────────────────────────────────────────
    public ClimbSubsystem() {

        // ── Winch motor ───────────────────────────────────────────────────────
        winchMotor = new SparkMax(Constants.ClimbConstants.winchID, MotorType.kBrushless);
        SparkMaxConfig winchConfig = new SparkMaxConfig();
        winchConfig
                .smartCurrentLimit(50)
                .idleMode(IdleMode.kBrake);
        winchMotor.configure(winchConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        // ── Arm motor (open-loop output only — PID runs in software) ──────────
        armMotor = new SparkMax(Constants.ClimbConstants.armID, MotorType.kBrushless);
        SparkMaxConfig armConfig = new SparkMaxConfig();
        armConfig
                .smartCurrentLimit(80)
                .idleMode(IdleMode.kBrake);
        armMotor.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        // ── CANcoder ──────────────────────────────────────────────────────────
        armCancoder = new CANcoder(Constants.ClimbConstants.armEncoderID);
        CANcoderConfiguration cc_cfg = new CANcoderConfiguration();
        cc_cfg.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 0.5;
        cc_cfg.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
        // ✅ Fixed: use the correct ClimbConstants offset, not IntakeConstants
        cc_cfg.MagnetSensor.MagnetOffset = Constants.ClimbConstants.armEncoderMagneticOffset;
        armCancoder.getConfigurator().apply(cc_cfg);

        // ── Software PID controller ───────────────────────────────────────────
        armPID = new PIDController(kP, kI, kD);
        armPID.setTolerance(POSITION_TOLERANCE);
        // Continuous input handles the wrap-around at ±0.5 rotations
        // (matches AbsoluteSensorDiscontinuityPoint = 0.5)
        armPID.enableContinuousInput(-0.5, 0.5);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Winch controls (unchanged)
    // ─────────────────────────────────────────────────────────────────────────

    public void runWinch() {
        winchMotor.set(winchMotorSpeed);
    }

    public void stopWinch() {
        winchMotor.set(0.0);
    }

    public void releaseWinch() {
        winchMotor.set(-winchMotorSpeed);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Arm controls
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Command the arm to move to an absolute position (in CANcoder rotations).
     * The software PID loop in periodic() will drive the motor toward this setpoint.
     */
    public void armGoToPosition(double position) {
        requestedPos = position;
        armPID.setSetpoint(position);
        closedLoopEnabled = true;
    }

    /**
     * Stop the arm and disable the PID loop.
     * Call this when a command ends or the arm should hold still open-loop.
     */
    public void stopArm() {
        closedLoopEnabled = false;
        armMotor.set(0.0);
        armPID.reset();
    }

    /**
     * Returns true when the arm is within tolerance of the requested position.
     * Use this as a command end condition: .until(climbSubsystem::armAtSetpoint)
     */
    public boolean armAtSetpoint() {
        return armPID.atSetpoint();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Periodic — software PID runs here every 20ms
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public void periodic() {
        double currentPos = armCancoder.getAbsolutePosition().getValueAsDouble();
        double currentVel = armCancoder.getVelocity().getValueAsDouble();

        if (closedLoopEnabled) {
            // Calculate PID output using the live CANcoder position
            double output = armPID.calculate(currentPos);

            // Clamp to safe output range
            output = MathUtil.clamp(output, -MAX_ARM_OUTPUT, MAX_ARM_OUTPUT);

            armMotor.set(output);
        }

        // ── SmartDashboard telemetry ──────────────────────────────────────────
        SmartDashboard.putNumber("Climb/Requested Pos",    requestedPos);
        SmartDashboard.putNumber("Climb/CANcoder Pos",     currentPos);
        SmartDashboard.putNumber("Climb/CANcoder Vel",     currentVel);
        SmartDashboard.putNumber("Climb/PID Error",        armPID.getError());
        SmartDashboard.putBoolean("Climb/At Setpoint",     armPID.atSetpoint());
        SmartDashboard.putBoolean("Climb/Closed Loop On",  closedLoopEnabled);
    }
}