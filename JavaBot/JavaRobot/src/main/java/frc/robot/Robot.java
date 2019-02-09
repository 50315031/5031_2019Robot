/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import java.lang.*;
import java.io.*;
import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {

String atonChoser;
SmartDashboard smartDashboard;

public boolean left;
public boolean middle;
public boolean right;

public int atonMode;

//atn variables
public int As;
public double stopTime;
public double readTime;
//left aton
public int straight_1_L;
public int straight_2_L;
public int turnTime_L;
public int atnEnd_l;
//middle aton
public int straight_M;
//right aton
public int straight_1_R;
public int straight_2_R;
public int turnTime_R;
public int atnEnd_R;




//drive train
  WPI_TalonSRX _rghtFront = new WPI_TalonSRX(3);
  WPI_TalonSRX _rghtFollower = new WPI_TalonSRX(4);
  WPI_TalonSRX _leftFront = new WPI_TalonSRX(1);
  WPI_TalonSRX _leftFollower = new WPI_TalonSRX(2);

  Talon elevator = new Talon(0);
  Talon intake = new Talon(1);

  DifferentialDrive _diffDrive = new DifferentialDrive(_leftFront, _rghtFront);
  //RobotDrive m_aton_robot = new RobotDrive(_leftFront, _leftFollower, _rghtFront, _rghtFollower);

  private Joystick m_leftStick = new Joystick(0);
  private Joystick m_rightStick = new Joystick(1);
  private Joystick m_shooter = new Joystick(2);

  Timer r_Timer = new Timer();

  ADXRS450_Gyro m_Gyro = new ADXRS450_Gyro();
  double kp = 0.035;

  /*
  Compressor c = new Compressor (0);
	DoubleSolenoid shoot = new DoubleSolenoid (2,3);
  DoubleSolenoid  Intake= new DoubleSolenoid (4,5);
  */
	DoubleSolenoid Lifter = new DoubleSolenoid (0,1);
  
  

  //private TalonSRX l_Master;  

  @Override
  public void autonomousInit() 
  {
    m_Gyro.calibrate();
    r_Timer.reset();
    r_Timer.start();

    //left aton
    straight_1_L = 3;
    straight_2_L = 3;
    turnTime_L = 3;
    atnEnd_l = 3;
    //aton right
    straight_M = 3;
    //aton left
    straight_1_R = 3;
    straight_2_R = 3;
    turnTime_R = 3;
    atnEnd_R = 3;

  }

  @Override
  public void autonomousPeriodic() 
  { 
    switch(atonMode)
    {
      case 0:
        LeftAton();
      break;
      case 1:
        RightAton();
      break;
      case 2:
        MiddleAton();
      break;
    }
  }

private void MiddleAton()
{
  switch(As)
  {
    case 0:
    //drive straight
    stopTime = r_Timer.get() + straight_M;
    _diffDrive.tankDrive(0.50, 0.50);
    As = 2;
    break;
    case 1:
    //atn end
    if(r_Timer.get() > stopTime)
    {
      _diffDrive.tankDrive(0, 0);
    }
    break;
  }
}

private void RightAton()
{
  switch(As)
  {
    case 0:
      
    break;
    case 1:
    //drive forward state
      stopTime = r_Timer.get() + straight_1_R;
      _diffDrive.tankDrive(0.50, 0.50);
      As = 2;
    break;
    case 2:
    //change state
      if(r_Timer.get() > stopTime)
      {
        _diffDrive.tankDrive(0, 0);
        As = 3;
      }
    break;
    case 3:
    //turn right state
      stopTime = r_Timer.get() + turnTime_R;
      _diffDrive.tankDrive(0.50, 0);
      As = 4;
    break;
    case 4:
    //change state
      if(r_Timer.get() > stopTime)
      {
        _diffDrive.tankDrive(0, 0);
        As = 5;
      }
    break;
    case 5:
    //forward drive state
      stopTime = r_Timer.get() + straight_2_R;
      _diffDrive.tankDrive(0.50, 0.50);
      As = 6;
    break;
    case 6:
    //end state
      if(r_Timer.get() > stopTime)
      {
      _diffDrive.tankDrive(0, 0);
    }
    break;
  }
}

private void LeftAton() 
{
	switch(As)
    {
      case 0:
        
      break;
      case 1:
      //drive forward state
        stopTime = r_Timer.get() + straight_1_L;
        _diffDrive.tankDrive(0.50, 0.50);
        As = 2;
      break;
      case 2:
      //change state
        if(r_Timer.get() > stopTime)
        {
          _diffDrive.tankDrive(0, 0);
          As = 3;
        }
      break;
      case 3:
      //turn right state
        stopTime = r_Timer.get() + turnTime_L;
        _diffDrive.tankDrive(0, 0.50);
        As = 4;
      break;
      case 4:
      //change state
        if(r_Timer.get() > stopTime)
        {
          _diffDrive.tankDrive(0, 0);
          As = 5;
        }
      break;
      case 5:
      //forward drive state
        stopTime = r_Timer.get() + straight_2_L;
        _diffDrive.tankDrive(0.50, 0.50);
        As = 6;
      break;
      case 6:
      //end state
        if(r_Timer.get() > stopTime)
        {
        _diffDrive.tankDrive(0, 0);
      }
      break;
    }
  }

  @Override
  public void robotInit() {
    CameraServer.getInstance().startAutomaticCapture(2);
    m_Gyro.calibrate();
    /*
     * _rghtFront = new WPI_TalonSRX(3); _rghtFollower = new WPI_TalonSRX(4);
     * _leftFront = new WPI_TalonSRX(1); _leftFollower = new WPI_TalonSRX(2);
     * 
     * _diffDrive = new DifferentialDrive(_leftFront, _rghtFront);
     * 
     * m_leftStick = new Joystick(1); m_rightStick = new Joystick(0); m_shooter =
     * new Joystick(2);
     */

    /*
     * Compressor c = new Compressor (0); shoot = new DoubleSolenoid (2,3); Intake=
     * new DoubleSolenoid (4,5); Lifter = new DoubleSolenoid (0,1);
     */
  }

  @Override
  public void disabledPeriodic() {
    smartDashboard.putString("DB/String 0", "test");

    if (m_shooter.getRawButton(8)) 
    {
      smartDashboard.putString("DB/String 1", "left");
      atonMode = 0;
    } 
    else if (m_shooter.getRawButton(9)) 
    {
      smartDashboard.putString("DB/String 1", "middle");
      atonMode = 2;
    } 
    else if (m_shooter.getRawButton(10))
    {
      smartDashboard.putString("DB/String 1", "right");
      atonMode = 1;
    }
  }

  @Override
  public void teleopPeriodic() 
  {
    //drive train
    _leftFollower.follow(_leftFront);
    _rghtFollower.follow(_rghtFront);
    _diffDrive.tankDrive(-m_leftStick.getY(), -m_rightStick.getY());

    //robot  lifter
    if(m_shooter.getRawButtonPressed(1))
    {
      Lifter.set(DoubleSolenoid.Value.kForward);
    }
    else if(m_shooter.getRawButtonReleased(1))
    {
      Lifter.set(DoubleSolenoid.Value.kReverse);
    }
    else
    {
      Lifter.set(DoubleSolenoid.Value.kOff);
    }
    
    //elevator
    if(m_shooter.getRawButton(4))
    {
      elevator.set(0.5f);
    }
    else if(m_shooter.getRawButton(5))
    {
      elevator.set(-0.5f);
    }
    else
    {
      elevator.set(0);
    }

    //intake
    if(m_shooter.getRawButton(2))
    {
      intake.set(0.5f);
    }
    else if(m_shooter.getRawButton(3))
    {
      intake.set(-0.5f);
    }
    else
    {
      intake.set(0);
    }
    
  }
}
