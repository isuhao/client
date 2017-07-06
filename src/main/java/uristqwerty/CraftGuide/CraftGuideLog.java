package uristqwerty.CraftGuide;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class CraftGuideLog
{
	private static PrintWriter output;
	private static int exceptionsLogged = 0;
	private static final int EXCEPTION_LIMIT = 1000;

	public static void init(File file)
	{
		try
		{
			output = new PrintWriter(file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void log(String text)
	{
		log(text, false);
	}

	public static void log(String text, boolean console)
	{
		if(console && CraftGuide.loaderSide != null)
		{
			CraftGuide.loaderSide.logConsole(text);
		}

		output.println(text);
		output.flush();
	}

	public static void log(Throwable e)
	{
		log(e, "", false);
	}

	public static void log(Throwable e, String text, boolean console)
	{
		if(exceptionsLogged <= EXCEPTION_LIMIT)
		{
			if(console && CraftGuide.loaderSide != null)
			{
				CraftGuide.loaderSide.logConsole(text, e);
			}

			output.println(text);
			e.printStackTrace(output);
			output.flush();

			if(exceptionsLogged == EXCEPTION_LIMIT)
			{
				log("Exception limit passed. To prevent excessively large log files, no further exceptions will be logged.");
			}

			exceptionsLogged++;
		}
	}

	public static void checkGlError()
	{
		int i = GL11.glGetError();

		if (i != 0)
		{
			String str = GLU.gluErrorString(i);
			log(new Throwable(), "Encountered OpenGL error #" + i + ": " + str, true);
		}
	}
}
