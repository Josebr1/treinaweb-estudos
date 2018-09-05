const { app, BrowserWindow } = require('electron');
const appUrl = `file://${__dirname}/index.html`;

let win;

function createElectronShell() {
    win = new BrowserWindow({ width: 800, height: 600, autoHideMenuBar: true, frame: true, transparent: true });
    win.loadURL(appUrl);
    win.webContents.openDevTools();
    win.on('closed', () => win = null);
}

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit();
    }
})

app.on('activate', () => {
    if (app == null) {
        createElectronShell();
    }
})

app.on('ready', createElectronShell);
