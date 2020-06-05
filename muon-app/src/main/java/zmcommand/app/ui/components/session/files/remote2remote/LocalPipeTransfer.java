package zmcommand.app.ui.components.session.files.remote2remote;

import javax.swing.JOptionPane;

import zmcommand.app.App;
import zmcommand.app.common.FileInfo;
import zmcommand.app.ssh.CachedCredentialProvider;
import zmcommand.app.ssh.RemoteSessionInstance;
import zmcommand.app.ssh.SshFileSystem;
import zmcommand.app.ui.components.session.NewSessionDlg;
import zmcommand.app.ui.components.session.SessionInfo;
import zmcommand.app.ui.components.session.files.FileBrowser;
import zmcommand.app.ui.components.session.files.transfer.FileTransfer.ConflictAction;

public class LocalPipeTransfer {
	public void transferFiles(FileBrowser fileBrowser, String currentDirectory, FileInfo[] selectedFiles) {
		SessionInfo info = new NewSessionDlg(App.getAppWindow()).newSession();
		if (info != null) {
			String path = JOptionPane.showInputDialog("Remote path");
			if (path != null) {
				RemoteSessionInstance ri = new RemoteSessionInstance(info, App.getInputBlocker(),
						new CachedCredentialProvider() {
							private char[] cachedPassword;
							private char[] cachedPassPhrase;
							private String cachedUser;

							@Override
							public synchronized char[] getCachedPassword() {
								return cachedPassword;
							}

							@Override
							public synchronized void cachePassword(char[] password) {
								this.cachedPassword = password;
							}

							@Override
							public synchronized char[] getCachedPassPhrase() {
								return cachedPassPhrase;
							}

							@Override
							public synchronized void setCachedPassPhrase(char[] cachedPassPhrase) {
								this.cachedPassPhrase = cachedPassPhrase;
							}

							@Override
							public synchronized String getCachedUser() {
								return cachedUser;
							}

							@Override
							public synchronized void setCachedUser(String cachedUser) {
								this.cachedUser = cachedUser;
							}
						});

				SshFileSystem sshFS = ri.getSshFs();
				fileBrowser.newFileTransfer(fileBrowser.getSSHFileSystem(), sshFS, selectedFiles, path, this.hashCode(),
						ConflictAction.Prompt);
			}
		}
	}
}
