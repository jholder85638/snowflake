/**
 * 
 */
package zmcommand.app.ui.components.session.zmutilpage.sysinfo;

import java.awt.Font;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import zmcommand.app.ui.components.SkinnedScrollPane;
import zmcommand.app.ui.components.SkinnedTextArea;
import zmcommand.app.ui.components.session.SessionContentPanel;
import util.ScriptLoader;
import zmcommand.app.ui.components.session.zmutilpage.ZMUtilPageItemView;

/**
 * @author subhro
 *
 */
public class SysInfoPanel extends ZMUtilPageItemView {
	/**
	 * 
	 */

	private JTextArea textArea;

	public SysInfoPanel(SessionContentPanel holder) {
		super(holder);
	}

	@Override
	protected void createUI() {
		textArea = new SkinnedTextArea();
		textArea.setFont(new Font(//"DejaVu Sans Mono"
				"Noto Mono"
				, Font.PLAIN, 14));
		JScrollPane scrollPane = new SkinnedScrollPane(textArea);
		this.add(scrollPane);

		AtomicBoolean stopFlag = new AtomicBoolean(false);
		holder.disableUi(stopFlag);
		holder.EXECUTOR.submit(() -> {
			try {
				StringBuilder output = new StringBuilder();
				int ret = holder
						.getRemoteSessionInstance().exec(
								ScriptLoader.loadShellScript(
										"/scripts/linux-sysinfo.sh"),
								stopFlag, output);
				if (ret == 0) {
					SwingUtilities.invokeAndWait(() -> {
						textArea.setText(output.toString());
						textArea.setCaretPosition(0);
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				holder.enableUi();
			}
		});
	}

	@Override
	protected void onComponentVisible() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onComponentHide() {
		// TODO Auto-generated method stub

	}
}
