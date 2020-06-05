/**
 * 
 */
package zmcommand.app.ui.components.session.zmutilpage;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import zmcommand.app.App;
import zmcommand.app.ui.components.SkinnedScrollPane;
import zmcommand.app.ui.components.session.Page;
import zmcommand.app.ui.components.session.SessionContentPanel;
import zmcommand.app.ui.components.session.utilpage.UtilityPageButton;
import zmcommand.app.ui.components.session.zmutilpage.keys.KeyPage;
import zmcommand.app.ui.components.session.zmutilpage.nettools.NetworkToolsPage;
import zmcommand.app.ui.components.session.zmutilpage.portview.PortViewer;
import zmcommand.app.ui.components.session.zmutilpage.services.ServicePanel;
import zmcommand.app.ui.components.session.zmutilpage.sysinfo.SysInfoPanel;
import zmcommand.app.ui.components.session.zmutilpage.sysload.ZMSysLoadPage;
import util.FontAwesomeContants;
import util.LayoutUtilities;

/**
 * @author subhro
 *
 */
public class ZMUtilityPage extends Page {
	private CardLayout cardLayout;
	private JPanel cardPanel;
	private AtomicBoolean init = new AtomicBoolean(false);
	private SessionContentPanel holder;

	/**
	 * 
	 */
	public ZMUtilityPage(SessionContentPanel holder) {
		super(new BorderLayout());
		this.holder = holder;
	}

	@Override
	public void onLoad() {
		if (!init.get()) {
			init.set(true);
			createUI();
		}
	}

	@Override
	public String getIcon() {
		return FontAwesomeContants.FA_BRIEFCASE;
		// return FontAwesomeContants.FA_SLIDERS;
	}

	@Override
	public String getText() {
		return "Zimbra Toolbox";
	}

	/**
	 * 
	 */
	private void createUI() {
		ButtonGroup bg = new ButtonGroup();
		Box vbox = Box.createVerticalBox();
		UtilityPageButton zimbraInfo = new UtilityPageButton("Zimbra info",
				FontAwesomeContants.FA_LINUX);

		UtilityPageButton JVMINfo = new UtilityPageButton("JVM Info",
				FontAwesomeContants.FA_AREA_CHART);

		UtilityPageButton mailQueueButton = new UtilityPageButton("Mail Queue",
				FontAwesomeContants.FA_MAILQUEUE);

		UtilityPageButton ServerConfigButton = new UtilityPageButton("Server Configuration",
				FontAwesomeContants.FA_JAVA);

		UtilityPageButton b5 = new UtilityPageButton("Process and ports",
				FontAwesomeContants.FA_DATABASE);

		UtilityPageButton b6 = new UtilityPageButton("SSH keys",
				FontAwesomeContants.FA_KEY);

		UtilityPageButton b7 = new UtilityPageButton("Network tools",
				FontAwesomeContants.FA_WRENCH);

		LayoutUtilities.equalizeSize(zimbraInfo, JVMINfo, mailQueueButton, ServerConfigButton, b5, b6);

		vbox.setBorder(
				new MatteBorder(0, 0, 0, 1, App.SKIN.getDefaultBorderColor()));

		zimbraInfo.setAlignmentX(Box.LEFT_ALIGNMENT);
		vbox.add(zimbraInfo);

		JVMINfo.setAlignmentX(Box.LEFT_ALIGNMENT);
		vbox.add(JVMINfo);

		mailQueueButton.setAlignmentX(Box.LEFT_ALIGNMENT);
		vbox.add(mailQueueButton);

		ServerConfigButton.setAlignmentX(Box.LEFT_ALIGNMENT);
		vbox.add(ServerConfigButton);

		b5.setAlignmentX(Box.LEFT_ALIGNMENT);
		vbox.add(b5);

		b6.setAlignmentX(Box.LEFT_ALIGNMENT);
		vbox.add(b6);

		b7.setAlignmentX(Box.LEFT_ALIGNMENT);
		vbox.add(b7);

		vbox.add(Box.createVerticalGlue());

		bg.add(zimbraInfo);
		bg.add(JVMINfo);
		bg.add(mailQueueButton);
		bg.add(ServerConfigButton);
		bg.add(b5);
		bg.add(b6);
		bg.add(b7);

		JScrollPane jsp = new SkinnedScrollPane(vbox);
		jsp.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.add(jsp, BorderLayout.WEST);

		zimbraInfo.setSelected(true);

		revalidate();
		repaint();

		zimbraInfo.addActionListener(e -> {
			cardLayout.show(cardPanel, "SYS_INFO");
		});

		JVMINfo.addActionListener(e -> {
			cardLayout.show(cardPanel, "SYS_LOAD");
		});

		mailQueueButton.addActionListener(e -> {
			cardLayout.show(cardPanel, "SYSTEMD_SERVICES");
		});

		ServerConfigButton.addActionListener(e -> {
			cardLayout.show(cardPanel, "PROC_PORT");
		});

		b5.addActionListener(e -> {
			cardLayout.show(cardPanel, "SSH_KEYS");
		});

		b6.addActionListener(e -> {
			cardLayout.show(cardPanel, "NET_TOOLS");
		});

		b7.addActionListener(e -> {
			cardLayout.show(cardPanel, "NET_TOOLS");
		});

		JPanel p1 = new zmcommand.app.ui.components.session.zmutilpage.sysinfo.SysInfoPanel(holder);
		JPanel p2 = new zmcommand.app.ui.components.session.zmutilpage.sysload.ZMSysLoadPage(holder);
		JPanel p3 = new zmcommand.app.ui.components.session.zmutilpage.services.ServicePanel(holder);
		JPanel p4 = new zmcommand.app.ui.components.session.zmutilpage.portview.PortViewer(holder);
		JPanel p5 = new zmcommand.app.ui.components.session.zmutilpage.keys.KeyPage(holder);
		JPanel p6 = new zmcommand.app.ui.components.session.zmutilpage.nettools.NetworkToolsPage(holder);

		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		cardPanel.add(p1, "SYS_INFO");
		cardPanel.add(p2, "SYS_LOAD");
		cardPanel.add(p3, "SYSTEMD_SERVICES");
		cardPanel.add(p4, "PROC_PORT");
		cardPanel.add(p5, "SSH_KEYS");
		cardPanel.add(p6, "NET_TOOLS");

		this.add(cardPanel);
	}

}
