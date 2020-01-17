# COSC3506 Project

### The Team
Johnny Console - Team Lead/Programmer

Yifan He - Team Communicator/Programmer

Alex Tailiaiti - Programmer

### The Project
The project we have decided to create is __*Project 2: Local Area Network Office Messaging System*__

Description of the Project:

_ Design an office messaging system, like a compact messenger system, which will allow staff members to send messages to each other that can be retained, deleted, archived, and organized by both the sender and receiver. The system can function on a peer-to-peer basis, or through a centralized message server. It will utilize the office LAN – ensuring that messages do not leave the office environment. Individuals can be assigned to one or more groups, allowing messages to be sent to a single individual, several individuals, one or more groups, or broadcast to the entire office.
As a minimum, the messaging system should provide the following features:


* Identify all members of the messaging system
* Indicate the status of each member (e.g., Offline or Connected)
* Ability of the user to set individual status (e.g., online, away, busy, etc…)
* Provide a facility for the creation of short messages (you must decide on the feasible size)
* Display a pop-up message to recipient upon arrival of message
* Store all incoming messages in message Inbox
* Store copies of all sent messages
* Once read, messages can be retained or removed from the Inbox
* Backup and restore capabilities

Some additional features of the system which should be considered in the design might be:
* Sound notification option when a new message arrives
* More advanced status settings such as: Online, Busy, Out to Lunch, On the Phone, Away from Desk
* Deferred messaging to offline users that will be delivered when the recipient comes online
* Creation of groups to which users can be assigned to facilitate group messaging
* Office wide message broadcasting that can be directed to all users or just users online.
* Out-of-Office return messages providing details such as “at meeting ABC until…”
* Long-term message archive where messages are removed from the primary storage to a secondary archive for long-term storage
* Categorization of messages based on Projects, Clients, or other Users

Please note this is not intended to be a chat system but rather a short message delivery system. In an office, chatting can be more efficiently handled using the office phone system or simply meeting at the water cooler.

### The Implementation
To implement this project, we used Java and JavaFX, as well as the JDBC library to connect to our database, and the
JBCrypt library to hash user passwords and to check entered passwords against hashes.