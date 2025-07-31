#!/bin/bash

mvn clean package

jpackage \
  --input target \
  --dest installer \
  --type dmg \
  --name "LasNotes-Desktop" \                       # App name and .dmg name
  --app-version "2.0.0" \
  --main-jar las-notes-1.0.0.jar \
  --main-class com.mitrakoff.lasnotes.App \
  --java-options -XstartOnFirstThread \             # Cocoa requirement for MacOS apps
  --vendor "Artem Mitrakov" \
  --copyright "Copyright 2025 Artem Mitrakov (mitrakov-artem@yandex.ru)" \
  --icon appicon.png \
  --mac-package-identifier com.mitrakoff.lasnotes.desktop \
  --mac-package-name "Las Notes for MacOS" \        # shown in App menu
  --mac-app-category "public.app-category.utilities" \
  --jlink-options "--strip-debug --no-header-files --no-man-pages" \
  --add-modules ALL-MODULE-PATH \                   # includes necessary jars from target/
  --verbose
#  --mac-sign \
#  --mac-signing-key-user-name "Developer ID Application: Artem Mitrakov (UUK2LMKK3C)" \
