// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANdleSubsystem;
import frc.robot.subsystems.FuelShooterSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterCommand extends Command {
  /** Creates a new ShooterCommand. */
  CANdleSubsystem candle;
  FuelShooterSubsystem fuelShooterSubsystem;
  BooleanSupplier triggerSupplier;

  public ShooterCommand(FuelShooterSubsystem fuelShooterSubsystem, BooleanSupplier trigger) {
    /* This command will run the shooter and the intake grab 
    (for the agitator) at the same time when the RT is fully depressed */

    // Use addRequirements() here to declare subsystem dependencies.
    this.fuelShooterSubsystem = fuelShooterSubsystem;
    this.triggerSupplier = trigger;
    this.candle = candle;

    addRequirements(fuelShooterSubsystem, candle);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // pass only the current trigger state; subsystem tracks timing
    fuelShooterSubsystem.motorControl(this.triggerSupplier.getAsBoolean());
    candle.setCANdleColor(0,255,0);
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    fuelShooterSubsystem.runMotor(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
