PREFIX ?= /usr/local
VERSION = $(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

all:
	mvn package

clean:
	mvn clean

install: all
	install -d -m 755 $(DESTDIR)$(PREFIX)/share/JKnobMan
	cp -rfd build/JKnobMan-$(VERSION)/* $(DESTDIR)$(PREFIX)/share/JKnobMan/
	install -D -m 644 misc/unix/JKnobMan.desktop $(DESTDIR)$(PREFIX)/share/applications/JKnobMan.desktop
	install -D -m 644 res/Resource/Images/JKnobMan.png $(DESTDIR)$(PREFIX)/share/pixmaps/JKnobMan.png
	install -D -m 755 misc/unix/jknobman.sh $(DESTDIR)$(PREFIX)/bin/jknobman

.PHONY: all clean install
