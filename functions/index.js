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

  var registrationToken = 'eNBDflUenzI:APA91bESAk_UslyFbQUVkUjcMtLjHym3w5C9jaLS4sKxNhzVEBvgAj0WlkfwoVVWzMGWEfO4MXoCy3pqXEE8z0tI-BTnvaJMPmND_n_jhH8fCaYUMlkXdDgRhjgV1ZeHAa2_KKFI30U5'; 
  var topic = 'pushNotifications';

  // Create a notification
  /*let message = {
    notification: {
      title: 'EARTHQUAKE',
      body: 'GIDDY UP YOOO',
      //sound: "default"
    },
    topic: topic,
  };*/

  var loc = snapshot.child('location').val();
  var mag = snapshot.child('mag').val();
  console.log(loc);
  console.log(mag);

  let message = {
    notification: {
      title: 'EARTHQUAKE',
      body: loc, mag,
    },
    topic: topic,
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