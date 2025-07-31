# PowerShell build script for Win64

echo "Run maven build..."
mvn clean package

echo "Run jpackage..."
jpackage `
  --input target `
  --dest installer `
  --type msi `
  --name "Las Notes Desktop" `
  --app-version "2.0.0" `
  --main-jar las-notes-1.0.0.jar `
  --main-class com.mitrakoff.lasnotes.App `
  --vendor "Artem Mitrakov" `
  --copyright "Copyright 2025 Artem Mitrakov (mitrakov-artem@yandex.ru)" `
  --icon AppIcon.ico `
  --win-dir-chooser `
  --win-help-url "https://lasnotes.com" `
  --win-update-url "https://lasnotes.com" `
  --win-menu `
  --win-menu-group "Las Notes" `
  --win-shortcut-prompt `
  --win-upgrade-uuid "39854e34-e0e9-4887-8c3d-42d716a68126" `
  --jlink-options "--strip-debug --no-header-files --no-man-pages" `
  --add-modules ALL-MODULE-PATH `
  --verbose `

echo "Sign the installer file"
signtool sign /v /a /tr http://timestamp.globalsign.com/tsa/r6advanced1 /td SHA256 /fd SHA256 '.\Las Notes Desktop-2.0.0.msi'
signtool verify /v '.\Las Notes Desktop-2.0.0.msi'
