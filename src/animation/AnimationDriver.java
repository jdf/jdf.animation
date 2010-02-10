package animation;

/*
Copyright 2010 Jonathan Feinberg

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import java.awt.Component;

/**
 * 
 * @author jdf
 *
 */
public class AnimationDriver implements Runnable
{
	private final Component canvas;
	private final long delayMillis;

	// mutable
	private Animation animation = null;
	// guarded by
	private final Object animationLock = new Object();

	public AnimationDriver(final Component canvas)
	{
		this(canvas, 30);
	}

	public AnimationDriver(final Component canvas, final int fps)
	{
		this.canvas = canvas;
		this.delayMillis = 1000L / fps;
	}

	public void runAnimation(final Animation animation)
	{
		synchronized (animationLock)
		{
			this.animation = animation;
			animationLock.notifyAll();
		}
	}

	public void start()
	{
		new Thread(this, "AnimationDriver on " + canvas).start();
	}

	public void run()
	{
		while (true)
			try
			{
				synchronized (animationLock)
				{
					while (animation == null)
						animationLock.wait();
				}
				if (canvas instanceof Animatable)
					((Animatable) canvas).startAnimating();
				while (true)
				{
					synchronized (animationLock)
					{
						if (animation == null)
							break;
						if (!animation.step())
						{
							animation = null;
							break;
						}
					}
					canvas.repaint();
					Thread.sleep(delayMillis);
				}
				if (canvas instanceof Animatable)
					((Animatable) canvas).stopAnimating();
				canvas.repaint();
			}
			catch (final InterruptedException e)
			{
				return;
			}
	}

}
