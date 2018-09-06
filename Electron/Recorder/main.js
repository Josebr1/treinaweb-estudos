const { app, BrowserWindow } = require('electron');

let win;

const appUrl = `file://${__dirname}/index.html`;

function createElectronSchell() {
    win = new BrowserWindow({
        width: 800,
        height: 600
    });
    win.loadURL(appUrl);
    win.on('closed', () => {
        win = null;
    });
}

app.on('ready', createElectronSchell);

app.on('window-all-closed', () => {
    if (process.platform != 'darwin') {
        app.quit();
    }
});

app.on('active', () => {
    if (app === null) {
        createElectronSchell();
    }
})