#!/bin/bash
set -e

echo "Run maven build..."
mvn clean package

echo "Run jpackage..."
jpackage \
  --input target \
  --dest installer \
  --type dmg \
  --name "Las Notes Desktop" \
  --app-version "2.0.0" \
  --main-jar las-notes-1.0.0.jar \
  --main-class com.mitrakoff.lasnotes.App \
  --java-options -XstartOnFirstThread \
  --vendor "Artem Mitrakov" \
  --copyright "Copyright 2025 Artem Mitrakov (mitrakov-artem@yandex.ru)" \
  --icon AppIcon.icns \
  --jlink-options "--strip-debug --no-header-files --no-man-pages" \
  --add-modules ALL-MODULE-PATH \
  --mac-package-identifier com.mitrakoff.lasnotes.desktop \
  --mac-package-name "Las Notes" \
  --mac-app-category "public.app-category.utilities" \
  --mac-sign \
  --mac-signing-key-user-name "Developer ID Application: Artem Mitrakov (UUK2LMKK3C)" \
  --verbose \
