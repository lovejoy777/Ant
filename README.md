# Ant
Home Automation done right.

This is an android app for use with the Ant Automation Project.
It controls base stations built with amicaV2 mcu's which in turn transmits data over a NRF2401 tranceiver to Nodes.
So far I have 3 types of Nodes.

1/ Switches which comprise of a NRF2401 tranceiver connected to arduino nano and 2 or 4 gang relays.
2/ RoomStats which comprise of NRF2401 tranceiver connected to arduino nano with 128x64 oled screen & DS18B20 temperature sensor.
3/ Dimmer Switches which comprise of a NRF2401 tranceiver connected to arduino nano and dimmer curcuit (work in progress).

All the Nodes are fully addressable from within the app and also send back an acknowledgment signal so you know if things worked.

The base stations are fully controllable from anywhere in the world where you have a wifi connection.

