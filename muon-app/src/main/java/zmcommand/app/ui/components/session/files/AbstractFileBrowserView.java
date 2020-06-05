package zmcommand.app.ui.components.session.files;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import zmcommand.app.App;
import zmcommand.app.common.FileSystem;
import zmcommand.app.ui.components.ClosableTabContent;
import zmcommand.app.ui.components.ClosableTabbedPanel.TabTitle;
import zmcommand.app.ui.components.session.files.view.AddressBar;
import zmcommand.app.ui.components.session.files.view.DndTransferData;
import zmcommand.app.ui.components.session.files.view.FolderView;
import zmcommand.app.ui.components.session.files.view.FolderViewEventListener;
import zmcommand.app.ui.components.session.files.view.OverflowMenuHandler;
import util.LayoutUtilities;
import util.PathUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public abstract class AbstractFileBrowserView extends JPanel implements FolderViewEventListener, ClosableTabContent {
	protected AddressBar addressBar;
	protected FolderView folderView;
	protected String path;
	protected PanelOrientation orientation;
	private NavigationHistory history;
	private JButton btnBack, btnNext;
	private OverflowMenuHandler overflowMenuHandler;
	protected TabTitle tabTitle;

//	protected TransferMode transferMode;
//	protected ConflictAction conflictAction;
	protected FileBrowser fileBrowser;

	public AbstractFileBrowserView(PanelOrientation orientation, FileBrowser fileBrowser) {
		super(new BorderLayout());
		this.fileBrowser = fileBrowser;
		this.orientation = orientation;
		this.tabTitle = new TabTitle();

		UIDefaults toolbarButtonSkin = App.SKIN.createToolbarSkin();

		overflowMenuHandler = new OverflowMenuHandler(this, fileBrowser);
		history = new NavigationHistory();
		JPanel toolBar = new JPanel(new BorderLayout());
//        toolBar.setBorder(new MatteBorder(0, 1, 0, 1,
//                new Color(240, 240, 240)));
		createAddressBar();
		addressBar.addActionListener(e -> {
			String text = e.getActionCommand();
			System.out.println("Address changed: " + text + " old: " + this.path);
			if (PathUtils.isSamePath(this.path, text)) {
				System.out.println("Same text");
				return;
			}
			if (text != null && text.length() > 0) {
				addBack(this.path);
				render(text, App.getGlobalSettings().isDirectoryCache());
			}
		});
		Box smallToolbar = Box.createHorizontalBox();

		AbstractAction upAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addBack(path);
				up();
			}
		};
		AbstractAction reloadAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		};

		btnBack = new JButton();
		btnBack.putClientProperty("Nimbus.Overrides", toolbarButtonSkin);
		// btnBack.setForeground(Color.DARK_GRAY);
		btnBack.setFont(App.SKIN.getIconFont());
		btnBack.setText("\uf060");
		btnBack.addActionListener(e -> {
			String item = history.prevElement();
			addNext(this.path);
			render(item, App.getGlobalSettings().isDirectoryCache());
		});

		btnNext = new JButton();
		// btnNext.setForeground(Color.DARK_GRAY);
		btnNext.putClientProperty("Nimbus.Overrides", toolbarButtonSkin);
		btnNext.setFont(App.SKIN.getIconFont());
		btnNext.setText("\uf061");
		btnNext.addActionListener(e -> {
			String item = history.nextElement();
			addBack(this.path);
			render(item, App.getGlobalSettings().isDirectoryCache());
		});

		JButton btnHome = new JButton();
		// btnHome.setForeground(Color.DARK_GRAY);
		btnHome.putClientProperty("Nimbus.Overrides", toolbarButtonSkin);
		btnHome.setFont(App.SKIN.getIconFont());
		btnHome.setText("\uf015");
		btnHome.addActionListener(e -> {
			addBack(this.path);
			home();
		});

		JButton btnUp = new JButton();
		// btnUp.setForeground(Color.DARK_GRAY);
		btnUp.putClientProperty("Nimbus.Overrides", toolbarButtonSkin);
		btnUp.addActionListener(upAction);
		btnUp.setFont(App.SKIN.getIconFont());
		btnUp.setText("\uf062");

		smallToolbar.add(Box.createHorizontalStrut(5));

		JButton btnReload = new JButton();
		// btnReload.setForeground(Color.DARK_GRAY);
		btnReload.putClientProperty("Nimbus.Overrides", toolbarButtonSkin);
		btnReload.addActionListener(reloadAction);
		btnReload.setFont(App.SKIN.getIconFont());
		btnReload.setText("\uf021");

//        JButton btnSort = new JButton();
//        btnSort.setForeground(Color.DARK_GRAY);
//        btnSort.putClientProperty("Nimbus.Overrides", App.toolBarButtonSkin);
//        btnSort.addActionListener(e -> {
//            JPopupMenu sortMenu = overflowMenuHandler.getSortMenu();
//            sortMenu.pack();
//            Dimension d = sortMenu.getPreferredSize();
//            int x = btnSort.getWidth() - d.width;
//            int y = btnSort.getHeight();
//            sortMenu.show(btnSort, x, y);
//        });
//        btnSort.setFont(App.getFontAwesomeFont());
//        btnSort.setText("\uf161");

		JButton btnMore = new JButton();
		// btnMore.setForeground(Color.DARK_GRAY);
		btnMore.putClientProperty("Nimbus.Overrides", toolbarButtonSkin);
		btnMore.setFont(App.SKIN.getIconFont());
		btnMore.setText("\uf142");
		btnMore.addActionListener(e -> {
			JPopupMenu popupMenu = overflowMenuHandler.getOverflowMenu();
			overflowMenuHandler.loadFavourites();
			popupMenu.pack();
			Dimension d = popupMenu.getPreferredSize();
			int x = btnMore.getWidth() - d.width;
			int y = btnMore.getHeight();
			popupMenu.show(btnMore, x, y);
		});

		LayoutUtilities.equalizeSize(btnMore, btnReload, btnUp, btnHome, btnNext, btnBack);// , btnSort);

		smallToolbar.add(btnBack);
		smallToolbar.add(btnNext);
		smallToolbar.add(btnHome);
		smallToolbar.add(btnUp);

		Box b2 = Box.createHorizontalBox();
		b2.add(btnReload);
		b2.setBorder(new EmptyBorder(3, 0, 3, 0));
		b2.add(btnReload);
		// b2.add(btnSort);
		b2.add(btnMore);

		toolBar.add(smallToolbar, BorderLayout.WEST);
		toolBar.add(addressBar);
		toolBar.add(b2, BorderLayout.EAST);
		toolBar.setBorder(new EmptyBorder(5, 5, 5, 5));

		add(toolBar, BorderLayout.NORTH);

		folderView = new FolderView(this);

		this.overflowMenuHandler.setFolderView(folderView);

		add(folderView);

		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "up");
		this.getActionMap().put("up", upAction);

		updateNavButtons();

		this.fileBrowser.registerForViewNotification(this);
		
//		setBorder(new LineBorder(App.SKIN.getDefaultBorderColor(), 1));

	}

	protected abstract void createAddressBar();

	public abstract String getHostText();

	public abstract String getPathText();

	public abstract String toString();

	public boolean close() {
		System.out.println("Unregistering for view mode notification");
		this.fileBrowser.unRegisterForViewNotification(this);
		return true;
//        if (fs != null) {
//            synchronized (fileViewMap) {
//                int c = fileViewMap.get(fs);
//                if (c > 1) {
//                    fileViewMap.put(fs, c - 1);
//                } else if (c == 1) {
//                    try {
//                        fs.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
	}

	public String getCurrentDirectory() {
		return this.path;
	}

	public abstract boolean handleDrop(DndTransferData transferData);

	protected abstract void up();

	protected abstract void home();

	@Override
	public void reload() {
		this.render(this.path, false);
	}

	public PanelOrientation getOrientation() {
		return orientation;
	}

	@Override
	public void addBack(String path) {
		history.addBack(path);
		updateNavButtons();
	}

	private void addNext(String path) {
		history.addForward(this.path);
		updateNavButtons();
	}

	private void updateNavButtons() {
		btnBack.setEnabled(history.hasPrevElement());
		btnNext.setEnabled(history.hasNextElement());
	}

	public OverflowMenuHandler getOverflowMenuHandler() {
		return this.overflowMenuHandler;
	}

	public enum PanelOrientation {
		Left, Right
	}

	public abstract FileSystem getFileSystem() throws Exception;

	/**
	 * @return the tabTitle
	 */
	public TabTitle getTabTitle() {
		return tabTitle;
	}

	public void refreshViewMode() {
		this.folderView.refreshViewMode();
		this.revalidate();
		this.repaint();
	}

}
