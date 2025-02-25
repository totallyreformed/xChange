# Αναφορά Κάλυψης Κώδικα: Κλάση `Request`

## Εισαγωγή
Η κλάση `Request` διαχειρίζεται τα αιτήματα ανταλλαγής που γίνονται μεταξύ δύο χρηστών της εφαρμογής xChange. Κάθε αίτημα περιλαμβάνει τον χρήστη που κάνει το αίτημα (`requester`), τον χρήστη που το δέχεται (`requestee`), τα αντικείμενα που εμπλέκονται στην ανταλλαγή, καθώς και άλλες πληροφορίες όπως η ημερομηνία δημιουργίας και η κατάσταση του αιτήματος. Η κλάση περιλαμβάνει επίσης έναν στατικό μετρητή για τη διαχείριση μοναδικών αναγνωριστικών για κάθε αίτημα (`requested_id`).

Παρακάτω ακολουθεί αναλυτική αναφορά της κάλυψης του κώδικα για τη συγκεκριμένη κλάση, περιλαμβάνοντας τα πεδία, τις μεθόδους και τα JUnit tests που καλύπτουν τη συμπεριφορά της κλάσης.

## Πεδία και Μεταβλητές
1. **`requester`** (xChanger): Ο χρήστης που κάνει το αίτημα ανταλλαγής.
    - **Κάλυψη:** Ελέγχουμε ότι ο `requester` έχει οριστεί σωστά κατά την αρχικοποίηση της κλάσης μέσω του constructor και ότι δεν μπορεί να είναι `null`.

2. **`requestee`** (xChanger): Ο χρήστης που δέχεται το αίτημα ανταλλαγής.
    - **Κάλυψη:** Ελέγχουμε ότι ο `requestee` έχει οριστεί σωστά κατά την αρχικοποίηση της κλάσης μέσω του constructor και ότι δεν μπορεί να είναι `null`.

3. **`previous_request_id`** (static Long): Στατικό πεδίο που χρησιμοποιείται για τη διαχείριση των μοναδικών αναγνωριστικών αιτημάτων.
    - **Κάλυψη:** Ελέγχουμε ότι το `previous_request_id` αυξάνεται σωστά κάθε φορά που δημιουργείται ένα νέο αντικείμενο `Request`.

4. **`requested_id`** (Long): Το μοναδικό αναγνωριστικό του αιτήματος.
    - **Κάλυψη:** Επαληθεύουμε ότι το `requested_id` ορίζεται σωστά και αυξάνεται διαδοχικά για κάθε νέο αίτημα.

5. **`offered_item`** (Item): Το αντικείμενο που προσφέρει ο `requester`.
    - **Κάλυψη:** Ελέγχουμε ότι το αντικείμενο `offered_item` έχει οριστεί σωστά κατά την αρχικοποίηση.

6. **`requested_item`** (Item): Το αντικείμενο που ζητά ο `requester`.
    - **Κάλυψη:** Ελέγχουμε ότι το αντικείμενο `requested_item` έχει οριστεί σωστά κατά την αρχικοποίηση.

7. **`date_initiated`** (SimpleCalendar): Η ημερομηνία που έγινε το αίτημα.
    - **Κάλυψη:** Επαληθεύουμε ότι η ημερομηνία είναι ορθή και δεν μπορεί να είναι στο μέλλον.

8. **`active`** (boolean): Κατάσταση του αιτήματος (ενεργό ή μη ενεργό).
    - **Κάλυψη:** Ελέγχουμε ότι το αίτημα αρχικοποιείται ως ενεργό και μπορεί να αλλάξει κατάσταση.

## Μέθοδοι
### 1. Constructor `Request(xChanger requester, xChanger requestee, Item offered_item, Item requested_item, SimpleCalendar date_initiated)`
- **Λειτουργία:** Δημιουργεί ένα νέο αντικείμενο `Request` με συγκεκριμένους χρήστες (`requester` και `requestee`), αντικείμενα και ημερομηνία.
- **JUnit Test Κάλυψης:**
    - Έλεγχος για τη σωστή αρχικοποίηση όλων των πεδίων (`requester`, `requestee`, `offered_item`, `requested_item`, `date_initiated`).
    - Έλεγχος ότι ο `requester` και ο `requestee` δεν μπορεί να είναι `null`.
    - Έλεγχος ότι το `requested_id` αυξάνεται σωστά.
    - Έλεγχος ότι η ημερομηνία δεν είναι στο μέλλον.

### 2. Μέθοδος `getRequestID()`
- **Λειτουργία:** Επιστρέφει το μοναδικό αναγνωριστικό του αιτήματος (`requested_id`).
- **JUnit Test Κάλυψης:**
    - Έλεγχος ότι η μέθοδος επιστρέφει το σωστό `requested_id`.

### 3. Μέθοδος `getRequester()`
- **Λειτουργία:** Επιστρέφει τον χρήστη που έκανε το αίτημα (`requester`).
- **JUnit Test Κάλυψης:**
    - Έλεγχος ότι η μέθοδος επιστρέφει σωστά τον `requester` που ορίστηκε κατά την αρχικοποίηση.

### 4. Μέθοδος `getRequestee()`
- **Λειτουργία:** Επιστρέφει τον χρήστη που δέχεται το αίτημα (`requestee`).
- **JUnit Test Κάλυψης:**
    - Έλεγχος ότι η μέθοδος επιστρέφει σωστά τον `requestee` που ορίστηκε κατά την αρχικοποίηση.

### 5. Μέθοδος `getOfferedItem()`
- **Λειτουργία:** Επιστρέφει το αντικείμενο που προσφέρει ο `requester`.
- **JUnit Test Κάλυψης:**
    - Έλεγχος ότι η μέθοδος επιστρέφει σωστά το αντικείμενο `offered_item`.

### 6. Μέθοδος `getRequestedItem()`
- **Λειτουργία:** Επιστρέφει το αντικείμενο που ζητά ο `requester`.
- **JUnit Test Κάλυψης:**
    - Έλεγχος ότι η μέθοδος επιστρέφει σωστά το αντικείμενο `requested_item`.

### 7. Μέθοδος `getDateInitiated()`
- **Λειτουργία:** Επιστρέφει την ημερομηνία που έγινε το αίτημα.
- **JUnit Test Κάλυψης:**
    - Έλεγχος ότι η μέθοδος επιστρέφει σωστά την ημερομηνία `date_initiated`.

### 8. Μέθοδος `add_to_list()`
- **Λειτουργία:** Προσθέτει το αίτημα στη λίστα αιτημάτων του `requestee` και του `requester`.
- **JUnit Test Κάλυψης:**
    - Έλεγχος ότι το αίτημα προστίθεται σωστά στη λίστα αιτημάτων του `requestee` και του `requester`.

### 9. Μέθοδος `isActive()`
- **Λειτουργία:** Επιστρέφει την κατάσταση του αιτήματος (αν είναι ενεργό).
- **JUnit Test Κάλυψης:**
    - Έλεγχος ότι η αρχική κατάσταση είναι ενεργή.
    - Έλεγχος μετά την αλλαγή κατάστασης (π.χ., μετά από κλήση της `make_unactive()`).

### 10. Μέθοδος `make_unactive()`
- **Λειτουργία:** Θέτει το αίτημα ως μη ενεργό.
- **JUnit Test Κάλυψης:**
    - Έλεγχος ότι το αίτημα τίθεται σε μη ενεργή κατάσταση μετά την κλήση της μεθόδου.

### 11. Μέθοδος `resetId()`
- **Λειτουργία:** Επαναφέρει το στατικό πεδίο `previous_request_id` στην αρχική του τιμή (1). Αυτή η μέθοδος μπορεί να χρησιμοποιηθεί για λόγους δοκιμών ή επανεκκίνησης του συστήματος.
- **JUnit Test Κάλυψης:**
    - Έλεγχος ότι το `previous_request_id` επανέρχεται στην αρχική τιμή μετά την κλήση της μεθόδου.

## Κάλυψη Τεστ (JUnit)
Για να διασφαλίσουμε πλήρη κάλυψη κώδικα της κλάσης `Request`, προτείνουμε τα παρακάτω JUnit tests:

1. **Αρχικοποίηση Αιτήματος**
    - Έλεγχος της σωστής αρχικοποίησης όλων των πεδίων (`requester`, `requestee`, `offered_item`, `requested_item`, `date_initiated`).
    - Έλεγχος ότι τα πεδία `requester` και `requestee` δεν μπορεί να είναι `null`.
    - Έλεγχος ότι το `requested_id` αυξάνεται σωστά.
    - Έλεγχος ότι η ημερομηνία δεν είναι στο μέλλον.

2. **Αναγνωριστικό Αιτήματος (`getRequestID()`)**
    - Έλεγχος ότι το αναγνωριστικό που επιστρέφεται είναι το σωστό και είναι διαδοχικό για κάθε νέο αίτημα.

3. **Λήψη Στοιχείων Χρηστών και Αντικειμένων**
    - Έλεγχος των μεθόδων `getRequester()`, `getRequestee()`, `getOfferedItem()`, `getRequestedItem()`, `getDateInitiated()`.

4. **Προσθήκη Αιτήματος στη Λίστα**
    - Έλεγχος ότι το αίτημα προστίθεται σωστά στη λίστα αιτημάτων του `requestee` και του `requester`.

5. **Κατάσταση Αιτήματος**
    - Έλεγχος της αρχικής κατάστασης (`isActive()`).
    - Έλεγχος της αλλαγής κατάστασης με τη μέθοδο `make_unactive()`.

6. **Επαναφορά Ταυτότητας (`resetId()`)**
    - Έλεγχος ότι το `previous_request_id` επανέρχεται στην αρχική τιμή.

7. **Έλεγχος Εξαιρέσεων**
    - Έλεγχος ότι δημιουργείται εξαίρεση όταν ο `requester` ή ο `requestee` είναι `null`.
    - Έλεγχος ότι δημιουργείται εξαίρεση όταν η ημερομηνία είναι στο μέλλον.

## Συμπέρασμα
Η κλάση `Request` παρέχει μια πλήρη διαχείριση των αιτημάτων ανταλλαγής στην εφαρμογή xChange, συμπεριλαμβάνοντας τη δημιουργία, τη διαχείριση της κατάστασης, και την αποθήκευση αιτημάτων. Η πλήρης κάλυψη κώδικα απαιτεί τον έλεγχο της σωστής αρχικοποίησης, της διαχείρισης των

