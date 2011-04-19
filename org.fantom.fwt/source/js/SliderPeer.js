//
// Copyright (c) 2011, Brian Frank and Andy Frank
// Licensed under the Academic Free License version 3.0
//
// History:
//   31 Jan 11  Ivan Kornienko  Creation
//

fan.fwt.SliderPeer = fan.sys.Obj.$extend(fan.fwt.WidgetPeer);
fan.fwt.SliderPeer.prototype.$ctor = function(self) {}

fan.fwt.SliderPeer.prototype.val   = function(self) { return this.m_val; }
fan.fwt.SliderPeer.prototype.val$  = function(self, val)
{
  this.m_val = val;
  if (this.elem != null) this.sync(self);
} 
fan.fwt.SliderPeer.prototype.m_val = 0;

fan.fwt.SliderPeer.prototype.min   = function(self) { return this.m_min; }
fan.fwt.SliderPeer.prototype.min$  = function(self, val)
{ 
  // for compatibility with Java SWT peer
  if (val < 0 || val >= this.m_max) return;
  this.m_min = val;
  if (this.elem != null) this.sync(self);
}
fan.fwt.SliderPeer.prototype.m_min = 0;

fan.fwt.SliderPeer.prototype.max   = function(self) { return this.m_max; }
fan.fwt.SliderPeer.prototype.max$  = function(self, val) 
{ 
  // for compatibility with Java SWT peer
  if (val < 0 || val <= this.m_min) return;
  this.m_max = val;
  if (this.elem != null) this.sync(self);
}

fan.fwt.SliderPeer.prototype.m_max = 100;

fan.fwt.SliderPeer.prototype.thumb  = function(self) { return this.m_thumb; }
fan.fwt.SliderPeer.prototype.thumb$ = function(self, val)
{
  this.m_thumb = val;
  if (this.elem != null) this.sync(self);
}
fan.fwt.SliderPeer.prototype.m_thumb = 10;

// really page option isn't supported
fan.fwt.SliderPeer.prototype.page   = function(self) { return this.m_page; }
fan.fwt.SliderPeer.prototype.page$  = function(self, val) { this.m_page = val; } 
fan.fwt.SliderPeer.prototype.m_page = 10;

fan.fwt.SliderPeer.prototype.prefSize = function(self, hints)
{
  var pref = fan.fwt.WidgetPeer.prototype.prefSize.call(this, self, hints);
  var thickness = fan.fwt.SliderPeer.thickness();
  if (self.m_orientation == fan.gfx.Orientation.m_horizontal)
    return fan.gfx.Size.make(pref.m_w, thickness);
  else
    return fan.gfx.Size.make(thickness, pref.m_h);
}

fan.fwt.SliderPeer.prototype.create = function(parentElem, self)
{
  var scrollDiv = document.createElement("div");
  scrollDiv.style.padding = "0px";       
  var scrollContent = document.createElement("div");
  scrollDiv.appendChild(scrollContent);

  var vertical = self.m_orientation == fan.gfx.Orientation.m_vertical;
  if (vertical)
  {
    scrollDiv.style.width = fan.fwt.SliderPeer.thickness() + "px";
    scrollDiv.style.overflowX = "hidden";
    scrollDiv.style.overflowY = "scroll";
    scrollContent.style.width = "1px";
  }
  else
  {
    scrollDiv.style.height = fan.fwt.SliderPeer.thickness() + "px";
    scrollDiv.style.overflowX = "scroll";
    scrollDiv.style.overflowY = "hidden";
    scrollContent.style.height = "1px";
  }

  scrollDiv.onscroll = function(event)
  { 
    var scrollIndent = 0;
    var scrollSize = 0;
    if (self.m_orientation == fan.gfx.Orientation.m_horizontal)
    {
      scrollSize = scrollDiv.scrollWidth - scrollDiv.clientWidth;
      scrollIndent = scrollDiv.scrollLeft;
    }  
    else
    {
      scrollSize = scrollDiv.scrollHeight - scrollDiv.clientHeight;
      scrollIndent = scrollDiv.scrollTop;
    }
    var newVal = Math.round(self.peer.m_min + scrollIndent * (self.peer.m_max - self.peer.m_min - self.peer.m_thumb) / scrollSize);
    if (self.peer.m_val == newVal)
      return

    self.peer.m_val = newVal;
    // fire onModify
    if (self.m_onModify.size() > 0)
    {
      var me = fan.fwt.Event.make();
      me.m_id = fan.fwt.EventId.m_modified;
      me.m_widget = self;
      var list = self.m_onModify.list();
      for (var i=0; i<list.size(); i++) list.get(i).call(me);
    }
  }

  // container element
  var div = this.emptyDiv();
  div.appendChild(scrollDiv);
  parentElem.appendChild(div);
  return div;
}

fan.fwt.SliderPeer.prototype.sync = function(self)
{
  var vert = self.m_orientation == fan.gfx.Orientation.m_vertical;
  var w = this.m_size.m_w;
  var h = this.m_size.m_h;
  var scrollDiv = this.elem.firstChild;
  var scrollContent = scrollDiv.firstChild;

  var maxRatio = (this.m_max - this.m_min) / this.m_thumb;
  var valRatio = (this.m_val - this.m_min) / this.m_thumb;

  if (vert)
  {
    scrollDiv.style.height = h + "px";
    if (this.m_enabled)
    {
      scrollContent.style.height = Math.round(h * maxRatio) + "px";
      scrollDiv.scrollTop = Math.round(h * valRatio);
    }
    else
    {
      scrollDiv.scrollTop = 0;
      scrollContent.style.height = "0px";
    }
  }
  else
  {
    scrollDiv.style.width = w + "px";
    if (this.m_enabled)
    {
      scrollContent.style.width = Math.round(w * maxRatio) + "px";
      scrollDiv.scrollLeft = Math.round(w * valRatio);
    }
    else
    {
      scrollDiv.scrollLeft = 0;
      scrollContent.style.width = "0px";
    }
  }

  fan.fwt.WidgetPeer.prototype.sync.call(this, self);
}

fan.fwt.SliderPeer.prototype.checkModifyListeners = function(self) {}

//////////////////////////////////////////////////////////////////////////
// Utils
//////////////////////////////////////////////////////////////////////////

fan.fwt.SliderPeer.thickness = function()
{
  if (fan.fwt.SliderPeer.m_thickness == 0)
  {
    var inner = document.createElement('div');
    inner.style.height = "100px";

    var outer = document.createElement('div');
    with (outer.style)
    {
      width = "50px"; height = "50px";
      overflow = "hidden"; position = "absolute";
      visibility = "hidden";
    }
    outer.appendChild(inner);

    document.body.appendChild(outer);
    var w1 = inner.offsetWidth;
    outer.style.overflow = 'scroll';
    var w2 = inner.offsetWidth;
    if (w1 == w2) w2 = outer.clientWidth;
    document.body.removeChild(outer);

    fan.fwt.SliderPeer.m_thickness = (w1 - w2);
  }
  return fan.fwt.SliderPeer.m_thickness;
}

fan.fwt.SliderPeer.m_thickness = 0;
