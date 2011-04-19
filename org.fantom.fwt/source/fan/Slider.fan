//
// Copyright (c) 2009, Brian Frank and Andy Frank
// Licensed under the Academic Free License version 3.0
//
// History:
//   16 Nov 10  Yuri Strot  Creation
//

using gfx

**
** Slider is a `Widget` that represent a range of positive, numeric values
**
@Js
@Serializable
class Slider : Widget
{

  **
  ** Default constructor.
  **
  new make(|This|? f := null)
  {
    if (f != null) f(this)
  }

  **
  ** Callback when slider value is modified.
  **
  ** Event id fired:
  **   - `EventId.modified`
  **
  ** Event fields:
  **   - `Event.data`: new value of scroll bar
  **
  @Transient EventListeners onModify := EventListeners()
    { it.onModify = |->| { checkModifyListeners } }
    { private set }
  internal native Void checkModifyListeners()

  **
  ** Horizontal or vertical.
  **
  const Orientation orientation := Orientation.vertical

  **
  ** The current value of the slider.
  **
  native Int val

  **
  ** The minimum value of the slider.
  **
  native Int min

  **
  ** The maximum value of the slider.
  **
  native Int max

  **
  ** The size of thumb relative to difference between min and max.
  **
  native Int thumb

  **
  ** Page increment size relative to difference between min and max.
  **
  native Int page

}