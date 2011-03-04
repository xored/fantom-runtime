using gfx

@Js
@Serializable { collection = true }
class AnimatedImage : Widget
{

  **
  ** Image to display on label. Defaults to null.
  **
  native Image? image

  **
  ** Background color. Defaults to null (system default).
  **
  native Color? bg

}
