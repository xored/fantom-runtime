//
// Copyright (c) 2007, Brian Frank and Andy Frank
// Licensed under the Academic Free License version 3.0
//
// History:
//   26 Dec 07  Brian Frank  Creation
//
package fan.sys;

import java.util.regex.*;

/**
 * RegexMatcher
 */
public final class RegexMatcher
  extends FanObj
{

//////////////////////////////////////////////////////////////////////////
// Constructors
//////////////////////////////////////////////////////////////////////////

  RegexMatcher(Matcher matcher)
  {
    this.matcher = matcher;
  }

//////////////////////////////////////////////////////////////////////////
// Identity
//////////////////////////////////////////////////////////////////////////

  public Type typeof() { return Sys.RegexMatcherType; }

//////////////////////////////////////////////////////////////////////////
// Methods
//////////////////////////////////////////////////////////////////////////

  public final boolean matches()
  {
    return matcher.matches();
  }

  public final boolean find()
  {
    return matcher.find();
  }

  public final long groupCount()
  {
    return matcher.groupCount();
  }

  public final String group() { return group(0L); }
  public final String group(long group)
  {
    try
    {
      return matcher.group((int)group);
    }
    catch (IllegalStateException e)
    {
      throw Err.make(e.getMessage()).val;
    }
    catch (IndexOutOfBoundsException e)
    {
      throw IndexErr.make(group).val;
    }
  }

  public final long start() { return start(0L); }
  public final long start(long group)
  {
    try
    {
      return matcher.start((int)group);
    }
    catch (IllegalStateException e)
    {
      throw Err.make(e.getMessage()).val;
    }
    catch (IndexOutOfBoundsException e)
    {
      throw IndexErr.make(group).val;
    }
  }

  public final long end() { return end(0L); }
  public final long end(long group)
  {
    try
    {
      return Long.valueOf(matcher.end((int)group));
    }
    catch (IllegalStateException e)
    {
      throw Err.make(e.getMessage()).val;
    }
    catch (IndexOutOfBoundsException e)
    {
      throw IndexErr.make(group).val;
    }
  }

//////////////////////////////////////////////////////////////////////////
// Fields
//////////////////////////////////////////////////////////////////////////

  Matcher matcher;
}