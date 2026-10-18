// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.Orchestra; //ggggggvgg
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.robot.Constants;

public class OrchestraSubsystem extends SubsystemBase {
  /** Creates a new OrchestraSubsystem. */
  private final TalonFX motor;
  private final TalonFX flDrive; //
  private final TalonFX frDrive;
  private final TalonFX blDrive;
  private final TalonFX brDrive;

  private final TalonFX flSteer;
  private final TalonFX frSteer;
  private final TalonFX blSteer;
  private final TalonFX brSteer;

  private Orchestra orchestra; 

  public OrchestraSubsystem() {
    motor = new TalonFX(23, "rio");
    flDrive = new TalonFX(12);
    frDrive = new TalonFX(2);
    blDrive = new TalonFX(5); //TODO: Swerve modules don't work here, figure this out
    brDrive = new TalonFX(8);

    flSteer = new TalonFX(11);
    frSteer = new TalonFX(3);
    blSteer = new TalonFX(9);
    brSteer = new TalonFX(4);

    orchestra = new Orchestra();

    orchestra.addInstrument(motor, 1);
    /*orchestra.addInstrument(flDrive, 2);
    orchestra.addInstrument(frDrive, 3);
    orchestra.addInstrument(blDrive, 4);
    orchestra.addInstrument(brDrive, 5);
    orchestra.addInstrument(flSteer, 6);
    orchestra.addInstrument(frSteer, 7);
    orchestra.addInstrument(blSteer, 8);
    orchestra.addInstrument(brSteer, 9);*/

    orchestra.loadMusic("onestop.chrp"); //test
    //Test (test) (ch.7 only)
    //One Stop (onestop) (C:/Windows/Media/onestop.mid)
    //Song Requests:
    //Africa - Toto
    //Bergentrucking
  }

  public void musicControl(boolean play, boolean stop) {
    if (play) {
      orchestra.play();
      System.out.println("Playing...");
    }
    if (stop) {
      orchestra.stop();
      System.out.println("Stopped.");
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
