/***********************************************************************************
 * youmusix/Client.java: YouMusix client
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

import java.awt.EventQueue;

import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.awt.image.BufferedImage;

import java.awt.Toolkit;

import java.awt.event.ActionEvent;

import java.awt.Color;

import java.awt.Desktop;

import javax.swing.JFrame;

import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;

import org.json.JSONException;

import org.json.JSONObject;

import org.json.JSONTokener;

import javax.swing.ImageIcon;

import javax.swing.JButton;

import javax.swing.JFileChooser;

import javax.swing.JLabel;

import javax.swing.JMenuBar;

import javax.swing.JMenu;

import javax.swing.JMenuItem;

import javax.swing.JOptionPane;

import javax.swing.JTextField;

import javax.swing.SwingConstants;

import javax.imageio.ImageIO;

import java.net.HttpURLConnection;

import java.net.MalformedURLException;

import java.net.URISyntaxException;

import java.net.URL;

import java.net.URLConnection;

import java.util.concurrent.TimeUnit;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

import java.io.BufferedInputStream;

import java.io.File;

import java.io.FileOutputStream;

import java.io.IOException;

import javazoom.jl.player.Player;

import javazoom.jl.decoder.JavaLayerException;

import java.awt.event.KeyAdapter;

import java.awt.event.KeyEvent;

@SuppressWarnings("serial")

public class Client extends JFrame {

	JLabel lblAppStatus, lblThumbnail, lblBackground, lblDownloadStatus;

	String VideoID, RepeatMusic;

	JTextField VideoURL;

	JPanel contentPane;

	JButton btnPlay, btnStop, btnPause, btnResume, btnRepeatDisabled, btnRepeatEnabled, btnDownload, btnCancelDownload,

			btnPauseDownload, btnResumeDownload;

	Thread thread, DownloadThread;

	Boolean InitialURLBarClick = false;

	JFileChooser fileChooser;

	static Boolean debugging = false;

	static JLabel lblElapsedTime;

	static Player mp3player;

	static Runnable BackgroundThread;

	static long millis;

	static String hms;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				Client frame = new Client();

				frame.setVisible(true);

				BackgroundThread = new Runnable() {

					public void run() {

						boolean temp = true;

						while (temp == true) {

							millis = 0;

							millis = mp3player.getPosition();

							hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),

									TimeUnit.MILLISECONDS.toMinutes(millis)

											- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),

									TimeUnit.MILLISECONDS.toSeconds(millis)

											- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

							lblElapsedTime.setText(hms);

						}

					}

				};

			}

		});

	}

	public Client() {

		setResizable(false);

		setIconImage(Toolkit.getDefaultToolkit().getImage(Client.class.getResource("/graphics/YouMusix_Logo.png")));

		setTitle("YouMusix \u266A");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 608, 342);

		JMenuBar menuBar = new JMenuBar();

		setJMenuBar(menuBar);

		JMenu mnMenu = new JMenu("Menu");

		menuBar.add(mnMenu);

		JMenuItem mntmExit = new JMenuItem("Exit");

		mntmExit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				System.exit(0);

			}

		});

		mnMenu.add(mntmExit);

		JMenu mnTools = new JMenu("Tools");

		menuBar.add(mnTools);

		JMenuItem mntmCalculateMpStream = new JMenuItem("Calculate MP3 stream size");

		mntmCalculateMpStream.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				String VideoURL = JOptionPane

						.showInputDialog("Enter YouTube video URL, ex. https://www.youtube.com/watch?v=EP625xQIGzs");

				try {

					String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

					Pattern compiledPattern = Pattern.compile(pattern);

					Matcher matcher = compiledPattern.matcher(VideoURL);

					if (matcher.find()) {

						long size;

						URL url = new URL("https://convertmp3.io/fetch/?video=" + VideoURL);

						URLConnection conn = url.openConnection();

						size = conn.getContentLength();

						size = size / 1024 / 1024;

						if (size < 0) {

							JOptionPane.showMessageDialog(null, "Error");

						} else {

							JOptionPane.showMessageDialog(null, "MP3 stream size: ~" + size + " MB");

						}

						conn.getInputStream().close();

					} else {

						JOptionPane.showMessageDialog(null, "Please enter a valid URL", "Error",

								JOptionPane.ERROR_MESSAGE);

					}

				} catch (IOException e) {

					if (debugging == true) {

						System.out.println("Error occured while calculating MP3 stream size\n");

						e.printStackTrace();

					}

				}

			}

		});

		mnTools.add(mntmCalculateMpStream);

		JMenuItem mntmSeverStatus = new JMenuItem("Sever status");

		mntmSeverStatus.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				int responseCode = 0;

				try {

					String ServerStatusCheck = "https://convertmp3.io/";

					HttpURLConnection connection = (HttpURLConnection) new URL(ServerStatusCheck).openConnection();

					connection.setRequestMethod("HEAD");

					responseCode = connection.getResponseCode();

					if ((responseCode == 200)) {

						JOptionPane.showMessageDialog(null, "Server is working normally");

					} else {

						JOptionPane.showMessageDialog(null, "Service currently unavailable!");

					}

				} catch (IOException e) {

					if (debugging == true) {

						System.out.println("Server returned the code: " + responseCode);

						System.out.println("Error occured while checking server status");

						e.printStackTrace();

					}

				}

			}

		});

		mnTools.add(mntmSeverStatus);

		JMenuItem mntmToggleDebugger = new JMenuItem("Toggle Debugger");

		mntmToggleDebugger.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				if (debugging == false) {

					debugging = true;

					JOptionPane.showMessageDialog(null,

							"Debugger is now activated.\nDebug log will be displayed in console!\nSo, make sure that you are running application\nfrom terminal or command prompt!");

				} else {

					debugging = false;

					JOptionPane.showMessageDialog(null, "Debugger is now deactivated");

				}

			}

		});

		mnTools.add(mntmToggleDebugger);

		JMenu mnHelp = new JMenu("Help");

		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");

		mntmAbout.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				About obj = new About();

				obj.setVisible(true);

			}

		});

		mnHelp.add(mntmAbout);

		JMenu mnNewMenu = new JMenu("License");

		mnHelp.add(mnNewMenu);

		JMenuItem mntmCommonioLicense = new JMenuItem("Commons-io License");

		mntmCommonioLicense.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				try {

					Desktop.getDesktop().browse(new URL("http://www.apache.org/licenses/").toURI());

				} catch (IOException | URISyntaxException e) {

					e.printStackTrace();

				}

			}

		});

		mnNewMenu.add(mntmCommonioLicense);

		JMenuItem mntmJlayerLicense = new JMenuItem("JLayer License");

		mntmJlayerLicense.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {

					Desktop.getDesktop().browse(new URL("http://www.javazoom.net/javalayer/about.html").toURI());

				} catch (IOException | URISyntaxException e1) {

					e1.printStackTrace();

				}

			}

		});

		JMenuItem mntmJSONjavaLicense = new JMenuItem("JSON-java License");

		mntmJSONjavaLicense.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				try {

					Desktop.getDesktop().browse(

							new URL("https://raw.githubusercontent.com/stleary/JSON-java/master/LICENSE").toURI());

				} catch (IOException | URISyntaxException e) {

					e.printStackTrace();

				}

			}

		});

		mnNewMenu.add(mntmJSONjavaLicense);

		mnNewMenu.add(mntmJlayerLicense);

		JMenuItem mntmMaterialIconsLicense = new JMenuItem("Material Icons License");

		mntmMaterialIconsLicense.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				try {

					Desktop.getDesktop().browse(new URL("https://material.io/icons/").toURI());

				} catch (IOException | URISyntaxException e) {

					e.printStackTrace();

				}

			}

		});

		mnNewMenu.add(mntmMaterialIconsLicense);

		JMenuItem mntmLicense = new JMenuItem("YouMusix License");

		mnNewMenu.add(mntmLicense);

		mntmLicense.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				License obj = new License();

				obj.setVisible(true);

			}

		});

		contentPane = new JPanel();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		contentPane.setLayout(null);

		VideoURL = new JTextField();

		VideoURL.addKeyListener(new KeyAdapter() {

			@Override

			public void keyTyped(KeyEvent arg0) {

				btnDownload.setVisible(true);

			}

		});

		VideoURL.addMouseListener(new MouseAdapter() {

			@Override

			public void mouseClicked(MouseEvent arg0) {

				if (InitialURLBarClick == false) {

					VideoURL.setText("");

					InitialURLBarClick = true;

				}

			}

		});

		btnRepeatDisabled = new JButton("");

		btnRepeatDisabled.addMouseListener(new MouseAdapter() {

			@Override

			public void mouseEntered(MouseEvent e) {

				btnRepeatDisabled.setIcon(new ImageIcon(Client.class.getResource("/graphics/No_Repeat_Hower.png")));

			}

			@Override

			public void mouseExited(MouseEvent e) {

				btnRepeatDisabled.setIcon(new ImageIcon(Client.class.getResource("/graphics/No_Repeat_Standard.png")));

			}

		});

		btnRepeatDisabled.setOpaque(false);

		btnRepeatDisabled.setContentAreaFilled(false);

		btnRepeatDisabled.setBorderPainted(false);

		btnRepeatDisabled.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				btnRepeatDisabled.setVisible(false);

				btnRepeatEnabled.setVisible(true);

				AppSettings.Write_Repeating_the_music_is_enabled();

			}

		});

		btnDownload = new JButton("Download");

		btnDownload.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				DownloadThread.start();

				btnResumeDownload.setVisible(false);

			}

		});

		JButton btnClearURL = new JButton("Clear");

		btnClearURL.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				VideoURL.setText("");

				if (InitialURLBarClick == false) {

					InitialURLBarClick = true;

				}

			}

		});

		btnClearURL.setBounds(525, 0, 75, 30);

		contentPane.add(btnClearURL);

		JLabel lblStreamTitle = new JLabel("");

		lblStreamTitle.setBounds(168, 137, 420, 15);

		contentPane.add(lblStreamTitle);

		btnDownload.setBounds(470, 215, 120, 25);

		contentPane.add(btnDownload);

		btnDownload.setVisible(false);

		lblDownloadStatus = new JLabel("");

		lblDownloadStatus.setHorizontalAlignment(SwingConstants.CENTER);

		lblDownloadStatus.setBounds(232, 257, 200, 15);

		contentPane.add(lblDownloadStatus);

		btnResumeDownload = new JButton("Resume");

		btnResumeDownload.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")

			public void actionPerformed(ActionEvent e) {

				DownloadThread.resume();

				btnResumeDownload.setVisible(false);

				btnPauseDownload.setVisible(true);

				btnCancelDownload.setVisible(true);

			}

		});

		btnResumeDownload.setBounds(470, 215, 120, 25);

		contentPane.add(btnResumeDownload);

		btnResumeDownload.setVisible(false);

		btnPauseDownload = new JButton("Pause");

		btnPauseDownload.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")

			public void actionPerformed(ActionEvent arg0) {

				DownloadThread.suspend();

				lblDownloadStatus.setText("Download Paused");

				btnPauseDownload.setVisible(false);

				btnResumeDownload.setVisible(true);

				btnCancelDownload.setVisible(true);

			}

		});

		btnPauseDownload.setBounds(470, 215, 120, 25);

		contentPane.add(btnPauseDownload);

		btnPauseDownload.setVisible(false);

		btnCancelDownload = new JButton("Cancel");

		btnCancelDownload.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				DownloadThread.interrupt();

				File file = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".mp3");

				if (file.exists() && !file.isDirectory()) {

					file.delete();

				}

				btnCancelDownload.setVisible(false);

				btnResumeDownload.setVisible(false);

				btnPauseDownload.setVisible(false);

				btnDownload.setVisible(true);

				lblDownloadStatus.setText("Download Canceled");

			}

		});

		btnCancelDownload.setBounds(470, 252, 120, 25);

		contentPane.add(btnCancelDownload);

		btnCancelDownload.setVisible(false);

		lblElapsedTime = new JLabel("");

		lblElapsedTime.setHorizontalAlignment(SwingConstants.CENTER);

		lblElapsedTime.setBounds(248, 37, 153, 21);

		contentPane.add(lblElapsedTime);

		btnRepeatDisabled.setIcon(new ImageIcon(Client.class.getResource("/graphics/No_Repeat_Standard.png")));

		btnRepeatDisabled.setBounds(337, 75, 64, 50);

		contentPane.add(btnRepeatDisabled);

		btnRepeatEnabled = new JButton("");

		btnRepeatEnabled.addMouseListener(new MouseAdapter() {

			@Override

			public void mouseEntered(MouseEvent arg0) {

				btnRepeatEnabled.setIcon(new ImageIcon(Client.class.getResource("/graphics/Repeat_Hover.png")));

			}

			@Override

			public void mouseExited(MouseEvent e) {

				btnRepeatEnabled.setIcon(new ImageIcon(Client.class.getResource("/graphics/Repeat_Standard.png")));

			}

		});

		btnRepeatEnabled.setOpaque(false);

		btnRepeatEnabled.setContentAreaFilled(false);

		btnRepeatEnabled.setBorderPainted(false);

		btnRepeatEnabled.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				btnRepeatEnabled.setVisible(false);

				btnRepeatDisabled.setVisible(true);

				AppSettings.Write_Repeating_the_music_is_disabled();

			}

		});

		btnRepeatEnabled.setIcon(new ImageIcon(Client.class.getResource("/graphics/Repeat_Standard.png")));

		btnRepeatEnabled.setBounds(337, 75, 64, 50);

		contentPane.add(btnRepeatEnabled);

		lblThumbnail = new JLabel("");

		lblThumbnail.setBounds(470, 37, 120, 80);

		contentPane.add(lblThumbnail);

		VideoURL.setForeground(Color.GRAY);

		VideoURL.setText("Enter YouTube video URL, ex. https://www.youtube.com/watch?v=EP625xQIGzs");

		VideoURL.setBounds(0, 0, 529, 30);

		contentPane.add(VideoURL);

		VideoURL.setColumns(10);

		btnPlay = new JButton("");

		btnPlay.setVisible(true);

		btnPlay.addMouseListener(new MouseAdapter() {

			@Override

			public void mouseEntered(MouseEvent arg0) {

				btnPlay.setIcon(new ImageIcon(Client.class.getResource("/graphics/Play_Hover.png")));

			}

			@Override

			public void mouseExited(MouseEvent e) {

				btnPlay.setIcon(new ImageIcon(Client.class.getResource("/graphics/Play_Standard.png")));

			}

		});

		btnPlay.setIcon(new ImageIcon(Client.class.getResource("/graphics/Play_Standard.png")));

		btnPlay.setOpaque(false);

		btnPlay.setContentAreaFilled(false);

		btnPlay.setBorderPainted(false);

		btnPlay.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				lblAppStatus.setText("Now Playing...");

				thread = new Thread(new Runnable() {

					@Override

					public void run() {

						BufferedInputStream in = null;

						try {

							URL Video = new URL("https://www.convertmp3.io/fetch/?video=" + VideoURL.getText());

							String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

							Pattern compiledPattern = Pattern.compile(pattern);

							Matcher matcher = compiledPattern.matcher(VideoURL.getText());

							if (matcher.find()) {

								VideoID = matcher.group();

								@SuppressWarnings("deprecation")

								JSONObject jo = (JSONObject) new JSONTokener(IOUtils.toString(new URL(
										"https://www.convertmp3.io/fetch/?format=JSON&video=" + VideoURL.getText())))
												.nextValue();

								lblStreamTitle.setText(jo.getString("title"));

								setTitle("YouMusix \u266A - " + jo.getString("title"));

								URL thumbnailurl = new URL("https://i1.ytimg.com/vi/" + VideoID + "/default.jpg");

								BufferedImage image = ImageIO.read(thumbnailurl);

								lblThumbnail.setIcon(new ImageIcon(image));

								System.out.println("working");

							}

							in = new BufferedInputStream(Video.openStream());

							mp3player = new Player(in);

							new Thread(BackgroundThread).start();

							mp3player.play();

							mp3player.close();

							thread.interrupt();

							if (RepeatMusic.equalsIgnoreCase("true")) {

								btnStop.doClick();

								btnPlay.doClick();

							}

							if (RepeatMusic.equalsIgnoreCase("false")) {

								lblAppStatus.setText("Stopped");

							}

						} catch (JavaLayerException DecoderError) {

							lblAppStatus.setText("Connection error! Trying again...");

							btnStop.doClick();

							lblAppStatus.setText("Playing...");

							thread.start();

							lblAppStatus.setText("Stopped");

							if (debugging == true) {

								System.out.println("Decoding Error(s):\n");

								DecoderError.printStackTrace();

							}

						} catch (IOException | JSONException e) {

							if (debugging == true) {

								e.printStackTrace();

							}

						}

					}

				});

				if (VideoURL.getText().startsWith("https://")) {

					btnResume.setVisible(false);

					btnPause.setVisible(true);

					btnStop.setVisible(true);

					btnPlay.setVisible(false);

					thread.start();

				} else {

					lblAppStatus.setText("Please enter a valid URL");

					if (debugging == true) {

						System.out.println("Please enter a valid URL");

					}

				}

			}

		});

		btnPlay.setBounds(232, 75, 75, 50);

		contentPane.add(btnPlay);

		btnPause = new JButton("");

		btnPause.setVisible(false);

		btnPause.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {

				btnPause.setIcon(new ImageIcon(Client.class.getResource("/graphics/Pause_Hover.png")));

			}

			public void mouseExited(MouseEvent e) {

				btnPause.setIcon(new ImageIcon(Client.class.getResource("/graphics/Pause_Standard.png")));

			}

		});

		btnPause.setIcon(new ImageIcon(Client.class.getResource("/graphics/Pause_Standard.png")));

		btnPause.setOpaque(false);

		btnPause.setContentAreaFilled(false);

		btnPause.setBorderPainted(false);

		btnPause.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")

			public void actionPerformed(ActionEvent arg0) {

				thread.suspend();

				btnResume.setVisible(true);

				btnPause.setVisible(false);

				btnStop.setVisible(true);

				lblAppStatus.setText("Paused");

			}

		});

		btnPause.setBounds(232, 75, 75, 50);

		contentPane.add(btnPause);

		btnResume = new JButton("");

		btnResume.setVisible(false);

		btnResume.addMouseListener(new MouseAdapter() {

			@Override

			public void mouseEntered(MouseEvent e) {

				btnResume.setIcon(new ImageIcon(Client.class.getResource("/graphics/Play_Hover.png")));

			}

			@Override

			public void mouseExited(MouseEvent e) {

				btnResume.setIcon(new ImageIcon(Client.class.getResource("/graphics/Play_Standard.png")));

			}

		});

		btnResume.setIcon(new ImageIcon(Client.class.getResource("/graphics/Play_Standard.png")));

		btnResume.setOpaque(false);

		btnResume.setContentAreaFilled(false);

		btnResume.setBorderPainted(false);

		btnResume.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")

			public void actionPerformed(ActionEvent e) {

				lblAppStatus.setText("Now Playing...");

				btnResume.setVisible(false);

				btnPause.setVisible(true);

				thread.resume();

			}

		});

		btnResume.setBounds(232, 75, 75, 50);

		contentPane.add(btnResume);

		btnStop = new JButton("");

		btnStop.setVisible(false);

		btnStop.addMouseListener(new MouseAdapter() {

			@Override

			public void mouseEntered(MouseEvent e) {

				btnStop.setIcon(new ImageIcon(Client.class.getResource("/graphics/Stop_Hover.png")));

			}

			@Override

			public void mouseExited(MouseEvent e) {

				btnStop.setIcon(new ImageIcon(Client.class.getResource("/graphics/Stop_Standard.png")));

			}

		});

		btnStop.setIcon(new ImageIcon(Client.class.getResource("/graphics/Stop_Standard.png")));

		btnStop.setOpaque(false);

		btnStop.setContentAreaFilled(false);

		btnStop.setBorderPainted(false);

		btnStop.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")

			public void actionPerformed(ActionEvent e) {

				btnPlay.setVisible(true);

				btnPause.setVisible(false);

				btnResume.setVisible(false);

				thread.resume();

				mp3player.close();

				thread.interrupt();

				btnStop.setVisible(false);

				setTitle("YouMusix \u266A");

				lblStreamTitle.setText("");

				lblThumbnail.setIcon(null);

				lblAppStatus.setText("Stopped");

			}

		});

		btnStop.setBounds(288, 75, 75, 50);

		contentPane.add(btnStop);

		lblAppStatus = new JLabel("");

		lblAppStatus.setHorizontalAlignment(SwingConstants.CENTER);

		lblAppStatus.setBounds(232, 215, 200, 24);

		contentPane.add(lblAppStatus);

		lblBackground = new JLabel("");

		lblBackground.setIcon(new ImageIcon(Client.class.getResource("/graphics/Background.png")));

		lblBackground.setBounds(0, 0, 608, 342);

		contentPane.add(lblBackground);

		btnRepeatEnabled.setVisible(false);

		btnRepeatDisabled.setVisible(false);

		AppSettings.Load_the_settings();

		RepeatMusic = AppSettings.RepeatMusic;

		if (RepeatMusic.equalsIgnoreCase("true")) {

			btnRepeatEnabled.setVisible(true);

		}

		if (RepeatMusic.equalsIgnoreCase("false")) {

			btnRepeatDisabled.setVisible(true);

		}

		DownloadThread = new Thread(new Runnable() {

			@Override

			public void run() {

				String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

				Pattern compiledPattern = Pattern.compile(pattern);

				Matcher matcher = compiledPattern.matcher(VideoURL.getText());

				if (matcher.find()) {

					btnDownload.setVisible(false);

					JFrame parentFrame = new JFrame();

					fileChooser = new JFileChooser();

					FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 Files", "mp3");

					fileChooser.setFileFilter(filter);

					fileChooser.setDialogTitle("Save as a MP3 file");

					int userSelection = fileChooser.showSaveDialog(parentFrame);

					if (userSelection == JFileChooser.APPROVE_OPTION) {

						lblDownloadStatus.setText("Starting download...");

						BufferedInputStream in = null;

						FileOutputStream out = null;

						try {

							URL url = new URL("https://www.convertmp3.io/fetch/?video=" + VideoURL);

							URLConnection conn = url.openConnection();

							int bytesize = conn.getContentLength();

							float mbsize = bytesize / 1024 / 1024;

							if (bytesize < 0) {

								lblDownloadStatus.setText("Error occured! Try again!");

								if (debugging == true) {

									System.out.println("An error occured while downloading!");

								}

								DownloadThread.interrupt();

								btnDownload.setVisible(true);

							} else {

								lblDownloadStatus.setText("File Size: ~" + mbsize + " MB");

								btnCancelDownload.setVisible(true);

								btnPauseDownload.setVisible(true);

							}

							in = new BufferedInputStream(url.openStream());

							out = new FileOutputStream(fileChooser.getSelectedFile().getAbsolutePath() + ".mp3");

							byte data[] = new byte[1024];

							int count;

							float sumCount = 0;

							while ((count = in.read(data, 0, 1024)) != -1) {

								out.write(data, 0, count);

								sumCount += count;

								int percentage = (int) (sumCount / bytesize * 100.0);

								if (bytesize > 0) {

									lblDownloadStatus.setText("Downloading: " + percentage + "%");

									if (percentage == 100) {

										lblDownloadStatus.setText("Download Completed");

										DownloadThread.interrupt();

										btnDownload.setVisible(true);

										btnPauseDownload.setVisible(false);

										btnResumeDownload.setVisible(false);

										btnCancelDownload.setVisible(false);

									}

								}

							}

						} catch (MalformedURLException e1) {

							if (debugging == true) {

								e1.printStackTrace();

							}

						} catch (IOException e2) {

							if (debugging == true) {

								e2.printStackTrace();

							}

						} finally {

							if (in != null)

								try {

									in.close();

									btnDownload.setVisible(true);

								} catch (IOException e3) {

									if (debugging == true) {

										e3.printStackTrace();

									}

								}

							if (out != null)

								try {

									out.close();

								} catch (IOException e4) {

									if (debugging == true) {

										e4.printStackTrace();

									}

								}

						}

					}

				} else {

					lblDownloadStatus.setText("Please enter a valid URL");

				}

			}

		});

	}

}
