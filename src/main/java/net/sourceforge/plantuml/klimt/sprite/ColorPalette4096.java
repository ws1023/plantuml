/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.klimt.sprite;

import java.awt.Color;
import java.util.Objects;

import net.sourceforge.plantuml.klimt.color.HColorSimple;
import net.sourceforge.plantuml.klimt.color.HColors;

public class ColorPalette4096 {

	// private static final String colorValue = "!#$%&*+-:;<=>?@^_~GHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	// Maybe ! and # will be used for something else in the future
	// So we replace them by . and '
	private static final String colorValue = ".,$%&*+-:;<=>?@^_~GHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public String getStringFor(Color dest) {
		return getStringFor((HColorSimple) HColors.simple(dest));
	}

	private String getStringFor(HColorSimple dest) {
		int result = 0;
		int resultDist = Integer.MAX_VALUE;
		for (int i = 0; i < 4096; i++) {
			final int dist = dest.distanceTo(getHtmlColorSimpleFor(i));
			if (dist < resultDist) {
				result = i;
				resultDist = dist;
			}
		}
		return encodeInt(result);
	}

	protected String encodeInt(int result) {
		final int v2 = result % 64;
		final int v1 = result / 64;
		assert v1 >= 0 && v1 <= 63 && v2 >= 0 && v2 <= 63;
		return "" + colorValue.charAt(v1) + colorValue.charAt(v2);
	}

	private HColorSimple getHtmlColorSimpleFor(int s) {
		final Color color = Objects.requireNonNull(getColorFor(s));
		return (HColorSimple) HColors.simple(color);
	}

	protected Color getColorFor(String s) {
		// Migration
		s = s.replace('!', '.');
		s = s.replace('#', ',');
		if (s.length() != 2)
			throw new IllegalArgumentException();

		final int v1 = colorValue.indexOf(s.charAt(0));
		if (v1 == -1)
			return null;

		final int v2 = colorValue.indexOf(s.charAt(1));
		if (v2 == -1)
			return null;

		final int code = v1 * 64 + v2;
		return getColorFor(code);
	}

	protected Color getColorFor(final int code) {
		final int blue = code % 16;
		final int green = (code / 16) % 16;
		final int red = (code / 256) % 16;
		return new Color(dup(red), dup(green), dup(blue));
	}

	private int dup(int v) {
		assert v >= 0 && v <= 15;
		return v * 16 + v;
	}

}
