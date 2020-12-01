# cordova-plugin-mediabuttons

This plugin listens for MEDIA_BUTTON events and sends it to javascript

## Methods

- cordova.plugins.MediaButtons.startActionMedia()
- cordova.plugins.MediaButtons.stopActionMedia()

## cordova.plugins.MediaButtons.startActionMedia

Start the media session to listen to MEDIA_BUTTON events
Every action sends to the action button event in the DOM document


### Example

```javascript

    cordova.plugins.MediaButtons.startActionMedia();
    
    document.addEventListener('actionbutton', () => {
      // code here
    });

```

## cordova.plugins.MediaButtons.stopActionMedia

Stop listening to MEDIA_BUTTON events