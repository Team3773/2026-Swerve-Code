package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimbSubsystem;

public class ClimbCommand extends Command {

  private final BooleanSupplier winchIn, winchRelease, climbStartPos, climbArmReady, climbPull;
  private final ClimbSubsystem climbSubsystem;

  // Arm positions in CANcoder rotations — tune these to match your robot
  private static final double START_POSITION = -0.25;
  private static final double READY_POSITION =  0.04;
  private static final double PULL_POSITION  = -0.1;

  // Track last commanded position so we don't spam armGoToPosition() every loop
  private double lastRequestedPos = Double.NaN;

  public ClimbCommand(ClimbSubsystem climbSubsystem, BooleanSupplier winchIn,
      BooleanSupplier winchRelease, BooleanSupplier climbStartPos,
      BooleanSupplier climbArmReady, BooleanSupplier climbPull) {

    this.climbSubsystem = climbSubsystem;
    this.winchIn        = winchIn;       // Start (Driver)
    this.winchRelease   = winchRelease;  // Select/Back (Driver)
    this.climbStartPos  = climbStartPos; // Y (Driver)
    this.climbArmReady  = climbArmReady; // X (Driver)
    this.climbPull      = climbPull;     // A (Driver)

    addRequirements(climbSubsystem);
  }

  @Override
  public void initialize() {
    // Reset state so the arm doesn't jump to a stale setpoint on re-schedule
    lastRequestedPos = Double.NaN;
    climbSubsystem.stopArm();
  }

  @Override
  public void execute() {

    // ── Arm position control ────────────────────────────────────────────────
    // Priority: climbPull > climbArmReady > climbStartPos

    double targetPos;

    if (climbPull.getAsBoolean()) {
      targetPos = PULL_POSITION;
    } else if (climbArmReady.getAsBoolean()) {
      targetPos = READY_POSITION;
      climbSubsystem.releaseWinch(); // ← runs winch simultaneously with arm
    } else if (climbStartPos.getAsBoolean()) {
      targetPos = START_POSITION;
      climbSubsystem.runWinch(); // ← runs winch simultaneously with arm
    } else {
      targetPos = Double.NaN; // No button held — don't change setpoint
    }

    if (!Double.isNaN(targetPos) && targetPos != lastRequestedPos) {
      climbSubsystem.armGoToPosition(targetPos);
      lastRequestedPos = targetPos;
    }

    // ── Winch control ───────────────────────────────────────────────────────
    if (winchIn.getAsBoolean()) {
      climbSubsystem.runWinch();
    } else if (winchRelease.getAsBoolean()) {
      climbSubsystem.releaseWinch();
    } else if (!climbArmReady.getAsBoolean() && !climbStartPos.getAsBoolean()) {
      // Only stop winch if neither climbArmReady nor climbStartPos is running it
      climbSubsystem.stopWinch();
    }
  }

  @Override
  public void end(boolean interrupted) {
    // Always stop everything when command ends
    climbSubsystem.stopArm();
    climbSubsystem.stopWinch();
  }

  @Override
  public boolean isFinished() {
    return false; // Runs until interrupted (correct for a driver-controlled command)
  }
}