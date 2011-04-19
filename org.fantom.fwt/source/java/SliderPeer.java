//
// Copyright (c) 2008, Brian Frank and Andy Frank
// Licensed under the Academic Free License version 3.0
//
// History:
//   16 Nov 10  Yuri Strot  Creation
//
package fan.fwt;

import fan.sys.*;
import fan.gfx.Orientation;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.events.*;

public class SliderPeer
  extends WidgetPeer
  implements SelectionListener
{

//////////////////////////////////////////////////////////////////////////
// Construction
//////////////////////////////////////////////////////////////////////////

  public static SliderPeer make(fan.fwt.Slider self)
    throws Exception
  {
    SliderPeer peer = new SliderPeer();
    ((fan.fwt.Widget)self).peer = peer;
    peer.self = self;
    return peer;
  }

  public Widget create(Widget parent)
  {
    fan.fwt.Slider self = (fan.fwt.Slider)this.self;
	int style = self.orientation == Orientation.horizontal ? SWT.HORIZONTAL : SWT.VERTICAL;
    Slider c = new Slider((Composite)parent, style);
    c.addSelectionListener(this);
    this.control = c;
    return c;
  }

//////////////////////////////////////////////////////////////////////////
// Fields
//////////////////////////////////////////////////////////////////////////

  // Int val := 0
  public long val(fan.fwt.Slider self) { return val.get(); }
  public void val(fan.fwt.Slider self, long v) { val.set(v); }
  public final Prop.IntProp val = new Prop.IntProp(this, 0)
  {
    public int get(Widget w) { return ((Slider)w).getSelection(); }
    public void set(Widget w, int v) { ((Slider)w).setSelection(v); }
  };

  // Int min := 0
  public long min(fan.fwt.Slider self) { return min.get(); }
  public void min(fan.fwt.Slider self, long v) { min.set(v); }
  public final Prop.IntProp min = new Prop.IntProp(this, 0)
  {
    public int get(Widget w) { return ((Slider)w).getMinimum(); }
    public void set(Widget w, int v) { ((Slider)w).setMinimum(v); }
  };

  // Int max := 100
  public long max(fan.fwt.Slider self) { return max.get(); }
  public void max(fan.fwt.Slider self, long v) { max.set(v); }
  public final Prop.IntProp max = new Prop.IntProp(this, 100)
  {
    public int get(Widget w) { return ((Slider)w).getMaximum(); }
    public void set(Widget w, int v) { ((Slider)w).setMaximum(v); }
  };

  // Int thumb := 10
  public long thumb(fan.fwt.Slider self) { return thumb.get(); }
  public void thumb(fan.fwt.Slider self, long v) { thumb.set(v); }
  public final Prop.IntProp thumb = new Prop.IntProp(this, 10)
  {
    public int get(Widget w) { return ((Slider)w).getThumb(); }
    public void set(Widget w, int v) { ((Slider)w).setThumb(v); }
  };

  // Int page := 10
  public long page(fan.fwt.Slider self) { return page.get(); }
  public void page(fan.fwt.Slider self, long v) { page.set(v); }
  public final Prop.IntProp page = new Prop.IntProp(this, 10)
  {
    public int get(Widget w) { return ((Slider)w).getPageIncrement(); }
    public void set(Widget w, int v) { ((Slider)w).setPageIncrement(v); }
  };

//////////////////////////////////////////////////////////////////////////
// Eventing
//////////////////////////////////////////////////////////////////////////

  public void widgetDefaultSelected(SelectionEvent se) {}

  public void widgetSelected(SelectionEvent se)
  {
    fireModified();
  }

  public void fireModified()
  {
    Slider sb = (Slider)control;
    fan.fwt.Event fe = event(EventId.modified);
    fe.data = Long.valueOf(sb.getSelection());
    ((fan.fwt.Slider)self).onModify().fire(fe);
  }

  public void checkModifyListeners(fan.fwt.Slider self)
  {
  }

}