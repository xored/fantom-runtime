//
// Copyright (c) 2008, Brian Frank and Andy Frank
// Licensed under the Academic Free License version 3.0
//
// History:
//   12 Jun 08  Brian Frank  Creation
//
package fan.fwt;

import fan.sys.*;
import fan.gfx.Halign;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import java.io.IOException;
import java.io.InputStream;


public class AnimatedImagePeer extends WidgetPeer
{

//////////////////////////////////////////////////////////////////////////
// Construction
//////////////////////////////////////////////////////////////////////////
  private int delay = 100;
  private ImageData[] imageData;
  private Uri uri;

  public static AnimatedImagePeer make(fan.fwt.AnimatedImage self)
    throws Exception
  {
    AnimatedImagePeer peer = new AnimatedImagePeer();
    ((fan.fwt.Widget)self).peer = peer;
    peer.self = self;
    return peer;
  }

  public Widget create(Widget parent)
  {
    return new CLabel((Composite)parent, 0);
  }

//////////////////////////////////////////////////////////////////////////
// Fields
//////////////////////////////////////////////////////////////////////////

  // Image image := null
  public fan.gfx.Image image(fan.fwt.AnimatedImage self) { return image.get(); }
  public void image(fan.fwt.AnimatedImage self, fan.gfx.Image v) { 
     uri = v.uri;
  	 loadImages(new JavaInputStream(((File) uri.get()).in()));
  	  if ((imageData != null) && (imageData.length>1)) {
  		  new AnimationThread().start();			
      }

      image.set(v); 
  }
  public final Prop.ImageProp image = new Prop.ImageProp(this)
  {
    public void set(Widget w, Image v) { 
        ((CLabel)w).setImage(v); 
    }
  };

  // Color bg := null
  public fan.gfx.Color bg(fan.fwt.AnimatedImage self) { return bg.get(); }
  public void bg(fan.fwt.AnimatedImage self, fan.gfx.Color v) { bg.set(v); }
  public final Prop.ColorProp bg = new Prop.ColorProp(this)
  {
    public void set(Widget w, Color v) { ((CLabel)w).setBackground(v); }
  };

    protected void loadImages(InputStream stream) {

		ImageLoader loader = new ImageLoader();

		imageData = loader.load(stream);
		if (stream != null)
			try {
				stream.close();
			} catch (IOException e) {
				// ignore
			}
		if (imageData == null)
			return;
/*
		for (int i = 0; i < imagesData.length; i++) {
			if (delay < imagesData[i].delayTime * 10)
				delay = imagesData[i].delayTime * 10;
			{
				ImageLoader imageLoader = new ImageLoader();
				imageLoader.data = new ImageData[] { imagesData[i] };
			}
		}
        */
	}


    private class AnimationThread extends Thread {
        private int frame = 0;
    	public void run() {						
        	int timer = 0;
			do {
				try {
					Thread.sleep(delay);
				}	
				catch (InterruptedException e) {
					return;
				}

                if ((control==null) || control.isDisposed())
                    break;

                timer+=delay;
                do {
                    timer-=imageData[frame].delayTime*10;
                    frame = (frame+1)%imageData.length;
                } while (timer>0);

				if (Display.getDefault()!=null) {
					Display.getDefault().asyncExec(
					new Runnable() {
						public void run() {
							
                            if ((control!=null) && !control.isDisposed())
                                ((CLabel)control).setImage(new Image(Display.getDefault(),
                                    imageData[frame]));
						}
					});
				}
				else
					System.out.println("null");

			} while (true);
		}
	}


  class JavaInputStream extends InputStream {

	private final InStream stream;

	public JavaInputStream(InStream stream) {
		this.stream = stream;
	}

	@Override
	public int read() throws IOException {
		try {
			return stream.read().intValue();
		} catch (Exception e) {
			return -1;
		}
   }
}

}


