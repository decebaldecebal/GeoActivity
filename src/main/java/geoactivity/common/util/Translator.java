package geoactivity.common.util;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import net.minecraft.util.text.translation.I18n;

public class Translator
{
	public static String translateToLocal(String key) {
		if (I18n.canTranslate(key)) {
			return I18n.translateToLocal(key);
		} else {
			return I18n.translateToFallback(key);
		}
	}

	public static List<String> getWrappedTranslation(String key)
	{
		String trans = translateToLocal(key);

		return Arrays.asList(wrap(trans, 40));
	}

	@SuppressWarnings("all")
	public static String[] wrap(String input, int len)
	{
		// return empty array for null text
		if (input == null)
		{
			return new String[] {};
		}

		// return text if len is zero or less
		if (len <= 0)
		{
			return new String[] { input };
		}

		// return text if less than length
		if (input.length() <= len)
		{
			return new String[] { input };
		}

		char[] chars = input.toCharArray();
		Vector lines = new Vector();
		StringBuffer line = new StringBuffer();
		StringBuffer word = new StringBuffer();

		for (char c : chars)
		{
			if (c == '|')
			{
				line.append(word);
				word.delete(0, word.length());

				lines.add(line.toString());
				line.delete(0, line.length());

			}
			else
			{
				word.append(c);
			}

			if (c == ' ')
			{
				if ((line.length() + word.length()) > len)
				{
					lines.add(line.toString());
					line.delete(0, line.length());
				}

				line.append(word);
				word.delete(0, word.length());
			}
		}

		// handle any extra chars in current word
		if (word.length() > 0)
		{
			if ((line.length() + word.length()) > len)
			{
				lines.add(line.toString());
				line.delete(0, line.length());
			}
			line.append(word);
		}

		// handle extra line
		if (line.length() > 0)
		{
			lines.add(line.toString());
		}

		String[] ret = new String[lines.size()];
		int c = 0; // counter
		for (Enumeration e = lines.elements(); e.hasMoreElements(); c++)
		{
			ret[c] = (String) e.nextElement();
		}

		return ret;
	}
}
