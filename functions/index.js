//import firebase functions modules
const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


// Listens for new messages added to messages/:pushId
exports.pushNotification = functions.database
.ref('/{data}')
.onWrite((change, context) => {

  console.log('Push notification event triggered');

  var registrationToken = 'd0naH8qfDOQ:APA91bE-XZYuH5zrpZy18hCitF2JarktS6lAwWCgg99xqG1gSo0oKjV3jDyJ3pBGvziMegmUZriBTOB9ZzmUYsAtYu_KjJYB2B6MavyN9ot8oOHfd550dIIvcN5vCNV1InuLGnc_ie7e'; 

  //  Grab the current value of what was written to the Realtime Database.
  //var valueObject = event.data.val();
  const oof = change.after.val;
  var topic = 'pushNotifications';

  // Create a notification
  let message = {
    notification: {
      title: 'EARTHQUAKE',
      body: 'GIDDY UP YOOO',
      //sound: "default"
    },
    topic: topic,
  };

  var payload = {
    notification: {
      title: "change",
      body: "test",
    }
  };

  return admin.messaging().send(message)
    .then((response) => {
      // Response is a message ID string.
      return console.log('Successfully sent message:', response);
    })
    .catch((error) => {
      console.log('Error sending message:', error);
  });

    /*var options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };*/

});