var { desktopCapturer } = require('electron');
var fs = require('fs');

var videoElement = document.querySelector('video');
var listlement = document.querySelector('ul');
var outputElement = document.querySelector('#output');

var Rec = {
    recorder: null,
    blobs: [],
    start() {
        if (this.recorder === null && ScreenManager.selectedSource) {
            outputElement.innerHTML = 'Recording....';
            navigator.webkitGetUserMedia({
                audio: false,
                video: {
                    mandatory: {
                        chromeMediaSource: 'desktop',
                        chromeMediaSourceId: ScreenManager.selectedSource.id,
                        minWidth: 800,
                        maxWidth: 1280,
                        minHeight: 600,
                        maxHeight: 720
                    }
                }
            }, this.handleStream, this.handleUserMediaError)
        }
    },
    stop() {

    },
    handleStream(stream) {
        videoElement.poster = '';
        videoElement.src = URL.createObjectURL(stream);
    },
    handleUserMediaError(e) {
        console.log('handleUserMediaError', e);
    }
}

var ScreenManager = {
    sources: [],
    selectedSource: null,
    listScreens() {
        desktopCapturer.getSources({ types: ['window', 'screen'] }, function (error, sources) {
            var template = '';
            ScreenManager.sources = sources;
            sources.forEach(source => {
                template += `<li onclick="ScreenManager.setScreen('${source.id}')">
                                <img src="${source.thumbnail.toDataURL()}" />
                                <h3>${source.name}</h3>
                            </li>    
                `;
            });
            listlement.innerHTML = template;
        });
    },
    setScreen(sourceId) {
        this.selectedSource = this.sources.find(source => source.id === sourceId);
        videoElement.poster = this.selectedSource.thumbnail.toDataURL();
        videoElement.src = '';
        videoElement.controls = false;
    }
}

ScreenManager.listScreens();