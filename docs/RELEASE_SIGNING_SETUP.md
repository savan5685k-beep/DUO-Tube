# DUO Android release signing setup

## Important: keep the existing signing identity

The release workflow currently expects certificate SHA-256:

`b5fb3a7defdc7a3e0c0d6c3b468bc6353cd22225bbfee0585d7f337667aeb87a`

**Do not create a replacement keystore unless you have confirmed that the original key is unavailable and no APK signed with it has been distributed.** A different signing key can prevent Android from installing updates over existing DUO installations.

## 1. Check an existing keystore (if you have one)

On your own computer, use:

```bash
keytool -list -v -keystore path/to/your-existing-keystore.jks
```

Enter the keystore password locally. Find the certificate's SHA-256 fingerprint and compare it to the expected fingerprint above. Never paste the keystore, passwords, or private key into chat or commit them.

## 2. Create a keystore only if the existing key is unavailable

Run locally (not in GitHub or chat):

```bash
keytool -genkeypair -v \
  -keystore duo-release.jks \
  -alias duo-release \
  -keyalg RSA -keysize 4096 -validity 10000
```

Use unique, strong passwords and store them in a password manager. Keep two secure backups of the keystore. Do not put it in the repository, cloud drive with public sharing, or source-control history.

Then inspect its fingerprint:

```bash
keytool -list -v -keystore duo-release.jks -alias duo-release
```

If it does not match the expected fingerprint, **stop**. The release workflow will reject it. Do not edit the workflow fingerprint to accept a new key until you have reviewed update compatibility and the release plan.

## 3. Prepare the GitHub Actions secret values locally

Base64-encode the keystore file. Keep the output private.

macOS/Linux:
```bash
base64 -w 0 duo-release.jks
```

On macOS, if `-w` is unsupported:
```bash
base64 duo-release.jks | tr -d '\\n'
```

Windows PowerShell:
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("duo-release.jks"))
```

## 4. Add repository secrets

In GitHub, open **Settings → Secrets and variables → Actions → New repository secret**. Add these four secrets, using values from your local machine/password manager:

- `RELEASE_KEYSTORE_BASE64` — Base64 output from step 3
- `RELEASE_KEYSTORE_PASSWORD` — keystore password
- `RELEASE_KEY_ALIAS` — alias (for the example above: `duo-release`)
- `RELEASE_KEY_PASSWORD` — private-key password

Optional Firebase secret:
- `GOOGLE_SERVICES_JSON` — contents of the Firebase configuration file, if DUO's release build needs Firebase. Otherwise leave it unset; the workflow builds without Firebase.

Never share these secret values with anyone, including in ChatGPT. Do not upload the keystore file to GitHub.

## 5. Configure the production environment

The release workflow uses the GitHub environment named `production`. In **Settings → Environments → production**, verify that the environment exists and configure appropriate protection/review rules if available. Repository owner should be the only person allowed to publish production releases.

## 6. Verify before release

After the secrets are configured, run the Release workflow manually only after CI succeeds and the signing certificate fingerprint is confirmed to match. The workflow validates the keystore/alias, signs APKs, verifies signatures and the fingerprint, creates SHA-256 checksums, and initially creates a draft release before publishing.

The signing key is critical for app updates. Preserve it and its passwords securely for the lifetime of DUO.
