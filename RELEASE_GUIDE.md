# Android Release and Play Console Guide

## App and Firebase identity

- Android application ID / Play Console package: `com.waves.androidapp`
- Current release version: `1.2` (`versionCode` 3; use a code greater than every version previously uploaded to any Play track)
- Firebase Android app: `1:1088277592046:android:1fccb895b44a59bd6f80ff`
- Firebase configuration: `app/google-services.json`
- Android Gradle namespace remains `com.example`; it is separate from the installed application ID and keeps existing source packages stable.
- Keep the Firebase API key restricted in Google Cloud Console to the APIs and Android app that need it. The Firebase config is client-side configuration, not a place for private service-account credentials.

## Build toolchain

- Gradle: 8.11.1
- Android Gradle Plugin: 8.10.1
- Kotlin: 2.2.20
- KSP: 2.2.20-2.0.4
- Kotlin Compose plugin: 2.2.20
- Compose BOM: 2024.09.00
- Compile SDK / target SDK: 36
- Minimum SDK: 24
- JDK: 21

The Kotlin Compose plugin replaces the old `kotlinCompilerExtensionVersion` setting. Do not restore a `composeOptions` compiler version while using Kotlin 2.1.0.

## Signing keys

The release signing configuration in `app/build.gradle.kts` uses:

- Keystore: `my-upload-key.jks` in the repository root by default, or the path in `KEYSTORE_PATH`
- Key alias: `upload`
- Environment variables: `STORE_PASSWORD` and `KEY_PASSWORD`
- Local-only credential copies and `signing.env`: `credentials/` (ignored by Git)
- Public upload certificate: [`upload_certificate.pem`](upload_certificate.pem)

The ignored local bundle contains `credentials/my-upload-key.jks`, `credentials/debug.keystore`, `credentials/upload_certificate.pem`, `credentials/google-services.json`, `credentials/sha-fingerprints.txt`, and `credentials/signing.env`. The active Firebase configuration remains `app/google-services.json`.

The private upload keystore, debug keystore, and local credential copies are excluded by `.gitignore`. Never commit, email, or publish the private keystore or its passwords. Keep encrypted backups of the upload key and store its passwords in a password manager. The supplied password values are stored only in the ignored local file `credentials/signing.env`; they are not recorded in this document or Git.

SHA-1 fingerprints:

- Release/upload certificate: `DB:B4:7E:C1:6D:A6:6C:59:62:CA:99:A1:EB:AF:1F:CF:30:28:40:BE`
- Release/upload certificate SHA-256: `7F:36:71:12:DF:46:F3:2F:34:F8:7E:91:27:36:DE:A1:5B:EC:8F:47:49:85:7A:CC:23:6A:AF:47:C7:8F:2F:FF`
- Local debug certificate: `94:0E:9C:D7:3A:44:95:76:28:62:FF:4D:F6:B6:AE:E8:55:81:D0:B4`
- Local debug certificate SHA-256: `A0:D5:70:CC:B0:AA:F0:37:0D:7C:98:68:20:4A:E7:74:F0:0D:DB:06:E6:AE:45:73:A6:D6:FC:94:2B:B8:E7:6D`

Register the upload certificate fingerprint where required by Firebase/Google services. Google Play App Signing uses a separate app-signing key after enrollment; use the fingerprints shown in Play Console for production app-signing identity and API integrations.

## Build release artifacts

Load signing values from the ignored local credentials file; do not print or paste the values:

```bash
set -a
. credentials/signing.env
set +a
export KEYSTORE_PATH="$PWD/credentials/my-upload-key.jks"
./gradlew :app:assembleRelease :app:bundleRelease
unset STORE_PASSWORD KEY_PASSWORD
```

Expected outputs:

- APK: `app/build/outputs/apk/release/app-release.apk`
- Android App Bundle: `app/build/outputs/bundle/release/app-release.aab`

Artifacts under `app/build/outputs/` are local build outputs and are not committed.

## Firebase Backend

The Firebase CLI project is `waves-64217` in `.firebaserc`. Email/Password authentication is enabled by the `auth.providers.emailPassword` setting in `firebase.json`. Verification and password-reset emails currently use Firebase's built-in email delivery. Custom SMTP is not configured yet.

Business data is scoped to the authenticated user's UID:

- `users/{uid}/metadata/account`: signup email, display name, terms acceptance, and timestamps
- `users/{uid}/clients/{clientId}`: client contact, billing address, tax, notes, and archive state
- `users/{uid}/products/{productId}`: SKU, description, price, unit, quantity, tax, and archive state
- `users/{uid}/invoices/{invoiceId}`: client snapshot, dates, line items, totals, status, and payment history
- `users/{uid}/settings/business`: business profile, logo URL, tax, bank details, and invoice defaults
- Storage `users/{uid}/business/logo`: owner-only image, limited to 5 MB

New accounts start with empty business data; no sample clients, bank details, or invoices are seeded. Firestore and Storage rules require a verified account and restrict records/files to their owner. The signup metadata rule permits only the exact terms-consent document before email verification.

Firestore and Storage are provisioned for project `waves-64217`:

- Firestore: `(default)` database, Standard edition, `nam5` (US multi-region), with delete protection enabled.
- Storage: default bucket `waves-64217.firebasestorage.app`, Standard storage class, `US` multi-region.
- Authentication: Email/Password provider enabled.
- Security rules: `firestore.rules` and `storage.rules` have been deployed.

The project is linked to a billing account. Storage for Firebase requires the Blaze pay-as-you-go plan under Firebase's current pricing requirements; review the billing plan and usage limits before production use. To deploy backend config and rules later, run `firebase deploy --only auth,firestore:rules,storage --project waves-64217`.

## Google Play submission checks

As of August 31, 2026, new apps and updates must target Android 16 (API 36) or higher. This release is configured for target API 36. Before uploading, check Play Console and ensure the version code exceeds every code already used in all tracks.

This app creates user accounts. Settings links to the supplied Privacy Policy URL, and the in-app account deletion flow sends a six-digit email code and removes the user's Firebase Auth account, Firestore data, and Storage files. Google Play also requires an external web resource for account/data deletion; add its public URL in Play Console and complete the Data safety form before submission. See [Google Play account deletion requirements](https://support.google.com/googleplay/android-developer/answer/13327111).

## Verified status

The release APK and AAB were built and signed with the local upload key. The APK signature was verified with `apksigner`, and the AAB signature was verified with `jarsigner` and Google's `bundletool`; both use the upload certificate fingerprints listed above. The generated artifacts are local build outputs and are not committed.
