# Περίπτωση Χρήσης: Ανέβασμα Αντικειμένου

## Περιγραφή
Αυτή η περίπτωση χρήσης επιτρέπει στον χρήστη (xChanger) να ανεβάσει ένα αντικείμενο στην πλατφόρμα xChange, ώστε να το διαθέσει για ανταλλαγή με άλλους χρήστες. Ο χρήστης μπορεί να εισαγάγει πληροφορίες, όπως τίτλο, περιγραφή, σε τι κατάσταση βρίσκεται, σε ποια κατηγορία προϊόντων ανήκει όπως και φωτογραφίες του αντικειμένου, και να το διαχειριστεί αργότερα αν χρειαστεί.



## Ενδιαφερόμενοι (Actors)
- **xChanger (Χρήστης)**: Ο τακτικός χρήστης της εφαρμογής που ανεβάζει αντικείμενα για ανταλλαγή.
- **Σύστημα xChange**: Επεξεργάζεται τα δεδομένα του αντικειμένου και τα αποθηκεύει στη βάση δεδομένων.



## Προϋποθέσεις
- Ο χρήστης πρέπει να έχει λογαριασμό στην πλατφόρμα και να είναι συνδεδεμένος.



## Βασική Ροή

&nbsp;&nbsp;&nbsp;&nbsp; **1**. Ο χρήστης συνδέεται στην εφαρμογή xChange και επιλέγει την επιλογή "Ανέβασμα Νέου Αντικειμένου".

&nbsp;&nbsp;&nbsp;&nbsp; **2**. Το σύστημα εμφανίζει τη φόρμα για το ανέβασμα αντικειμένου.

&nbsp;&nbsp;&nbsp;&nbsp; **3**. Ο χρήστης εισάγει τα ακόλουθα στοιχεία για το αντικείμενο:
   - **Τίτλος**: Σύντομη περιγραφή του αντικειμένου.
   - **Περιγραφή**: Λεπτομερής περιγραφή του αντικειμένου.
   - **Κατηγορία**: Επιλογή κατηγορίας για το αντικείμενο.
   - **Κατάσταση**: Επιλογή της κατάστασης του αντικειμένου (π.χ., Καινούργιο, Μεταχειρισμένο).
   - **Φωτογραφίες**: Ανεβάζει μία ή περισσότερες φωτογραφίες του αντικειμένου.

&nbsp;&nbsp;&nbsp;&nbsp; **4**. Ο χρήστης επιβεβαιώνει τα στοιχεία και επιλέγει "Ανέβασμα".

&nbsp;&nbsp;&nbsp;&nbsp; **5**. Το σύστημα ελέγχει αν ο χρήστης έχει συμπληρώσει όλες τις απαιτούμενες πληροφορίες.

&nbsp;&nbsp;&nbsp;&nbsp; **6**. Αν οι πληροφορίες είναι έγκυρες, το σύστημα αποθηκεύει το αντικείμενο στη βάση δεδομένων και το κατατάσσει στη λίστα διαθέσιμων αντικειμένων για ανταλλαγή.

&nbsp;&nbsp;&nbsp;&nbsp; **7**. Το σύστημα ενημερώνει τον χρήστη ότι το αντικείμενο ανέβηκε επιτυχώς και είναι πλέον διαθέσιμο για άλλους χρήστες.



## Εναλλακτικές Ροές

### Εναλλακτική Ροή 1: Ελλιπείς Πληροφορίες
&nbsp;&nbsp;&nbsp;&nbsp; **1**. Αν ο χρήστης δεν συμπληρώσει κάποια από τις απαιτούμενες πληροφορίες ή αν υπάρχει κάποιο λάθος στα δεδομένα, το σύστημα εμφανίζει μήνυμα σφάλματος, εξηγώντας τι λείπει ή τι πρέπει να διορθωθεί.

&nbsp;&nbsp;&nbsp;&nbsp; **2**. Ο χρήστης διορθώνει τα στοιχεία και επαναλαμβάνει το βήμα της επιβεβαίωσης.



## Μεταγενέστερες Ενέργειες
- Το αντικείμενο γίνεται διαθέσιμο στη λίστα αντικειμένων για ανταλλαγή, όπου μπορεί να το δουν και άλλοι χρήστες.
- Το σύστημα αποθηκεύει τις λεπτομέρειες του αντικειμένου στο προφίλ του χρήστη για μελλοντική διαχείριση (π.χ., επεξεργασία, διαγραφή).
- Το ιστορικό των ενεργειών ενημερώνεται, καταγράφοντας την ημερομηνία και την ώρα του ανεβάσματος του αντικειμένου.



## Επιχειρησιακοί Κανόνες
- Ο χρήστης είναι υπεύθυνος για την ακρίβεια των πληροφοριών του αντικειμένου.
- Η πλατφόρμα διατηρεί το δικαίωμα να απορρίψει αντικείμενα με ακατάλληλο περιεχόμενο.



## Διαγράμματα

### Διάγραμμα Δραστηριοτήτων για Ανέβασμα Αντικειμένου

![Διάγραμμα Δραστηριοτήτων - Ανέβασμα Αντικειμένου](uml/requirements/activity-diagram-upload.png)