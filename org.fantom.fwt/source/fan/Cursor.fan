using gfx

@Js
const class Cursor
{

  static const Cursor def := predefine("default")
  static const Cursor pointer := predefine("pointer")
  static const Cursor text := predefine("text")
  static const Cursor crosshair := predefine("crosshair")
  static const Cursor wait := predefine("wait")
  static const Cursor help := predefine("help")
  static const Cursor progress := predefine("progress")
  static const Cursor move := predefine("move")

  static const Cursor[] vals := [def, pointer, text, crosshair, wait, help, progress, move]

  internal const Str name
  internal const Image? image
  internal const Int x
  internal const Int y

  new make(Image image, Int x, Int y)
  {
    this.image = image; name = image.toStr()
    this.x = x; this.y = y
  }

  private new predefine(Str name) { this.name = name }

  override Str toStr() { name }
}
