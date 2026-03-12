// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import com.ctre.phoenix.led.TwinkleAnimation.TwinklePercent;
import com.ctre.phoenix.led.TwinkleOffAnimation.TwinkleOffPercent;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANdleConstants;

public class CANdleSubsystem extends SubsystemBase {
  /** Creates a new CANdleSubsystem. */
  private final CANdle candle = new CANdle(CANdleConstants.CANdleID, "rio");
  private final int Ledcount = CANdleConstants.LEDcount;
  private Animation Animation = null;

  public enum AnimationTypes {
        ColorFlow,
        Fire,
        Larson,
        Rainbow,
        RgbFade,
        SingleFade,
        Strobe,
        Twinkle,
        TwinkleOff,
        SetAll
    }
  
  public CANdleSubsystem() {
    CANdleConfiguration LEDconfig = new CANdleConfiguration();

     LEDconfig.stripType = LEDStripType.RGB;   // WS2812 / NeoPixel
     LEDconfig.brightnessScalar = CANdleConstants.brightness;         // 100% brightness
     LEDconfig.disableWhenLOS = false;

    candle.configAllSettings(LEDconfig);
  }

  public void setCANdleColor(int Red, int Green, int Blue) {
    candle.setLEDs(Red,Green,Blue);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
