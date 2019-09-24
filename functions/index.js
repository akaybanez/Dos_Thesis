//import firebase functions modules
const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// Listens for new messages added to messages/:pushId
exports.pushNotification = functions.database
.ref('/data/{pushId}')
.onCreate((snapshot, context) => {

  console.log('Push notification event triggered');

  var registrationToken = 'd0naH8qfDOQ:APA91bE-XZYuH5zrpZy18hCitF2JarktS6lAwWCgg99xqG1gSo0oKjV3jDyJ3pBGvziMegmUZriBTOB9ZzmUYsAtYu_KjJYB2B6MavyN9ot8oOHfd550dIIvcN5vCNV1InuLGnc_ie7e'; 
  var topic = 'pushNotifications';

  var loc = snapshot.child('location').val();
  var mag = snapshot.child('mag').val();
  console.log(loc);
  console.log(mag);

  let message = {
    notification: {
      title: mag + ' MAGNITUDE EARTHQUAKE',
      body: loc,
    },
    topic: topic,
  };

  return admin.messaging().send(message)
    .then((response) => {
      return console.log('Successfully sent message:', response);
    })
    .catch((error) => {
      console.log('Error sending message:', error);
  });

});