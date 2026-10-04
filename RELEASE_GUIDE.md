# Android Release and Play Console Guide

## App and Firebase identity

- Android application ID / Play Console package: `com.waves.androidapp`
- Firebase Android app: `1:1088277592046:android:1fccb895b44a59bd6f80ff`
- Firebase configuration: `app/google-services.json`
- Android Gradle namespace remains `com.example`; it is separate from the installed application ID and keeps existing source packages stable.
- Keep the Firebase API key restricted in Google Cloud Console to the APIs and Android app that need it. The Firebase config is client-side configuration, not a place for private service-account credentials.

## Build toolchain

- Gradle: 8.7
- Android Gradle Plugin: 8.5.0
- Kotlin: 2.1.0
- KSP: 2.1.0-1.0.29
- Kotlin Compose plugin: 2.1.0
- Compose BOM: 2024.09.00
- Compile SDK / target SDK: 34
- Minimum SDK: 24
- JDK: 17

The Kotlin Compose plugin replaces the old `kotlinCompilerExtensionVersion` setting. Do not restore a `composeOptions` compiler version while using Kotlin 2.1.0.

## Signing keys

The release signing configuration in `app/build.gradle.kts` uses:

- Keystore: `my-upload-key.jks` in the repository root by default, or the path in `KEYSTORE_PATH`
- Key alias: `upload`
- Environment variables: `STORE_PASSWORD` and `KEY_PASSWORD`
- Public upload certificate: [`upload_certificate.pem`](upload_certificate.pem)

The private upload keystore and debug keystore are excluded by `.gitignore`. Never commit, email, or publish the private keystore or its passwords. Keep encrypted backups of the upload key and store its passwords in a password manager. The passwords are intentionally not recorded in this document or Git. If they are unavailable, recover them from the password manager or secret store used when the key was created before attempting a release build.

SHA-1 fingerprints:

- Release/upload certificate: `DB:B4:7E:C1:6D:A6:6C:59:62:CA:99:A1:EB:AF:1F:CF:30:28:40:BE`
- Local debug certificate: `94:0E:9C:D7:3A:44:95:76:28:62:FF:4D:F6:B6:AE:E8:55:81:D0:B4`

Register the upload certificate fingerprint where required by Firebase/Google services. Google Play App Signing uses a separate app-signing key after enrollment; use the fingerprints shown in Play Console for production app-signing identity and API integrations.

## Build release artifacts

Load the actual passwords interactively from the password manager; do not put literal values in shell history:

```bash
export KEYSTORE_PATH="$PWD/my-upload-key.jks"
read -rsp "Keystore password: " STORE_PASSWORD; printf '\n'; export STORE_PASSWORD
read -rsp "Key password: " KEY_PASSWORD; printf '\n'; export KEY_PASSWORD
./gradlew :app:assembleRelease :app:bundleRelease
unset STORE_PASSWORD KEY_PASSWORD
```

Expected outputs:

- APK: `app/build/outputs/apk/release/app-release.apk`
- Android App Bundle: `app/build/outputs/bundle/release/app-release.aab`

Artifacts under `app/build/outputs/` are local build outputs and are not committed.

## Verified status

At the time this guide was written, the release APK exists and its signature was verified with `apksigner`. Its package is `com.waves.androidapp`, and its signer SHA-1 matches the upload certificate above. An AAB was not present in the output directory at verification time; run the combined release command above before uploading a bundle to Play Console.
