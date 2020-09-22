Version:	1.2
Date: 		20/09/2020
///////////////////////////////////////////////////////////////////////////////////////////

There are 3 input files for this program. 

## Do not change the file name ##
/data/gpq.txt
/data/assign.txt


*******************************************************************************************
/data/gpq.txt file contains the participants for this upcoming gpq assignment.
You can copy and paste the discord listing here. 
You may add a date to display. 
    Use "Date>29 Aug" to indicate to the program. 
    If there are multiple "Date>", the last "Date>" will be used. 
    *Case-sensitive
For members who are listed in database, they may use an alias or actual ign in gpq.txt
    Existing member's info (job, floor) will be pulled out
For new members, follow the syntax: 
    Syntax:  #. <IGN>/<JOB>/<FLOOR>
    Example: 3. NyanBreath/Evan/50

Other types of delimiter allowed: (. , <space>)
    1. First
    2.Second
    3)Third
    4 Forth
*If one of these 3 delimeters are not used, the entire line will be used as ign. 

*******************************************************************************************
/data/assign.txt file contains the hard assignment of members
Members in this file will be assigned to the team first. 
Members who are in assign.txt must be a participant (inside gpq.txt) in order for it to be part of the team. 
Using alias within assign.txt is fine. 

Syntax for each entry: 
    Syntax:  <IGN>>TUNNEL#_TEAM#
    Example: iUnder18>1_2
// This represents that "iUnder18" is assigned to Tunnel 1, Team 2

*******************************************************************************************
Database file contains the database of existing members information and their alias. 
It is hosted online on codepile: https://www.codepile.net/pile/PrNjYerZ
The program will automatically download from this page for every execution. 

Alias are only accepted if they are manually added here.

Syntax for each database entry: 
    Syntax:   <IGN>{<JOB>,<FLOOR>}=[<alias1>, <alias2>, ...] 
    Example:  MooJieJie{NL,50}=[moo, cow]

    *Do take note that IGN is case-sensitive, and alias is not case-sensitive

**WARNING: Do not put any other notes within database.txt; it will be removed for every run

*******************************************************************************************

General note: 
1. You may use "//" within assign.txt, gpq.txt and database (located online) to indicate notes. These will be ignored by the program.
2. If you need help, extract template.zip and run to see the output. 

