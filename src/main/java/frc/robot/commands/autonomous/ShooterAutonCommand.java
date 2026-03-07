// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.subsystems.FuelShooterSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterAutonCommand extends Command {
  /** Creates a new ShooterAutonCommand. */
  FuelShooterSubsystem fuelShooterSubsystem;
  private final Timer timer = new Timer();

  public ShooterAutonCommand(FuelShooterSubsystem fuelShooterSubsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.fuelShooterSubsystem = fuelShooterSubsystem;

    addRequirements(fuelShooterSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timer.start();
    System.out.println("ShooterAuton Timer started!");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.out.println("Running shooter...");
    fuelShooterSubsystem.runMotor(Constants.ShooterConstants.shooterSpeed); //Run shooter every loop

    if (timer.get() >= 1) {
      fuelShooterSubsystem.runAgitator(Constants.ShooterConstants.agitatorSpeed); //Run agitator after 1 second
    } else {
      fuelShooterSubsystem.runAgitator(0.0); //Stop agitator until 1 second has passed
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    fuelShooterSubsystem.runMotor(0.0);
    fuelShooterSubsystem.runAgitator(0.0);
    timer.stop();
    System.out.println("ShooterAuton Timer stopped!");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get() >= 5; //Runs for 3 seconds
  }
}
