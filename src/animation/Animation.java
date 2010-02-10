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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * An <code>Animation</code> controls one or more <code>Animator</code>s
 * over a shared period of time, with a shared interpolator. For example,
 * if you
 * 
 * @author jdf
 *
 */
public class Animation
{
	private final Interpolator interpolator;
	private final double t0, t1;
	private final List<Animator> animations;

	public Animation(final long duration, final TimeUnit timeUnit,
			final Animator... animators)
	{
		this(Interpolator.COSINE, duration, timeUnit, animators);
	}

	public Animation(final Interpolator interpolator, final long duration,
			final TimeUnit timeUnit, final Animator... animators)
	{
		this(interpolator, duration, timeUnit, Arrays.asList(animators));
	}

	public Animation(final long duration, final TimeUnit timeUnit,
			final List<Animator> animations)
	{
		this(Interpolator.COSINE, duration, timeUnit, animations);
	}

	public Animation(final Interpolator interpolator, final long duration,
			final TimeUnit timeUnit, final List<Animator> animations)
	{
		this.interpolator = interpolator;
		this.t0 = System.currentTimeMillis();
		this.t1 = t0 + timeUnit.toMillis(duration);
		this.animations = animations;
	}

	private double mu(final long time)
	{
		if (time <= t0)
			return 0;
		else if (time >= t1)
			return 1;
		return (time - t0) / (t1 - t0);
	}

	public boolean step()
	{
		final long now = System.currentTimeMillis();
		final double u = mu(now);
		final double v = interpolator.eval(u);
		for (final Animator animation : animations)
			animation.step(v);
		return now < t1;
	}
}
