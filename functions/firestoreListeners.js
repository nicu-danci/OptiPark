const admin = require('firebase-admin');
const functions = require('firebase-functions');

// Firestore listener function
exports.listenToExpirationChanges = functions.firestore
  .document('parking_spaces/{parkingSpaceId}')
  .onUpdate((change) => {
    const newValue = change.after.data(); // New document data
    const previousValue = change.before.data(); // Previous document data

    // Check if the expiration time has changed
    if (newValue.expirationTime !== previousValue.expirationTime) {
      const currentTime = admin.firestore.Timestamp.now().seconds;
      const expirationTime = newValue.expirationTime;

      // Check if expiration time has passed
      if (currentTime > expirationTime) {
        // Update availability status to true (available)
        return change.after.ref.update({ availability: true });
      }
    }

    // No changes needed
    return null;
  });

// Export the Firestore listener function
module.exports = {
  listenToExpirationChanges: exports.listenToExpirationChanges
};
