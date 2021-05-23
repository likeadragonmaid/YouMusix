/*****************************************************************************
 * youmusix/About.java: About JFrame for YouMusix
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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.net.URISyntaxException;
import java.net.URL;

@SuppressWarnings("serial")
public class About extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					About frame = new About();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public About() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/graphics/YouMusix_Logo.png")));
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("About");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 540, 335);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnGitHub = new JButton("Fork me on GitHub");
		btnGitHub.setToolTipText("Access YouMusix repository on GitHub");
		btnGitHub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URL("https://github.com/gibcheesepuffs/YouMusix/").toURI());
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});

		JLabel lblMyname = new JLabel("Shou");
		lblMyname.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URL("https://shoukolate.ml/").toURI());
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblMyname.setForeground(Color.BLUE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblMyname.setForeground(Color.RED);
			}
		});

		JLabel lblDeveloper = new JLabel("Developer");
		lblDeveloper.setFont(new Font("Dialog", Font.BOLD, 16));
		lblDeveloper.setBounds(25, 12, 338, 24);
		contentPane.add(lblDeveloper);

		JLabel lblApplicationVersion = new JLabel("Version: Experimental build 180104");
		lblApplicationVersion.setFont(new Font("Dialog", Font.BOLD, 14));
		lblApplicationVersion.setBounds(25, 60, 338, 18);
		contentPane.add(lblApplicationVersion);
		lblMyname.setForeground(Color.RED);
		lblMyname.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 16));
		lblMyname.setBounds(25, 36, 338, 24);
		contentPane.add(lblMyname);
		btnGitHub.setBounds(25, 92, 175, 35);
		contentPane.add(btnGitHub);

		JLabel lblGoogle = new JLabel(
				"YouTube, is trademark of Google, Inc. We are not affliated with Google, Inc. in any way.");
		lblGoogle.setHorizontalAlignment(SwingConstants.LEFT);
		lblGoogle.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblGoogle.setBounds(25, 191, 495, 24);
		contentPane.add(lblGoogle);

		JLabel lblAppLogo = new JLabel("");
		lblAppLogo.setIcon(new ImageIcon(About.class.getResource("/graphics/YouMusix_Logo.png")));
		lblAppLogo.setBounds(375, 12, 145, 125);
		contentPane.add(lblAppLogo);

		JLabel lblIconNotice = new JLabel(
				"Material design icons are licensed under Apache License 2.0 and are provided by Google, Inc.");
		lblIconNotice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URL("https://material.io/icons/").toURI());
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblIconNotice.setForeground(Color.BLUE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblIconNotice.setForeground(new Color(51, 51, 51));
			}
		});
		lblIconNotice.setHorizontalAlignment(SwingConstants.LEFT);
		lblIconNotice.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblIconNotice.setBounds(25, 170, 495, 24);
		contentPane.add(lblIconNotice);

		JLabel lblJLayerNotice = new JLabel(
				"JLayer decoder library has been used for decoding the MP3 stream which is licensed under LGPL.");
		lblJLayerNotice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblJLayerNotice.setForeground(Color.BLUE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblJLayerNotice.setForeground(new Color(51, 51, 51));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URL("http://www.javazoom.net/javalayer/sources.html").toURI());
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		lblJLayerNotice.setHorizontalAlignment(SwingConstants.LEFT);
		lblJLayerNotice.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblJLayerNotice.setBounds(25, 149, 495, 24);
		contentPane.add(lblJLayerNotice);

		JLabel lblRightInYourFace = new JLabel("Q. Having problems with playback/streaming/shuttering sound?");
		lblRightInYourFace.setBounds(25, 223, 495, 15);
		contentPane.add(lblRightInYourFace);

		JLabel lblGoComplainTo = new JLabel("A. Complain at webmaster@convertmp3.io as their API is buggy.");
		lblGoComplainTo.setBounds(25, 238, 495, 15);
		contentPane.add(lblGoComplainTo);

		JLabel lblComplainToConvertmp3io = new JLabel("     It produces random errors while downloading MP3 stream!");
		lblComplainToConvertmp3io.setBounds(25, 250, 495, 15);
		contentPane.add(lblComplainToConvertmp3io);

		JLabel lblStreamsSourceBy = new JLabel("* Streams are provided by convertmp3.io/");
		lblStreamsSourceBy.setBounds(25, 276, 495, 15);
		contentPane.add(lblStreamsSourceBy);
	}
}
