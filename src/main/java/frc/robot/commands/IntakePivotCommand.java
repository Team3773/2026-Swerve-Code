// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakePivotCommand extends Command {
  /** Creates a new IntakePivotCommand. */
  IntakeSubsystem intakeSubsystem;
  BooleanSupplier intakeIn, intakeOut;
  double startPos = 0.0;
  double deployedPos = 0.1;

  public IntakePivotCommand(IntakeSubsystem intakeSubsystem, BooleanSupplier intakeIn, BooleanSupplier intakeOut) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.intakeSubsystem = intakeSubsystem;
    this.intakeIn = intakeIn; //DPad Up
    this.intakeOut = intakeOut; //DPad Down
    addRequirements(intakeSubsystem);

    /* The plan for the intake is to immediately extend when the match starts and also start a timer
      at the same time, so when there's about 20 seconds left in the match, the intake automatically
      retracts and the climb arm extends. This will likely involve using a ParallelCommand.
    */
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (intakeOut.getAsBoolean()) {
      //Put the intake out
      this.intakeSubsystem.intakeGoToPosition(deployedPos);
    }
    else if (intakeIn.getAsBoolean()) {
      //Put the intake in
      this.intakeSubsystem.intakeGoToPosition(startPos);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
