/**
 * 
 */
package zmcommand.app.ui.components.session.zmutilpage;

import java.awt.BorderLayout;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import zmcommand.app.ui.components.session.SessionContentPanel;

/**
 * @author subhro
 *
 */
public abstract class ZMUtilPageItemView extends JPanel {
	protected AtomicBoolean init = new AtomicBoolean(false);
	protected SessionContentPanel holder;

	/**
	 * 
	 */
	protected ZMUtilPageItemView(SessionContentPanel holder) {
		super(new BorderLayout());
		this.holder = holder;
		addAncestorListener(new AncestorListener() {

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				onComponentHide();
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
			}

			@Override
			public void ancestorAdded(AncestorEvent event) {
				if (!init.get()) {
					init.set(true);
					createUI();
					revalidate();
					repaint(0);
				}
				onComponentVisible();
			}
		});
	}

	protected abstract void createUI();

	protected abstract void onComponentVisible();

	protected abstract void onComponentHide();
}
