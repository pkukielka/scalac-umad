package com.pkukielka;

import com.typesafe.config.Config;
import javassist.CannotCompileException;
import javassist.CtMethod;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class ChaosRewriter extends MethodRewriter {

  private int sleepThreshold = 0;
  private int sleepTime = 100;
  private int methodPercent = 15;


  public ChaosRewriter(Config config) {
    super(config);
    if (config.hasPath("sleepThreshold")) sleepThreshold = config.getInt("sleepThreshold");
    if (config.hasPath("sleepTime")) sleepTime = config.getInt("sleepTime");
    if (config.hasPath("methodPercent")) methodPercent = config.getInt("methodPercent");
    if (isEnabled()) {
      String msg = "[INFO] Introduce chaos with sleepThreshold=%s, methodPercent=%s  and sleepTime=%s";
      System.out.println(String.format(msg, sleepThreshold, methodPercent, sleepTime));
    }
  }

  private static final Random random = new Random();

  public static void chaoticSleep(int sleepThreshold, int sleepTime) {
    try {
      int picked = random.nextInt(sleepThreshold + sleepTime);
      if(picked > sleepThreshold) TimeUnit.NANOSECONDS.sleep(sleepTime -sleepThreshold);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void editMethod(final CtMethod editableMethod, String ifCalledFrom, String dottedName) throws CannotCompileException {
    if (random.nextInt(100) < methodPercent) {
      String code = "com.pkukielka.ChaosRewriter.chaoticSleep(%s, %s);";
      System.out.println("Transforming: " + editableMethod.getLongName() + " from " + dottedName);
      editableMethod.insertBefore(String.format(code, sleepThreshold, sleepTime));
    }
  }
}
