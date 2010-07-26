/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package fan.sys;

import java.util.EventObject;

public class PodEvent extends EventObject
{

  public static final int LOADED = 1;
  public static final int STARTED = 2;

  private static final long serialVersionUID = 1L;

  private final int kind;

  /**
   * @param pod
   */
  public PodEvent(Pod pod, int kind)
  {
    super(pod);
    this.kind = kind;
  }

  public int kind()
  {
    return kind;
  }

  public Pod pod()
  {
    return (Pod) getSource();
  }

  public String podName()
  {
    return pod().name();
  }

  private String describeKind()
  {
    switch (kind)
    {
    case LOADED:
      return "LOADED";
    case STARTED:
      return "STARTING";
    default:
      return "[kind:" + kind + "]";
    }
  }

  @Override
  public String toString()
  {
    return describeKind() + " " + getSource();
  }

}
