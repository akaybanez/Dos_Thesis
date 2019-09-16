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

  //  Grab the current value of what was written to the Realtime Database.
  //var valueObject = event.data.val();

  const after = change.after.val;

  // Create a notification
    const payload = {
        notification: {
            title: after.val.mag,
            body: after.location || after.datetime,
            sound: "default"
        },
    };

  //Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };


    return admin.messaging().sendToTopic("pushNotification", payload, options);
  });