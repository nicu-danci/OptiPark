// Import the Firebase Admin SDK
const admin = require('firebase-admin');
admin.initializeApp();

// Import the Cloud Functions for Firebase SDK
const functions = require('firebase-functions');
// Cloud Function to update parking space availability based on reservation end time
exports.updateAvailabilityScheduled = functions.pubsub.schedule('every 1 minutes').onRun(() => {
    // Get the current time
    const currentTime = new Date();

    // Get a reference to the Firestore collection
    const parkingSpacesRef = admin.firestore().collection('parking_spaces');

    // Query for reservations that have passed their end time
    return parkingSpacesRef.where('reservation.endTime', '<=', currentTime).get()
        .then(snapshot => {
            // Iterate through each document in the result set
            snapshot.forEach(doc => {
                // Update the availability field to true and clear the reservation field
                return doc.ref.update({
                    availability: true,
                    reservation: null
                });
            });
            // Return a success message
            return console.log('Availability updated for expired reservations.');
        })
        .catch(error => {
            // Handle errors
            console.error('Error updating availability:', error);
            return null;
        });
});
