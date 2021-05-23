/***********************************************************************************
 * youmusix/AppSettings.java: Settings loader and writer for YouMusix
 ***********************************************************************************
 * MIT License
 *
 * Copyright (c) 2019 Shou
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **********************************************************************************/

package youmusix;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class AppSettings {

	static String RepeatMusic = null;

	public static void Write_the_default_settings() {
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			output = new FileOutputStream(".config.properties");
			prop.setProperty("RepeatMusic", "false");
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void Load_the_settings() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(".config.properties");
			prop.load(input);
			RepeatMusic = prop.getProperty("RepeatMusic");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void Write_Repeating_the_music_is_enabled() {
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			output = new FileOutputStream(".config.properties");
			prop.setProperty("RepeatMusic", "true");
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void Write_Repeating_the_music_is_disabled() {
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			output = new FileOutputStream(".config.properties");
			prop.setProperty("RepeatMusic", "false");
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
