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

/**
 * 
 * An <code>Animator</code> sets up one or more <code>Range</code>s of values,
 * and knows how to interpret those values as they change over time. 
 * It implements a single method, <code>void animate(final double[] currentValues)</code>.
 * 
 * <p>For example, imagine some object that has a color and a size, and we
 * wish to turn it big and red:
 * 
 * <pre>
 * class Balloon
 * {
 *     private double size;
 *     private double hue;
 * 
 *     // ...
 * 
 *     Animator getBlowUpAnimator()
 *     {
 *         return new Animator(new Range(size, size + 100), new Range(hue, 0)) {
 *             public void animate(final double[] currentValues) {
 *                 size = currentValues[0];
 *                 hue = currentValues[1];
 *             }
 *         }
 *     }
 * }
 * </pre>
 * @author jdf
 * @see Animation
 */
abstract public class Animator
{
	public static class Range
	{
		private final double startValue;
		private final double endValue;

		public Range(final double startValue, final double endValue)
		{
			this.startValue = startValue;
			this.endValue = endValue;
		}

		private double interpolate(final double u)
		{
			return u * endValue + (1 - u) * startValue;
		}
	}

	private final Range[] ranges;
	private final double[] values;

	protected Animator(final Range... ranges)
	{
		this.ranges = ranges;
		values = new double[ranges.length];
	}

	void step(final double u)
	{
		for (int i = 0; i < ranges.length; i++)
			values[i] = ranges[i].interpolate(u);
		animate(values);
	}

	protected abstract void animate(final double[] ranges);
}
