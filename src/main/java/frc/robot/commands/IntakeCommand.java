// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakeCommand extends Command {
  /** Creates a new IntakeCommand. */
  
  IntakeSubsystem intakeSubsystem;
  BooleanSupplier triggerSupplier, intakeIn, intakeOut, intakeReverse;

  public static double startPos = -0.07;
  public static double deployedPos = -0.32;

  public IntakeCommand(IntakeSubsystem intakeSubsystem, BooleanSupplier triggerSupplier, BooleanSupplier intakeIn, BooleanSupplier intakeOut, BooleanSupplier intakeReverse) {
    this.intakeSubsystem = intakeSubsystem;
    this.triggerSupplier = triggerSupplier; //LT
    this.intakeIn = intakeIn; //DPad Up
    this.intakeOut = intakeOut; //DPad Down
    this.intakeReverse = intakeReverse;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeSubsystem);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intakeSubsystem.runIntakeGrabber(this.triggerSupplier.getAsBoolean(), this.intakeReverse.getAsBoolean());
    
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
