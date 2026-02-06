// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimbSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ClimbCommand extends Command {
  /** Creates a new ClimbCommand. */
  BooleanSupplier winchIn, winchRelease, climbStartPos, climbArmReady, climbPull;
  ClimbSubsystem climbSubsystem;
  
  double startPosition = 0.0; //position in rotations
  double readyPosition = -0.35;
  double pullPosition = 0.25;

  public ClimbCommand(ClimbSubsystem climbSubsystem, BooleanSupplier winchIn, 
  BooleanSupplier winchRelease, BooleanSupplier climbStartPos, BooleanSupplier climbArmReady, 
  BooleanSupplier climbPull) {
    this.climbSubsystem = climbSubsystem;
    this.winchIn = winchIn; //Start (Driver)
    this.winchRelease = winchRelease; //Select/Back (Driver)

    this.climbStartPos = climbStartPos; //Y (Driver)
    this.climbArmReady = climbArmReady; //X (Driver)
    this.climbPull = climbPull; //A (Driver)
    addRequirements(climbSubsystem);
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
     if (climbPull.getAsBoolean()) {
      // Go to Climb Pull position
      this.climbSubsystem.armGoToPosition(pullPosition);
    } else if (climbArmReady.getAsBoolean()) {
      // Go to Climb Hook Position
      this.climbSubsystem.armGoToPosition(readyPosition);
    } else if (climbStartPos.getAsBoolean()) { //This one will be removed later
      //Go to Zero Position
      this.climbSubsystem.armGoToPosition(startPosition);
    }

     if (winchIn.getAsBoolean()) {
      this.climbSubsystem.runWinch();
    } else if (winchRelease.getAsBoolean()) {
      this.climbSubsystem.releaseWinch();
    } else {
      this.climbSubsystem.stopWinch();
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climbSubsystem.stopWinch();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
